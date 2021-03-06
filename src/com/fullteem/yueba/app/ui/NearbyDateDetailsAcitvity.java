package com.fullteem.yueba.app.ui;

import java.util.LinkedList;
import java.util.List;

import org.robobinding.ViewBinder;
import org.robobinding.binder.BinderFactory;
import org.robobinding.binder.BinderFactoryBuilder;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.fullteem.yueba.R;
import com.fullteem.yueba.app.AppContext;
import com.fullteem.yueba.app.adapter.EnrollAndPartakeAdapter;
import com.fullteem.yueba.db.DBFriendListDao;
import com.fullteem.yueba.globle.GlobleConstant;
import com.fullteem.yueba.model.DateDetailModel;
import com.fullteem.yueba.model.DateModel;
import com.fullteem.yueba.model.DatePersonModel;
import com.fullteem.yueba.model.ResponeModel;
import com.fullteem.yueba.model.UserCommonModel;
import com.fullteem.yueba.model.presentmodel.NearbyDateDetailsPresentModel;
import com.fullteem.yueba.net.http.CustomAsyncResponehandler;
import com.fullteem.yueba.net.http.HttpRequest;
import com.fullteem.yueba.util.DisplayUtils;
import com.fullteem.yueba.util.ImageLoaderUtil;
import com.fullteem.yueba.util.LogUtil;
import com.fullteem.yueba.util.UmengUtil;
import com.fullteem.yueba.widget.EmptyView;
import com.fullteem.yueba.widget.ExpandListView;
import com.fullteem.yueba.widget.HintConfirmationPopWindow;
import com.fullteem.yueba.widget.HintContentPopWindow;
import com.fullteem.yueba.widget.TopTitleView;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月2日&emsp;下午5:43:07
 * @use 附近约会详细界面
 */
public class NearbyDateDetailsAcitvity extends BaseActivity {

	private NearbyDateDetailsPresentModel presentMode;
	private ExpandListView lvEnrollAndPartake;
	private List<DatePersonModel> listEnrollAndPartake,
			listEnrollAndPartakeTmp;// listEnrollAndPartakeTmp存储临时数据，如果实际条数大于2条，则全部存储进来
	private EnrollAndPartakeAdapter adapterEnrollAndPartakeAdapter;
	private Button btnPartake;// 我要报名
	private Button btnLoadMore;
	private EventListener mListener;
	private DateModel dateModel;
	private HintConfirmationPopWindow mConfirmation;
	private TextView barName;

	private int pageSize = 3;// 默认3条为1页,如果有三条记录则只显示两条记录并显示加载更多，不足三条则不显示加载更多
	private int pageNo = 1;
	private boolean isFirstLoad = true;

