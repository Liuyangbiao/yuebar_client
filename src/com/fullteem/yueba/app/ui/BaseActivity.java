package com.fullteem.yueba.app.ui;

import java.io.Serializable;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.fullteem.yueba.R;
import com.fullteem.yueba.app.AppContext;
import com.fullteem.yueba.app.AppManager;
import com.fullteem.yueba.globle.GlobleVariable;
import com.fullteem.yueba.net.http.HttpRequestFactory;
import com.fullteem.yueba.net.http.IHttpTaskCallBack;
import com.fullteem.yueba.util.StringUtils;
import com.fullteem.yueba.util.UIHelper;
import com.fullteem.yueba.util.UmengUtil;
import com.fullteem.yueba.widget.MyProgressDialog;
import com.umeng.analytics.AnalyticsConfig;

/**
 * 基类界面
 * 
 * @author allen
 * @version 1.0.0
 * @created 2014-4-16
 */
public abstract class BaseActivity extends FragmentActivity {
	protected int resId = -1;
	public AppContext appContext;
	public Context context;
	protected MyProgressDialog dialog;
	private Toast toast;
	protected NotificationManager notificationManager;

	public BaseActivity(int resId) {
		this.resId = resId;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//程序包含activity，fragment，禁止以默认的页面统计方式
		UmengUtil.openActivityDurationTrack(this,false);
		UmengUtil.updateOnlineConfig(this);
		AnalyticsConfig.enableEncrypt(true);
		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		firstLoad();
		// 添加Activity到堆栈
		AppManager.getAppManager().addActivity(this);
		appContext = (AppContext) BaseActivity.this.getApplication();
		context = this;
		if (resId != -1) {
			setContentView(resId);
		}
		initViews();
		initData();
		bindViews();
		lastLoad();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 结束Activity&从堆栈中移除
		AppManager.getAppManager().finishActivity(this);
		// appContext.logout();
	}

	/**
	 * 
	 * @Title firstLoad
	 * @Description 初始化界面，最先执行
	 */
	public void firstLoad() {
	};

	/**
	 * 
	 * @Title lastLoad
	 * @Description 初始化界面，最后执行
	 */
	public void lastLoad() {
	};

	/**
	 * 
	 * @Title initViews
	 * @Description 初始化界面，对界面进行赋值等操作
	 */
	public abstract void initViews();

	/**
	 * 
	 * @Title initViews
	 * @Description 初始化界面，对界面进行赋值等操作
	 */
	public abstract void initData();

	/**
	 * 
	 * @Title initViews
	 * @Description 初始化界面，对界面进行赋值等操作
	 */
	public abstract void bindViews();

	/**
	 * @Title JumpToActivity
	 * @Description 不带参数的Activity界面跳转
	 * @param cls
	 *            跳转的目标界面
	 * @param isEffect
	 *            是否有效果
	 */
	public void JumpToActivity(Class<?> cls, boolean isEffect) {
		JumpToActivity(cls, null, isEffect);
	}

	/**
	 * 
	 * @Title JumpToActivity
	 * @Description 带参数的界面跳转
	 * @param cls
	 *            跳转的目标界面
	 * @param obj
	 *            带过去的界面参数
	 * @param isEffect
	 *            是否有效果
	 */
	public void JumpToActivity(Class<?> cls, Object obj, boolean isEffect) {
		Intent intent = new Intent(this, cls);
		// 设置跳转标志为如此Activity存在则把其从任务堆栈中取出放到最上方
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		if (obj != null)
			intent.putExtra("data", (Serializable) obj);
		startActivity(intent);
		if (isEffect)
			setOverridePendingTransition();
	}

	/**
	 * 
	 * @Title JumpToActivityForResult
	 * @Description 带返回结果的界面跳转
	 * @param cls
	 *            界面目标跳转目标界面
	 * @param requestCode
	 *            请求参数
	 * @param isEffect
	 *            是否有效果
	 * 
	 */
	public void JumpToActivityForResult(Class<?> cls, int requestCode,
			boolean isEffect) {
		JumpToActivityForResult(cls, null, requestCode, isEffect);
	}

