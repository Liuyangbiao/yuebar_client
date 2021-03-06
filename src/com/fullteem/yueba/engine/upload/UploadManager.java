package com.fullteem.yueba.engine.upload;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.MediaColumns;
import android.text.TextUtils;
import android.util.Log;

import com.fullteem.yueba.app.AppContext;
import com.fullteem.yueba.globle.GlobleConstant;
import com.fullteem.yueba.model.AlbumModel;
import com.fullteem.yueba.model.AlbumPhotoModel;
import com.fullteem.yueba.model.GroupAlbumModel;
import com.fullteem.yueba.model.ResponeModel;
import com.fullteem.yueba.model.UploadModel;
import com.fullteem.yueba.model.UploadModel.UploadDataModel;
import com.fullteem.yueba.net.http.CustomAsyncResponehandler;
import com.fullteem.yueba.net.http.HttpRequest;
import com.fullteem.yueba.net.http.HttpRequestFactory;
import com.fullteem.yueba.util.DisplayUtils;
import com.fullteem.yueba.util.FileUtils;
import com.fullteem.yueba.util.ImageUtils;
import com.fullteem.yueba.util.JsonUtil;
import com.fullteem.yueba.util.LogUtil;

public class UploadManager {

	private static UploadManager instance;
	private static Context context;

	public static UploadManager getInstance(Context context) {
		if (instance == null) {
			instance = new UploadManager();
		}

		setContext(context);
		return instance;
	}

	private static void setContext(Context ct) {
		context = ct;
	}

	private UploadManager() {
	}

	/*
	 * compress image
	 */
	public File compressImage(File file) {
		PhotoUtil.printTrace("enter compressImage");

		Bitmap bitUploadPic = ImageUtils.getCompressBitmapByFile(
				file.getAbsolutePath(), DisplayUtils.getScreenWidht(context),
				DisplayUtils.getScreenHeight(context));
		bitUploadPic = ImageUtils.compressBitmap(bitUploadPic, 100); // 图片大于100kb则压缩
		if (bitUploadPic != null) {
			String fileName = java.util.UUID.randomUUID() + file.getName();
			LogUtil.LogDebug("PerssonalInfoAcitivity",
					"old name:" + file.getName() + " new name:" + fileName,
					null);
			try {
				FileUtils.saveBitmapToPath(bitUploadPic,
						GlobleConstant.USERPHOTO_DIR, fileName);
			} catch (IOException e1) {
				System.out.println(e1.toString());
				e1.printStackTrace();
			}

			file = new File(GlobleConstant.USERPHOTO_DIR + fileName);
		}
		System.out.println("图片上传的路径::" + file.getAbsolutePath());
		LogUtil.LogDebug("PerssonalInfoAcitivity",
				"full path" + file.getAbsolutePath(), null);

		return file;
	}

	/**
	 * 上传图片
	 */
	public void upLoadPic(String path, final int handlerCode,
			final Handler uploadHandler) {
		PhotoUtil.printTrace("enter upLoadPic:" + path);

		File file = new File(path);
		HttpRequestFactory.getInstance(context).uploadFile(context, file,
				new CustomAsyncResponehandler() {

					@Override
					public void onStart() {
					}

					@Override
					public void onSuccess(String content) {
						super.onSuccess(content);
						PhotoUtil.printTrace("success:" + content);
					}

					@Override
					public void onFailure(Throwable error, String content) {
						PhotoUtil.printTrace("fail:" + content);
						Message msg = new Message();
						msg.what = PhotoUtil.UPLOAD_FAIL;
						uploadHandler.sendMessage(msg);
					}

					@Override
					public void onFinish() {
					}

					@Override
					public void onSuccess(ResponeModel baseModel) {
						PhotoUtil.printTrace("upLoadPic on Success");

						UploadModel uploadModel = new UploadModel();
						uploadModel = JsonUtil.convertJsonToObject(
								baseModel.getJson(), UploadModel.class);

						if (uploadModel != null
								&& uploadModel.getResult() != null) {
							UploadDataModel upModel = uploadModel.getResult();

							if (upModel != null) {
								PhotoUtil
										.printTrace("upload model is ok. path:"
												+ upModel.getSmallImageFile());
							}

							uploadHandler.obtainMessage(handlerCode, upModel)
									.sendToTarget();
						}
					}

				});
	}

