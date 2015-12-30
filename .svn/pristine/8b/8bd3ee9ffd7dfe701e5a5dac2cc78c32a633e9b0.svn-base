package com.fullteem.yueba.app.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.fullteem.yueba.R;
import com.fullteem.yueba.app.adapter.RechargRecordsAdapter;
import com.fullteem.yueba.app.adapter.RechargRecordsAdapter.RechargRecordsType;
import com.fullteem.yueba.app.ui.fragment.SeeMoreFragment;
import com.fullteem.yueba.app.ui.fragment.WantRechargFragment;
import com.fullteem.yueba.model.RechargRecordsModel;
import com.fullteem.yueba.widget.TopTitleView;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月8日&emsp;下午5:26:25
 * @use 充值记录页面
 */
public class RechargRecordsActivity extends BaseActivity {
	private ListView lvRechargRecords, lvRechargPreferential;
	private LinearLayout llLoadMoreRechargRecords,
			llLoadMoreRechargPreferential;
	private Button btnWantRecharg, btnRechargRecords, btnRechargPreferential;
	private Drawable[] drawableArrow; // drawableArrow[0]
										// down_arrow,drawableArrow[1] up_arrow,
	private List<RechargRecordsModel> listRechargRecords,
			listRechargPreferential;
	private EventListener mListener;
	private Button btnLoadMoreRechargRecords, btnLoadMoreRechargPreferential;

	public RechargRecordsActivity() {
		super(R.layout.activity_recharg_records);
	}

	@Override
	public void initViews() {
		initTopTitle();
		lvRechargRecords = (ListView) findViewById(R.id.lv_rechargRecords);
		lvRechargPreferential = (ListView) findViewById(R.id.lv_rechargPreferential);
		llLoadMoreRechargRecords = (LinearLayout) findViewById(R.id.ll_loadMoreRechargRecords);
		llLoadMoreRechargPreferential = (LinearLayout) findViewById(R.id.ll_loadMoreRechargPreferential);
		btnWantRecharg = (Button) findViewById(R.id.btn_wantRecharg);
		btnRechargRecords = (Button) findViewById(R.id.btn_rechargRecords);
		btnRechargPreferential = (Button) findViewById(R.id.btn_rechargPreferential);
		btnLoadMoreRechargRecords = (Button) findViewById(R.id.btn_loadMoreRechargRecords);
		btnLoadMoreRechargPreferential = (Button) findViewById(R.id.btn_loadMoreRechargPreferential);
	}

	@Override
	public void initData() {
		drawableArrow = new Drawable[2];
		drawableArrow[0] = getResources().getDrawable(
				R.drawable.down_arrow_icon);
		drawableArrow[1] = getResources().getDrawable(R.drawable.up_arrow_icon);
		drawableArrow[0].setBounds(0, 0, drawableArrow[0].getMinimumWidth(),
				drawableArrow[0].getMinimumHeight());
		drawableArrow[1].setBounds(0, 0, drawableArrow[1].getMinimumWidth(),
				drawableArrow[1].getMinimumHeight());

		mListener = new EventListener();

		listRechargRecords = new ArrayList<RechargRecordsModel>();
		listRechargPreferential = new ArrayList<RechargRecordsModel>();

		// --------------------------examples data--------------------------
		Random rd = new Random();
		int rechargRecordsToatal = rd.nextInt(10);
		int rechargPreferentialToatal = rd.nextInt(5);

		for (int i = 0; i < rechargRecordsToatal; i++) {
			listRechargRecords.add(new RechargRecordsModel(
					getString(R.string.records_content_normal),
					getString(R.string.date_normal)));
		}

		for (int i = 0; i < rechargPreferentialToatal; i++) {
			listRechargPreferential.add(new RechargRecordsModel(
					getString(R.string.records_content_normal),
					getString(R.string.date_normal)));
		}

		if (listRechargRecords.size() > 2) {
			List<RechargRecordsModel> listTempRechargRecords = new ArrayList<RechargRecordsModel>(
					2);
			for (int i = 0; i < 2; i++) {
				listTempRechargRecords.add(listRechargRecords.get(i));
			}
			lvRechargRecords.setAdapter(new RechargRecordsAdapter(
					listTempRechargRecords, RechargRecordsType.NONSHOWDATE));
		} else {
			llLoadMoreRechargRecords.setVisibility(View.GONE);
			lvRechargRecords.setAdapter(new RechargRecordsAdapter(
					listRechargRecords, RechargRecordsType.NONSHOWDATE));
		}

		if (listRechargPreferential.size() > 2) {
			List<RechargRecordsModel> listTempRechargPreferential = new ArrayList<RechargRecordsModel>(
					2);
			for (int i = 0; i < 2; i++) {
				listTempRechargPreferential.add(listRechargPreferential.get(i));
			}
			lvRechargPreferential.setAdapter(new RechargRecordsAdapter(
					listTempRechargPreferential, RechargRecordsType.SHOWDATE));
		} else {
			llLoadMoreRechargPreferential.setVisibility(View.GONE);
			lvRechargPreferential.setAdapter(new RechargRecordsAdapter(
					listRechargPreferential, RechargRecordsType.SHOWDATE));
		}
		// --------------------------examples data--------------------------

	}