	// 未使用该方法设置布局
	public NearbyDateDetailsAcitvity() {
		super(-1);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		BinderFactory binderFactory = new BinderFactoryBuilder().build();
		ViewBinder vb = binderFactory.createViewBinder(this, true);
		presentMode = new NearbyDateDetailsPresentModel(
				NearbyDateDetailsAcitvity.this);
		View rootView = vb.inflateAndBind(
				R.layout.activity_naerby_date_details, presentMode);
		setContentView(rootView);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void initViews() {
		dateModel = (DateModel) getIntent().getSerializableExtra(
				GlobleConstant.DATE_MODEL);

		initTopTitle();
		lvEnrollAndPartake = (ExpandListView) findViewById(R.id.lv_enrollAndPartake);
		btnPartake = (Button) findViewById(R.id.btn_partake);
		btnLoadMore = (Button) findViewById(R.id.btnLoadMore);
		btnLoadMore.setVisibility(View.GONE);
		EmptyView emptyView = new EmptyView(NearbyDateDetailsAcitvity.this);
		emptyView.setVisibility(View.GONE);
		((ViewGroup) lvEnrollAndPartake.getParent()).addView(emptyView);
		lvEnrollAndPartake.setEmptyView(emptyView);
		lvEnrollAndPartake.setVisibility(View.GONE);
		findViewById(R.id.lvEmpty).setVisibility(View.VISIBLE);

		barName = (TextView) findViewById(R.id.tv_date_title);
	}

	@Override
	public void initData() {

		loadData(dateModel.getUserAppointmentId());
		if (dateModel.isSuccessful())
			btnPartake.setVisibility(View.GONE);

		ImageView iv_pubheader = (ImageView) findViewById(R.id.iv_pubheader);
		iv_pubheader.setBackgroundColor(Color.TRANSPARENT);
		ImageLoaderUtil.getImageLoader().displayImage(
				DisplayUtils.getAbsolutelyUrl(dateModel.getBarLogoUrl()),
				iv_pubheader, ImageLoaderUtil.getOptions());

		presentMode.setModel(dateModel);

		mListener = new EventListener();

		listEnrollAndPartakeTmp = new LinkedList<DatePersonModel>();

		lvEnrollAndPartake
				.setAdapter(adapterEnrollAndPartakeAdapter = adapterEnrollAndPartakeAdapter == null ? new EnrollAndPartakeAdapter(
						NearbyDateDetailsAcitvity.this,
						listEnrollAndPartake = listEnrollAndPartake == null ? new LinkedList<DatePersonModel>()
								: listEnrollAndPartake)
						: adapterEnrollAndPartakeAdapter);

		// --------------------------examples data--------------------------
		// presentMode.setDateTitle(getResString(R.string.date_title_normal));
		// presentMode.setDateLocation(getResString(R.string.name_place_normal));
		// presentMode.setDateTime(new Date().toString());
		// presentMode.setSponsor("碗碗");
		// presentMode.setFees("AA制");
		// presentMode.setDescribe("工作让人疲倦，工资让人奋斗，工作让人疲倦，工资让人奋斗，工作让人疲倦，工资让人奋斗～");
		//
		// int dataTotal = new Random().nextInt(10);
		// for (int i = 0; i < dataTotal; i++) {
		// UserInfoModel userInfoModel = new UserInfoModel();
		// userInfoModel.setUserHeader("drawable://" +
		// R.drawable.ico_loginmain_sina);
		// if (i % 2 == 0)
		// userInfoModel.setUserGender("男");
		// else
		// userInfoModel.setUserGender("女");
		// userInfoModel.setUserAge(i + "");
		// userInfoModel.setUserMood("说点什么～");
		// userInfoModel.setUserNickname("叫什么来着");
		// listEnrollAndPartake.add(userInfoModel);
		// }
		// if (listEnrollAndPartake.size() > 2) {
		// // 默认只展示两条数据
		// }
		//
		// lvEnrollAndPartake.setAdapter(new
		// EnrollAndPartakeAdapter(listEnrollAndPartake));

		// --------------------------examples data--------------------------
	}

	@Override
	public void bindViews() {
		btnPartake.setOnClickListener(mListener);
		btnLoadMore.setOnClickListener(mListener);
		lvEnrollAndPartake.setOnItemClickListener(mListener);
		barName.setOnClickListener(mListener);
	}

	private void initTopTitle() {
		TopTitleView topTitle = (TopTitleView) findViewById(R.id.top_title);
		topTitle.showLeftImag(R.drawable.back, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		topTitle.showCenterText(dateModel.getUserAppointmentTitle(), null);
	}

	private class EventListener implements OnClickListener, OnItemClickListener {

		@Override
		public void onClick(View v) {
			if (v == btnPartake) {
				datePartake();
				return;
			}
			if (v == btnLoadMore) {
				pageNo++;
				loadData(dateModel.getUserAppointmentId());
				return;
			}
			if (v == barName) {
				Intent intent = new Intent(NearbyDateDetailsAcitvity.this,
						PubDetailsActivity.class);
				LogUtil.printPushLog("bar id:" + dateModel.getBarId()
						+ " bar name:" + dateModel.getBarName());
				intent.putExtra(GlobleConstant.PUB_ID, dateModel.getBarId());
				intent.putExtra(GlobleConstant.PUB_NAME, dateModel.getBarName());
				startActivity(intent);
				return;
			}
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view,
				final int position, long id) {
			Intent intent = new Intent(NearbyDateDetailsAcitvity.this,
					ChatActivity.class);
			intent.putExtra("userId", listEnrollAndPartake.get(position)
					.getImServerId());
			intent.putExtra("nickname", listEnrollAndPartake.get(position)
					.getUserNickname());
			intent.putExtra("imgurl", listEnrollAndPartake.get(position)
					.getUserHeaderUrl());

			// 直接在跳转之前就在数据库插入信息
			new Thread(new Runnable() {
				@Override
				public void run() {
					DBFriendListDao dao = new DBFriendListDao(
							NearbyDateDetailsAcitvity.this);
					UserCommonModel user = new UserCommonModel();
					user.setUserMobile(listEnrollAndPartake.get(position)
							.getUserMobile());
					user.setUserId(listEnrollAndPartake.get(position)
							.getUserId() + "");
					user.setUserName(listEnrollAndPartake.get(position)
							.getUserNickname());
					String imgUrl;
					if (listEnrollAndPartake.get(position).getUserHeaderUrl() == null) {
						imgUrl = "null";
					} else {
						imgUrl = listEnrollAndPartake.get(position)
								.getUserHeaderUrl();
					}
					user.setUserLogoUrl(imgUrl);
					dao.saveContacter(user);
				}
			}).start();
			startActivity(intent);
		}

	}

	/**
	 * 从服务器获取数据 ,
	 */
	private void loadData(int userAppointmentId) {
		// 获取已加入成员列表
		HttpRequest.getInstance(NearbyDateDetailsAcitvity.this).getDatePerson(
				userAppointmentId, pageNo, pageSize, null,
				new CustomAsyncResponehandler() {

					@Override
					public void onSuccess(ResponeModel baseModel) {
						if (baseModel != null && baseModel.isStatus()) {
							List<DatePersonModel> listDatePersson = (List<DatePersonModel>) baseModel
									.getListObject("list",
											DatePersonModel.class);
							if (listDatePersson != null
									&& listDatePersson.size() > 0
									&& listDatePersson.get(0) != null) {
								if (isFirstLoad) {
									if (listDatePersson.size() > 2) {
										listEnrollAndPartakeTmp
												.addAll(listDatePersson);
										for (int i = 0; i < 2; i++) {
											listEnrollAndPartake
													.add(listDatePersson.get(i));
										}
									} else
										listEnrollAndPartake
												.addAll(listDatePersson);
									isFirstLoad = false;
								} else {
									if (listEnrollAndPartakeTmp != null
											&& listEnrollAndPartakeTmp.size() > 0
											&& listEnrollAndPartakeTmp.get(0) != null) {
										listEnrollAndPartake.clear();
										listEnrollAndPartake
												.addAll(listEnrollAndPartakeTmp);
										listEnrollAndPartakeTmp = null;
									}
									listEnrollAndPartake
											.addAll(listDatePersson);
								}
								adapterEnrollAndPartakeAdapter
										.notifyDataSetChanged();
								btnLoadMore.setVisibility(listDatePersson
										.size() >= pageSize ? View.VISIBLE
										: View.GONE);
							} else {
								if (!isFirstLoad
										&& listEnrollAndPartakeTmp != null
										&& listEnrollAndPartakeTmp.size() > 0
										&& listEnrollAndPartakeTmp.get(0) != null) {
									listEnrollAndPartake.clear();
									listEnrollAndPartake
											.addAll(listEnrollAndPartakeTmp);
									listEnrollAndPartakeTmp = null;
									adapterEnrollAndPartakeAdapter
											.notifyDataSetChanged();
								}
								lvEnrollAndPartake.setVisibility(View.GONE);
								btnLoadMore.setVisibility(View.GONE);
							}
						} else {
							lvEnrollAndPartake.setVisibility(View.GONE);
							btnLoadMore.setVisibility(View.GONE);
						}
					}

					@Override
					public void onFinish() {
						findViewById(R.id.lvEmpty).setVisibility(View.GONE);
						lvEnrollAndPartake.setVisibility(View.VISIBLE);
					}

					@Override
					public void onFailure(Throwable error, String content) {
						btnLoadMore.setVisibility(View.GONE);
					}
				});

		loadDateDetail(userAppointmentId);
	}

	/**
	 * 参与约会
	 */
	private void datePartake() {
		UmengUtil.onEvent(this, "enroll_date_button_hits");
		LogUtil.printUmengLog("enroll_date_button_hits");
		if(appContext.isEnrollDateClickable()){
			UmengUtil.onEvent(this, "enroll_date_button_users");
			LogUtil.printUmengLog("enroll_date_button_users");
			appContext.setEnrollDateClickable(false);
		}
		Integer userId = Integer.valueOf(AppContext.getApplication()
				.getUserInfo().getUserId());
		if (userId == null || userId < 0) {
			showToast(getString(R.string.hint_longinFirst));
			return;
		}
		btnPartake.setEnabled(false);
		HttpRequest.getInstance(NearbyDateDetailsAcitvity.this).getDatePartake(
				dateModel.getUserAppointmentId(), userId,
				new CustomAsyncResponehandler() {

					@Override
					public void onSuccess(ResponeModel baseModel) {
						if (baseModel != null && baseModel.isStatus()) {
							btnPartake
									.setText(getString(R.string.date_enrolling));
							btnPartake.setEnabled(false);
							if (presentMode.getModel().getUserMobile() == null
									|| presentMode.getModel().getUserName() == null) {
								new HintContentPopWindow(
										NearbyDateDetailsAcitvity.this)
										.showWindow(
												null,
												getString(R.string.hint_dateHasSubmitted),
												null, null)
										.getPopupWindow()
										.setOnDismissListener(
												new OnDismissListener() {
													@Override
													public void onDismiss() {
														loadDateDetail(dateModel
																.getUserAppointmentId());
													}
												});
								return;
							}
							mConfirmation = mConfirmation == null ? new HintConfirmationPopWindow(
									NearbyDateDetailsAcitvity.this)
									: mConfirmation;
							mConfirmation
									.setCenterText(
											getString(R.string.hint_datePartake))
									.setSureButton(
											getString(R.string.hint_dateChat),
											new OnClickListener() {
												@Override
												public void onClick(View v) {
													Intent intent = new Intent(
															NearbyDateDetailsAcitvity.this,
															ChatActivity.class);
													intent.putExtra(
															"userId",
															presentMode
																	.getModel()
																	.getImServerId());
													intent.putExtra(
															"nickname",
															presentMode
																	.getModel()
																	.getUserName());
													intent.putExtra(
															"imgurl",
															presentMode
																	.getModel()
																	.getUserLogoUrl());

													// 直接在跳转之前就在数据库插入信息
													new Thread(new Runnable() {
														@Override
														public void run() {
															DBFriendListDao dao = new DBFriendListDao(
																	NearbyDateDetailsAcitvity.this);
															UserCommonModel user = new UserCommonModel();
															user.setUserMobile(presentMode
																	.getModel()
																	.getUserMobile());
															user.setUserId(presentMode
																	.getModel()
																	.getUserId()
																	+ "");
															user.setUserName(presentMode
																	.getModel()
																	.getUserName());
															String imgUrl;
															if (presentMode
																	.getModel()
																	.getUserLogoUrl() == null) {
																imgUrl = "null";
															} else {
																imgUrl = presentMode
																		.getModel()
																		.getUserLogoUrl();
															}
															user.setUserLogoUrl(imgUrl);
															dao.saveContacter(user);
														}
													}).start();

													mConfirmation
															.disMissWindow();
													mConfirmation = null;// inform
																			// system
																			// to
																			// gc
													startActivity(intent);
												}
											})
									.setCancelButton(
											getString(R.string.hint_chatNext),
											null).showWindow();
						}

					}

					@Override
					public void onStart() {
					}

					@Override
					public void onFinish() {
					}

					@Override
					public void onFailure(Throwable error, String content) {
						btnPartake.setEnabled(true);
					}
				});
	}

	// 获取约会详情
	private void loadDateDetail(int userAppointmentId) {
		HttpRequest.getInstance().getDateDetail(userAppointmentId,
				new CustomAsyncResponehandler() {

					@Override
					public void onSuccess(ResponeModel baseModel) {
						if (baseModel != null && baseModel.isStatus()) {
							DateDetailModel dateDetailModel = (DateDetailModel) baseModel
									.getObject("userAppointmentMap",
											DateDetailModel.class);
							if (dateDetailModel != null) {
								dateModel
										.setUserAppointmentLocation(dateDetailModel
												.getUserAppointmentLocation());
								dateModel.setCount(dateDetailModel
										.getHasCount());
								dateModel.setHasCount(dateDetailModel
										.getCount());
								dateModel.setUserMobile(dateDetailModel
										.getUserMobile());
								dateModel.setImServerId(dateDetailModel
										.getImServerId());
								presentMode.setModel(dateModel);
							}
						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
					}
				});

	}
}
