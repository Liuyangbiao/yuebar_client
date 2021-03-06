package com.fullteem.yueba.app.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.alibaba.fastjson.JSONObject;
import com.fullteem.yueba.R;
import com.fullteem.yueba.app.adapter.BaseListAdapter;
import com.fullteem.yueba.engine.upload.PhotoUtil;
import com.fullteem.yueba.engine.upload.UploadManager;
import com.fullteem.yueba.model.ResponeModel;
import com.fullteem.yueba.model.UploadModel;
import com.fullteem.yueba.net.Urls;
import com.fullteem.yueba.net.http.AsyncHttpResponseHandler;
import com.fullteem.yueba.net.http.CustomAsyncResponehandler;
import com.fullteem.yueba.net.http.HttpRequestFactory;
import com.fullteem.yueba.util.CuttingPicturesUtil;
import com.fullteem.yueba.util.DisplayUtils;
import com.fullteem.yueba.util.ImageLoaderUtil;
import com.fullteem.yueba.util.JsonUtil;
import com.fullteem.yueba.widget.CommonPopWindow;
import com.fullteem.yueba.widget.TopTitleView;

/**
 * 编辑用户
 * 
 * @author Administrator
 * 
 */
public class EditUserMoodActivity extends BaseActivity {

	public EditUserMoodActivity() {
		super(R.layout.activity_editmood);
	}

	private GridView gridView;
	private EditText editMood;
	private List<String> localImgUrisList;
	private List<String> imgUrisList;
	private GridMoodAdapter gridAdapter;
	private UploadModel upModel;
	private CommonPopWindow popMenu;
	private PopupWindow popupWindow;

	public boolean isClickable;

	// 上传图片的最大Size
	private int totalSize;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// 全屏在代码中设置
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void initViews() {
		isClickable = true;
		localImgUrisList = new ArrayList<String>();
		imgUrisList = new ArrayList<String>();
		gridView = (GridView) findViewById(R.id.gridMood);
		editMood = (EditText) findViewById(R.id.editMood);
	}

	@Override
	public void initData() {
		checkPic();
		gridAdapter = new GridMoodAdapter(this, localImgUrisList);
		gridView.setAdapter(gridAdapter);

	}

	/**
	 * 判断+号放在哪
	 */
	private void checkPic() {
		// 最多展示八张图
		if (localImgUrisList.size() < 8) {
			// 添加最后一张图为添加按钮
			String str = "add";
			localImgUrisList.add(str);
		}
	}

	@Override
	public void bindViews() {
		// for title
		final TopTitleView topTitle = (TopTitleView) findViewById(R.id.topTitle);
		topTitle.showLeftImag(R.drawable.back, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		topTitle.showCenterText(getString(R.string.mood), null);

		// 自己进入心情界面可以新增心情
		topTitle.showRightText(getString(R.string.save),
				R.style.toptitle_right, new OnClickListener() {
					@Override
					public void onClick(View v) {
						// 防止同一张图片被多次点击，导致多次上传
						if (!isClickable) {
							return;
						}
						isClickable = false;
						// 心情页面点击保存
						// 开始上传图片
						totalSize = localImgUrisList.size();
						imgUrisList.clear();
						if (totalSize == 0
								|| "add".equalsIgnoreCase(localImgUrisList
										.get(0))) {
							String text = editMood.getText().toString();
							sendRequest(text, "");
						}

						PhotoUtil.printTrace("begin to upload photos, size:"
								+ totalSize);
						for (int i = 0; i < totalSize; i++) {
							if (!"add".equalsIgnoreCase(localImgUrisList.get(i))) {
								upLoadPic(localImgUrisList.get(i), i);
							}
						}

					}

				});

	}

	/**
	 * 发送心情
	 */
	private void sendRequest(String text, String url) {
		if (TextUtils.isEmpty(text)) {
			showToast(getString(R.string.hint_pleaseInputMood));
			return;
		}

		PhotoUtil.printTrace("begin to send update request, text:" + text);

		JSONObject jo = new JSONObject();
		jo.put("moodRecordText", text);
		jo.put("moodRecordImgUrl", url);
		jo.put("userId", appContext.getUserInfo().getUserId());
		HttpRequestFactory.getInstance().postRequest(Urls.UPDATE_USER_MOOD, jo,
				MoodRequestHandler);
	}

	AsyncHttpResponseHandler MoodRequestHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(String content) {
			PhotoUtil.printTrace(content);
		};

		@Override
		public void onFinish() {
			EditUserMoodActivity.this.finish();
		};
	};

	/**
	 * 展示popwindow
	 * 
	 * @param position
	 */
	private void showPopWindow(final int position) {
		popMenu = new CommonPopWindow(this, "是否删除此图片？", new OnClickListener() {
			@Override
			public void onClick(View v) {
				localImgUrisList.remove(position);
				gridAdapter.notifyDataSetChanged();
				popupWindow.dismiss();
			}
		}, new OnClickListener() {

			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
			}
		});