	/**
	 * 
	 * @Title JumpToActivityForResult
	 * @Description 带返回结果的界面跳转
	 * @param cls
	 *            目标界面
	 * @param obj
	 *            带过去的参数
	 * @param requestCode
	 *            请求参数
	 * @param isEffect
	 *            是否有效果
	 */
	public void JumpToActivityForResult(Class<?> cls, Object obj,
			int requestCode, boolean isEffect) {
		Intent intent = new Intent(this, cls);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);// 设置跳转标志为如此Activity存在则把其从任务堆栈中取出放到最上方
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		if (obj != null)
			intent.putExtra("data", (Serializable) obj);
		startActivityForResult(intent, requestCode);
		if (isEffect)
			setOverridePendingTransition();
	}

	private void setOverridePendingTransition() {
		// default
		overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_in);
		// overridePendingTransition(android.R.anim.slide_in_left,
		// android.R.anim.slide_out_right);
		// overridePendingTransition(R.anim.opt_slide_in, R.anim.opt_slide_out);
		// overridePendingTransition(R.anim.opt_zoom_in, R.anim.opt_zoom_out);
	}

	public void showToast(String message) {
		toast = Toast.makeText(getApplicationContext(), message,
				Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	public void showToastInThread(String message) {
		msgHandler.obtainMessage(0, message).sendToTarget();
	}

	public void showLoadingDialog() {
		dialog = new MyProgressDialog(this, true);
		dialog.show();
	}

	public void showLoadingDialog(String title) {
		dialog = new MyProgressDialog(this, title, true);
		dialog.show();
	}

	public void dismissLoadingDialog() {
		if (dialog != null) {
			dialog.dismiss();
		}
	}

	public boolean isLoadingDialogShowing() {
		if (dialog != null)
			return dialog.isShowing();
		else
			return false;
	}

	public interface OnSurePress {
		void onClick(View view);
	}

	Handler msgHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			toast = Toast.makeText(getApplicationContext(), msg.obj.toString(),
					Toast.LENGTH_SHORT);
			toast.show();
		};
	};

	/**
	 * 确认对话框
	 * 
	 * @param title
	 * @param btn
	 * @param sureDo
	 */
	public void showSureDialog(String title, String btn,
			final OnSurePress sureDo) {
		showSureDialog(appContext, title, btn, sureDo, null);

	}

	/**
	 * 确认对话框
	 * 
	 * @param context
	 * @param title
	 * @param btn
	 * @param sureDo
	 * @param cancel
	 */
	@SuppressLint("InflateParams")
	public void showSureDialog(Context context, String title, String btn,
			final OnSurePress sureDo, OnCancelListener cancel) {
		final Dialog dialog = new Dialog(context, R.style.MySureDialog);
		View view = getLayoutInflater().inflate(R.layout.dialog_sure, null);
		dialog.setContentView(view);
		TextView WTitle = (TextView) view.findViewById(R.id.title);
		WTitle.setText(title);
		Button sureButton = (Button) view.findViewById(R.id.btn_show);
		sureButton.setText(btn);
		sureButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sureDo.onClick(v);
				dialog.dismiss();
			}
		});

		if (cancel != null)
			dialog.setOnCancelListener(cancel);
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}

	/**
	 * 设置图像
	 * 
	 * @param searchAlbumDo
	 * @param takePhotoDo
	 */
	public void showPhotoDialog(final OnSurePress searchAlbumDo,
			final OnSurePress takePhotoDo) {
		new AlertDialog.Builder(BaseActivity.this)
				.setTitle(getResString(R.string.photo_set))
				.setIcon(R.drawable.default_image)
				.setNeutralButton(getResString(R.string.photo_album),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								searchAlbumDo.onClick(null);
								dialog.dismiss();
								// if (MediaUtil.isSDCardExisd()) {
								// MediaUtil.searhcAlbum(MyNoticeActivity.this,
								// MediaUtil.ALBUM);
								// } else {
								// showToast("SDCARD不存在！");
								// }
							}
						})
				.setPositiveButton(getResString(R.string.photo_takephoto),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int whichButton) {
								takePhotoDo.onClick(null);
								dialog.dismiss();
								// if (MediaUtil.isSDCardExisd()) {
								// MediaUtil.takePhoto(MyNoticeActivity.this,
								// MediaUtil.PHOTO, appPath, userPhotoName);
								// } else {
								// showToast("SDCARD不存在！");
								// }
							}
						})
				.setNegativeButton(getResString(R.string.cancle), null).show()
				.setCanceledOnTouchOutside(true);
	}

	/**
	 * EditText内容为空提示
	 * 
	 * @param edit
	 * @param showToast
	 */
	public boolean cantEmpty(EditText edit, int showToast) {
		if (StringUtils.isStringNone(edit.getText().toString())) {
			UIHelper.ShowMessage(context, getResString(showToast));
			return true;
		}
		return false;
	}

	public String getResString(int strId) {
		return getResources().getString(strId);
	}

	public int getResColor(int colId) {
		return getResources().getColor(colId);
	}

	@Override
	public boolean onKeyDown(int keycode, KeyEvent keyevent) {
		if (keycode == KeyEvent.KEYCODE_BACK) {

			if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
				getSupportFragmentManager().popBackStack();
				return true;
			} else if (context instanceof MainActivity) {
				if (GlobleVariable.isBackPress) {
					finish();
				} else {
					showToast("再按一次退出应用");
					BackHandler.sendEmptyMessageDelayed(0, 2000);
					GlobleVariable.isBackPress = true;
				}

			} else {
				finish();
			}
		}
		return false;
	}

	Handler BackHandler = new Handler() {
		@Override
		public void handleMessage(Message paramMessage) {
			GlobleVariable.isBackPress = false;
		}
	};

	/**
	 * 上传图片 放在BaseActivity可以任何时候在后台维持线程
	 * 
	 * @param path
	 *            文件路径
	 */
	public void upLoadPic(String path, IHttpTaskCallBack httpCallBack) {
		HttpRequestFactory.getInstance().uploadFile(httpCallBack, path);
	}

	/**
	 * fragment接收从activity启动的带返回值activity的返回结果
	 */
	public interface IActivityResult {
		void setActivityResult(int requestCode, int resultCode, Intent data);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// onresume时，取消notification显示
		EMChatManager.getInstance().activityResumed();
		UmengUtil.onBaseResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		UmengUtil.onBasePause(this);
	}
}
