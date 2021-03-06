package com.fullteem.yueba.app.singer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.robobinding.ViewBinder;
import org.robobinding.binder.BinderFactory;
import org.robobinding.binder.BinderFactoryBuilder;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.fullteem.yueba.R;
import com.fullteem.yueba.app.AppContext;
import com.fullteem.yueba.app.adapter.PerssonalAlbumAdapter;
import com.fullteem.yueba.app.adapter.PerssonalGiftAdapter;

import com.fullteem.yueba.app.singer.adapter.SingerVideoAdapter;
import com.fullteem.yueba.app.singer.adapter.VideoListAdapter;
import com.fullteem.yueba.app.singer.model.SingerDetailModel;
import com.fullteem.yueba.app.singer.model.SingerVideoListModel;
import com.fullteem.yueba.app.ui.BaseActivity;
import com.fullteem.yueba.app.ui.PubDetailsActivity;
import com.fullteem.yueba.app.ui.fragment.MoodDetailsFragment;
import com.fullteem.yueba.engine.upload.PhotoOwnerEnum;
import com.fullteem.yueba.engine.upload.UploadManager;
import com.fullteem.yueba.globle.GlobleConstant;
import com.fullteem.yueba.model.AlbumPhotoModel;
import com.fullteem.yueba.model.GiftModel;
import com.fullteem.yueba.model.MoodModel;
import com.fullteem.yueba.model.ResponeModel;
import com.fullteem.yueba.model.presentmodel.SingerVideoListPresentModel;
import com.fullteem.yueba.net.http.CustomAsyncResponehandler;
import com.fullteem.yueba.net.http.HttpRequest;
import com.fullteem.yueba.util.DisplayUtils;
import com.fullteem.yueba.util.ImageLoaderUtil;
import com.fullteem.yueba.util.LogUtil;
import com.fullteem.yueba.util.UmengUtil;
import com.fullteem.yueba.widget.ChildViewPager;
import com.fullteem.yueba.widget.ExpandListView;
import com.fullteem.yueba.widget.NearbyDateItemView;
import com.fullteem.yueba.widget.TopTitleView;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月13日&emsp;上午9:54:21
 * @use 歌手视频列表页面
 */
public class SingerVideoListActivity extends BaseActivity {
	private TextView tvUserGender;
	private SingerDetailModel singerModel;
	private SingerVideoListPresentModel presentMode;
	private RelativeLayout rlMood;
	private RecyclerView recyclerviewGift, recyclerview_album;
	private ImageView ivArrowRightGift, ivArrowLeftGift, ivArrowLeftAlbum,
			ivArrowRightAlbum;
	private ImageView ivLevel;
	private ExpandListView lvVideo;
	private CheckBox ckbHeart;
	private List<GiftModel> listGift;
	private List<SingerVideoListModel> listVideo;
	private List<AlbumPhotoModel> photoModelList;
	private PerssonalGiftAdapter adapterGift;
	//private VideoListAdapter adapterVideoList;
	private PerssonalAlbumAdapter adapterAlbum;
	private MoodDetailsFragment fragmentMood;

	private boolean isBack;
	
	// video
	private ChildViewPager singerViewPager;
	//private ArrayList<ImageView> mSingerVideoList;
	private SingerVideoAdapter singerVideoAdapter;
	private List<View> singerVideoViewList;

	private Button btnBarName;// 歌手所属酒吧名称按钮

	private String pubName;// 歌手对应酒吧名称；
	private TextView singerIntroduction;// 歌手简介

