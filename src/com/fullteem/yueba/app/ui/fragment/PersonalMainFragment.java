package com.fullteem.yueba.app.ui.fragment;

import java.io.File;
import java.io.IOException;

import org.robobinding.ViewBinder;
import org.robobinding.binder.BinderFactory;
import org.robobinding.binder.BinderFactoryBuilder;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONObject;
import com.fullteem.yueba.R;
import com.fullteem.yueba.app.AppContext;
import com.fullteem.yueba.app.ui.BaseActivity;
import com.fullteem.yueba.app.ui.ChangePasswdActivity;
import com.fullteem.yueba.app.ui.ConsumeRecordsActivity;
import com.fullteem.yueba.app.ui.GiftActivity;
import com.fullteem.yueba.app.ui.MainActivity;
import com.fullteem.yueba.app.ui.NewFriendsMsgActivity;
import com.fullteem.yueba.app.ui.OrderManageActivity;
import com.fullteem.yueba.app.ui.PerssonalInfoAcitivity;
import com.fullteem.yueba.app.ui.RechargRecordsActivity;
import com.fullteem.yueba.app.ui.ScoreQueryActivity;
import com.fullteem.yueba.app.ui.SetActivity;
import com.fullteem.yueba.engine.upload.PhotoUtil;
import com.fullteem.yueba.entry.LoginActivity;
import com.fullteem.yueba.globle.GlobleConstant;
import com.fullteem.yueba.model.ResponeModel;
import com.fullteem.yueba.model.UploadModel;
import com.fullteem.yueba.model.UploadModel.UploadDataModel;
import com.fullteem.yueba.model.User;
import com.fullteem.yueba.model.UserCommonModel;
import com.fullteem.yueba.model.UserInfoModel;
import com.fullteem.yueba.model.presentmodel.PersonalMainPresentModel;
import com.fullteem.yueba.model.presentmodel.PerssonalInfoPresentModel;
import com.fullteem.yueba.net.Urls;
import com.fullteem.yueba.net.http.AsyncHttpResponseHandler;
import com.fullteem.yueba.net.http.CustomAsyncResponehandler;
import com.fullteem.yueba.net.http.HttpRequestFactory;
import com.fullteem.yueba.util.CuttingPicturesUtil;
import com.fullteem.yueba.util.DisplayUtils;
import com.fullteem.yueba.util.FileUtils;
import com.fullteem.yueba.util.ImageLoaderUtil;
import com.fullteem.yueba.util.JsonUtil;
import com.fullteem.yueba.util.UmengUtil;
import com.fullteem.yueba.widget.TopTitleView;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年11月26日&emsp;下午3:21:25
 * @use 主界面->我
 */
public class PersonalMainFragment extends Fragment {
	private PersonalMainPresentModel presentMode;

	public PersonalMainFragment() {
	}

	private static PersonalMainFragment instance;

	public static PersonalMainFragment getInstance() {
		if (instance == null) {
			instance = new PersonalMainFragment();
		}
		return instance;
	}

	private TopTitleView topTitle;
	private static ImageView usrHeader;
	private LinearLayout llPerssonalInfo, llOrderManage, llGift,
			llRechargRecords, llScoreQuery, llConsumptionRecords, llDeleteChat,
			llChangePwd, llSetSys, ll_system_msg;
	private Button btnLogout;
	private EventListener mListener;

