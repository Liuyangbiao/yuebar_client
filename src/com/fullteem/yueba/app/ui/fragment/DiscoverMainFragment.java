package com.fullteem.yueba.app.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;

import com.fullteem.yueba.R;
import com.fullteem.yueba.app.singer.DiscoverNearbySingerActivity;
import com.fullteem.yueba.app.ui.DiscoverNearbyFriendActivity;
import com.fullteem.yueba.app.ui.DiscoverNearbyGroupActivity;
import com.fullteem.yueba.app.ui.FriendsListActivity;
import com.fullteem.yueba.util.SharePreferenceUtil;
import com.fullteem.yueba.util.UmengUtil;
import com.fullteem.yueba.widget.HintContentPopWindow;
import com.fullteem.yueba.widget.TopTitleView;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年11月26日&emsp;下午3:21:25
 * @use 主界面->发现
 */
public class DiscoverMainFragment extends Fragment {

	public DiscoverMainFragment() {
	}

	private static DiscoverMainFragment instance;

	public static DiscoverMainFragment getInstance() {
		if (instance == null) {
			instance = new DiscoverMainFragment();
		}
		return instance;
	}

	private TopTitleView topTitle;
	private RelativeLayout rlMyFollow, rlNearbyGroup;
	private RelativeLayout rlNearbyFriend, rlNearbySinger;
	private CheckBox ckbLuckMeet;
	private EventListener mListener;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_discover_main,
				container, false);
		initViews(view);
		initDate();
		bindViews();
		return view;
	}

	private void initViews(View view) {
		topTitle = (TopTitleView) view.findViewById(R.id.top_title);
		rlMyFollow = (RelativeLayout) view.findViewById(R.id.rl_myFollow);
		// rlDynamic = (RelativeLayout) view.findViewById(R.id.rl_dynamic);
		rlNearbyGroup = (RelativeLayout) view.findViewById(R.id.rl_nearbyGroup);
		rlNearbyFriend = (RelativeLayout) view
				.findViewById(R.id.rl_nearbyFriend);
		rlNearbySinger = (RelativeLayout) view
				.findViewById(R.id.rl_nearbySinger);
		ckbLuckMeet = (CheckBox) view.findViewById(R.id.ckb_luckMeet);
		ckbLuckMeet.setSelected(SharePreferenceUtil.getInstance(getActivity())
				.getBooleanFromShare("IS_LUCKY_MEET", false));
	}

	private void initDate() {
		topTitle.showCenterText(getString(R.string.discover), null);
		mListener = new EventListener();
	}

	private void bindViews() {
		rlMyFollow.setOnClickListener(mListener);
		// rlDynamic.setOnClickListener(mListener);
		rlNearbyGroup.setOnClickListener(mListener);
		rlNearbyFriend.setOnClickListener(mListener);
		rlNearbySinger.setOnClickListener(mListener);
		ckbLuckMeet.setOnCheckedChangeListener(mListener);
	}

	private class EventListener implements OnClickListener,
			OnCheckedChangeListener {
		@Override
		public void onClick(View v) {
			// v.setBackgroundResource(R.drawable.bg_nearby_data_state);
			// DisplayUtils.resetBack(v, R.drawable.bg_nearby_datecontent);
			switch (v.getId()) {
			case R.id.rl_myFollow:
				startActivity(new Intent(getActivity(),
						FriendsListActivity.class));
				break;

			// case R.id.rl_dynamic:
			// startActivity(new Intent(getActivity(), DynamicActivity.class));
			// break;
			case R.id.rl_nearbyGroup:
				startActivity(new Intent(getActivity(),
						DiscoverNearbyGroupActivity.class));
				break;
			case R.id.rl_nearbyFriend:
				startActivity(new Intent(getActivity(),
						DiscoverNearbyFriendActivity.class));
				break;
			case R.id.rl_nearbySinger:
				startActivity(new Intent(getActivity(),
						DiscoverNearbySingerActivity.class));
				break;
			}
		}

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (isChecked)
				new HintContentPopWindow(getActivity()).setCenterGravity(
						Gravity.LEFT).showWindow(
						"已开启 幸运相遇 功能，每天10点和22点将会有小惊喜哦！");
			SharePreferenceUtil.getInstance(getActivity()).saveBooleanToShare(
					"IS_LUCKY_MEET", isChecked);
		}

	}
	
	@Override
	public void onResume() {
		super.onResume();
		UmengUtil.onPageStart(getActivity(),"MainScreen");
	}
	
	@Override
	public void onPause() {
		super.onPause();
		UmengUtil.onPageEnd(getActivity(),"MainScreen");
	}

}
