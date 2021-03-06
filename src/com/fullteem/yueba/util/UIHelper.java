package com.fullteem.yueba.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.view.Gravity;
import android.widget.Toast;

import com.fullteem.yueba.R;
import com.fullteem.yueba.app.AppManager;

/**
 * 应用程序Activity帮助类
 * 
 * @author 黃俊彬
 * @version 1.0
 * @created 2014-1-2
 */
@SuppressLint("SimpleDateFormat")
public class UIHelper {
	/** 表情图片匹配 */
	private static Pattern facePattern = Pattern
			.compile("[\u4e00-\u9fa5]{1,3}");
	public static int noticeSize;
	private static Toast toast;

	/**
	 * @Name: ShowMessage
	 * @Description: 弹出提示信息
	 * @Author: 黄俊彬
	 * @Version: V1.00
	 * @Create 2013-8-9
	 * @Parameters: context 上下文 meesage提示的信息
	 * @Return: 无
	 */
	public static void ShowMessage(Context context, String message) {
		toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	@SuppressWarnings("unused")
	private static String getDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());
		return formatter.format(curDate);
	}

	/**
	 * 刪除通知
	 * 
	 * @param context
	 *            上下文
	 */
	public static void clearNotification(Context context) {
		// 启动后删除之前我们定义的通知
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(0);

	}

	public static String GetFilePath(Context context) {
		String savePath = "";
		// 判断是否挂载了SD卡
		String storageState = Environment.getExternalStorageState();
		if (storageState.equals(Environment.MEDIA_MOUNTED)) {
			savePath = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/LinkLnk/Shop";// 存放照片的文件夹
			File savedir = new File(savePath);
			if (!savedir.exists()) {
				savedir.mkdirs();
			}
		}
		// 没有挂载SD卡，无法保存文件
		if (StringUtils.isEmpty(savePath)) {
			ShowMessage(context, "无法保存照片，请检查SD卡是否挂载");
			return null;
		} else {
			return savePath;
		}
	}

	public static int getPositionFaceByText(Context context, String content) {
		Matcher matcher = facePattern.matcher(content);
		int position = -1;
		while (matcher.find()) {
			// 使用正则表达式找出其中的数字
			position = StringUtils.toInt(matcher.group(1));
			try {
				if (position > 65 && position < 102)
					position = position - 1;
				else if (position > 102)
					position = position - 2;
			} catch (Exception e) {
			}
		}
		return position;
	}

	/**
	 * 发送App异常崩溃报告
	 * 
	 * @param cont
	 * @param crashReport
	 */
	public static void sendAppCrashReport(final Context cont,
			final String crashReport) {
		AlertDialog.Builder builder = new AlertDialog.Builder(cont);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setTitle(R.string.app_error);
		builder.setMessage(R.string.app_error_message);
		builder.setPositiveButton(R.string.submit_report,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						// 发送异常报告
						Intent i = new Intent(Intent.ACTION_SEND);
						// i.setType("text/plain"); //模拟器
						i.setType("message/rfc822"); // 真机
						i.putExtra(Intent.EXTRA_EMAIL,
								new String[] { "jxsmallmouse@163.com" });
						i.putExtra(Intent.EXTRA_SUBJECT,
								"开源中国Android客户端 - 错误报告");
						i.putExtra(Intent.EXTRA_TEXT, crashReport);
						cont.startActivity(Intent.createChooser(i, "发送错误报告"));
						// 退出
						AppManager.getAppManager().AppExit(cont);
					}
				});
		builder.setNegativeButton(R.string.sure,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						// 退出
						AppManager.getAppManager().AppExit(cont);
					}
				});
		builder.show();
	}

	public static boolean isInBackground(Context context) {
		List<RunningTaskInfo> tasksInfo = ((ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE)).getRunningTasks(1);
		if (tasksInfo.size() > 0) {
			if (context.getPackageName().equals(
					tasksInfo.get(0).topActivity.getPackageName())) {

				return false;
			}
		}
		return true;
	}

}