	private Bitmap userPhoto;
	private String logoUrlStr;
	private PerssonalInfoPresentModel personalModel;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		BinderFactory binderFactory = new BinderFactoryBuilder().build();
		ViewBinder vb = binderFactory.createViewBinder(getActivity(), true);
		presentMode = new PersonalMainPresentModel();
		personalModel = new PerssonalInfoPresentModel(
				UserInfoModel.getInstance());
		View rootView = vb.inflateAndBind(R.layout.fragment_perssonal_main,
				presentMode);
		initViews(rootView);
		initDatq();
		bindViews();
		return rootView;
	}

	private void initViews(View rootView) {
		topTitle = (TopTitleView) rootView.findViewById(R.id.top_title);
		usrHeader = (ImageView) rootView.findViewById(R.id.iv_usrHeader);
		btnLogout = (Button) rootView.findViewById(R.id.btn_logout);
		llPerssonalInfo = (LinearLayout) rootView
				.findViewById(R.id.ll_perssonalInfo);
		llOrderManage = (LinearLayout) rootView
				.findViewById(R.id.ll_orderManage);
		llGift = (LinearLayout) rootView.findViewById(R.id.ll_gift);
		llRechargRecords = (LinearLayout) rootView
				.findViewById(R.id.ll_rechargRecords);
		llScoreQuery = (LinearLayout) rootView.findViewById(R.id.ll_scoreQuery);
		llConsumptionRecords = (LinearLayout) rootView
				.findViewById(R.id.ll_consumptionRecords);
		llDeleteChat = (LinearLayout) rootView.findViewById(R.id.ll_deleteChat);
		llChangePwd = (LinearLayout) rootView.findViewById(R.id.ll_changePwd);
		llSetSys = (LinearLayout) rootView.findViewById(R.id.ll_setSys);

		// 系统消息
		ll_system_msg = (LinearLayout) rootView
				.findViewById(R.id.ll_system_msg);
	}

	private void initDatq() {
		mListener = new EventListener();
		topTitle.showCenterText(
				getActivity().getString(R.string.personal_main), null);

		// --------------------------examples data--------------------------

		presentMode.setCharm(getString(R.string.gold_remnants_normal));
		presentMode.setUsrNickname(getString(R.string.singer_name_normal));

		// --------------------------examples data--------------------------
	}

	private void bindViews() {
		btnLogout.setOnClickListener(mListener);
		llChangePwd.setOnClickListener(mListener);
		llOrderManage.setOnClickListener(mListener);
		llGift.setOnClickListener(mListener);
		llRechargRecords.setOnClickListener(mListener);
		llScoreQuery.setOnClickListener(mListener);
		llConsumptionRecords.setOnClickListener(mListener);
		llSetSys.setOnClickListener(mListener);
		llPerssonalInfo.setOnClickListener(mListener);

		ll_system_msg.setOnClickListener(mListener);
		usrHeader.setOnClickListener(mListener);
	}

	private class EventListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.iv_usrHeader:
				PhotoUtil.printTrace("user head photo is clicked");
				if (CuttingPicturesUtil.isSDCardExisd()) {
					// CuttingPicturesUtil.searhcAlbum(getActivity(),
					// CuttingPicturesUtil.LOCAL_PHOTO);
					Intent intent = new Intent(Intent.ACTION_PICK, null);
					intent.setDataAndType(
							MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
							"image/*");
					startActivityForResult(intent,
							CuttingPicturesUtil.LOCAL_PHOTO);
				} else {
					((BaseActivity) getActivity())
							.showToast(getString(R.string.sdcard_not_exsit));
				}
				break;

			case R.id.ll_changePwd:
				startActivity(new Intent(getActivity(),
						ChangePasswdActivity.class));
				break;

			case R.id.ll_orderManage:
				startActivity(new Intent(getActivity(),
						OrderManageActivity.class));
				break;

			case R.id.ll_gift:
				startActivity(new Intent(getActivity(), GiftActivity.class));
				break;

			case R.id.ll_rechargRecords:
				startActivity(new Intent(getActivity(),
						RechargRecordsActivity.class));
				break;

			case R.id.ll_scoreQuery:
				startActivity(new Intent(getActivity(),
						ScoreQueryActivity.class));
				break;

			case R.id.ll_consumptionRecords:
				startActivity(new Intent(getActivity(),
						ConsumeRecordsActivity.class));
				break;

			case R.id.ll_setSys:
				startActivity(new Intent(getActivity(), SetActivity.class));
				break;

			case R.id.ll_perssonalInfo:
				Intent intentPerssonalInfo = new Intent(getActivity(),
						PerssonalInfoAcitivity.class);
				intentPerssonalInfo.putExtra("SHOW_BOTTOM_BAR", true);
				startActivity(intentPerssonalInfo);
				break;

			// 系统消息
			case R.id.ll_system_msg:
				startActivity(new Intent(getActivity(),
						NewFriendsMsgActivity.class));
				// 设置未读消息树为0，刷新消息提示
				User user = ((MainActivity) getActivity()).appContext
						.getContactList().get(
								GlobleConstant.NEW_FRIENDS_USERNAME);
				user.setUnreadMsgCount(0);
				((MainActivity) getActivity()).updateUnReadLableInPersonal();
				break;

			// 退出客户端
			case R.id.btn_logout:
				Intent intent = new Intent(getActivity(), LoginActivity.class);
				AppContext.getApplication().logout();
				// 清除LoginActivity
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				getActivity().finish();
				break;
			}
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			switch (requestCode) {
			case CuttingPicturesUtil.CAMERA:
				File temp = new File(GlobleConstant.USERPHOTO_DIR
						+ GlobleConstant.USERPHOTO_NAME);
				startActivityForResult(
						CuttingPicturesUtil.startPhotoZoom(getActivity(),
								Uri.fromFile(temp)),
						CuttingPicturesUtil.ZOOMPHOTO);
				break;
			case CuttingPicturesUtil.ZOOMPHOTO:
				Log.e("REGISTER", "start zoom");
				// userOldPhoto = userPhoto;
				userPhoto = data.getExtras().getParcelable("data");
				usrHeader.setImageBitmap(userPhoto);
				try {
					FileUtils.saveBitmapToPath(userPhoto,
							GlobleConstant.USERPHOTO_DIR,
							GlobleConstant.USERPHOTO_NAME);
					System.out.println("开始图片上传");
					upLoadPic();

				} catch (IOException e) {
					Log.e("REGISTER", e.toString());
					e.printStackTrace();
				}
				break;
			case CuttingPicturesUtil.LOCAL_PHOTO:
				startActivityForResult(
						CuttingPicturesUtil.startPhotoZoom(getActivity(),
								data.getData()), CuttingPicturesUtil.ZOOMPHOTO);
				break;
			}

		}
	}

	/**
	 * 上传图片
	 */
	public void upLoadPic() {
		File file = new File(PhotoUtil.USER_PHOTO_PATH);
		HttpRequestFactory.getInstance().uploadFile(getActivity(), file,
				new CustomAsyncResponehandler() {

					@Override
					public void onStart() {
					}

					@Override
					public void onSuccess(String content) {
						super.onSuccess(content);

					}

					@Override
					public void onFailure(Throwable error, String content) {
						PhotoUtil.printTrace("upload pic: on failure:"
								+ content);
					}

					@Override
					public void onFinish() {
					}

					@Override
					public void onSuccess(ResponeModel baseModel) {
						PhotoUtil.printTrace("upload pic: on success");

						UploadModel uploadModel = new UploadModel();
						uploadModel = JsonUtil.convertJsonToObject(
								baseModel.getJson(), UploadModel.class);

						if (uploadModel != null
								&& uploadModel.getResult() != null) {
							PhotoUtil.printTrace("uploadModel not null");
							UploadDataModel upModel = uploadModel.getResult();
							logoUrlStr = upModel.getSmallImageFile();
							UserCommonModel userInfoModel = AppContext
									.getApplication().getUserInfo();
							userInfoModel.setUserLogoUrl(logoUrlStr);

							saveUserHeaderPic();
							Message msg = Message.obtain();
							msg.what = PhotoUtil.HEADER_CODE;
							msg.obj = logoUrlStr;
							uploadHandler.sendMessage(msg);
						}
					}

				});
	}

	protected void saveUserHeaderPic() {
		// TODO Auto-generated method stub
		JSONObject jo = new JSONObject();
		jo.put("userId", AppContext.getApplication().getUserInfo().getUserId());
		jo.put("userMobile", AppContext.getApplication().getUserInfo()
				.getUserMobile());
		jo.put("userName", AppContext.getApplication().getUserInfo()
				.getUserName());
		jo.put("userLogoUrl",
				PhotoUtil.filterUserLogoUrl(AppContext.getApplication()
						.getUserInfo().getUserLogoUrl()));
		System.out.println("userLogoUrl "
				+ PhotoUtil.filterUserLogoUrl(AppContext.getApplication()
						.getUserInfo().getUserLogoUrl()));
		HttpRequestFactory.getInstance().postRequest(Urls.UPDATE_USER_INFO, jo,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String content) {
						super.onSuccess(content);
					}

					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
					}
				});
	}

	private static Handler uploadHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case PhotoUtil.HEADER_CODE:
				ImageLoaderUtil.getImageLoader().displayImage(
						DisplayUtils.getAbsolutelyUrl(AppContext
								.getApplication().getUserInfo()
								.getUserLogoUrl()), usrHeader,
						ImageLoaderUtil.getOptions());
				break;

			default:
				break;
			}
		};
	};

	@Override
	public void onResume() {
		super.onResume();
		 UmengUtil.onPageStart(getActivity(),"MainScreen");
		if (AppContext.getApplication().getUserInfo() == null)
			return;
		if (!TextUtils.isEmpty(AppContext.getApplication().getUserInfo()
				.getUserLogoUrl()))
			ImageLoaderUtil.getImageLoader().displayImage(
					DisplayUtils.getAbsolutelyUrl(AppContext.getApplication()
							.getUserInfo().getUserLogoUrl()), usrHeader,
					ImageLoaderUtil.getOptions());
		presentMode.setUsrNickname(AppContext.getApplication().getUserInfo()
				.getUserName());

	}
	
	@Override
	public void onPause() {
		super.onPause();
		UmengUtil.onPageEnd(getActivity(),"MainScreen");
	}
}
