package com.fullteem.yueba.app.ui;

import java.util.HashMap;
import java.util.Map.Entry;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fullteem.yueba.R;
import com.fullteem.yueba.app.AppContext;
import com.fullteem.yueba.globle.GlobleConstant;
import com.fullteem.yueba.model.CommonModel;
import com.fullteem.yueba.net.Urls;
import com.fullteem.yueba.net.http.AsyncHttpResponseHandler;
import com.fullteem.yueba.net.http.HttpRequestFactory;
import com.fullteem.yueba.util.LogUtil;
import com.fullteem.yueba.widget.TopTitleView;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月16日&emsp;上午9:28:04
 * @use 举报界面
 */
public class WhistleBlowActivity extends BaseActivity {
	private CheckBox ckb_blow_porn, ckb_blow_politics, ckb_blow_violence,
			ckb_blow_cheat, ckb_blow_harassment, ckb_blow_abuse;
	private HashMap<CheckBox, Integer> map;
	private EditText et_blow_detail;
	private Button btn_submit;
	private EventListener mListener;

	private int type = 0;
	private int targetId;

	private static String tag = "WhistleBlow";

	public WhistleBlowActivity() {
		super(R.layout.activity_whistle_blow);
		map = new HashMap<CheckBox, Integer>();
	}

	@Override
	public void initViews() {
		initTopTitle();
		et_blow_detail = (EditText) findViewById(R.id.et_blow_detail);

		ckb_blow_porn = (CheckBox) findViewById(R.id.ckb_blow_porn);
		ckb_blow_politics = (CheckBox) findViewById(R.id.ckb_blow_politics);
		ckb_blow_violence = (CheckBox) findViewById(R.id.ckb_blow_violence);
		ckb_blow_cheat = (CheckBox) findViewById(R.id.ckb_blow_cheat);
		ckb_blow_harassment = (CheckBox) findViewById(R.id.ckb_blow_harassment);
		ckb_blow_abuse = (CheckBox) findViewById(R.id.ckb_blow_abuse);

		map.put(ckb_blow_porn, R.id.ckb_blow_porn);
		map.put(ckb_blow_politics, R.id.ckb_blow_politics);
		map.put(ckb_blow_violence, R.id.ckb_blow_violence);
		map.put(ckb_blow_cheat, R.id.ckb_blow_cheat);
		map.put(ckb_blow_harassment, R.id.ckb_blow_harassment);
		map.put(ckb_blow_abuse, R.id.ckb_blow_abuse);

		btn_submit = (Button) findViewById(R.id.btn_submit);

		targetId = getIntent().getIntExtra(GlobleConstant.DATE_FAVORITE_ID, -1);
		System.out.println("targetId: " + targetId);
	}

	private void initTopTitle() {
		TopTitleView topTitle = (TopTitleView) findViewById(R.id.top_title);
		topTitle.showLeftImag(R.drawable.back, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		topTitle.showCenterText(getString(R.string.whistle_blow), null);
	}

	@Override
	public void initData() {
		mListener = new EventListener();
	}

	@Override
	public void bindViews() {
		for (Entry<CheckBox, Integer> entry : map.entrySet()) {
			entry.getKey().setOnClickListener(mListener);
		}

		btn_submit.setOnClickListener(mListener);
	}

	private void setChecked(int id) {
		int count = 0;
		for (Entry<CheckBox, Integer> entry : map.entrySet()) {
			count++;
			if (entry.getValue() == id) {
				entry.getKey().setChecked(true);
				type = count;// start from 1.
			} else {
				entry.getKey().setChecked(false);
			}
		}
	}

	private class EventListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			LogUtil.printLog(tag, "onClick");
			if (v.getId() == R.id.btn_submit) {
				if (type == 0) {
					showToast(getString(R.string.whistle_blow_popup_type));
					return;
				} else {
					submit();
				}
			} else {
				setChecked(v.getId());
			}
		}

	}

	private void submit() {
		Integer userId = Integer.valueOf(AppContext.getApplication()
				.getUserInfo().getUserId());
		if (userId == null || userId < 0) {
			showToast(getString(R.string.hint_longinFirst));
			return;
		}

		JSONObject ob = new JSONObject();
		ob.put("rUId", targetId);
		ob.put("BrUId", userId);
		ob.put("rType", type);
		ob.put("rContent", et_blow_detail.getText());

		LogUtil.printLog(tag, "submit info. type:" + type + " content:"
				+ et_blow_detail.getText() + " target:" + targetId + " user:"
				+ userId);

		HttpRequestFactory.getInstance().postRequest(Urls.BLOW_WHISTLE, ob,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(String content) {
						super.onSuccess(content);

						LogUtil.printLog(tag,
								"blow whistle request is handled:" + content);

						CommonModel rm = new CommonModel();
						rm = JSON.parseObject(content, CommonModel.class);

						if (rm.getCode() != null
								&& "200".equalsIgnoreCase(rm.getCode())) {
							//
							LogUtil.printLog(tag, "blow whistle succeed");
						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
						LogUtil.printLog(tag, "blow whistle failed:" + content);
					};

					@Override
					public void onFinish() {
						LogUtil.printLog(tag, "blow whistle finished");
						finish();
					}

				});
	};

}
