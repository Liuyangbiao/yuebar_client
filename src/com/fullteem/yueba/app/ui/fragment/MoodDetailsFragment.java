package com.fullteem.yueba.app.ui.fragment;

import java.util.LinkedList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.fullteem.yueba.R;
import com.fullteem.yueba.app.AppContext;
import com.fullteem.yueba.app.adapter.MoodDetailsAdapter;
import com.fullteem.yueba.app.ui.BaseActivity;
import com.fullteem.yueba.app.ui.BaseActivity.IActivityResult;
import com.fullteem.yueba.app.ui.EditUserMoodActivity;
import com.fullteem.yueba.app.ui.PerssonalInfoAcitivity;
import com.fullteem.yueba.app.singer.model.SingerDetailModel;
import com.fullteem.yueba.engine.upload.PhotoUtil;
import com.fullteem.yueba.model.MoodModel;
import com.fullteem.yueba.model.ResponeModel;
import com.fullteem.yueba.net.http.CustomAsyncResponehandler;
import com.fullteem.yueba.net.http.HttpRequest;
import com.fullteem.yueba.util.LogUtil;
import com.fullteem.yueba.util.TimeUtil;
import com.fullteem.yueba.util.UmengUtil;
import com.fullteem.yueba.widget.EmptyView;
import com.fullteem.yueba.widget.TopTitleView;
import com.fullteem.yueba.widget.XListView;
import com.fullteem.yueba.widget.XListView.IXListViewListener;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月14日&emsp;上午8:26:25
 * @use 歌手心情界面
 */
@SuppressLint("ValidFragment")
public class MoodDetailsFragment extends Fragment implements IActivityResult {
	private SwipeRefreshLayout srlRefreshMood;
	private XListView lvMood;
	private List<MoodModel> listMood;
	private MoodDetailsAdapter adapterMood;
	private SingerDetailModel singerModel;
	private View lvEmpty;

	// 判断是否是自己
	private boolean isMyself;
	// 判断是不是歌手;
	private boolean isSinger;

	private int pageNo = 1;
	private int pageSize = 6;// 默认10条为1页
	private boolean isLoadMore = false;
	private boolean isRefresh = false;
	private AppContext appcontext;

	public MoodDetailsFragment(SingerDetailModel model) {
		this.singerModel = model;
	}

	public MoodDetailsFragment() {
	}