		// 非文本
		popMenu.setIsEditText(false);

		// 准备工作
		popMenu.preperShow();
		popupWindow = popMenu.getMenu();
		popupWindow.setOutsideTouchable(true);
		if (popupWindow == null) {
			return;
		}

		if (popupWindow.isShowing()) {
			popupWindow.dismiss();
			return;
		}
		popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER,
				0, 0);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case CuttingPicturesUtil.LOCAL_PHOTO:
			if (data == null)
				return;

			Uri originalUri = data.getData();
			PhotoUtil.printTrace("originalUri:" + originalUri.toString());

			String path = UploadManager.fetchImagePath(this, originalUri);
			PhotoUtil.printTrace(path);

			addPictures(path);

			gridAdapter.notifyDataSetChanged();
			if (TextUtils.isEmpty(editMood.getText().toString()))
				editMood.setText(getString(R.string.mood_noneNormal));

			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 上传方法
	 * 
	 * @param path
	 * @param handlerCode
	 */
	private void upLoadPic(String path, final int CurrentPosition) {
		path = path.substring(7, path.length());
		// 向SD卡中写入图片缓存
		PhotoUtil.printTrace("enter upLoadPic:" + path);
		File file = new File(path);
		// Bitmap bitmap = null;
		// 若该文件存在
		if (file.exists()) {
			// bitmap = BitmapFactory.decodeFile(path);
		}
		HttpRequestFactory.getInstance(this).uploadFile(this, file,
				new CustomAsyncResponehandler() {

					@Override
					public void onStart() {
					}

					@Override
					public void onSuccess(String content) {
						super.onSuccess(content);
						PhotoUtil.printTrace(content);
					}

					@Override
					public void onFailure(Throwable error, String content) {
					}

					@Override
					public void onFinish() {
					}

					@Override
					public void onSuccess(ResponeModel baseModel) {
						PhotoUtil.printTrace("succeed to upload pic");
						System.out.println("succeed to upload pic");
						upModel = new UploadModel();
						upModel = JsonUtil.convertJsonToObject(
								baseModel.getJson(), UploadModel.class);
						if (!TextUtils.isEmpty(upModel.getResult()
								.getSmallImageFile()))
							uploadHandler.obtainMessage(CurrentPosition,
									upModel).sendToTarget();
					}

				});
	}

	// 添加相册相片的方法
	private void addPictures(String uri) {
		localImgUrisList.remove(localImgUrisList.size() - 1);
		localImgUrisList.add("file://" + uri);
		// 最多展示八张图
		checkPic();
	}

	class GridMoodAdapter extends BaseListAdapter<String> {

		public GridMoodAdapter(Activity context, List<String> list) {
			super(context, list);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(EditUserMoodActivity.this)
						.inflate(R.layout.adapter_dynamic_grid, null);
			}
			String imgUrl = localImgUrisList.get(position);
			ImageView imgView = (ImageView) convertView
					.findViewById(R.id.imgMyPics);

			int width = DisplayUtils.getScreenWidht(EditUserMoodActivity.this);
			LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(
					width / 4, width / 4);
			ll.setMargins(2, 2, 2, 2);
			ll.gravity = Gravity.CENTER;
			imgView.setLayoutParams(ll);

			final int currentPosition = position;
			if ("add".equalsIgnoreCase(imgUrl)) {
				imgView.setScaleType(ScaleType.FIT_XY);
				ImageLoaderUtil.getImageLoader().displayImage(
						"drawable://" + R.drawable.addgroup, imgView);
				convertView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						if (CuttingPicturesUtil.isSDCardExisd()) {
							CuttingPicturesUtil.searhcAlbum(
									EditUserMoodActivity.this,
									CuttingPicturesUtil.LOCAL_PHOTO);
						} else {
							showToast(getString(R.string.sdcard_not_exsit));
						}
					}
				});
			} else {
				ImageLoaderUtil.getImageLoader().displayImage(
						DisplayUtils.getAbsolutelyUrl(imgUrl), imgView);
				convertView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// showToast("remove");
						showPopWindow(currentPosition);
					}
				});

			}
			return convertView;
		}
	}

	/**
	 * 上传结束后的handler处理事件
	 */
	Handler uploadHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			PhotoUtil.printTrace("after upload finished");

			upModel = (UploadModel) msg.obj;
			imgUrisList.add(upModel.getResult().getSmallImageFile());
			if (imgUrisList.size() < ("add".equals(localImgUrisList
					.get(localImgUrisList.size() - 1)) ? localImgUrisList
					.size() - 1 : localImgUrisList.size()))
				return;
			// 说明上传图片完毕
			// if (msg.what == totalSize - 2) {
			String text = editMood.getText().toString();
			String urls = null;
			for (int i = 0; i < imgUrisList.size(); i++) {
				urls = urls == null ? imgUrisList.get(i) : urls + ","
						+ imgUrisList.get(i);
			}
			sendRequest(text, urls);
			// }
		};
	};

}