	@Override
	public void bindViews() {
		btnWantRecharg.setOnClickListener(mListener);
		btnRechargRecords.setOnClickListener(mListener);
		btnRechargPreferential.setOnClickListener(mListener);
		btnLoadMoreRechargRecords.setOnClickListener(mListener);
		btnLoadMoreRechargPreferential.setOnClickListener(mListener);
	}

	private void initTopTitle() {
		TopTitleView topTitle = (TopTitleView) findViewById(R.id.top_title);
		topTitle.showLeftImag(R.drawable.back, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		topTitle.showCenterText(getString(R.string.recharg_records), null);
	}

	private class EventListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_wantRecharg:
				getSupportFragmentManager()
						.beginTransaction()
						.replace(android.R.id.content,
								new WantRechargFragment()).addToBackStack(null)
						.commit();
				break;

			case R.id.btn_rechargRecords:
				if (lvRechargRecords.getVisibility() == View.GONE) {
					btnRechargRecords.setCompoundDrawables(null, null,
							drawableArrow[1], null);
					lvRechargRecords.setVisibility(View.VISIBLE);
					if (listRechargRecords.size() > 2)
						llLoadMoreRechargRecords.setVisibility(View.VISIBLE);
					else if (llLoadMoreRechargRecords.getVisibility() != View.GONE)
						llLoadMoreRechargRecords.setVisibility(View.GONE);
				} else {
					btnRechargRecords.setCompoundDrawables(null, null,
							drawableArrow[0], null);
					lvRechargRecords.setVisibility(View.GONE);
					if (llLoadMoreRechargRecords.getVisibility() != View.GONE)
						llLoadMoreRechargRecords.setVisibility(View.GONE);
				}
				break;

			case R.id.btn_rechargPreferential:
				if (lvRechargPreferential.getVisibility() == View.GONE) {
					btnRechargPreferential.setCompoundDrawables(null, null,
							drawableArrow[1], null);
					lvRechargPreferential.setVisibility(View.VISIBLE);
					if (listRechargPreferential.size() > 2)
						llLoadMoreRechargPreferential
								.setVisibility(View.VISIBLE);
					else if (llLoadMoreRechargPreferential.getVisibility() != View.GONE)
						llLoadMoreRechargPreferential.setVisibility(View.GONE);
				} else {
					btnRechargPreferential.setCompoundDrawables(null, null,
							drawableArrow[0], null);
					lvRechargPreferential.setVisibility(View.GONE);
					if (llLoadMoreRechargPreferential.getVisibility() != View.GONE)
						llLoadMoreRechargPreferential.setVisibility(View.GONE);
				}
				break;

			case R.id.btn_loadMoreRechargRecords:
				getSupportFragmentManager()
						.beginTransaction()
						.setCustomAnimations(R.anim.slide_in_from_left,
								R.anim.slide_out_to_right)
						.replace(
								android.R.id.content,
								new SeeMoreFragment<RechargRecordsModel>(
										getString(R.string.recharg_records),
										new RechargRecordsAdapter(
												listRechargRecords,
												RechargRecordsType.SHOWDATE),
										null, null)).addToBackStack(null)
						.commit();
				break;

			case R.id.btn_loadMoreRechargPreferential:
				getSupportFragmentManager()
						.beginTransaction()
						.setCustomAnimations(R.anim.slide_in_from_left,
								R.anim.slide_out_to_right)
						.replace(
								android.R.id.content,
								new SeeMoreFragment<RechargRecordsModel>(
										getString(R.string.recharg_preferential),
										new RechargRecordsAdapter(
												listRechargRecords,
												RechargRecordsType.SHOWDATE),
										null, null)).addToBackStack(null)
						.commit();
				break;
			}
		}
	}

}
