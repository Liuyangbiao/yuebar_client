package com.fullteem.yueba.app.ui;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.fullteem.yueba.R;
import com.fullteem.yueba.app.ui.fragment.DynamicBarsFragment;
import com.fullteem.yueba.app.ui.fragment.DynamicFriendsFragment;
import com.fullteem.yueba.app.ui.fragment.DynamicMineFragment;
import com.fullteem.yueba.util.BitmapManager;
import com.fullteem.yueba.widget.TopTitleView;

/**
 * 动态界面
 * 
 * @author ssy
 * 
 */
public class DynamicActivity extends BaseActivity implements OnClickListener {
	private List<Fragment> listFragment;
	private FriendsListAdapter firendListAdapter;
	private ViewPager vpFragmentContainer;
	private TopTitleView topTitle;
	private Button btnFirends;
	private Button btnBars;
	private Button btnMine;
	private List<Button> btnList;

	public DynamicActivity() {
		super(R.layout.activity_dynamic);
	}

	@Override
	public void initViews() {
		btnList = new ArrayList<Button>();
		btnFirends = (Button) findViewById(R.id.btnFirends);
		btnBars = (Button) findViewById(R.id.btnBars);
		btnMine = (Button) findViewById(R.id.btnMine);
		btnList.add(btnFirends);
		btnList.add(btnBars);
		btnList.add(btnMine);
		topTitle = (TopTitleView) findViewById(R.id.toptile);
		listFragment = new ArrayList<Fragment>();
		// listFragment.add(DynamicFriendsFragment.getInstance());
		// listFragment.add(DynamicBarsFragment.getInstance());
		// listFragment.add(DynamicMineFragment.getInstance());
		listFragment.add(new DynamicFriendsFragment());
		listFragment.add(new DynamicBarsFragment());
		listFragment.add(new DynamicMineFragment());
		firendListAdapter = new FriendsListAdapter(getSupportFragmentManager());
		vpFragmentContainer = (ViewPager) findViewById(R.id.vp_fragment_container);
		vpFragmentContainer.setAdapter(firendListAdapter);
		vpFragmentContainer.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				ButtonSelect(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

	}

	@Override
	public void initData() {
		btnFirends.setOnClickListener(this);
		btnBars.setOnClickListener(this);
		btnMine.setOnClickListener(this);
		topTitle.showCenterButton(getString(R.string.dynamic_title), null,
				null, null);

		topTitle.showLeftImag(
				BitmapManager.getBitmapFromDrawable(this, R.drawable.back),
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});

	}

	@Override
	public void bindViews() {

	}

	class FriendsListAdapter extends FragmentPagerAdapter {

		public FriendsListAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return listFragment.get(position);
		}

		@Override
		public int getCount() {
			return listFragment.size();
		}
	}

	private void ButtonSelect(int position) {
		switch (position) {
		case 0:
			vpFragmentContainer.setCurrentItem(0);
			btnFirends.setBackgroundResource(R.drawable.btn_group_selected);
			btnFirends.setTextColor(getResources().getColor(
					R.color.btn_nearby_selected));
			btnBars.setBackgroundResource(R.drawable.btn_group_unselected);
			btnBars.setTextColor(getResources().getColor(
					R.color.btn_nearby_unselected));
			btnMine.setBackgroundResource(R.drawable.btn_group_unselected);
			btnMine.setTextColor(getResources().getColor(
					R.color.btn_nearby_unselected));

			break;
		case 1:
			vpFragmentContainer.setCurrentItem(1);
			btnFirends.setBackgroundResource(R.drawable.btn_group_unselected);
			btnFirends.setTextColor(getResources().getColor(
					R.color.btn_nearby_unselected));
			btnBars.setBackgroundResource(R.drawable.btn_group_selected);
			btnBars.setTextColor(getResources().getColor(
					R.color.btn_nearby_selected));
			btnMine.setBackgroundResource(R.drawable.btn_group_unselected);
			btnMine.setTextColor(getResources().getColor(
					R.color.btn_nearby_unselected));

			break;

		case 2:
			vpFragmentContainer.setCurrentItem(2);
			btnFirends.setBackgroundResource(R.drawable.btn_group_unselected);
			btnFirends.setTextColor(getResources().getColor(
					R.color.btn_nearby_unselected));
			btnBars.setBackgroundResource(R.drawable.btn_group_unselected);
			btnBars.setTextColor(getResources().getColor(
					R.color.btn_nearby_unselected));
			btnMine.setBackgroundResource(R.drawable.btn_group_selected);
			btnMine.setTextColor(getResources().getColor(
					R.color.btn_nearby_selected));
			break;
		default:
			break;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnFirends:
			ButtonSelect(0);
			break;
		case R.id.btnBars:
			ButtonSelect(1);
			break;
		case R.id.btnMine:
			ButtonSelect(2);
			break;
		default:
			break;
		}

	}

	// @Override
	// protected void onResume() {
	// for (int i = 0; i < listFragment.size(); i++) {
	// listFragment.get(i).onResume();
	// }
	// super.onResume();
	// }

	// @Override
	// protected void onDestroy() {
	// for (int i = 0; i < listFragment.size(); i++) {
	// listFragment.get(i).onDestroy();
	// }
	// super.onDestroy();
	// }

}
