package com.fullteem.yueba.app.ui;

import java.util.LinkedList;
import java.util.List;

import org.robobinding.ViewBinder;
import org.robobinding.binder.BinderFactory;
import org.robobinding.binder.BinderFactoryBuilder;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.fullteem.yueba.R;
import com.fullteem.yueba.app.adapter.WantPartakeAdapter;
import com.fullteem.yueba.app.adapter.WantPartakeAdapter.INotifyRefresh;
import com.fullteem.yueba.db.DBFriendListDao;
import com.fullteem.yueba.globle.GlobleConstant;
import com.fullteem.yueba.model.DateDetailModel;
import com.fullteem.yueba.model.DateModel;
import com.fullteem.yueba.model.DatePersonModel;
import com.fullteem.yueba.model.ResponeModel;
import com.fullteem.yueba.model.UserCommonModel;
import com.fullteem.yueba.model.presentmodel.DateManagePresentModel;
import com.fullteem.yueba.net.http.CustomAsyncResponehandler;
import com.fullteem.yueba.net.http.HttpRequest;
import com.fullteem.yueba.util.DisplayUtils;
import com.fullteem.yueba.util.ImageLoaderUtil;
import com.fullteem.yueba.widget.EmptyView;
import com.fullteem.yueba.widget.ExpandListView;
import com.fullteem.yueba.widget.HintConfirmationPopWindow;
import com.fullteem.yueba.widget.TopTitleView;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月2日&emsp;下午5:43:07
 * @use 约会管理详细界面
 */
