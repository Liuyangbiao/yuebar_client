package com.fullteem.yueba.app.ui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.robobinding.ViewBinder;
import org.robobinding.binder.BinderFactory;
import org.robobinding.binder.BinderFactoryBuilder;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.fullteem.yueba.R;
import com.fullteem.yueba.app.AppContext;
import com.fullteem.yueba.app.adapter.PerssonalAlbumAdapter;
import com.fullteem.yueba.app.adapter.PerssonalGiftAdapter;
import com.fullteem.yueba.app.adapter.PerssonalVisitorAdapter;
import com.fullteem.yueba.app.singer.model.SingerDetailModel;
import com.fullteem.yueba.app.ui.fragment.MoodDetailsFragment;
import com.fullteem.yueba.db.DBFriendListDao;
import com.fullteem.yueba.engine.upload.IPhotoHandleCallback;
import com.fullteem.yueba.engine.upload.PhotoUtil;
import com.fullteem.yueba.engine.upload.UploadManager;
import com.fullteem.yueba.globle.GlobleConstant;
import com.fullteem.yueba.model.AlbumModel;
import com.fullteem.yueba.model.AlbumPhotoModel;
import com.fullteem.yueba.model.BaseModel;
import com.fullteem.yueba.model.GiftModel;
import com.fullteem.yueba.model.PerssonalInfoModel;
import com.fullteem.yueba.model.PerssonalVisitorModel;
import com.fullteem.yueba.model.PullDownModel;
import com.fullteem.yueba.model.PullDownModel.Constellation;
import com.fullteem.yueba.model.PullDownModel.Hobby;
import com.fullteem.yueba.model.PullDownModel.IndustryName;
import com.fullteem.yueba.model.PullDownModel.MusicStyleName;
import com.fullteem.yueba.model.UploadModel.UploadDataModel;
import com.fullteem.yueba.model.UserCommonModel;
import com.fullteem.yueba.model.UserHobbyListModel;
import com.fullteem.yueba.model.UserInfoModel;
import com.fullteem.yueba.model.UserMapModel;
import com.fullteem.yueba.model.UserMusicListModel;
import com.fullteem.yueba.model.presentmodel.PerssonalInfoPresentModel;
import com.fullteem.yueba.net.Urls;
import com.fullteem.yueba.net.http.AsyncHttpResponseHandler;
import com.fullteem.yueba.net.http.HttpRequestFactory;
import com.fullteem.yueba.util.CuttingPicturesUtil;
import com.fullteem.yueba.util.DisplayUtils;
import com.fullteem.yueba.util.FileUtils;
import com.fullteem.yueba.util.ImageLoaderUtil;
import com.fullteem.yueba.util.LogUtil;
import com.fullteem.yueba.util.SharePreferenceUtil;
import com.fullteem.yueba.util.StringUtils;
import com.fullteem.yueba.util.StringUtils.Gender;
import com.fullteem.yueba.util.UmengUtil;
import com.fullteem.yueba.widget.ChoseDialog;
import com.fullteem.yueba.widget.CommonPopWindow;
import com.fullteem.yueba.widget.TopTitleView;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月11日&emsp;上午9:54:21
 * @use 详细资料页面
 */
public class PerssonalInfoAcitivity extends BaseActivity {
	private PerssonalInfoPresentModel presentMode;
	private RecyclerView recyclerviewAlbum, recyclerviewGift,
			recyclerviewVisitor;
	private ImageView ivArrowRightAlbum, ivArrowLeftAlbum, ivArrowRightGift,
			ivArrowLeftGift, ivArrowRightVisitor, ivArrowLeftVisitor;
	private ImageView ivUsrHeader, ivMoodHeader;
	private RelativeLayout rlMood;
	private Button btnBottomGift, btnBottomDialogue, btnBottomDate /*
																	 * ,
																	 * btnBottomMore
																	 */;
	private TextView tvUserGender;
	private TextView tvUserAge;
	private CheckBox ckbHeart;
	private String userId;
	private TopTitleView topTitle;

	// 可编辑状态，为false时界面不可编辑
	private boolean isEditStatu;

	/***************************************** popwindow *******************************************/
	private PopupWindow PopWindow;
	private CommonPopWindow popMenu;
	private Bitmap userPhoto;

	/******************* 时间选择器 ****************/
	private int mYear = 1980;
	private int mMonth;
	private int mDay;
	private static final int DATE_DIALOG_ID = 1;

	// 经常出没酒吧
	public static final int USUAL_BAR = 701;
	// 喜欢酒水
	public static final int LOVELY_WINE = 702;
	// 个人签名
	public static final int SIGN = 703;

	// 心情
	public static final int MOOD = 704;

	// 判断是否是自己
	private boolean isMySelf;

	// 获取用户是否返回成功
	private boolean isRetrunSuccess;

	private final int PAGE_TOATAL = 5;
	// 是否可以查看心情
	private boolean enableCheckMood;

	/**
	 * 以String为key的map
	 */
	private Map<String, Integer> constellationMap, musicStyleMap,
			industryNameMap, hobbyMap;

	/**
	 * 以ID为key的map
	 */
	private Map<Integer, String> IntKeyConstellMap, IntKeyMusicStyleMap,
			IntKeyIndustryNameMap, IntKeyHobbyMap;

	/**
	 * 存储返回字段的list
	 */
	private ArrayList<String> constellationList, musicStyleList,
			industryNameList, hobbyList;

	private MoodDetailsFragment moodDetailsFragment;

	BaseModel<PerssonalInfoModel> perssonalInfoModel;

	private Context context;
	/*
	 * Handling photos in the album. Operation includes: request to display
	 * enter edit mode to select and upload photo when exit edit mode, save
	 * first then send request to re-display
	 */
	private List<AlbumPhotoModel> photoModelList;// photos in the album
	private PerssonalAlbumAdapter perssonalAlbumAdapter;
	private List<String> localImgList;// 本地图片
	private LinkedList<String> uploadedImgList;// used only handle 'upload
												// finished' event.

	private GridView gridViewAlbum;
	private PopupWindow popupWindow;

	private TextView mUserBith;