	public static boolean getAlbumList(List<AlbumModel> listResultAlbum,
			List<AlbumPhotoModel> photoModelList) {
		PhotoUtil.printTrace("enter getAlbumList");

		if (listResultAlbum != null && listResultAlbum.size() > 0
				&& listResultAlbum.get(0) != null) {

			AlbumModel album = listResultAlbum.get(0);

			if (album != null) {
				String photoPaths = album.getUserPhotoImgUrl();
				PhotoUtil.printTrace("photoPaths is:" + photoPaths);
				photoModelList.clear();

				if (!TextUtils.isEmpty(photoPaths)) {
					String[] picUrls = photoPaths.split(",");

					for (String string : picUrls) {
						AlbumPhotoModel photoModel = new AlbumPhotoModel();
						photoModel.setPhotoUrl(string);
						photoModelList.add(photoModel);
						PhotoUtil.printTrace("added photo:" + string);
					}
				}

				return true;

			} else {
				PhotoUtil.printTrace("album is null");
				return false;
			}
		}

		return false;
	}

	public static boolean getAlbumList(PhotoOwnerEnum owner,
			ResponeModel baseModel, List<AlbumPhotoModel> photoModelList) {
		// 群图片
		List<GroupAlbumModel> listResultAlbum = null;
		if (owner == PhotoOwnerEnum.GROUP) {
			listResultAlbum = (List<GroupAlbumModel>) baseModel.getListObject(
					"userGroupImgUrlList", GroupAlbumModel.class);
		} else if (owner == PhotoOwnerEnum.SINGER) {
			listResultAlbum = (List<GroupAlbumModel>) baseModel.getListObject(
					"singerPhototList", GroupAlbumModel.class);
		} else {
			return false;
		}

		return getGroupAlbumList(listResultAlbum, photoModelList);
	}

	public static boolean getGroupAlbumList(
			List<GroupAlbumModel> listResultAlbum,
			List<AlbumPhotoModel> photoModelList) {
		PhotoUtil.printTrace("enter getAlbumList");

		if (listResultAlbum != null && listResultAlbum.size() > 0
				&& listResultAlbum.get(0) != null) {
			photoModelList.clear();
			for (int i = 0; i < listResultAlbum.size(); i++) {
				System.out.println("listResultAlbum: "+listResultAlbum.size());
				GroupAlbumModel album = listResultAlbum.get(i);
				if (album != null) {
					String photoPaths = album.getUserGroupImgUrl();
					if (!TextUtils.isEmpty(photoPaths)) {
						AlbumPhotoModel photoModel = new AlbumPhotoModel();
						photoModel.setPhotoUrl(photoPaths);
						photoModelList.add(photoModel);
						PhotoUtil.printTrace("added photo:" + photoPaths);
					}
				}
			}
			
			return true;
			

		} else {
			PhotoUtil.printTrace("album is null");
			return false;
		}

	}

