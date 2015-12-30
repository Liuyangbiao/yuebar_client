package com.fullteem.yueba.util;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

public class MediaUtil {
	public static final int ALBUM = 1;
	public static final int PHOTO = 2;
	public static final int ZOOMPHOTO = 3;
	public static String ROOTPATH = Environment.getExternalStorageDirectory()
			.getAbsolutePath();

	public static boolean isSDCardExisd() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		} else
			return false;
	}

	public static void takePhoto(Activity activity, int flag, String filePath,
			String fileName) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// 下面这句指定调用相机拍照后的照片存储的路径
		final String dir = ROOTPATH + filePath;
		File file = new File(dir);
		if (!file.exists()) {
			file.mkdirs();
		}

		intent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(new File(dir, fileName)));
		activity.startActivityForResult(intent, flag);
	};

	public static void searhcAlbum(Activity activity, int flag) {
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				"image/*");
		activity.startActivityForResult(intent, flag);
	};
}
