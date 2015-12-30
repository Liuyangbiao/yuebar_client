package com.fullteem.yueba.app.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.robobinding.ViewBinder;
import org.robobinding.binder.BinderFactory;
import org.robobinding.binder.BinderFactoryBuilder;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.fullteem.yueba.R;
import com.fullteem.yueba.app.adapter.ConsumeRecordsAdapter;
import com.fullteem.yueba.model.ConsumeRecordsModel;
import com.fullteem.yueba.model.presentmodel.ConsumeRecordsPresentModel;
import com.fullteem.yueba.widget.TopTitleView;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月10日&emsp;上午10:21:46
 * @use 消费记录界面
 */
public class ConsumeRecordsActivity extends BaseActivity {

	private ConsumeRecordsPresentModel presentMode;
	private ListView lvConsumeRecords;
	private List<ConsumeRecordsModel> listConsumeRecords;

	public ConsumeRecordsActivity() {
		super(-1);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		BinderFactory binderFactory = new BinderFactoryBuilder().build();
		ViewBinder vb = binderFactory.createViewBinder(this, true);
		presentMode = new ConsumeRecordsPresentModel();
		View rootView = vb.inflateAndBind(R.layout.activity_consume_records,
				presentMode);
		setContentView(rootView);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void initViews() {
		initTopTitle();
		lvConsumeRecords = (ListView) findViewById(R.id.lv_consumeRecords);
	}

	@Override
	public void initData() {

		listConsumeRecords = new ArrayList<ConsumeRecordsModel>();
		// --------------------------examples data--------------------------
		int total = new Random().nextInt(7);
		for (int i = 0; i < total; i++) {
			listConsumeRecords.add(new ConsumeRecordsModel("9月8日", "900" + i));
		}

		lvConsumeRecords.setAdapter(new ConsumeRecordsAdapter(
				listConsumeRecords));
		// --------------------------examples data--------------------------
	}

	@Override
	public void bindViews() {
	}

	private void initTopTitle() {
		TopTitleView topTitle = (TopTitleView) findViewById(R.id.top_title);
		topTitle.showLeftImag(R.drawable.back, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		topTitle.showCenterText(getString(R.string.consumption_records), null);
	}

}
