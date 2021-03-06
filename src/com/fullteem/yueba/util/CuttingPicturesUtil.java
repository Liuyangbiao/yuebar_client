package com.fullteem.yueba.util;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.fullteem.yueba.R;
import com.fullteem.yueba.engine.upload.PhotoUtil;
import com.fullteem.yueba.globle.GlobleConstant;

/**
 * 裁剪图片util，共享一些裁剪图片的方法
 * 
 * @author ssy
 * 
 */
public class CuttingPicturesUtil {
	public static final int CAMERA = 121;
	public static final int ZOOMPHOTO = 122;
	public static final int LOCAL_PHOTO = 123;
	public static final int UPLOAD_PIC = 124;
	public static final int HEADER_PIC = 125;
	public static final int ZOOMPHOTOHEAD = 126;

	/**
	 * 判断SD卡是否存在
	 * 
	 * @return
	 */
	public static boolean isSDCardExisd() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		} else
			return false;
	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public static Intent startPhotoZoom(Context context, Uri uri) {
		PhotoUtil.printTrace("enter CuttingPicturesUtil:startPhotoZoom. url"
				+ uri.toString());
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);
		intent.putExtra("return-data", true);
		File dir = new File((GlobleConstant.USERPHOTO_DIR));
		if (!dir.exists()) {
			dir.mkdirs();
			PhotoUtil.printTrace("created photo dir:" + dir.getPath());
		}

		String tmp = "userPhoto";// userPhoto.jpg//
									// GlobleConstant.USERPHOTO_NAME)
		File saveFile = new File(GlobleConstant.USERPHOTO_DIR
				+ GlobleConstant.USERPHOTO_NAME);

		if (saveFile.exists()) {
			PhotoUtil.printTrace("saveFile exist:" + saveFile.getPath());
		} else {
			PhotoUtil.printTrace("saveFile does not exist");
		}

		intent.putExtra("output", Uri.fromFile(saveFile)); // 传入目标文件
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString()); // 输入文件格式
		Intent it = Intent.createChooser(intent,
				context.getString(R.string.cuttingPic));
		return it;
		// context.startActivityForResult(it, ZOOMPHOTO);
	}

	/**
	 * 调用相机
	 * 
	 * @param activity
	 * @param flag
	 */
	public static void searhcAlbum(Activity activity, int flag) {
		PhotoUtil.printTrace("enter CuttingPicturesUtil:searhcAlbum");
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				"image/*");
		activity.startActivityForResult(intent, flag);
	};

}
