package com.fullteem.yueba.app.ui;

import java.util.LinkedList;
import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fullteem.yueba.R;
import com.fullteem.yueba.app.adapter.GiftAdapter;
import com.fullteem.yueba.app.adapter.GiftAdapter.IBuyGiftPopupWindDismissListener;
import com.fullteem.yueba.globle.GlobleConstant;
import com.fullteem.yueba.model.GiftModel;
import com.fullteem.yueba.model.ResponeModel;
import com.fullteem.yueba.net.http.CustomAsyncResponehandler;
import com.fullteem.yueba.net.http.HttpRequest;
import com.fullteem.yueba.util.LogUtil;
import com.fullteem.yueba.util.SoundPoolUtil;
import com.fullteem.yueba.widget.EmptyView;
import com.fullteem.yueba.widget.TopTitleView;
import com.fullteem.yueba.widget.XListView;
import com.fullteem.yueba.widget.XListView.IXListViewListener;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月8日&emsp;下午2:32:38
 * @use 礼物仓界面
 */
public class GiftActivity extends BaseActivity implements
		IBuyGiftPopupWindDismissListener {
	private TopTitleView topTitle;
	private TextView tvGold;
	private XListView lvGift;
	private List<GiftModel> listGift;
	private View lvLoading;
	private GiftAdapter adapterGift;

	private int pageNo = 1;
	private int pageSize = 10;// 默认10条为1页

	private boolean isLoadMore = false;
	private boolean isRefresh = false;

	public GiftActivity() {
		super(R.layout.activity_gift);
	}

	@Override
	public void initViews() {
		initTopTitle();
		tvGold = (TextView) findViewById(R.id.tv_gold);
		lvGift = (XListView) findViewById(R.id.lv_gift);

		LinearLayout emptyView = new EmptyView(GiftActivity.this);
		emptyView.setVisibility(View.GONE);
		((ViewGroup) lvGift.getParent()).addView(emptyView);
		lvGift.setEmptyView(emptyView);
		lvGift.setVisibility(View.GONE);

		lvLoading = findViewById(R.id.lvLoading);
		lvGift.setPullLoadEnable(false);

	}

	@Override
	public void initData() {
		int singerId = getIntent().getIntExtra(GlobleConstant.GIFT_GIVE, -1); // 送礼页面跳过来？
		lvGift.setAdapter(adapterGift = adapterGift == null ? new GiftAdapter(
				GiftActivity.this,
				listGift = listGift == null ? new LinkedList<GiftModel>()
						: listGift, singerId, this) : adapterGift);

		loadData();
		loadDataGold();

		// lvGift.setEmptyView(emptyView);
		// --------------------------examples data--------------------------
		//
		// tvGold.setText(getResString(R.string.gold_remnants_normal) +
		// getResString(R.string.gold));
		// int total = new Random().nextInt(10);
		// for (int i = 0; i < total; i++) {
		// listGift.add(new GiftModel("drawable://" + R.drawable.img_gift_bmw,
		// getResString(R.string.gift_name_normal), i, i, 50 + i, 100));
		// }
		// lvGift.setAdapter(new GiftAdapter(listGift));
		// --------------------------examples data--------------------------
	}

	@Override
	public void bindViews() {
		lvGift.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				isRefresh = true;
				isLoadMore = false;
				pageNo = 1;
				loadData();
			}

			@Override
			public void onLoadMore() {
				isRefresh = false;
				isLoadMore = true;
				pageNo++;
				loadData();
			}
		});

	}

	private void initTopTitle() {
		topTitle = (TopTitleView) findViewById(R.id.top_title);
		topTitle.showLeftImag(R.drawable.back, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		topTitle.showCenterText(getString(R.string.perssonal_gift), null);
	}

	/**
	 * 从服务器获取数据
	 */
	private void loadData() {
		HttpRequest.getInstance(GiftActivity.this).getGiftList(pageNo,
				pageSize, new CustomAsyncResponehandler() {

					@Override
					public void onSuccess(ResponeModel baseModel) {
						LogUtil.LogDebug(getClass().getName(),
								baseModel.toString(), null);
						if (baseModel != null && baseModel.isStatus()) {
							List<GiftModel> listGiftModels = (List<GiftModel>) baseModel
									.getResultObject();
							lvGift.setPullLoadEnable(listGiftModels == null ? false
									: listGiftModels.size() >= pageSize);
							if (listGiftModels != null
									&& listGiftModels.size() > 0
									&& listGiftModels.get(0) != null) {
								if (isRefresh) {
									if (listGift.equals(listGiftModels))
										return;
									listGift.clear();
									SoundPoolUtil.playSound(GiftActivity.this,
											R.raw.dengdengdeng);
								}
								listGift.addAll(listGiftModels);
								adapterGift.notifyDataSetChanged();
							}
						}
					}

					@Override
					public void onStart() {
					}

					@Override
					public void onFinish() {
						lvGift.setVisibility(View.VISIBLE);
						lvLoading.setVisibility(View.GONE);
						if (isRefresh)
							lvGift.stopRefresh();
					}

					@Override
					public void onFailure(Throwable error, String content) {
					}
				});
	}

	/**
	 * 从服务器获取金币数据
	 */
	private void loadDataGold() {
		adapterGift.setSurplusGold(300);
		tvGold.setText("" + 300 + "金币");
	}

	/*
	 * popupwindow消失回调
	 */
	@Override
	public void onDismiss() {
		loadDataGold();
		isRefresh = true;
		loadData();
	}
}