	public SingerVideoListActivity() {
		super(-1);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		BinderFactory binderFactory = new BinderFactoryBuilder().build();
		ViewBinder vb = binderFactory.createViewBinder(this, true);
		singerModel = new SingerDetailModel();
		presentMode = new SingerVideoListPresentModel(singerModel); // 当前登录用户的信息直接获取实例，其他用户传对应bean
		View rootView = vb.inflateAndBind(R.layout.activity_singer_video_list,
				presentMode);
		setContentView(rootView);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void initViews() {
		initTopTitle();
		btnBarName = (Button) findViewById(R.id.btnBarName);
		tvUserGender = (TextView) findViewById(R.id.tv_userGender);
		// lvVideo = (ExpandListView) findViewById(R.id.lv_video);
		singerViewPager = (ChildViewPager) findViewById(R.id.cp_singer_video);
		rlMood = (RelativeLayout) findViewById(R.id.rl_mood);
		ivArrowLeftGift = (ImageView) findViewById(R.id.iv_arrowLeftGift);
		ivArrowRightGift = (ImageView) findViewById(R.id.iv_arrowRightGift);
		ivArrowLeftAlbum = (ImageView) findViewById(R.id.iv_arrowLeftAlbum);
		ivArrowRightAlbum = (ImageView) findViewById(R.id.iv_arrowRightAlbum);
		ivLevel = (ImageView) findViewById(R.id.iv_level);
		recyclerviewGift = (RecyclerView) findViewById(R.id.recyclerview_gift);
		recyclerview_album = (RecyclerView) findViewById(R.id.recyclerview_album);

		singerIntroduction = (TextView) findViewById(R.id.singer_introduction);

		ckbHeart = (CheckBox) findViewById(R.id.ckbHeart);
	}

	@Override
	public void initData() {

		pubName = getIntent().getStringExtra(GlobleConstant.PUB_NAME);
		if (TextUtils.isEmpty(pubName)) {
			btnBarName.setVisibility(View.GONE);
		} else {
			btnBarName.setVisibility(View.VISIBLE);
			btnBarName.setText(pubName);
		}

		if (!isBack)
			loadData(getIntent().getIntExtra(GlobleConstant.SINGER_ID, -1));

		LinearLayoutManager[] layoutManager = new LinearLayoutManager[2];
		for (int i = 0; i < layoutManager.length; i++) {
			layoutManager[i] = new LinearLayoutManager(this);
			layoutManager[i].setOrientation(LinearLayoutManager.HORIZONTAL);
		}
		recyclerview_album.setLayoutManager(layoutManager[0]);
		recyclerviewGift.setLayoutManager(layoutManager[1]);

		recyclerview_album
				.setAdapter(adapterAlbum = adapterAlbum == null ? new PerssonalAlbumAdapter(
						photoModelList = photoModelList == null ? new LinkedList<AlbumPhotoModel>()
								: photoModelList)
						: adapterAlbum);
		recyclerviewGift
				.setAdapter(adapterGift = adapterGift == null ? new PerssonalGiftAdapter(
						listGift = listGift == null ? new LinkedList<GiftModel>()
								: listGift)
						: adapterGift);
		fragmentMood = fragmentMood == null ? new MoodDetailsFragment()
		: fragmentMood;
		
		initVideoData();
	}

	
	private void initVideoData(){
		listVideo = listVideo == null ? new LinkedList<SingerVideoListModel>() : listVideo;
		singerVideoViewList = new ArrayList<View>();
		singerVideoAdapter = singerVideoAdapter == null ? new
				 SingerVideoAdapter(this, singerVideoViewList, listVideo) : singerVideoAdapter;

		singerViewPager.setAdapter(singerVideoAdapter);
		
		singerViewPager.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
			}
			
		});
	}	
	
	private void makeupVideoData(){
		LogUtil.printSingerLog("enter makeupVideoData");
		
		if (!listVideo.isEmpty()){
			 listVideo.clear();
		 }
		 
		for (int i = 0; i < SingerUtil.fakeSize; i++){
			SingerVideoListModel model = new SingerVideoListModel();
			
			if (i == 1){
				model.setVideoLogoUrl(SingerUtil.defaultVideoLogoUrl2);
				model.setVideoPlayUri(SingerUtil.defaultVideoFileUrl2);
			}else{
				model.setVideoLogoUrl(SingerUtil.defaultVideoLogoUrl);
				model.setVideoPlayUri(SingerUtil.defaultVideoFileUrl);
			}
			
			listVideo.add(model);
		}

		 updateVideoData();
	}
	
	private void updateVideoData(){
		LogUtil.printSingerLog("enter updateVideoData");
		singerVideoViewList.clear();

		if (listVideo == null || listVideo.size() == 0){
			return;
		}
		
		int size = listVideo.size();
		LogUtil.printSingerLog("video list size:" + size);
		
		for (int i = 0; i < size; i++) {
			SingerVideoItemView view = new SingerVideoItemView(
					this);
			singerVideoViewList.add(view.createItemView(listVideo
					.get(i)));
		}
		
		LogUtil.printSingerLog("notify data changed");
		singerVideoAdapter.notifyDataSetChanged();
	}


	@Override
	public void bindViews() {
		btnBarName.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int barId = getIntent().getIntExtra(GlobleConstant.PUB_ID, -1);
				Intent intent = new Intent(context, PubDetailsActivity.class);
				intent.putExtra(GlobleConstant.PUB_ID, barId);
				intent.putExtra(GlobleConstant.PUB_NAME, pubName);
				startActivity(intent);
			}
		});

		rlMood.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (singerModel.getSingerId() == -1) {
					showToast(getString(R.string.hint_singerDataLoading));
					return;
				}
				if (presentMode.getMoodNoneVisibility() == View.VISIBLE) {
					showToast(getString(R.string.hint_noneMood));
					return;
				}

				getSupportFragmentManager()
						.beginTransaction()
						.setCustomAnimations(R.anim.slide_in_from_left,
								R.anim.slide_out_to_right)
						.replace(android.R.id.content, fragmentMood)
						.addToBackStack(null).commit();
			}
		});

		ckbHeart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Integer userId = Integer.valueOf(AppContext.getApplication()
						.getUserInfo().getUserId());
				if (userId == null || userId <= -1)
					return;
				if (singerModel.getSingerId() == -1) {
					ckbHeart.setChecked(!ckbHeart.isChecked());
					showToast("请等待歌手信息加载完成后再试");
					return;
				}
				HttpRequest.getInstance(SingerVideoListActivity.this)
						.operateFollow(
								2,
								singerModel.getSingerId(),
								userId,
								singerModel.getSingerPhone(),
								AppContext.getApplication().getUserInfo()
										.getUserMobile(),
								singerModel.isFollow(),
								new CustomAsyncResponehandler() {

									@Override
									public void onSuccess(ResponeModel baseModel) {
										if (baseModel != null
												&& baseModel.isStatus()){
											if(singerModel.isFollow()){
												UmengUtil.onEvent(SingerVideoListActivity.this, "bar_singer_cancel_hits");
												LogUtil.printUmengLog("bar_singer_cancel_hits");
											}else{
												UmengUtil.onEvent(SingerVideoListActivity.this, "bar_singer_praise_hits");
												LogUtil.printUmengLog("bar_singer_praise_hits");
											}
											loadData(singerModel.getSingerId());
										}else {
											ckbHeart.setChecked(!ckbHeart
													.isChecked());
											showToast(getString(R.string.hint_operationError));
										}
									}

									@Override
									public void onFailure(Throwable error,
											String content) {
										ckbHeart.setChecked(!ckbHeart
												.isChecked());
										showToast(getString(R.string.hint_operationError));
									}
								});
				// if (singerModel.isFollow()) {
				// HttpRequest.getInstance().cancelFollow(singerModel.getSingerId(),
				// userId, new CustomAsyncResponehandler() {
				//
				// @Override
				// public void onSuccess(ResponeModel baseModel) {
				// if (baseModel != null) {
				// if (!baseModel.isStatus()) {
				// ckbHeart.setChecked(true);
				// showToast("取消关注失败！\n" + baseModel.getJson());
				// }
				// }
				// }
				//
				// @Override
				// public void onFailure(Throwable error, String content) {
				// ckbHeart.setChecked(true);
				// showToast("取消关注失败！\n");
				// }
				// });
				// return;
				// }
				// HttpRequest.getInstance(SingerVideoListActivity.this).addFollow(2,
				// singerModel.getSingerId(), userId, new
				// CustomAsyncResponehandler() {
				//
				// @Override
				// public void onSuccess(ResponeModel baseModel) {
				// if (baseModel != null) {
				// if (!baseModel.isStatus()) {
				// ckbHeart.setChecked(false);
				// showToast("关注失败！\n" + baseModel.getJson());
				// }
				// }
				// }
				//
				// @Override
				// public void onStart() {
				// ckbHeart.setChecked(true);
				// }
				//
				// @Override
				// public void onFinish() {
				//
				// }
				//
				// @Override
				// public void onFailure(Throwable error, String content) {
				// showToast("关注失败！\n");
				// ckbHeart.setChecked(false);
				// }
				// });
			}
		});

	}

	private void setShowUserGender(String userGender) {
		Drawable drawableLeft;
		if ("男".equals(userGender)) {
			drawableLeft = getResources().getDrawable(R.drawable.sex_boy);
			tvUserGender.setEnabled(false);
		} else {
			drawableLeft = getResources().getDrawable(R.drawable.sex_girl);
		}
		drawableLeft.setBounds(0, 0, drawableLeft.getMinimumWidth(),
				drawableLeft.getMinimumHeight());
		tvUserGender.setCompoundDrawables(drawableLeft, null, null, null);
	}

	private void initTopTitle() {
		TopTitleView topTitle = (TopTitleView) findViewById(R.id.top_title);
		topTitle.showLeftImag(R.drawable.back, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		topTitle.showCenterText(
				getIntent().getStringExtra(GlobleConstant.SINGER_NAME), null);
	}

	/**
	 * 从服务器获取数据
	 */
	private void loadData(int singerId) {
		Integer userId = Integer.valueOf(AppContext.getApplication()
				.getUserInfo().getUserId());
		if (singerId == -1 || userId == null) {
			showToast(getString(R.string.hint_getSingerDetailError));
			finish();
		}

		HttpRequest.getInstance(SingerVideoListActivity.this).getSingerDetait(
				singerId, userId, new CustomAsyncResponehandler() {
					@Override
					public void onStart() {
					}

					@Override
					public void onSuccess(ResponeModel baseModel) {
						if (baseModel == null || !baseModel.isStatus()) {
							showToast(getString(R.string.hint_getSingerDetailError));
							return;
						}

						// 歌手收到额礼物列表
						List<GiftModel> listResultGift = (List<GiftModel>) baseModel
								.getListObject("singerGiftList",
										GiftModel.class);
						if (listResultGift == null
								|| listResultGift.size() <= 0
								|| listResultGift.get(0) == null)
							presentMode.setGiftVisibility(View.GONE);
						else {
							if (!listGift.isEmpty())
								listGift.clear();
							listGift.addAll(listResultGift);
							adapterGift.notifyDataSetChanged();
						}

						// 歌手视频列表
						List<SingerVideoListModel> listResultVideo = (List<SingerVideoListModel>) baseModel
								.getListObject("singerVideoList",
										SingerVideoListModel.class);
						
						if (listResultVideo == null
								|| listResultVideo.size() <= 0
								|| listResultVideo.get(0) == null) {
							if (SingerUtil.useFakeData){
								makeupVideoData();
							}else{
								presentMode.setVideoVisibility(View.GONE);
							}

						} else {
							 if (!listVideo.isEmpty()){
								 listVideo.clear();
							 }
							 
							 listVideo.addAll(listResultVideo);
							 updateVideoData();
							
						}

						// 歌手详情
						singerModel = (SingerDetailModel) baseModel.getObject(
								"singerMap", SingerDetailModel.class);
						if (singerModel != null) {
							setShowUserGender(singerModel.getSingerGender());
							singerIntroduction.setText(singerModel
									.getSingerDetail());
							ImageView ivUserHeader = (ImageView) findViewById(R.id.iv_usrHeader);
							ivUserHeader.setBackgroundColor(Color.TRANSPARENT);
							ImageLoaderUtil
									.getImageLoader()
									.displayImage(
											DisplayUtils
													.getAbsolutelyUrl(TextUtils
															.isEmpty(singerModel
																	.getLogoUrl()) ? singerModel
															.getUserLogoUrl()
															: singerModel
																	.getLogoUrl()),
											ivUserHeader,
											ImageLoaderUtil.getOptions());
						}

						// 获取关注、魅力值等
						String isFollow = baseModel
								.getTreeInResult("isAttention");
						Integer total = baseModel.getTreeInResult("total");
						Integer num = baseModel.getTreeInResult("num");
						singerModel.setIsFollow("true"
								.equalsIgnoreCase(isFollow));
						singerModel.setTotal(total == null ? 0 : total);
						singerModel.setNum(num == null ? 0 : num);
						ckbHeart.setChecked(singerModel.isFollow());
						ivLevel.setImageResource(singerModel.getSingerLevel());
						presentMode.setModel(singerModel);
						fragmentMood.setModel(singerModel);

						// 歌手心情记录
						List<MoodModel> listResultMood = (List<MoodModel>) baseModel
								.getListObject("singerMoodeRecordList",
										MoodModel.class);
						if (listResultMood == null
								|| listResultMood.size() <= 0
								|| listResultMood.get(0) == null)
							presentMode.setMoodNoneVisibility(View.VISIBLE);
						else {
							presentMode.setUserMood(listResultMood.get(0)
									.getMoodRecordText());
							ImageView ivMoodHeader = (ImageView) findViewById(R.id.iv_moodHeader);
							ivMoodHeader.setBackgroundColor(Color.TRANSPARENT);
							if (TextUtils.isEmpty(listResultMood.get(0)
									.getMoodRecordImgUrl())) {
								ivMoodHeader.setVisibility(View.GONE);
							} else {
								ivMoodHeader.setVisibility(View.VISIBLE);
								ImageLoaderUtil
										.getImageLoader()
										.displayImage(
												DisplayUtils
														.getAbsolutelyUrl(listResultMood
																.get(0)
																.getMoodRecordImgUrl()
																.contains(",") ? listResultMood
																.get(0)
																.getMoodRecordImgUrl()
																.split(",")[0]
																: listResultMood
																		.get(0)
																		.getMoodRecordImgUrl()),
												ivMoodHeader,
												ImageLoaderUtil
														.getOptions(
																R.drawable.img_loading_default_copy,
																R.drawable.img_loading_empty_copy,
																R.drawable.img_loading_fail_copy));
							}
						}

						// 歌手相册
						UploadManager.getAlbumList(PhotoOwnerEnum.SINGER,
								baseModel, photoModelList);
						if (photoModelList.size() <= 0) {
							presentMode.setAlbumNoneVisibility(View.VISIBLE);
						} else {
							presentMode.setAlbumNoneVisibility(View.GONE);
						}

						adapterAlbum.notifyDataSetChanged();
					}

					@Override
					public void onFailure(Throwable error, String content) {
					}

					@Override
					public void onFinish() {
					}
				});
	}
}