	/*
	 * update photo
	 */
	public static void updateUserPhoto(String urls,
			final IPhotoHandleCallback callback) {
		PhotoUtil.printTrace("enter updateUserPhoto" + urls);

		String userId = AppContext.getApplication().getUserInfo().getUserId();
		HttpRequest.getInstance().getUserUpPhoto(Integer.valueOf(userId), urls,
				new CustomAsyncResponehandler() {

					@Override
					public void onSuccess(ResponeModel baseModel) {
						if (baseModel == null || !baseModel.isStatus()) {
							PhotoUtil.printTrace("failed to upload photo");
							callback.onFailure();
						} else {
							PhotoUtil.printTrace("succeed to upload photo");
							callback.onSuccess();
						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
						PhotoUtil.printTrace("failed to upload photo:"
								+ content);
						callback.onFailure();
					}
				});
	}

	/*
	 * smallImageFile "/files/20150206/6e189ee8-cbcf-4d34-80d9-f5e221bb77e1.jpg"
	 * (id=830033083248) smallImageFile2
	 * "/data/appAppTemp/20150206/6e189ee8-cbcf-4d34-80d9-f5e221bb77e1.jpg"
	 * (id=830033083072) while photo path in sd card:
	 * "file:///storage/ext_sd/DCIM/100MEDIA/IMAG0004.jpg" (id=830036879040)
	 */
	// uploadedImgList contains the path of uploaded file path in the file
	// system of the server.
	// smallImageFile2 is used just for ali baichun, which will save the photo
	// to other places.
	public static String getPhotoPaths(LinkedList<String> uploadedImgList,
			List<AlbumPhotoModel> photoModelList) {
		PhotoUtil.printTrace("enter getPhotoPaths");

		// first add urls of the existing photos, then add newly uploaded photo
		// url
		String urls = null;
		if (photoModelList != null && photoModelList.size() > 0) {
			String str;
			for (AlbumPhotoModel photoModel : photoModelList) {
				str = photoModel.getPhotoUrl();
				if (TextUtils.isEmpty(str) || str.contains("file://")
						|| str.contains("drawable://"))
					continue;
				urls = urls == null ? str : urls + "," + str;
			}
		}

		PhotoUtil.printTrace("existing photos:" + urls);
		for (int i = 0; i < uploadedImgList.size(); i++) {
			urls = urls == null ? uploadedImgList.get(i) : urls + ","
					+ uploadedImgList.get(i);
		}
		PhotoUtil.printTrace("all photos in the album:" + urls);

		return urls;

	}

	// 添加相册相片的方法
	public static boolean addPictures(List<AlbumPhotoModel> photoModelList,
			String picUrl) {
		PhotoUtil.printTrace("enter addPictures:" + picUrl);

		if (photoModelList == null) {
			return false;
		}

		// first remove the 'add' fake photo.
		if (photoModelList.size() > 0
				&& "add".equals(photoModelList.get(photoModelList.size() - 1)
						.getTypeTag())) {
			photoModelList.remove(photoModelList.size() - 1);
			PhotoUtil.printTrace("remove fake photo");
		}

		if (!TextUtils.isEmpty(picUrl)) {
			AlbumPhotoModel photoModel = new AlbumPhotoModel();
			photoModel.setPhotoUrl("file://" + picUrl);
			photoModelList.add(photoModel);
		}
		// 最多展示八张图
		if (photoModelList.size() < PhotoUtil.MAX_UPLOAD_PHOTO_NUM) {
			// 添加最后一张图为添加按钮
			AlbumPhotoModel photoModel = new AlbumPhotoModel();
			photoModel.setTypeTag("add");
			photoModelList.add(photoModel);
			PhotoUtil.printTrace("add fake photo again");
		}

		// just for test
		displayList(photoModelList);
		return true;
	}

	/*
	 * Not used by now
	 */
	public static void deletePicture(List<AlbumPhotoModel> photoModelList,
			String picUrl) {
		String str;
		for (AlbumPhotoModel photoModel : photoModelList) {
			str = photoModel.getPhotoUrl();
			if (str != null && str.endsWith(picUrl)) {
				PhotoUtil.printTrace("to delete photo:" + str);
				photoModelList.remove(photoModel);
			}

		}
	}

	public static void deleteAllPictures(List<AlbumPhotoModel> photoModelList) {
		if (photoModelList != null) {
			photoModelList.clear();
		}
	}

	private static void displayList(List<AlbumPhotoModel> photoModelList) {
		String str;
		for (AlbumPhotoModel photoModel : photoModelList) {
			str = photoModel.getPhotoUrl();
			PhotoUtil.printTrace("to add photo:" + str);
		}
	}

	public static String fetchImagePath(Activity context, Uri originalUri) {
		PhotoUtil.printTrace("enter fetchImagePath:" + originalUri);

		String retVal = null;
		ContentResolver cr = context.getContentResolver();
		try {

			String[] proj = { MediaColumns.DATA };
			// 好像是android多媒体数据库的封装接口，具体的看Android文档
			Cursor cursor = context.managedQuery(originalUri, proj, null, null,
					null);
			// 获得用户选择的图片的索引值
			int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
			// 将光标移至开头 ，这个很重要，不小心很容易引起越界
			cursor.moveToFirst();
			// 最后根据索引值获取图片路径
			retVal = cursor.getString(column_index);
		} catch (Exception e) {
			PhotoUtil.printTrace("exception in fetchImagePath:"
					+ e.getMessage());
			Log.e("Exception", e.getMessage(), e);
		}

		return retVal;
	}

}
