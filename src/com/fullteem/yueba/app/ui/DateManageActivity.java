package com.fullteem.yueba.app.ui;

import java.util.LinkedList;
import java.util.List;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.fullteem.yueba.R;
import com.fullteem.yueba.app.AppContext;
import com.fullteem.yueba.app.adapter.DateEnrolledAdapter;
import com.fullteem.yueba.app.adapter.DateManageAdapter;
import com.fullteem.yueba.app.adapter.DateManageAdapter.DateType;
import com.fullteem.yueba.app.ui.fragment.SeeMoreFragment;
import com.fullteem.yueba.app.ui.fragment.SeeMoreFragment.IIXListViewEventListener;
import com.fullteem.yueba.globle.GlobleConstant;
import com.fullteem.yueba.model.DateModel;
import com.fullteem.yueba.model.NearbyDateModel;
import com.fullteem.yueba.model.ResponeModel;
import com.fullteem.yueba.net.http.CustomAsyncResponehandler;
import com.fullteem.yueba.net.http.HttpRequest;
import com.fullteem.yueba.util.DisplayUtils;
import com.fullteem.yueba.util.LogUtil;
import com.fullteem.yueba.util.UmengUtil;
import com.fullteem.yueba.widget.EmptyView;
import com.fullteem.yueba.widget.TopTitleView;
import com.fullteem.yueba.widget.XListView;

/**
 * @author jun
 * @version 1.0.0
 * @created 201SHOWITEMTOTAL年12月16日&emsp;下午1:56:02
 * @use 约会管理界面
 */
public class DateManageActivity extends BaseActivity {
	private ListView lvDatePublished, lvDateEnrolled;
	private LinearLayout llLoadMoreDatePublished, llLoadMoreDateEnrolled,
			llDatePublished, llDateEnrolled;
	private View lvLoadingEnrolled, lvLoadingPublished;
	private Button btnDatePublished, btnDateEnrolled, btnDatePublish;
	private ImageView ivDatePublished, ivDateEnrolled;
	// private Drawable[] drawableArrow; // drawableArrow[0]
	// down_arrow,drawableArrow[1] up_arrow,
	private List<DateModel> listDatePublished, listDateEnrolled;
	private List<DateModel> listDatePublishedMore, listDateEnrolledMore;
	private DateManageAdapter adapterPublished;
	private DateEnrolledAdapter adapterEnrolled;
	private EventListener mListener;
	private Button btnLoadMoreDatePublished, btnLoadMoreDateEnrolled;
	/** item显示条数 */
	private final int SHOW_ITEM_TOTAL = 4;

	private boolean isMorePublished, isMoreEnrolled;

	private int pageNo = 1;
	private int pageSize = 10;// 默认10条为1页
	private boolean isRefresh = false;

	private int userId = -1;

	public DateManageActivity() {
		super(R.layout.activity_date_manage);
	}

