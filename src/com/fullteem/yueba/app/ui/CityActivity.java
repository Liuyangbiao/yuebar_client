package com.fullteem.yueba.app.ui;

import java.util.ArrayList;
import java.util.List;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.fullteem.yueba.R;
import com.fullteem.yueba.app.AppContext;
import com.fullteem.yueba.app.AppManager;
import com.fullteem.yueba.app.adapter.HotCityAdapter;
import com.fullteem.yueba.app.adapter.SearchCityAdapter;
import com.fullteem.yueba.model.City;
import com.fullteem.yueba.service.LocationManager;
import com.fullteem.yueba.widget.TopTitleView;

/**
 * @className：CityActivity.java
 * @author: lmt
 * @Function: 城市列表
 * @createDate: 2014-12-2 下午2:55:16
 * @update:
 */
public class CityActivity extends BaseActivity implements TextWatcher,
		OnClickListener, AppContext.EventHandler {

	public static final int FROM_HOME_TAG = 1;// 来自首页
	public static final int FROM_APPSTART_TAG = 2;// 来自启动页

	public int comeFrom;// 来自
	// ------------------------------
	private GridView gridViewCitysList;
	private ListView mSearchListView;
	private HotCityAdapter hotCityAdapter;

	private View mSearchContainer;
	private View mCityContainer;
	private EditText mSearchEditText;
	private ImageButton mClearSearchBtn;
	private SearchCityAdapter mSearchCityAdapter;

	private InputMethodManager mInputMethodManager;

	private List<City> cities;
	private long mExitTime; // 退出时间
	private static final int INTERVAL = 2000; // 退出间隔
	private boolean isHotCityUpdate = false;// 热门城市页面是否跟新完成
	private Integer loc = 1;// 锁

	public CityActivity() {
		super(R.layout.activity_city);

		// when user has the intention to switch city, update the location.
		LocationManager.getInstance().getLocation();
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stubag
		initTopTitle();
		comeFrom = getIntent().getIntExtra("from", FROM_HOME_TAG);
		EditText editText = (EditText) findViewById(R.id.search_edit);
		editText.setBackgroundResource(R.drawable.edit_city_search_bg);
		editText.setCompoundDrawablesWithIntrinsicBounds(
				R.drawable.city_edit_search, 0, 0, 0);
		editText.setHint(R.string.city_search_hint);
		editText.setHintTextColor(getResColor(R.color.gray));
		editText.addTextChangedListener(this);

		gridViewCitysList = (GridView) findViewById(R.id.citys_list);
		gridViewCitysList.setEmptyView(findViewById(R.id.citys_list_empty));

		mClearSearchBtn = (ImageButton) findViewById(R.id.ib_clear_text);
		mClearSearchBtn.setOnClickListener(this);
		mSearchEditText = (EditText) findViewById(R.id.search_edit);
		mSearchEditText.addTextChangedListener(this);
		mCityContainer = findViewById(R.id.city_content_container);
		mSearchContainer = findViewById(R.id.search_content_container);
		mSearchListView = (ListView) findViewById(R.id.search_list);
		mSearchListView.setEmptyView(findViewById(R.id.search_empty));
		mSearchContainer.setVisibility(View.GONE);
		mSearchListView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mInputMethodManager.hideSoftInputFromWindow(
						mSearchEditText.getWindowToken(), 0);
				return false;
			}
		});
	}

	private void initTopTitle() {
		TopTitleView topTitle = (TopTitleView) findViewById(R.id.top_title);
		topTitle.showLeftImag(R.drawable.back, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		topTitle.showCenterText(getString(R.string.city_title), null);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		cities = new ArrayList<City>();
		hotCityAdapter = new HotCityAdapter(CityActivity.this, cities);
		gridViewCitysList.setAdapter(hotCityAdapter);

		mSearchCityAdapter = new SearchCityAdapter(CityActivity.this);
		mSearchListView.setAdapter(mSearchCityAdapter);
	}

	@Override
	public void lastLoad() {
		// TODO Auto-generated method stub
		super.lastLoad();

		if (AppContext.ISAllHOTCITYGETED) {
			synchronized (loc) {
				isHotCityUpdate = true;
				updateHotCity();
			}
		} else {
			synchronized (loc) {
				isHotCityUpdate = false;
			}
		}
	}

	/**
	 * 跟新热门城市页面
	 */
	private void updateHotCity() {
		// TODO Auto-generated method stub
		cities.clear();
		cities.add(new City());
		cities.addAll(AppContext.allhotCitiesList);

		hotCityAdapter.notifyDataSetChanged();
	}

	@Override
	public void bindViews() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (comeFrom) {
		case FROM_HOME_TAG:
			finish();
			break;
		case FROM_APPSTART_TAG:
			exit();
			break;
		}
		return false;
	}

	/**
	 * 判断两次返回时间间隔,小于两秒则退出程序
	 */
	private void exit() {
		if (System.currentTimeMillis() - mExitTime > INTERVAL) {
			showToast(getResString(R.string.main_exit));
			mExitTime = System.currentTimeMillis();
		} else {
			AppManager.getAppManager().AppExit(CityActivity.this);
		}
	}

	@Override
	public void onHotCityComplite() {
		// TODO Auto-generated method stub
		synchronized (loc) {
			if (!isHotCityUpdate) {
				updateHotCity();
			}
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		if (AppContext.citiesList.size() < 1 || TextUtils.isEmpty(s)) {
			mCityContainer.setVisibility(View.VISIBLE);
			mSearchContainer.setVisibility(View.INVISIBLE);
			mClearSearchBtn.setVisibility(View.GONE);
		} else {
			mClearSearchBtn.setVisibility(View.VISIBLE);
			mCityContainer.setVisibility(View.INVISIBLE);
			mSearchContainer.setVisibility(View.VISIBLE);
			mSearchCityAdapter.getFilter().filter(s);
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ib_clear_text:
			if (!TextUtils.isEmpty(mSearchEditText.getText().toString())) {
				mSearchEditText.setText("");
				mInputMethodManager.hideSoftInputFromWindow(
						mSearchEditText.getWindowToken(), 0);
			}
			break;
		}
	}

	@Override
	public void onCityComplite() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNetChange() {
		// TODO Auto-generated method stub

	}
}
