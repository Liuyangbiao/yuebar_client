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
import com.fullteem.yueba.app.ui.fragment.FriendListFragment;
import com.fullteem.yueba.app.ui.fragment.GroupFragment;
import com.fullteem.yueba.util.BitmapManager;
import com.fullteem.yueba.widget.TopTitleView;

/**
 * 好友列表/群组列表
 * 
 * @author ssy
 */
public class FriendsListActivity extends BaseActivity implements
		OnClickListener {
	private List<Fragment> listFragment;
	private FriendsListAdapter firendListAdapter;
	private ViewPager vpFragmentContainer;
	private TopTitleView topTitle;
	private Button btnFriends;
	private Button btnGroup;
	private List<Button> btnList;

	public FriendsListActivity() {
		super(R.layout.activity_firendslist);
	}

	@Override
	public void initViews() {
		btnList = new ArrayList<Button>();
		btnFriends = (Button) findViewById(R.id.btn_friends);
		btnGroup = (Button) findViewById(R.id.btn_group);
		btnList.add(btnFriends);
		btnList.add(btnGroup);
		topTitle = (TopTitleView) findViewById(R.id.toptile);
		listFragment = new ArrayList<Fragment>();
		listFragment.add(FriendListFragment.getInstance());
		listFragment.add(GroupFragment.getInstance());
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
		btnFriends.setOnClickListener(this);
		btnGroup.setOnClickListener(this);
		topTitle.showCenterButton(getString(R.string.discover_myFollow), null,
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_friends:
			ButtonSelect(0);
			break;
		case R.id.btn_group:
			ButtonSelect(1);
			break;
		default:
			break;
		}

	}

	private void ButtonSelect(int position) {
		switch (position) {
		case 0:
			vpFragmentContainer.setCurrentItem(0);
			btnFriends.setBackgroundResource(R.drawable.btn_group_selected);
			btnGroup.setBackgroundResource(R.drawable.btn_group_unselected);
			btnFriends.setTextColor(getResources().getColor(
					R.color.btn_nearby_selected));
			btnGroup.setTextColor(getResources().getColor(
					R.color.btn_nearby_unselected));

			break;
		case 1:
			vpFragmentContainer.setCurrentItem(1);
			btnFriends.setBackgroundResource(R.drawable.btn_group_unselected);
			btnGroup.setBackgroundResource(R.drawable.btn_group_selected);
			btnFriends.setTextColor(getResources().getColor(
					R.color.btn_nearby_unselected));
			btnGroup.setTextColor(getResources().getColor(
					R.color.btn_nearby_selected));
			break;
		default:
			break;
		}

	}
}