	public void setModel(SingerDetailModel model) {
		this.singerModel = model;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_mood_details, container,
				false);
		initViews(view);
		initDate();
		bindViews();
		return view;
	}

	private void initViews(View view) {
		// 防止你呢个点击到activity的东西
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});

		// for title
		TopTitleView topTitle = (TopTitleView) view
				.findViewById(R.id.top_title);
		topTitle.showLeftImag(R.drawable.back, new OnClickListener() {
			@Override
			public void onClick(View v) {
				((BaseActivity) getActivity()).getSupportFragmentManager()
						.popBackStack();
			}
		});
		topTitle.showCenterText(getString(R.string.mood), null);

		Integer userId = Integer.valueOf(AppContext.getApplication()
				.getUserInfo().getUserId());
		if (userId == null) {
			LogUtil.LogError(getClass().getName(), "更多心情。。。userid为null", null);
			return;
		}

		if (singerModel != null) {
			isSinger = singerModel.getUserType() == 2;
			isMyself = userId == singerModel.getUserId();
		}

		// 自己进入心情界面可以新增心情
		if (isMyself)
			topTitle.showRightText(getString(R.string.add),
					R.style.toptitle_right, new OnClickListener() {
						@Override
						public void onClick(View v) {
							// 心情页面点击新增
							PhotoUtil.printTrace("click add photo");

							Intent intent = new Intent(getActivity(),
									EditUserMoodActivity.class);
							// intent.putExtra("editTextShow", true);
							// intent.putExtra("titleIsCancel", true);
							// intent.putExtra("msg", "请输入您的心情履历");
							((PerssonalInfoAcitivity) getActivity())
									.startActivityForResult(intent, 704);
							PhotoUtil.printTrace("after started activity");
						}
					});

		// srlRefreshMood = (SwipeRefreshLayout)
		// view.findViewById(R.id.srl_refreshMood);
		lvMood = (XListView) view.findViewById(R.id.lv_mood);
		lvMood.setPullLoadEnable(false);
		lvEmpty = view.findViewById(R.id.lvEmpty);
		EmptyView emptyView = new EmptyView(getActivity());
		emptyView.setVisibility(View.GONE);
		((ViewGroup) lvMood.getParent()).addView(emptyView);
		lvMood.setEmptyView(emptyView);
		lvMood.setVisibility(View.GONE);
	}

	private void initDate() {

		appcontext = AppContext.getApplication();
		if (singerModel == null) {
			((BaseActivity) getActivity())
					.showToast(getString(R.string.hint_moodLoadError));
			return;
		}

		lvMood.setAdapter(adapterMood = adapterMood == null ? new MoodDetailsAdapter(
				listMood = listMood == null ? new LinkedList<MoodModel>()
						: listMood, singerModel) : adapterMood);

		// // --------------------------examples data--------------------------
		// int dataTotal = new Random().nextInt(10);
		// for (int i = 0; i < 10; i++) {
		// MoodDetailsModel moodModel = new MoodDetailsModel();
		// moodModel.setUserNickname(getString(R.string.name_normal));
		// moodModel.setUserHeader("drawable://" +
		// R.drawable.ico_loginmain_sina);
		// moodModel.setPostDate(getActivity().getString(R.string.moodPostData_normal));
		// moodModel.setUserMoodHeader("drawable://" +
		// R.drawable.temp_portrait01);
		// moodModel.setUserMood(getString(R.string.pay_wechat_describe));
		// moodModel.setUserViews("100" + i);
		// moodModel.setPraise("99" + i);
		// listMood.add(moodModel);
		// }
		//
		// lvMood.setAdapter(new MoodDetailsAdapter(listMood));
		// // --------------------------examples data--------------------------

	}

	private void bindViews() {
		lvMood.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				isLoadMore = false;
				isRefresh = true;
				pageNo = 1;
				if (isSinger)
					loadSingerData();
				else
					loadUserData();
				lvMood.setRefreshTime(TimeUtil.getCurrentDateTime());
			}

			@Override
			public void onLoadMore() {
				isLoadMore = true;
				isRefresh = false;
				pageNo++;
				if (isSinger)
					loadSingerData();
				else
					loadUserData();
			}
		});
	}

	/**
	 * 从服务器获取歌手心情数据
	 */
	private void loadSingerData() {
		HttpRequest.getInstance(getActivity()).getSingerMood(
				singerModel.getSingerId(), pageNo, pageSize,
				new CustomAsyncResponehandler() {

					@Override
					public void onSuccess(ResponeModel baseModel) {
						if (baseModel != null && baseModel.isStatus()) {
							List<MoodModel> listResultMood = (List<MoodModel>) baseModel
									.getListObject("singerMoodeRecordList",
											MoodModel.class);
							lvMood.setPullLoadEnable(listResultMood == null ? false
									: listResultMood.size() >= pageSize);
							if (listResultMood != null) {
								if (!isLoadMore)
									listMood.clear();
								listMood.addAll(listResultMood);
								adapterMood.notifyDataSetChanged();
							}
						}
					}

					@Override
					public void onFinish() {
						if (lvMood.getVisibility() != View.VISIBLE)
							lvMood.setVisibility(View.VISIBLE);
						if (lvEmpty.getVisibility() != View.GONE)
							lvEmpty.setVisibility(View.GONE);
						if (isRefresh) {
							lvMood.stopRefresh();
							isRefresh = false;
						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
					}
				});
	}

	/**
	 * 从服务器获取用户心情数据
	 */
	private void loadUserData() {
		HttpRequest.getInstance(getActivity()).getUserMood(
				singerModel.getUserId(), pageNo, pageSize,
				new CustomAsyncResponehandler() {

					@Override
					public void onSuccess(ResponeModel baseModel) {
						if (baseModel != null && baseModel.isStatus()) {
							List<MoodModel> listResultMood = (List<MoodModel>) baseModel
									.getResultObject();
							lvMood.setPullLoadEnable(listResultMood == null ? false
									: listResultMood.size() >= pageSize);
							if (listResultMood != null) {
								if (!isLoadMore)
									listMood.clear();
								listMood.addAll(listResultMood);
								adapterMood.notifyDataSetChanged();
							}
						}
					}

					@Override
					public void onFinish() {
						if (lvMood.getVisibility() != View.VISIBLE)
							lvMood.setVisibility(View.VISIBLE);
						if (lvEmpty.getVisibility() != View.GONE)
							lvEmpty.setVisibility(View.GONE);
						if (isRefresh) {
							lvMood.stopRefresh();
							isRefresh = false;
						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
					}
				});
	}

	public IActivityResult getResultListener() {
		return this;
	}

	@Override
	public void setActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 704:
			String mood = data.getStringExtra("edittext");
			if (mood != null && mood.length() != 0) {
				sendUpdateMoodRequest(mood);
			} else {
				BaseActivity activity = ((BaseActivity) getActivity());
				if (activity != null) {
					activity.showToast("输入内如不可为空");
				}
			}
			break;

		default:
			break;
		}
	}

	private void sendUpdateMoodRequest(String str) {
		if (appcontext.getUserInfo() == null
				|| TextUtils.isEmpty(appcontext.getUserInfo().getUserId())) {
			((BaseActivity) getActivity())
					.showToast(getString(R.string.hint_longinFirst));
			return;
		}
		Integer userId = Integer.valueOf(appcontext.getUserInfo().getUserId());
		if (userId == null) {
			((BaseActivity) getActivity())
					.showToast(getString(R.string.hint_longinFirst));
			return;
		}
		HttpRequest.getInstance(getActivity()).getUpdateUserMood(userId, str,
				MoodRequestHandler);
	}

	CustomAsyncResponehandler MoodRequestHandler = new CustomAsyncResponehandler() {
		@Override
		public void onSuccess(ResponeModel baseModel) {
			if (baseModel != null && baseModel.isStatus()) {
				loadUserData();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
		};
	};

	@Override
	public void onResume() {
		if (isSinger) {
			isRefresh = true;
			loadSingerData();
		} else {
			isRefresh = true;
			loadUserData();
		}
		super.onResume();
		UmengUtil.onPageStart(getActivity(),"MainScreen");
	}
	
	@Override
	public void onPause() {
		super.onPause();
		UmengUtil.onPageEnd(getActivity(),"MainScreen");
	}

}