	@Override
	public void initViews() {
		initTopTitle();
		lvDatePublished = (ListView) findViewById(R.id.lv_datePublished);
		lvDateEnrolled = (ListView) findViewById(R.id.lv_dateEnrolled);
		llDatePublished = (LinearLayout) findViewById(R.id.llDatePublished);
		llDateEnrolled = (LinearLayout) findViewById(R.id.llDateEnrolled);
		lvLoadingPublished = findViewById(R.id.lvLoadingPublished);
		lvLoadingEnrolled = findViewById(R.id.lvLoadingEnrolled);
		llLoadMoreDatePublished = (LinearLayout) findViewById(R.id.ll_loadMoreDatePublished);
		llLoadMoreDateEnrolled = (LinearLayout) findViewById(R.id.ll_loadMoreDateEnrolled);
		btnDatePublished = (Button) findViewById(R.id.btn_datePublished);
		ivDatePublished = (ImageView) findViewById(R.id.ivDatePublished);
		btnDateEnrolled = (Button) findViewById(R.id.btn_dateEnrolled);
		ivDateEnrolled = (ImageView) findViewById(R.id.ivDateEnrolled);
		btnDatePublish = (Button) findViewById(R.id.btn_datePublish);
		btnLoadMoreDatePublished = (Button) findViewById(R.id.btn_loadMoreDatePublished);
		btnLoadMoreDateEnrolled = (Button) findViewById(R.id.btn_loadMoreDateEnrolled);

		EmptyView emptyViewEnrolled = new EmptyView(DateManageActivity.this);
		emptyViewEnrolled.setVisibility(View.GONE);
		((ViewGroup) lvDateEnrolled.getParent()).addView(emptyViewEnrolled);
		lvDateEnrolled.setEmptyView(emptyViewEnrolled);
		lvDateEnrolled.setVisibility(View.GONE);
		EmptyView emptyViewPublished = new EmptyView(DateManageActivity.this);
		emptyViewPublished.setVisibility(View.GONE);
		((ViewGroup) lvDatePublished.getParent()).addView(emptyViewPublished);
		lvDatePublished.setEmptyView(emptyViewPublished);
		lvDatePublished.setVisibility(View.GONE);
	}