public class DateManageDetailAcitvity extends BaseActivity implements
		INotifyRefresh {

	private DateManagePresentModel presentMode;
	private ExpandListView lvWantPartake;
	private WantPartakeAdapter adapter;
	private List<DatePersonModel> listDateUser, listDateUserTmp;// 想要参与约会的人,Tmp存储临时数据，如果实际条数大于2条，则全部存储进来
	private DateDetailModel dateModel;
	private Button btnLoadMore;
	private Button btnDateStop, btnDateCancel;
	private EventListener mListener;
	private HintConfirmationPopWindow mConfirmation;

	private int pageSize = 3;// 默认3条为1页,如果有三条记录则只显示两条记录并显示加载更多，不足三条则不显示加载更多
	private int pageNo = 1;
	private boolean isFirstLoad = true;
	private EmptyView emptyView;

	// 未使用该方法设置布局
	public DateManageDetailAcitvity() {
		super(-1);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		BinderFactory binderFactory = new BinderFactoryBuilder().build();
		ViewBinder vb = binderFactory.createViewBinder(this, true);
		presentMode = new DateManagePresentModel(DateManageDetailAcitvity.this);
		View rootView = vb.inflateAndBind(R.layout.activity_date_manage_detail,
				presentMode);
		setContentView(rootView);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void initViews() {
		initTopTitle();
		lvWantPartake = (ExpandListView) findViewById(R.id.lv_wantPartake);
		btnLoadMore = (Button) findViewById(R.id.btnLoadMore);
		btnLoadMore.setVisibility(View.GONE);
		btnDateStop = (Button) findViewById(R.id.btn_stop);
		btnDateCancel = (Button) findViewById(R.id.btn_cancel);
		emptyView = new EmptyView(DateManageDetailAcitvity.this);
		emptyView.setVisibility(View.GONE);
		((ViewGroup) lvWantPartake.getParent()).addView(emptyView);
		// lvWantPartake.setEmptyView(emptyView);
		lvWantPartake.setVisibility(View.GONE);
		findViewById(R.id.lvEmpty).setVisibility(View.VISIBLE);
	}

	@Override
	public void initData() {
		mListener = new EventListener();

		listDateUserTmp = new LinkedList<DatePersonModel>();

		DateModel dateModeTmp = (DateModel) getIntent().getSerializableExtra(
				GlobleConstant.DATE_MODEL);

		dateModel = new DateDetailModel();
		dateModel.setDateModel(dateModeTmp);

		ImageView iv_pubheader = (ImageView) findViewById(R.id.iv_pubheader);
		iv_pubheader.setBackgroundColor(Color.TRANSPARENT);
		ImageLoaderUtil.getImageLoader().displayImage(
				DisplayUtils.getAbsolutelyUrl(dateModel.getBarLogoUrl()),
				iv_pubheader, ImageLoaderUtil.getOptions());

		presentMode.setModel(dateModel);

		lvWantPartake
				.setAdapter(adapter = adapter == null ? new WantPartakeAdapter(
						appContext,
						listDateUser = listDateUser == null ? new LinkedList<DatePersonModel>()
								: listDateUser)
						: adapter);

		adapter.setRefreshListener(this);

		loadDateDetail(dateModeTmp.getUserAppointmentId());

		dateModeTmp = null;
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
		// listDateUser.add(userInfoModel);
		// }
		// if (listDateUser.size() > 2) {
		// // 默认只展示两条数据
		// }
		//
		// lvWantPartake.setAdapter(new WantPartakeAdapter(listDateUser));

		// --------------------------examples data--------------------------
	}

	@Override
	public void bindViews() {
		btnDateStop.setOnClickListener(mListener);
		btnDateCancel.setOnClickListener(mListener);
		btnLoadMore.setOnClickListener(mListener);
		lvWantPartake.setOnItemClickListener(mListener);
	}

	private void initTopTitle() {
		TopTitleView topTitle = (TopTitleView) findViewById(R.id.top_title);
		topTitle.showLeftImag(R.drawable.back, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		topTitle.showCenterText(getString(R.string.date_manage), null);
	}

	private class EventListener implements OnClickListener, OnItemClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_stop:
				mConfirmation = mConfirmation == null ? new HintConfirmationPopWindow(
						DateManageDetailAcitvity.this) : mConfirmation;
				mConfirmation.setCenterText(getString(R.string.hint_stopDate))
						.setSureButton(null, new OnClickListener() {

							@Override
							public void onClick(View v) {
								HttpRequest.getInstance().getDateUpdate(
										dateModel.getUserAppointmentId(), 2,
										new CustomAsyncResponehandler() {

											@Override
											public void onSuccess(
													ResponeModel baseModel) {
												if (baseModel == null
														|| !baseModel
																.isStatus()) {
													showToast(getString(R.string.hint_operationError));
													return;
												}
												isFirstLoad = true;
												loadDateDetail(dateModel
														.getUserAppointmentId());
											}

											@Override
											public void onFailure(
													Throwable error,
													String content) {
												showToast(getString(R.string.hint_operationError));
											}
										});
								mConfirmation.getPopupWindow().dismiss();
							}
						}).setCancelButton(null, null).showWindow();
				break;
			case R.id.btn_cancel:
				mConfirmation = mConfirmation == null ? new HintConfirmationPopWindow(
						DateManageDetailAcitvity.this) : mConfirmation;
				mConfirmation
						.setCenterText(getString(R.string.hint_cancelDate))
						.setSureButton(null, new OnClickListener() {

							@Override
							public void onClick(View v) {
								HttpRequest.getInstance().getDateUpdate(
										dateModel.getUserAppointmentId(), 0,
										new CustomAsyncResponehandler() {

											@Override
											public void onSuccess(
													ResponeModel baseModel) {
												if (baseModel == null
														|| !baseModel
																.isStatus()) {
													showToast(getString(R.string.hint_operationError));
													return;
												}
												isFirstLoad = true;
												loadDateDetail(dateModel
														.getUserAppointmentId());
											}

											@Override
											public void onFailure(
													Throwable error,
													String content) {
												showToast(getString(R.string.hint_operationError));
											}
										});
								mConfirmation.getPopupWindow().dismiss();
							}
						}).setCancelButton(null, null).showWindow();
				break;

			case R.id.btnLoadMore:
				pageNo++;
				loadData(dateModel.getUserAppointmentId(),
						dateModel.getStatus());
				break;

			}
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view,
				final int position, long id) {
			Intent intent = new Intent(DateManageDetailAcitvity.this,
					ChatActivity.class);
			intent.putExtra("userId", listDateUser.get(position)
					.getUserMobile());
			intent.putExtra("nickname", listDateUser.get(position)
					.getUserNickname());
			intent.putExtra("imgurl", listDateUser.get(position)
					.getUserHeaderUrl());

			// 直接在跳转之前就在数据库插入信息
			new Thread(new Runnable() {
				@Override
				public void run() {
					DBFriendListDao dao = new DBFriendListDao(
							DateManageDetailAcitvity.this);
					UserCommonModel user = new UserCommonModel();
					user.setUserMobile(listDateUser.get(position)
							.getUserMobile());
					user.setUserId(listDateUser.get(position).getUserId() + "");
					user.setUserName(listDateUser.get(position)
							.getUserNickname());
					String imgUrl;
					if (listDateUser.get(position).getUserHeaderUrl() == null) {
						imgUrl = "null";
					} else {
						imgUrl = listDateUser.get(position).getUserHeaderUrl();
					}
					user.setUserLogoUrl(imgUrl);
					dao.saveContacter(user);
				}
			}).start();
			startActivity(intent);
		}

	}

	private void loadData(int userAppointmentId, int status) {
		Integer userApponitmentJoinType = status == 0 ? null : status == 2 ? 1
				: 2; // 终止约会返回全部列表，停止报名即约会成功，只需要成功的列表,不然就是申请状态中的
		if (status == 0 || status == 2)
			adapter.setShowManage(false);

		// 获取已加入成员列表
		HttpRequest.getInstance().getDatePerson(userAppointmentId, pageNo,
				pageSize, userApponitmentJoinType,
				new CustomAsyncResponehandler() {

					@Override
					public void onStart() {
						adapter.notifyDataSetChanged();
					}

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
										if (listDateUserTmp.size() > 0)
											listDateUserTmp.clear();
										listDateUserTmp.addAll(listDatePersson);
										for (int i = 0; i < 2; i++) {
											listDateUser.add(listDatePersson
													.get(i));
										}
									} else
										listDateUser.addAll(listDatePersson);
									isFirstLoad = false;
								} else {
									if (listDateUserTmp != null
											&& listDateUserTmp.size() > 0
											&& listDateUserTmp.get(0) != null) {
										listDateUser.clear();
										listDateUser.addAll(listDateUserTmp);
										adapter.notifyDataSetChanged();
										listDateUserTmp = null;
									}
									listDateUser.addAll(listDatePersson);
									adapter.notifyDataSetChanged();
								}
								btnLoadMore.setVisibility(listDatePersson
										.size() >= pageSize ? View.VISIBLE
										: View.GONE);
							} else {
								if (!isFirstLoad && listDateUserTmp != null
										&& listDateUserTmp.size() > 0
										&& listDateUserTmp.get(0) != null) {
									listDateUser.clear();
									listDateUser.addAll(listDateUserTmp);
									adapter.notifyDataSetChanged();
									listDateUserTmp = null;
									adapter.notifyDataSetChanged();
								}
								lvWantPartake.setVisibility(View.GONE);
								btnLoadMore.setVisibility(View.GONE);
							}
						} else {
							lvWantPartake.setVisibility(View.GONE);
							btnLoadMore.setVisibility(View.GONE);
						}
					}

					@Override
					public void onFinish() {
						findViewById(R.id.lvEmpty).setVisibility(View.GONE);
						if (listDateUser == null || listDateUser.size() < 1)
							emptyView.setVisibility(View.VISIBLE);
						lvWantPartake.setVisibility(View.VISIBLE);
					}

					@Override
					public void onFailure(Throwable error, String content) {
						btnLoadMore.setVisibility(View.GONE);
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
							String usrtName = dateModel.getUserName();
							dateModel = (DateDetailModel) baseModel
									.getObject("userAppointmentMap",
											DateDetailModel.class);
							if (dateModel != null) {
								dateModel.setUserName(TextUtils
										.isEmpty(dateModel.getUserName()) ? usrtName
										: dateModel.getUserName());
								presentMode.setModel(dateModel);
								if (dateModel.getStatus() == 0) {
									btnDateCancel.setVisibility(View.GONE);
									btnDateStop.setText(R.string.hint_cancled);
									btnDateStop
											.setBackgroundResource(R.drawable.btn_9red2white_selector);
									btnDateStop.setEnabled(false);
								}
								if (dateModel.getStatus() == 2) {
									btnDateCancel.setVisibility(View.GONE);
									btnDateStop.setText(R.string.hint_sured);
									btnDateStop.setEnabled(false);
								}
								if (!isFirstLoad) {
									listDateUser.clear();
									loadData(dateModel.getUserAppointmentId(),
											dateModel.getStatus());
								}
							}
						}
					}

					@Override
					public void onFinish() {
						if (isFirstLoad) {
							loadData(dateModel.getUserAppointmentId(),
									dateModel.getStatus());
						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
					}
				});
	}

	@Override
	public void onRefresh() {
		listDateUser.clear();
		loadData(dateModel.getUserAppointmentId(), dateModel.getStatus());
	}

}