	public PerssonalInfoAcitivity() {
		super(-1);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		context = this;

		// 全屏在代码中设置
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		BinderFactory binderFactory = new BinderFactoryBuilder().build();
		ViewBinder vb = binderFactory.createViewBinder(this, true);
		presentMode = new PerssonalInfoPresentModel(UserInfoModel.getInstance()); // 当前登录用户的信息直接获取实例，其他用户传对应bean

		View rootView = vb.inflateAndBind(R.layout.activity_perssonal_info,
				presentMode);
		setContentView(rootView);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void initViews() {
		constellationList = new ArrayList<String>();
		musicStyleList = new ArrayList<String>();
		industryNameList = new ArrayList<String>();
		hobbyList = new ArrayList<String>();

		userId = getIntent().getStringExtra("userId");
		recyclerviewAlbum = (RecyclerView) findViewById(R.id.recyclerview_album);
		recyclerviewGift = (RecyclerView) findViewById(R.id.recyclerview_gift);
		recyclerviewVisitor = (RecyclerView) findViewById(R.id.recyclerview_visitor);

		ivArrowRightAlbum = (ImageView) findViewById(R.id.iv_arrowRightAlbum);
		ivArrowLeftAlbum = (ImageView) findViewById(R.id.iv_arrowLeftAlbum);
		ivArrowRightGift = (ImageView) findViewById(R.id.iv_arrowRightGift);
		ivArrowLeftGift = (ImageView) findViewById(R.id.iv_arrowLeftGift);
		ivArrowRightVisitor = (ImageView) findViewById(R.id.iv_arrowRightVisitor);
		ivArrowLeftVisitor = (ImageView) findViewById(R.id.iv_arrowLeftVisitor);

		ivUsrHeader = (ImageView) findViewById(R.id.iv_usrHeader);
		ivMoodHeader = (ImageView) findViewById(R.id.iv_moodHeader);
		rlMood = (RelativeLayout) findViewById(R.id.rl_mood);

		tvUserGender = (TextView) findViewById(R.id.tv_userGender);

		tvUserAge = (TextView) findViewById(R.id.tv_age);

		mUserBith = (TextView) findViewById(R.id.tv_user_bith_content);

		ckbHeart = (CheckBox) findViewById(R.id.ckbHeart);

		// gridViewAlbum = (GridView) findViewById(R.id.grid_album);

		ivUsrHeader.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (isEditStatu) {
					PhotoUtil.printTrace("user head photo is clicked");
					if (CuttingPicturesUtil.isSDCardExisd()) {
						CuttingPicturesUtil.searhcAlbum(
								PerssonalInfoAcitivity.this,
								CuttingPicturesUtil.LOCAL_PHOTO);
					} else {
						showToast(getString(R.string.sdcard_not_exsit));
					}
				}

			}
		});

		initTopTitle();
		initBottomBar(null);
	}

	private void initTopTitle() {
		topTitle = (TopTitleView) findViewById(R.id.top_title);
		topTitle.showLeftImag(R.drawable.back, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		topTitle.showCenterText(presentMode.getUserNickname(), null);

		topTitle.showRightText(getString(R.string.edit),
				R.style.toptitle_right, new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (!isRetrunSuccess) {
							showToastInThread("页面数据获取错误，无法编辑本页面");
							return;
						}
						if (isEditStatu) {
							LogUtil.LogDebug("PhotoHandle",
									"start to save modification", null);

							topTitle.updateRightText(R.string.edit);

							if (photoModelList.size() > 0
									&& "add".equals(photoModelList.get(
											photoModelList.size() - 1)
											.getTypeTag())) {
								photoModelList.remove(photoModelList.size() - 1);
								perssonalAlbumAdapter
										.notifyItemChanged(photoModelList
												.size() - 1);
							}

							if (localImgList != null && localImgList.size() > 0) {
								PhotoUtil
										.printTrace("local image list size is not null, will load user photo");
								showLoadingDialog("照片保存中");

								/*
								 * when user photo changed, need to upload
								 * photos first(save to file system on server
								 * side), then update user photo(save to db on
								 * server side). Finally call savePage, which
								 * will trigger request
								 */
								for (String str : localImgList) {
									UploadManager.getInstance(context)
											.upLoadPic(str,
													PhotoUtil.USER_PHOTO,
													uploadHandler);
								}

							} else {
								PhotoUtil
										.printTrace("save page directly, since no user photo change");
								savePage();
							}

						} else {
							LogUtil.LogDebug("PhotoHandle",
									"start modification", null);
							topTitle.updateRightText(R.string.save);
							UploadManager.getInstance(context);
							UploadManager.addPictures(photoModelList, null);
							perssonalAlbumAdapter
									.notifyItemChanged((photoModelList.size() - 1) > 0 ? photoModelList
											.size() - 1 : 0);
							// TODO
						}
						isEditStatu = !isEditStatu;
						SharePreferenceUtil.getInstance(context)
								.saveBooleanToShare("isEditStatu", isEditStatu);
						if (photoModelList.size() <= 0)
							presentMode.setAlbumNoneVisibility(View.VISIBLE);
						else
							presentMode.setAlbumNoneVisibility(View.GONE);
					}
				});
	}

	/**
	 * true 不显示，本人，false为显示，null为根据intent传进来的值判断
	 * 
	 * @param bool
	 */
	private void initBottomBar(Boolean bool) {
		if (bool == null)
			bool = getIntent().getBooleanExtra("SHOW_BOTTOM_BAR", false);
		if (bool) {
			findViewById(R.id.ll_botomBar).setVisibility(View.GONE);
			return;
		}
		// btnBottomGift = (Button) findViewById(R.id.btn_bottomGift);
		// btnBottomDialogue = (Button) findViewById(R.id.btn_bottomDialogue);
		// btnBottomDate = (Button) findViewById(R.id.btn_bottomDate);

		mTvLists = new ArrayList<TextView>(PAGE_TOATAL);
		mTvLists.add((TextView) findViewById(R.id.tv_bottomGift));
		mTvLists.add((TextView) findViewById(R.id.tv_bottomDialogue));
		mTvLists.add((TextView) findViewById(R.id.tv_bottomDate));
		mTvLists.add((TextView) findViewById(R.id.tv_bottomWhistleBlow));
		mTvLists.add((TextView) findViewById(R.id.tv_bottomMore));

		// for show image
		int[] imgId = { R.drawable.img_personal_info_gift,
				R.drawable.img_personal_info_conversation,
				R.drawable.img_personal_info_date,
				R.drawable.img_personal_info_more,
				R.drawable.img_personal_info_gift_selected,
				R.drawable.img_personal_info_conversation_selected,
				R.drawable.img_personal_info_date_selected,
				R.drawable.img_personal_info_more_selected };

		mImgUnSelected = new ArrayList<Drawable>(PAGE_TOATAL);
		for (int i = 0; i < PAGE_TOATAL; i++) {
			Drawable drawable = getResources().getDrawable(imgId[i]);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),
					drawable.getMinimumHeight());
			mImgUnSelected.add(drawable);
		}

		mImgSelected = new ArrayList<Drawable>(PAGE_TOATAL);
		for (int i = 4; i < imgId.length; i++) {
			Drawable drawable = getResources().getDrawable(imgId[i]);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),
					drawable.getMinimumHeight());
			mImgSelected.add(drawable);
		}

		// 点击对话跳转对话详情
		mTvLists.get(1).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (perssonalInfoModel != null
						&& perssonalInfoModel.getResult() != null
						&& perssonalInfoModel.getResult().getUserMap() != null) {
					if (getIntent().getBooleanExtra("isFromDateDetail", false)) {
						UmengUtil.onEvent(PerssonalInfoAcitivity.this,"organiser_chat_button_hits");
						LogUtil.printUmengLog("organiser_chat_button_hits");
					}
					Intent intent = new Intent(PerssonalInfoAcitivity.this,
							ChatActivity.class);
					intent.putExtra("userId", perssonalInfoModel.getResult()
							.getUserMap().getImServerId());
					intent.putExtra("nickname", perssonalInfoModel.getResult()
							.getUserMap().getUserName());
					intent.putExtra("imgurl", perssonalInfoModel.getResult()
							.getUserMap().getUserLogoUrl());

					// 直接在跳转之前就在数据库插入信息,从这里跳转对话必然要更新一次数据库
					new Thread(new Runnable() {
						@Override
						public void run() {
							DBFriendListDao dao = new DBFriendListDao(
									PerssonalInfoAcitivity.this);
							UserCommonModel user = new UserCommonModel();
							user.setUserMobile(perssonalInfoModel.getResult()
									.getUserMap().getUserMobile());
							user.setUserId(userId);
							user.setImServerId(perssonalInfoModel.getResult()
									.getUserMap().getImServerId());
							user.setUserName(perssonalInfoModel.getResult()
									.getUserMap().getUserName());
							String imgUrl;
							if (perssonalInfoModel.getResult().getUserMap()
									.getUserLogoUrl() == null) {
								imgUrl = "null";
							} else {
								imgUrl = perssonalInfoModel.getResult()
										.getUserMap().getUserLogoUrl();
							}
							user.setUserLogoUrl(imgUrl);
							dao.saveContacter(user);
						}
					}).start();
					startActivity(intent);

				}
			}
		});
		// 点击发起约会
		mTvLists.get(2).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (TextUtils.isEmpty(userId))
					return;
				if (perssonalInfoModel != null
						&& perssonalInfoModel.getResult() != null
						&& perssonalInfoModel.getResult().getUserMap() != null) {
					if (getIntent().getBooleanExtra("isFromDateDetail", false)) {
						UmengUtil.onEvent(PerssonalInfoAcitivity.this,"date_organiser_button_hits");
						LogUtil.printUmengLog("date_organiser_button_hits");
					}
					Intent intentDatePublish = new Intent(
							PerssonalInfoAcitivity.this,
							DatePublishActivity.class);
					intentDatePublish.putExtra(GlobleConstant.DATE_FAVORITE_ID,
							Integer.valueOf(userId));
					intentDatePublish.putExtra(
							GlobleConstant.DATE_FAVORITE_NAME,
							presentMode.getUserNickname());
					intentDatePublish.putExtra(
							GlobleConstant.DATE_FAVORITE_MOBILE,
							perssonalInfoModel.getResult().getUserMap()
									.getUserMobile());
					intentDatePublish.putExtra(
							GlobleConstant.DATE_FAVORITE_URL,
							perssonalInfoModel.getResult().getUserMap()
									.getUserLogoUrl());
					startActivity(intentDatePublish);
				}
			}
		});
		// 点击举报
		mTvLists.get(3).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (TextUtils.isEmpty(userId))
					return;
				if (perssonalInfoModel != null
						&& perssonalInfoModel.getResult() != null
						&& perssonalInfoModel.getResult().getUserMap() != null) {
					Intent intent = new Intent(PerssonalInfoAcitivity.this,
							WhistleBlowActivity.class);
					intent.putExtra(GlobleConstant.DATE_FAVORITE_ID,
							Integer.valueOf(userId));

					startActivity(intent);
				}
			}
		});

		// btnBottomMore = (Button) findViewById(R.id.btn_bottomMore);
	}

	@Override
	public void initData() {

		// 创建一个线性布局管理器提供给RecyclerView,一个布局管理器提供给一个RecyclerView
		LinearLayoutManager[] layoutManager = new LinearLayoutManager[3];
		for (int i = 0; i < layoutManager.length; i++) {
			layoutManager[i] = new LinearLayoutManager(this);
			layoutManager[i].setOrientation(LinearLayoutManager.HORIZONTAL);
		}

		// 设置布局管理器
		recyclerviewAlbum.setLayoutManager(layoutManager[0]);
		recyclerviewGift.setLayoutManager(layoutManager[1]);
		recyclerviewVisitor.setLayoutManager(layoutManager[2]);

		photoModelList = new LinkedList<AlbumPhotoModel>();
		List<GiftModel> listGift = new LinkedList<GiftModel>();
		List<PerssonalVisitorModel> listVisitor = new LinkedList<PerssonalVisitorModel>();

		// --------------------------examples data--------------------------

		// int dataTotal = new Random().nextInt(5);

		// for (int i = 0; i < dataTotal; i++) {
		// AlbumModel gm = new AlbumModel();
		// gm.setGiftLogoUrl("drawable://" + R.drawable.pub_icon);
		// photoModelList.add(gm);
		// photoModelList.add("drawable://" + R.drawable.pub_icon);
		// photoModelList.add(new AlbumModel("drawable://" +
		// R.drawable.pub_icon,
		// null, i, i, 0, 0));
		// listGift.add(new AlbumModel("drawable://" + R.drawable.pub_icon,
		// null,
		// i, i, 0, 0));
		// listVisitor.add(new PerssonalVisitorModel("drawable://" +
		// R.drawable.pub_icon, getString(R.string.name_normal)));
		// }
		/**
		 * ==================================图片两行显示测试代码========================
		 * ===================
		 */
		// gridViewAdapter = new GridViewAdapter(this, photoModelList);
		// gridViewAlbum.setAdapter(gridViewAdapter);
		/**
		 * ==================================图片两行显示测试代码========================
		 * ===================
		 */

		// 相册布局图
		perssonalAlbumAdapter = new PerssonalAlbumAdapter(photoModelList);
		System.out.println("个人相册长度： " + photoModelList.size());
		recyclerviewAlbum.setAdapter(perssonalAlbumAdapter);
		recyclerviewGift.setAdapter(new PerssonalGiftAdapter(listGift));
		recyclerviewVisitor
				.setAdapter(new PerssonalVisitorAdapter(listVisitor));

		if (photoModelList == null || photoModelList.size() <= 0)
			presentMode.setAlbumNoneVisibility(View.VISIBLE);
		if (listGift == null || listGift.size() <= 0)
			presentMode.setGiftVisibility(View.GONE);
		if (listVisitor == null || listVisitor.size() <= 0)
			presentMode.setVisitorVisibility(View.GONE);
		setShowUserGender(presentMode.getUserGender());

		initUserAger();
		// --------------------------examples data--------------------------
		// 发送获取个人信息请求
		getUserInfoRequest();
	}

	private void initUserAger() {
		String userBith = mUserBith.getText().toString();
		if (!TextUtils.isEmpty(userBith)) {
			mYear = Integer.parseInt(userBith.substring(0, 4));
			updataUserAge();
		}
	}

	@Override
	public void bindViews() {

	}

	private void setShowUserGender(String userGender) {
		Drawable drawableLeft;
		if ("男".equals(userGender))
			drawableLeft = getResources().getDrawable(R.drawable.sex_boy);
		else
			drawableLeft = getResources().getDrawable(R.drawable.sex_girl);
		drawableLeft.setBounds(0, 0, drawableLeft.getMinimumWidth(),
				drawableLeft.getMinimumHeight());
		tvUserGender.setCompoundDrawables(drawableLeft, null, null, null);
	}

	/**
	 * ==================================相册两行显示测试代码adapter======================
	 * =====================
	 */
	/*
	 * public class GridViewAdapter extends BaseListAdapter<AlbumPhotoModel>{
	 * 
	 * private float x, y;
	 * 
	 * public GridViewAdapter(Activity context, List<AlbumPhotoModel> list) {
	 * super(context, list); }
	 * 
	 * @Override public View getView(int position, View convertView, ViewGroup
	 * parent) { if (convertView == null) { convertView =
	 * LayoutInflater.from(PerssonalInfoAcitivity.this).inflate(
	 * R.layout.adapter_perssonal_album, null); } ImageView imgView =
	 * (ImageView) convertView.findViewById(R.id.iv_album); String imgUrl =
	 * photoModelList.get(position).getPhotoUrl(); String tag =
	 * photoModelList.get(position).getTypeTag(); final int currentPosition =
	 * position; if
	 * ("add".equalsIgnoreCase(photoModelList.get(position).getTypeTag())) {
	 * ImageLoaderUtil.getImageLoader().displayImage( "drawable://" +
	 * R.drawable.addgroup, imgView); imgView.setOnClickListener(new
	 * OnClickListener() {
	 * 
	 * @Override public void onClick(View v) { addPic(); } }); } else {
	 * 
	 * ImageLoaderUtil.getImageLoader().displayImage(
	 * DisplayUtils.getAbsolutelyUrl
	 * (photoModelList.get(position).getPhotoUrl()), imgView);
	 * imgOnClicked(imgView, position); convertView.setOnClickListener(new
	 * OnClickListener() {
	 * 
	 * @Override public void onClick(View v) { // showToast("remove");
	 * showPopWindow(currentPosition); } }); }
	 * 
	 * return convertView; }
	 * 
	 * private void addPic() { if (CuttingPicturesUtil.isSDCardExisd()) {
	 * CuttingPicturesUtil.searhcAlbum((Activity) context,
	 * CuttingPicturesUtil.UPLOAD_PIC); } else { Activity activity = (Activity)
	 * context; if (activity != null) ((BaseActivity)
	 * activity).showToast(context .getString(R.string.sdcard_not_exsit)); } }
	 * 
	 * private void imgOnClicked(final ImageView ivAlbum, final int position) {
	 * 
	 * ivAlbum.setOnClickListener(new OnClickListener() {
	 * 
	 * @Override public void onClick(View v) { Intent intent = new
	 * Intent(context, PicturesActivity.class); Bundle b = new Bundle();
	 * //b.putSerializable("list", (Serializable) listAlbumUrl);
	 * intent.putExtra("position", position); intent.putExtra("images",
	 * photoModelList.get(position).getPhotoUrl());// 非必须 int[] location = new
	 * int[2]; ivAlbum.getLocationOnScreen(location);
	 * intent.putExtra("locationX", location[0]);// 必须
	 * intent.putExtra("locationY", location[1]);// 必须 intent.putExtra("width",
	 * ivAlbum.getWidth());// 必须 intent.putExtra("height",
	 * ivAlbum.getHeight());// 必须 //intent.putExtra("bundle", b);
	 * context.startActivity(intent); activity.overridePendingTransition(0, 0);
	 * 
	 * } });
	 * 
	 * } }
	 * 
	 * private void showPopWindow(final int position) { popMenu = new
	 * CommonPopWindow(this, "是否删除此图片？", new OnClickListener() {
	 * 
	 * @Override public void onClick(View v) { photoModelList.remove(position);
	 * gridViewAdapter.notifyDataSetChanged(); popupWindow.dismiss(); } }, new
	 * OnClickListener() {
	 * 
	 * @Override public void onClick(View v) { popupWindow.dismiss(); } });
	 * 
	 * // 非文本 popMenu.setIsEditText(false);
	 * 
	 * // 准备工作 popMenu.preperShow(); popupWindow = popMenu.getMenu();
	 * popupWindow.setOutsideTouchable(true); if (popupWindow == null) { return;
	 * }
	 * 
	 * if (popupWindow.isShowing()) { popupWindow.dismiss(); return; }
	 * popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0,
	 * 0);
	 * 
	 * }
	 */
	/**
	 * ==================================相册两行显示测试代码adapter======================
	 * =====================
	 */
	/**
	 * 获取用户信息
	 */
	private void getUserInfoRequest() {
		LogUtil.LogDebug("PhotoHandle", "enter getUserInfoRequest", null);
		JSONObject ob = new JSONObject();
		// 此处判断取自己ID还是取传递进来的ID
		// if (userId != null && !"".equalsIgnoreCase(userId))
		ob.put("userId", userId);
		// else
		ob.put("userId", userId = TextUtils.isEmpty(userId) ? appContext
				.getUserInfo().getUserId() : userId);

		isMySelf = userId
				.equalsIgnoreCase(appContext.getUserInfo().getUserId());

		if (!isMySelf)
			// if (topTitle.getEditBtn() != null)
			// topTitle.getEditBtn().setVisibility(View.GONE);
			if (topTitle.ll_title_right != null) {
				topTitle.ll_title_right.setVisibility(View.GONE);
				enableCheckMood = true;
			}
		initBottomBar(isMySelf);
		HttpRequestFactory.getInstance().postRequest(Urls.GET_USER_INFO, ob,
				getUserInfoHandler);

	}

	/**
	 * 获取用户信息
	 */
	AsyncHttpResponseHandler getUserInfoHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onStart() {
			showLoadingDialog();
		};

		@Override
		public void onSuccess(String content) {
			PhotoUtil.printTrace(content);
			perssonalInfoModel = JSON.parseObject(content,
					new TypeReference<BaseModel<PerssonalInfoModel>>() {
					});
			if (perssonalInfoModel != null
					&& perssonalInfoModel.getCode().equalsIgnoreCase(
							GlobleConstant.REQUEST_SUCCESS)) {
				LogUtil.LogDebug("PhotoHandle", "succeed to get user info",
						null);
				isRetrunSuccess = true;
				resetPage(perssonalInfoModel);
			} else {
				LogUtil.LogDebug("PhotoHandle",
						"failed to get user info though response was returned",
						null);
				isRetrunSuccess = false;
			}
		};

		@Override
		public void onFailure(Throwable error) {
			LogUtil.LogDebug("PhotoHandle", "failed to get user info", null);
			isRetrunSuccess = false;
		};

		@Override
		public void onFinish() {
			dismissLoadingDialog();
			// 获取用户信息结束后，如果是本人则需要发送星座等的查询请求
			if (isMySelf) {
				getSelectGeneralSummary();
			}
		};
	};

	/**
	 * 获取星座等的列表请求
	 */
	private void getSelectGeneralSummary() {
		LogUtil.LogDebug("PhotoHandle", "enter getSelectGeneralSummary", null);
		HttpRequestFactory.getInstance().postRequest(Urls.GET_GENERAL_SUMMARY,
				null, new AsyncHttpResponseHandler() {
					@Override
					public void onStart() {
						super.onStart();
					}

					@Override
					public void onSuccess(String content) {
						super.onSuccess(content);
						PhotoUtil.printTrace(content);
						constellationMap = new HashMap<String, Integer>();
						musicStyleMap = new HashMap<String, Integer>();
						industryNameMap = new HashMap<String, Integer>();
						hobbyMap = new HashMap<String, Integer>();
						IntKeyConstellMap = new HashMap<Integer, String>();
						IntKeyHobbyMap = new HashMap<Integer, String>();
						IntKeyIndustryNameMap = new HashMap<Integer, String>();
						IntKeyMusicStyleMap = new HashMap<Integer, String>();
						BaseModel<PullDownModel> model = JSON.parseObject(
								content,
								new TypeReference<BaseModel<PullDownModel>>() {
								});

						if (model != null && model.getResult() != null) {
							LogUtil.LogDebug("PhotoHandle",
									"succeed to get general summary", null);
							for (Constellation constellation : model
									.getResult().getConstellation()) {
								constellationList.add(constellation
										.getConstellationName());
								constellationMap.put(
										constellation.getConstellationName(),
										constellation.getConstellationId());
								IntKeyConstellMap.put(
										constellation.getConstellationId(),
										constellation.getConstellationName());
							}

							for (MusicStyleName musicStyle : model.getResult()
									.getMusicStyle()) {
								musicStyleList.add(musicStyle
										.getMusicStyleName());
								musicStyleMap.put(
										musicStyle.getMusicStyleName(),
										musicStyle.getMusicStyleId());
								IntKeyMusicStyleMap.put(
										musicStyle.getMusicStyleId(),
										musicStyle.getMusicStyleName());
							}

							for (IndustryName industry : model.getResult()
									.getIndustry()) {
								industryNameList.add(industry.getIndustryName());
								industryNameMap.put(industry.getIndustryName(),
										industry.getIndustryId());
								IntKeyIndustryNameMap.put(
										industry.getIndustryId(),
										industry.getIndustryName());
							}

							for (Hobby hobby : model.getResult().getHobby()) {
								hobbyList.add(hobby.getHobbyName());
								hobbyMap.put(hobby.getHobbyName(),
										hobby.getHobbyId());
								IntKeyHobbyMap.put(hobby.getHobbyId(),
										hobby.getHobbyName());
							}
						}

					}

					@Override
					public void onFinish() {
						super.onFinish();
					}
				});

	}

	/**
	 * 更新接口信息
	 */

	AsyncHttpResponseHandler updateUserInfo = new AsyncHttpResponseHandler() {

		@Override
		public void onStart() {
			showLoadingDialog();
		};

		@Override
		public void onSuccess(String content) {
			LogUtil.LogDebug("PhotoHandle", "succeed to get general summary",
					null);
		};

		@Override
		public void onFailure(Throwable error) {
		};

		@Override
		public void onFinish() {
			dismissLoadingDialog();
		};

	};

	/**
	 * 重构页面
	 * 
	 * @param baseModel
	 */
	private void resetPage(BaseModel<PerssonalInfoModel> baseModel) {
		LogUtil.LogDebug("PhotoHandle", "enter resetPage", null);
		if (baseModel.getResult() == null)
			return;

		// 其他个人信息
		UserMapModel userMap = baseModel.getResult().getUserMap();
		if (userMap != null) {
			presentMode.setUserNickname(userMap.getUserName());
			presentMode.setUserAge(userMap.getUserAge());
			presentMode.setUserAccount(userMap.getUserAccount());
			presentMode
					.getModel()
					.setUserHeader(userMap.getUserLogoUrl())
					.setUserId(
							TextUtils.isEmpty(userMap.getUserId()) ? -1
									: Integer.valueOf(userMap.getUserId())); // 存在里面，方便取出跳到心情

			TextView tvGender = (TextView) findViewById(R.id.tv_userGender);
			if ("1".equalsIgnoreCase(userMap.getSex())) {
				presentMode.setUserGender("男");
				StringUtils.changeStyle(context, tvGender, Gender.GENDER_BOY);
			} else {
				presentMode.setUserGender("女");
				StringUtils.changeStyle(context, tvGender, Gender.GENDER_GIRL);
			}

			// 头像
			LogUtil.LogDebug("PhotoHandle",
					"logo url:" + userMap.getUserLogoUrl() + "   small url"
							+ userMap.getUserLogoUrl(), null);
			String imgUrl = DisplayUtils.getAbsolutelyUrl(userMap
					.getUserLogoUrl());
			// if (imgUrl != null && imgUrl.length() > 0) {
			// if (imgUrl.contains("http:")) {
			// ImageLoaderUtil.getImageLoader().displayImage(
			// userMap.getUserLogoUrl(), ivUsrHeader);
			// } else {
			// ImageLoaderUtil.getImageLoader().displayImage(
			// Urls.SERVERHOST + userMap.getUserLogoUrl(),
			// ivUsrHeader);
			// }
			// }
			ImageLoaderUtil.getImageLoader().displayImage(imgUrl, ivUsrHeader);
			topTitle.showCenterText(userMap.getUserName(), null);

			// 星座
			presentMode.setUserConstellation(userMap.getConstellation_name());
			// 出生日期
			presentMode.setUserDateOfBirth(userMap.getUserBirthday());
			// 喜欢的酒水
			presentMode.setUserFavoriteWine(userMap.getUserLikeWine());

			// 经常出没的酒吧
			presentMode.setUserOftenPub(userMap.getUserOftenBar());

			// 用户签名
			presentMode.setUserSign(userMap.getUserAsign());

			// 个性签名
			presentMode.setUserPersonalitySignature(userMap.getUserAsign());
		}

		// 相册
		List<AlbumModel> listResultAlbum = baseModel.getResult()
				.getUserPhototList();
		if (listResultAlbum == null || listResultAlbum.size() <= 0
				|| listResultAlbum.get(0) == null) {
			presentMode.setAlbumNoneVisibility(View.VISIBLE);
		} else {
			UploadManager.getAlbumList(listResultAlbum, photoModelList);
			if (photoModelList.size() <= 0) {
				presentMode.setAlbumNoneVisibility(View.VISIBLE);
			} else {
				presentMode.setAlbumNoneVisibility(View.GONE);
			}
			perssonalAlbumAdapter.notifyDataSetChanged();
		}

		// 心情
		presentMode.setUserMood("");
		if (baseModel.getResult().getUserMoodeRecordList() != null) {
			presentMode.setUserMood(baseModel.getResult()
					.getUserMoodeRecordList().get(0).getMoodRecordText());
			if (!TextUtils.isEmpty(baseModel.getResult()
					.getUserMoodeRecordList().get(0).getMoodRecordImgUrl())) {
				ivMoodHeader.setVisibility(View.VISIBLE);
				ImageLoaderUtil.getImageLoader()
						.displayImage(
								DisplayUtils.getAbsolutelyUrl(baseModel
										.getResult().getUserMoodeRecordList()
										.get(0).getMoodRecordImgUrl()
										.contains(",") ? baseModel.getResult()
										.getUserMoodeRecordList().get(0)
										.getMoodRecordImgUrl().split(",")[0]
										: baseModel.getResult()
												.getUserMoodeRecordList()
												.get(0).getMoodRecordImgUrl()),
								ivMoodHeader,
								ImageLoaderUtil.getOptions(
										R.drawable.img_loading_default_copy,
										R.drawable.img_loading_empty_copy,
										R.drawable.img_loading_fail_copy));
			}
		}

		// 音乐
		if (baseModel.getResult().getUserMusicList() != null) {
			List<UserMusicListModel> moodList = baseModel.getResult()
					.getUserMusicList();
			String moodStr = "";
			for (int i = 0; i < moodList.size(); i++) {
				moodStr = moodStr.length() > 0 ? moodList.get(i)
						.getMusicStyleName() + "、" + moodStr : moodList.get(i)
						.getMusicStyleName();
			}
			presentMode.setUserFavoriteMusicStyle(moodStr);
		}

		// 爱好
		if (baseModel.getResult().getUserHobbyList() != null) {
			List<UserHobbyListModel> hobbyList = baseModel.getResult()
					.getUserHobbyList();
			String hobby = "";
			for (int i = 0; i < hobbyList.size(); i++) {
				hobby = hobby.length() > 0 ? hobbyList.get(i).getHobbyName()
						+ "、" + hobby : hobbyList.get(i).getHobbyName();
			}
			presentMode.setUserHobby(hobby);
		}

		// 行业
		if (baseModel.getResult().getUserIndustryList() != null) {
			PhotoUtil.printTrace("getUserIndustryList:"
					+ baseModel.getResult().getUserIndustryList().get(0)
							.getIndustryName());
			presentMode.setUserIndustry(baseModel.getResult()
					.getUserIndustryList().get(0).getIndustryName());
		} else {
			PhotoUtil.printTrace("getUserIndustryList:null");
		}

	}

	/**
	 * 保存页面并发送接口
	 */
	private void savePage() {
		PhotoUtil.printTrace("enter savePage");

		// TODO
		JSONObject jo = new JSONObject();
		jo.put("userId", appContext.getUserInfo().getUserId());
		jo.put("userMobile", appContext.getUserInfo().getUserMobile());
		jo.put("userName", appContext.getUserInfo().getUserName());

		// 性别
		if ("男".equalsIgnoreCase(appContext.getUserInfo().getUserSex())) {
			jo.put("userSex", 1);
		} else {
			jo.put("userSex", 2);
		}
		jo.put("userAge", presentMode.getUserAge());
		jo.put("userAsign", presentMode.getUserPersonalitySignature());
		jo.put("userLikeWine", presentMode.getUserFavoriteWine());
		jo.put("userOftenBar", presentMode.getUserOftenPub());
		jo.put("isSinger", "1"); // TODO 存疑
		jo.put("userLogoUrl", PhotoUtil.filterUserLogoUrl(appContext
				.getUserInfo().getUserLogoUrl()));
		jo.put("userMoodRecord", "");
		jo.put("userBirthday", presentMode.getUserDateOfBirth());

		// 单选
		jo.put("userConstellationId",
				constellationMap.get(presentMode.getUserConstellation()));
		jo.put("industryIds",
				industryNameMap.get(presentMode.getUserIndustry()));

		// 多选
		String strFavorite = presentMode.getUserFavoriteMusicStyle();
		String strHobby = presentMode.getUserHobby();

		if (strFavorite != null && strFavorite.length() > 0) {
			String[] music = strFavorite.split("、");
			String musicIds = "";
			for (String str : music) {
				musicIds = musicIds.length() > 0 ? musicIds + ","
						+ musicStyleMap.get(str) : musicStyleMap.get(str) + "";
			}
			jo.put("musicStyleIds", musicIds);
		}

		if (strHobby != null && strHobby.length() > 0) {
			String[] hobby = strHobby.split("、");
			String hobbyIds = "";
			for (String str : hobby) {
				hobbyIds = hobbyIds.length() > 0 ? hobbyIds + ","
						+ hobbyMap.get(str) : hobbyMap.get(str) + "";
			}
			jo.put("hobbyIds", hobbyIds);
		}
		PhotoUtil.printTrace(jo.toString());

		HttpRequestFactory.getInstance().postRequest(Urls.UPDATE_USER_INFO, jo,
				updateUserInfoHandler);

	}

	/**
	 * 处理更新返回请求
	 */
	AsyncHttpResponseHandler updateUserInfoHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onStart() {
		};

		@Override
		public void onSuccess(String content) {
			LogUtil.LogDebug("PhotoHandle", "succeed to update user info:"
					+ content, null);
			getUserInfoRequest();
		};

		@Override
		public void onFailure(Throwable error) {
			LogUtil.LogDebug("PhotoHandle", "failed to update user info"
					+ error.toString(), null);
		};

		@Override
		public void onFinish() {
		};
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		LogUtil.LogDebug("PhotoHandle", "enter onActivityResult. requestCode:"
				+ requestCode + " result code:" + resultCode, null);
		if (data != null) {
			switch (requestCode) {
			case CuttingPicturesUtil.CAMERA:
				File temp = new File(GlobleConstant.USERPHOTO_DIR
						+ GlobleConstant.USERPHOTO_NAME);
				startActivityForResult(CuttingPicturesUtil.startPhotoZoom(
						PerssonalInfoAcitivity.this, Uri.fromFile(temp)),
						CuttingPicturesUtil.ZOOMPHOTO);
				break;
			case CuttingPicturesUtil.ZOOMPHOTO:
				PhotoUtil.printTrace("onActivityResult: case ZOOMPHOTO");
				// userOldPhoto = userPhoto;
				userPhoto = data.getExtras().getParcelable("data");
				ivUsrHeader.setImageBitmap(userPhoto);

				try {
					FileUtils.saveBitmapToPath(userPhoto,
							GlobleConstant.USERPHOTO_DIR,
							GlobleConstant.USERPHOTO_NAME);
					UploadManager.getInstance(context).upLoadPic(
							PhotoUtil.USER_PHOTO_PATH, PhotoUtil.HEADER_CODE,
							uploadHandler);
				} catch (IOException e) {
					Log.e("REGISTER", e.toString());
					e.printStackTrace();
				}
				break;
			case CuttingPicturesUtil.LOCAL_PHOTO:
				PhotoUtil
						.printTrace("onActivityResult: case LOCAL_PHOTO. data content:"
								+ data.getData().getPath());
				startActivityForResult(CuttingPicturesUtil.startPhotoZoom(
						PerssonalInfoAcitivity.this, data.getData()),
						CuttingPicturesUtil.ZOOMPHOTO);
				break;

			case CuttingPicturesUtil.UPLOAD_PIC:
				PhotoUtil.printTrace("path is:" + data.getData().toString());
				Uri originalUri = data.getData();

				String path = UploadManager.fetchImagePath(this, originalUri);
				PhotoUtil.printTrace("image path is:" + path);
				if (!TextUtils.isEmpty(path)) {
					UploadManager.addPictures(photoModelList, path);
					if (localImgList == null)
						localImgList = new LinkedList<String>();
					localImgList.add(path);

					PhotoUtil.printTrace("added image path to local list");
					perssonalAlbumAdapter.notifyDataSetChanged();
				}

				break;

			// 个性签名
			case SIGN:
				String sign = data.getStringExtra("edittext");
				if (sign != null && sign.length() != 0) {
					presentMode.setUserPersonalitySignature(sign);
				}
				break;

			// 喜欢的酒水
			case LOVELY_WINE:
				String wine = data.getStringExtra("edittext");
				if (wine != null && wine.length() != 0) {
					presentMode.setUserFavoriteWine(wine);
				}
				break;

			// 常去酒吧
			case USUAL_BAR:
				String bar = data.getStringExtra("edittext");
				if (bar != null && bar.length() != 0) {
					presentMode.setUserOftenPub(bar);
				}
				break;

			// 星座
			case ChoseDialog.CONSTELLATION:
				String constellation = data
						.getStringExtra(ChoseDialog.CHOSED_TEXT);
				if (constellation != null && constellation.length() != 0) {
					presentMode.setUserConstellation(constellation);
				}
				break;
			// 行业
			case ChoseDialog.INDUSTRY:
				String industry = data.getStringExtra(ChoseDialog.CHOSED_TEXT);
				if (industry != null && industry.length() != 0) {
					presentMode.setUserIndustry(industry);
				}
				break;
			// 爱好
			case ChoseDialog.HOBBY:
				String hobby = data.getStringExtra(ChoseDialog.CHOSED_TEXT);
				if (hobby != null && hobby.length() != 0) {
					presentMode.setUserHobby(hobby);
				}
				break;
			// 喜欢的音乐类型
			case ChoseDialog.MUSIC_STYLE:
				String music_style = data
						.getStringExtra(ChoseDialog.CHOSED_TEXT);
				if (music_style != null && music_style.length() != 0) {
					presentMode.setUserFavoriteMusicStyle(music_style);
				}
				break;

			// 心情
			case MOOD:
				String mood = data.getStringExtra("edittext");
				if (moodDetailsFragment != null)
					moodDetailsFragment.getResultListener().setActivityResult(
							requestCode, resultCode, data);
				PhotoUtil.printTrace(mood);
				if (mood != null && mood.length() != 0) {
					presentMode.setUserMood(mood);
				}
				break;

			}
			super.onActivityResult(requestCode, resultCode, data);
		} else {
			PhotoUtil.printTrace("data is null");
		}
	}

	/**
	 * 上传结束后的handler处理事件
	 */
	Handler uploadHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			PhotoUtil.printTrace("enter msg handler after finish iamge upload");
			UploadDataModel upModel = (UploadDataModel) msg.obj;
			switch (msg.what) {
			case PhotoUtil.HEADER_CODE:
				UserCommonModel userInfoModel = AppContext.getApplication()
						.getUserInfo();
				userInfoModel.setUserLogoUrl(upModel.getSmallImageFile());
				System.out.println("upModel.getSmallImageFile(): "
						+ upModel.getSmallImageFile());
				PhotoUtil.printTrace("updated set logo url:"
						+ userInfoModel.getUserLogoUrl());
				break;
			case PhotoUtil.UPLOAD_FAIL:
				dismissLoadingDialog();
				showToast("图片上传失败");
				break;
			case PhotoUtil.USER_PHOTO:
				upModel = (UploadDataModel) msg.obj;
				if (uploadedImgList == null) {
					uploadedImgList = new LinkedList<String>();
				}

				uploadedImgList.add(upModel.getSmallImageFile());

				if (uploadedImgList.size() < localImgList.size()) {
					PhotoUtil.printTrace("uploaded size is:"
							+ uploadedImgList.size());
					return;// they should be equal
				}

				String urls = UploadManager.getPhotoPaths(uploadedImgList,
						photoModelList);
				UploadManager.updateUserPhoto(urls, new IPhotoHandleCallback() {

					@Override
					public void onSuccess() {
						dismissLoadingDialog();
						showToast("图片上传成功");

						// after upload/update user photos, save other change,
						// and send out user info request again to update all
						// user info.
						savePage();
					}

					@Override
					public void onFailure() {
						dismissLoadingDialog();
						showToast("图片上传失败");

						// even failed to upload/update user photo, still need
						// to save other user info
						savePage();
					}

				});

				localImgList.clear();
				uploadedImgList.clear();
				break;

			default:
				break;
			}

		}
	};

	// **************************************由于页面代码过于冗余，为方便视野，以下集中处理点击事件，事件绑定在xml中*********************/
	/**
	 * 心情记录
	 * 
	 * @param view
	 */
	public void click_mood(View view) {
		// if (!isEditStatu && !enableCheckMood)
		// return;
		SingerDetailModel singerDetailModel = new SingerDetailModel();
		singerDetailModel.setUserId(presentMode.getModel().getUserId());
		singerDetailModel.setLogoUrl(presentMode.getModel().getUserHeaderUrl());
		singerDetailModel.setSingerId(presentMode.getModel().getUserId()); // 目前接口只有获取歌手心情，
		singerDetailModel.setSingerName(presentMode.getModel()
				.getUserNickname());
		singerDetailModel.setUserType(1);
		getSupportFragmentManager()
				.beginTransaction()
				.setCustomAnimations(R.anim.slide_in_from_right,
						R.anim.slide_out_to_right)
				.replace(
						android.R.id.content,
						moodDetailsFragment = moodDetailsFragment == null ? new MoodDetailsFragment(
								singerDetailModel) : moodDetailsFragment)
				.addToBackStack(null).commit();
	}

	/**
	 * 约吧账号
	 * 
	 * @param view
	 */
	public void click_account(View view) {

	}

	/**
	 * 签名
	 * 
	 * @param view
	 */
	public void click_sign(View view) {
		if (!isEditStatu)
			return;
		Intent intent = new Intent(PerssonalInfoAcitivity.this,
				com.fullteem.yueba.app.ui.AlertDialog.class);
		intent.putExtra("editTextShow", true);
		intent.putExtra("titleIsCancel", true);
		intent.putExtra("msg", "请输入您的个性签名");
		startActivityForResult(intent, SIGN);
	}

	/**
	 * 出生日期
	 * 
	 * @param view
	 */
	public void click_birth(View view) {
		if (!isEditStatu)
			return;
		Message msg = new Message();
		msg.what = DATE_DIALOG_ID;
		this.dateandtimeHandler.sendMessage(msg);
	}

	/**
	 * 星座
	 * 
	 * @param view
	 */
	public void click_constellation(View view) {
		if (!isEditStatu)
			return;
		Intent intent = new Intent(this, ChoseDialog.class);
		intent.putExtra(ChoseDialog.CHOSE_TYPE, ChoseDialog.SINGLE_TYPE);
		intent.putExtra(ChoseDialog.PULL_TYPE, ChoseDialog.CONSTELLATION);
		intent.putExtra(ChoseDialog.TITLE, "请选择星座");
		intent.putStringArrayListExtra(ChoseDialog.INTENT_LIST,
				constellationList);
		startActivityForResult(intent, ChoseDialog.CONSTELLATION);
	}

	/**
	 * 性别
	 * 
	 * @param view
	 */
	public void click_gender(View view) {

	}

	/**
	 * 行业
	 * 
	 * @param view
	 */
	public void click_industry(View view) {
		if (!isEditStatu)
			return;
		Intent intent = new Intent(this, ChoseDialog.class);
		intent.putExtra(ChoseDialog.CHOSE_TYPE, ChoseDialog.SINGLE_TYPE);
		intent.putExtra(ChoseDialog.PULL_TYPE, ChoseDialog.INDUSTRY);
		intent.putExtra(ChoseDialog.TITLE, "请选择行业");
		intent.putStringArrayListExtra(ChoseDialog.INTENT_LIST,
				industryNameList);
		startActivityForResult(intent, ChoseDialog.INDUSTRY);
	}

	/**
	 * 爱好
	 * 
	 * @param view
	 */
	public void click_hobby(View view) {
		if (!isEditStatu)
			return;
		Intent intent = new Intent(this, ChoseDialog.class);
		intent.putExtra(ChoseDialog.CHOSE_TYPE, ChoseDialog.MORE_TYPE);
		intent.putExtra(ChoseDialog.PULL_TYPE, ChoseDialog.HOBBY);
		intent.putExtra(ChoseDialog.TITLE, "请选择爱好");
		intent.putStringArrayListExtra(ChoseDialog.INTENT_LIST, hobbyList);
		startActivityForResult(intent, ChoseDialog.HOBBY);

	}

	/**
	 * 经常出没的酒吧
	 * 
	 * @param view
	 */
	public void click_pub(View view) {
		if (!isEditStatu)
			return;
		Intent intent = new Intent(PerssonalInfoAcitivity.this,
				com.fullteem.yueba.app.ui.AlertDialog.class);
		intent.putExtra("editTextShow", true);
		intent.putExtra("titleIsCancel", true);
		intent.putExtra("msg", "请输入您爱逛的酒吧");
		startActivityForResult(intent, USUAL_BAR);
	}

	/**
	 * 喜欢的酒水
	 * 
	 * @param view
	 */
	public void click_wine(View view) {
		if (!isEditStatu)
			return;
		Intent intent = new Intent(PerssonalInfoAcitivity.this,
				com.fullteem.yueba.app.ui.AlertDialog.class);
		intent.putExtra("editTextShow", true);
		intent.putExtra("titleIsCancel", true);
		intent.putExtra("msg", "请输入您喜爱的酒水");
		startActivityForResult(intent, LOVELY_WINE);
	}

	/**
	 * 喜欢的音乐风格
	 * 
	 * @param view
	 */
	public void click_musicStyle(View view) {
		if (!isEditStatu)
			return;
		Intent intent = new Intent(this, ChoseDialog.class);
		intent.putExtra(ChoseDialog.CHOSE_TYPE, ChoseDialog.MORE_TYPE);
		intent.putExtra(ChoseDialog.PULL_TYPE, ChoseDialog.MUSIC_STYLE);
		intent.putExtra(ChoseDialog.TITLE, "请选择音乐风格");
		intent.putStringArrayListExtra(ChoseDialog.INTENT_LIST, musicStyleList);
		startActivityForResult(intent, ChoseDialog.MUSIC_STYLE);
	}

	/**
	 * 处理日期和时间控件的Handler
	 */
	Handler dateandtimeHandler = new Handler() {

		@SuppressWarnings("deprecation")
		@Override
		public void handleMessage(Message msg) {
			showDialog(DATE_DIALOG_ID);
		}

	};

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);
		}

		return null;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
		case DATE_DIALOG_ID:
			((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
			break;
		}
	}

	/**
	 * 日期控件的事件
	 */
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			updateDateDisplay();
			updataUserAge();
		}
	};
	private List<TextView> mTvLists;
	private List<Drawable> mImgUnSelected;
	private List<Drawable> mImgSelected;

	/**
	 * 更新日期显示
	 */
	private void updateDateDisplay() {
		String str = new StringBuilder().append(mYear).append("-")
				.append((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1))
				.append("-").append((mDay < 10) ? "0" + mDay : mDay).toString();
		presentMode.setUserDateOfBirth(str);
	}

	/**
	 * 更新年龄显示
	 */
	private void updataUserAge() {
		Calendar calendar = Calendar.getInstance();
		int currentYear = calendar.get(Calendar.YEAR);
		int userAge = currentYear - mYear;
		presentMode.setUserAge(userAge + "");

	}

}