	private void initTopTitle() {
		TopTitleView topTitle = (TopTitleView) findViewById(R.id.top_title);
		topTitle.showLeftImag(R.drawable.back, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		topTitle.showCenterText(getString(R.string.date), null);
	}

	@Override
	public void initData() {

		mListener = new EventListener();

		lvDatePublished
				.setAdapter(adapterPublished = adapterPublished == null ? new DateManageAdapter(
						listDatePublished = listDatePublished == null ? new LinkedList<DateModel>()
								: listDatePublished, DateType.DATEPUBLISHED)
						: adapterPublished);
		lvDateEnrolled
				.setAdapter(adapterEnrolled = adapterEnrolled == null ? new DateEnrolledAdapter(
						listDateEnrolled = listDateEnrolled == null ? new LinkedList<DateModel>()
								: listDateEnrolled)
						: adapterEnrolled);

		listDateEnrolledMore = new LinkedList<DateModel>();
		listDatePublishedMore = new LinkedList<DateModel>();

		Integer userIdTmp = Integer.valueOf(AppContext.getApplication()
				.getUserInfo().getUserId());
		if (userIdTmp == null) {
			showToast("请登录后重试!");
			return;
		}
		this.userId = userIdTmp;

		loadDateEnrolled();

		// drawableArrow = new Drawable[2];
		// drawableArrow[0] =
		// getResources().getDrawable(R.drawable.down_arrow_icon);
		// drawableArrow[1] =
		// getResources().getDrawable(R.drawable.up_arrow_icon);
		// drawableArrow[0].setBounds(0, 0, drawableArrow[0].getMinimumWidth(),
		// drawableArrow[0].getMinimumHeight());
		// drawableArrow[1].setBounds(0, 0, drawableArrow[1].getMinimumWidth(),
		// drawableArrow[1].getMinimumHeight());

		// --------------------------examples data--------------------------
		// Random rd = new Random();
		// int rechargRecordsToatal = rd.nextInt(10);
		//
		// for (int i = 0; i < rechargRecordsToatal; i++) {
		// listDatePublished.add(new NearbyDateModel("一起约吧", "保罗酒吧",
		// getString(R.string.time_normal), 99 - i, i, "不限", "drawable://" +
		// R.drawable.pub_icon, null, null));
		// listDateEnrolled.add(new NearbyDateModel("一起约吧", "保罗酒吧",
		// getString(R.string.time_normal), 99 - i, i, "不限", "drawable://" +
		// R.drawable.pub_icon, null, null));
		// }
		//
		// if (listDatePublished.size() > SHOWITEMTOTAL) {
		// List<NearbyDateModel> listTempDatePublished = new
		// ArrayList<NearbyDateModel>(SHOWITEMTOTAL);
		// for (int i = 0; i < SHOWITEMTOTAL; i++) {
		// listTempDatePublished.add(listDatePublished.get(i));
		// }
		// lvDatePublished.setAdapter(new
		// DateManageAdapter(listTempDatePublished, DateType.DATEPUBLISHED));
		// } else {
		// llLoadMoreDatePublished.setVisibility(View.GONE);
		// lvDatePublished.setAdapter(new DateManageAdapter(listDatePublished,
		// DateType.DATEPUBLISHED));
		// }
		//
		// if (listDateEnrolled.size() > SHOWITEMTOTAL) {
		// List<NearbyDateModel> listTempDateEnrolled = new
		// ArrayList<NearbyDateModel>(SHOWITEMTOTAL);
		// for (int i = 0; i < SHOWITEMTOTAL; i++) {
		// listTempDateEnrolled.add(listDateEnrolled.get(i));
		// }
		// lvDateEnrolled.setAdapter(new DateManageAdapter(listTempDateEnrolled,
		// DateType.DATEENROLLED));
		// } else {
		// llLoadMoreDateEnrolled.setVisibility(View.GONE);
		// lvDateEnrolled.setAdapter(new DateManageAdapter(listDateEnrolled,
		// DateType.DATEENROLLED));
		// }
		// --------------------------examples data--------------------------

	}

	@Override
	public void bindViews() {
		btnDatePublished.setOnClickListener(mListener);
		btnDateEnrolled.setOnClickListener(mListener);
		btnDatePublish.setOnClickListener(mListener);
		btnLoadMoreDatePublished.setOnClickListener(mListener);
		btnLoadMoreDateEnrolled.setOnClickListener(mListener);
		lvDateEnrolled.setOnItemClickListener(mListener);
		lvDatePublished.setOnItemClickListener(mListener);
		// llLoadMoreDatePublished.setOnClickListener(mListener);
		// llLoadMoreDateEnrolled.setOnClickListener(mListener);
	}

	private class EventListener implements OnClickListener, OnItemClickListener {
		@SuppressLint("NewApi")
		// api 11
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_datePublished:
				if (llDatePublished.getVisibility() == View.GONE) {
					// btnDatePublished.setCompoundDrawables(null, null,
					// drawableArrow[1], null);
					ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(
							ivDatePublished, "rotation", 0F, 180F);
					objectAnimator
							.setInterpolator(new DecelerateInterpolator(1)); // Decelerate的增值器
					objectAnimator.setDuration(200).start();
					llDatePublished.setVisibility(View.VISIBLE);
					int[] location = new int[2];
					llDatePublished.getLocationOnScreen(location);
					int offsetY = DisplayUtils
							.getScreenWidht(DateManageActivity.this)
							- location[1] + llDatePublished.getHeight();
					ObjectAnimator objectAnimator1 = ObjectAnimator.ofInt(
							llDatePublished, "", 0, 200);
					objectAnimator1.setDuration(500).start();
					llLoadMoreDatePublished
							.setVisibility(isMorePublished ? View.VISIBLE
									: View.GONE);
					btnDatePublished
							.setBackgroundResource(R.drawable.content_top_bg);
				} else {
					// btnDatePublished.setCompoundDrawables(null, null,
					// drawableArrow[0], null);
					ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(
							ivDatePublished, "rotation", 180F, 360F);
					objectAnimator
							.setInterpolator(new DecelerateInterpolator(2));
					objectAnimator.setDuration(200).start();
					llDatePublished.setVisibility(View.GONE);
					if (llLoadMoreDatePublished.getVisibility() != View.GONE)
						llLoadMoreDatePublished.setVisibility(View.GONE);
					btnDatePublished
							.setBackgroundResource(R.drawable.date_item_content);
				}
				break;

			case R.id.btn_dateEnrolled:
				if (llDateEnrolled.getVisibility() == View.GONE) {
					// btnDateEnrolled.setCompoundDrawables(null, null,
					// drawableArrow[1], null);
					ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(
							ivDateEnrolled, "rotation", 0F, 180F);
					objectAnimator
							.setInterpolator(new DecelerateInterpolator(1)); // Decelerate的增值器
					objectAnimator.setDuration(200).start();
					llDateEnrolled.setVisibility(View.VISIBLE);
					llLoadMoreDateEnrolled
							.setVisibility(isMoreEnrolled ? View.VISIBLE
									: View.GONE);
					btnDateEnrolled
							.setBackgroundResource(R.drawable.content_top_bg);
				} else {
					// btnDateEnrolled.setCompoundDrawables(null, null,
					// drawableArrow[0], null);
					ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(
							ivDateEnrolled, "rotation", 180F, 360F);
					objectAnimator
							.setInterpolator(new DecelerateInterpolator(2));
					objectAnimator.setDuration(200).start();
					llDateEnrolled.setVisibility(View.GONE);
					if (llLoadMoreDateEnrolled.getVisibility() != View.GONE)
						llLoadMoreDateEnrolled.setVisibility(View.GONE);
					btnDateEnrolled
							.setBackgroundResource(R.drawable.date_item_content);
				}
				break;

			case R.id.btn_datePublish:
				UmengUtil.onEvent(DateManageActivity.this, "post_date_hits");
				LogUtil.printUmengLog("post_date_hits");
				startActivity(new Intent(DateManageActivity.this,
						DatePublishActivity.class));
				break;

			case R.id.btn_loadMoreDatePublished:
				moreDatePublished();
				break;
			case R.id.btn_loadMoreDateEnrolled:
				moreDateEnrolled();
				break;

			}
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (parent == lvDateEnrolled) {
				Intent intent = new Intent(DateManageActivity.this,
						NearbyDateDetailsAcitvity.class);
				DateModel dateModel = listDateEnrolled.get(position);
				dateModel.setSuccessful(true);
				Bundle bundle = new Bundle();
				bundle.putSerializable(GlobleConstant.DATE_MODEL, dateModel);
				intent.putExtras(bundle);
				startActivity(intent);
				return;
			}
			if (parent == lvDatePublished) {
				Intent intent = new Intent(DateManageActivity.this,
						NearbyDateDetailsAcitvity.class);
				DateModel dateModel = listDatePublished.get(position);
				dateModel.setSuccessful(true);
				Bundle bundle = new Bundle();
				bundle.putSerializable(GlobleConstant.DATE_MODEL, dateModel);
				intent.putExtras(bundle);
				startActivity(intent);
				return;
			}
		}
	}

	/**
	 * 查看更多加入的约会
	 */
	private void moreDateEnrolled() {
		getSupportFragmentManager()
				.beginTransaction()
				.setCustomAnimations(R.anim.slide_in_from_left,
						R.anim.slide_out_to_right)
				.replace(
						android.R.id.content,
						new SeeMoreFragment<NearbyDateModel>(
								getString(R.string.xlistview_footer_hint_normal),
								new DateManageAdapter(listDateEnrolledMore,
										DateType.DATEENROLLED),
								new OnItemClickListener() {
									@Override
									public void onItemClick(
											AdapterView<?> parent, View view,
											int position, long id) {
										if (position == 0
												|| position == listDateEnrolledMore
														.size() + 1)
											return;
										Intent intent = new Intent(
												DateManageActivity.this,
												NearbyDateDetailsAcitvity.class);
										DateModel dateModel = listDateEnrolledMore
												.get(position - 1);
										dateModel.setSuccessful(true);
										Bundle bundle = new Bundle();
										bundle.putSerializable(
												GlobleConstant.DATE_MODEL,
												dateModel);
										intent.putExtras(bundle);
										startActivity(intent);
									}
								}, new IIXListViewEventListener() {
									@Override
									public void onFirstLoad(View loadView,
											XListView view) {
										view.setPullLoadEnable(true);
										pageNo = pageNo == 1 ? pageNo++ : 2;
									}

									@Override
									public void onRefresh(XListView view,
											BaseAdapter adapter) {
										isRefresh = true;
										pageNo = 1;
										loadMoreDateEnrolled(view, adapter);
									}

									@Override
									public void onLoadMore(XListView view,
											BaseAdapter adapter) {
										isRefresh = false;
										pageNo++;
										loadMoreDateEnrolled(view, adapter);
									}

								})).addToBackStack(null).commit();
	}

	/**
	 * 查看更多发布的约会
	 */
	private void moreDatePublished() {
		getSupportFragmentManager()
				.beginTransaction()
				.setCustomAnimations(R.anim.slide_in_from_left,
						R.anim.slide_out_to_right)
				.replace(
						android.R.id.content,
						new SeeMoreFragment<NearbyDateModel>(
								getString(R.string.xlistview_footer_hint_normal),
								new DateManageAdapter(listDatePublishedMore,
										DateType.DATEPUBLISHED),
								new OnItemClickListener() {
									@Override
									public void onItemClick(
											AdapterView<?> parent, View view,
											int position, long id) {
										if (position == 0
												|| position == listDatePublishedMore
														.size() + 1)
											return;
										Intent intent = new Intent(
												DateManageActivity.this,
												NearbyDateDetailsAcitvity.class);
										DateModel dateModel = listDatePublishedMore
												.get(position - 1);
										dateModel.setSuccessful(true);
										Bundle bundle = new Bundle();
										bundle.putSerializable(
												GlobleConstant.DATE_MODEL,
												dateModel);
										intent.putExtras(bundle);
										startActivity(intent);
									}
								}, new IIXListViewEventListener() {

									@Override
									public void onFirstLoad(View loadView,
											XListView view) {
										view.setPullLoadEnable(true);
										pageNo = pageNo == 1 ? pageNo++ : 2;
									}

									@Override
									public void onRefresh(XListView view,
											BaseAdapter adapter) {
										isRefresh = true;
										pageNo = 1;
										loadMoreDatePublished(view, adapter);
									}

									@Override
									public void onLoadMore(XListView view,
											BaseAdapter adapter) {
										isRefresh = false;
										pageNo++;
										loadMoreDatePublished(view, adapter);
									}

								})).addToBackStack(null).commit();
	}

	/**
	 * 从服务器获取已加入的约会 ，不需要即时刷新状态，创建是加载一次即可
	 */
	private void loadDateEnrolled() {
		if (userId == -1)
			return;
		HttpRequest.getInstance().getDateEnrolled(userId, 1, pageSize,
				new CustomAsyncResponehandler() {
					@Override
					public void onSuccess(ResponeModel baseModel) {
						if (baseModel != null && baseModel.isStatus()) {
							listDateEnrolledMore = (List<DateModel>) baseModel
									.getResultObject();
							if (listDateEnrolledMore == null
									|| (listDateEnrolledMore.size() > 0 && listDateEnrolledMore
											.get(0) == null))
								return;
							if (listDateEnrolledMore.size() > SHOW_ITEM_TOTAL) {

								for (int i = 0; i < SHOW_ITEM_TOTAL; i++) {
									listDateEnrolled.add(listDateEnrolledMore
											.get(i));
									LogUtil.printPushLog("bar id:"
											+ listDateEnrolledMore.get(i)
													.getBarId()
											+ " bar name:"
											+ listDateEnrolledMore.get(i)
													.getBarName());
								}
								isMoreEnrolled = true;
							} else
								listDateEnrolled.addAll(listDateEnrolledMore);
							adapterEnrolled.notifyDataSetChanged();
						}
					}

					@Override
					public void onFinish() {
						lvDateEnrolled.setVisibility(View.VISIBLE);
						lvLoadingEnrolled.setVisibility(View.GONE);
					}

					@Override
					public void onFailure(Throwable error, String content) {
					}
				});
	}

	/**
	 * 从服务器获取已加入的约会 ，每次管理可能出现加入人数、参解人数等变化，所以放到onResume了
	 */
	private void loadDatePublished() {
		if (userId == -1)
			return;
		HttpRequest.getInstance().getDatePublished(userId, 1, pageSize,
				new CustomAsyncResponehandler() {
					@Override
					public void onSuccess(ResponeModel baseModel) {
						if (baseModel != null && baseModel.isStatus()) {
							listDatePublishedMore = (List<DateModel>) baseModel
									.getResultObject();
							if (listDatePublishedMore == null
									|| (listDatePublishedMore.size() > 0 && listDatePublishedMore
											.get(0) == null))
								return;
							listDatePublished.clear();
							if (listDatePublishedMore.size() > SHOW_ITEM_TOTAL) {
								for (int i = 0; i < SHOW_ITEM_TOTAL; i++) {
									listDatePublished.add(listDatePublishedMore
											.get(i));
								}
								isMorePublished = true;
							} else
								listDatePublished.addAll(listDatePublishedMore);
							adapterPublished.notifyDataSetChanged();
						}
					}

					@Override
					public void onFinish() {
						lvDatePublished.setVisibility(View.VISIBLE);
						lvLoadingPublished.setVisibility(View.GONE);
					}

					@Override
					public void onFailure(Throwable error, String content) {
					}
				});
	}

	@Override
	protected void onResume() {
		super.onResume();
		loadDatePublished();
	}

	/**
	 * 从服务器获取已加入的约会 ，不需要即时刷新状态，创建是加载一次即可
	 * 
	 * @param view
	 */
	private void loadMoreDateEnrolled(final XListView view,
			final BaseAdapter adapter) {
		if (userId == -1)
			return;
		HttpRequest.getInstance().getDateEnrolled(userId, pageNo, pageSize,
				new CustomAsyncResponehandler() {
					@Override
					public void onSuccess(ResponeModel baseModel) {
						if (baseModel != null && baseModel.isStatus()) {
							List<DateModel> listEnrolledTmp = (List<DateModel>) baseModel
									.getResultObject();
							view.setPullLoadEnable(listEnrolledTmp == null ? false
									: listEnrolledTmp.size() >= pageSize);
							if (listEnrolledTmp == null
									|| (listEnrolledTmp.size() > 0 && listEnrolledTmp
											.get(0) == null))
								return;
							if (isRefresh)
								listDateEnrolledMore.clear();
							listDateEnrolledMore.addAll(listEnrolledTmp);
							adapter.notifyDataSetChanged();
						}
					}

					@Override
					public void onFinish() {
						if (isRefresh)
							view.stopRefresh();
					}

					@Override
					public void onFailure(Throwable error, String content) {
					}
				});
	}

	/**
	 * 从服务器获取已加入的约会 ，每次管理可能出现加入人数、参解人数等变化，所以放到onResume了
	 * 
	 * @param view
	 */
	private void loadMoreDatePublished(final XListView view,
			final BaseAdapter adapter) {
		if (userId == -1)
			return;
		HttpRequest.getInstance().getDatePublished(userId, pageNo, pageSize,
				new CustomAsyncResponehandler() {
					@Override
					public void onSuccess(ResponeModel baseModel) {
						if (baseModel != null && baseModel.isStatus()) {
							List<DateModel> listEnrolledTmp = (List<DateModel>) baseModel
									.getResultObject();
							view.setPullLoadEnable(listEnrolledTmp == null ? false
									: listEnrolledTmp.size() >= pageSize);
							if (listEnrolledTmp == null
									|| (listEnrolledTmp.size() > 0 && listEnrolledTmp
											.get(0) == null))
								return;
							if (isRefresh)
								listDatePublishedMore.clear();
							listDatePublishedMore.addAll(listEnrolledTmp);
							adapter.notifyDataSetChanged();
						}
					}

					@Override
					public void onFinish() {
						if (isRefresh)
							view.stopRefresh();
					}

					@Override
					public void onFailure(Throwable error, String content) {
					}
				});
	}

}
