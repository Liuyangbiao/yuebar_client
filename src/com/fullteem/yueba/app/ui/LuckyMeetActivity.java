package com.fullteem.yueba.app.ui;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.robobinding.ViewBinder;
import org.robobinding.binder.BinderFactory;
import org.robobinding.binder.BinderFactoryBuilder;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fullteem.yueba.R;
import com.fullteem.yueba.app.AppContext;
import com.fullteem.yueba.app.adapter.NearByFriendsListAdapter;
import com.fullteem.yueba.db.DBFriendListDao;
import com.fullteem.yueba.model.HobbyModel;
import com.fullteem.yueba.model.IndustryModel;
import com.fullteem.yueba.model.NearbyPubFriendsModel;
import com.fullteem.yueba.model.NearbyPubFriendsModel.PubFriendsModel;
import com.fullteem.yueba.model.ResponeModel;
import com.fullteem.yueba.model.UserCommonModel;
import com.fullteem.yueba.model.presentmodel.LuckyMeetPresentModel;
import com.fullteem.yueba.net.http.CustomAsyncResponehandler;
import com.fullteem.yueba.net.http.HttpRequest;
import com.fullteem.yueba.util.DisplayUtils;
import com.fullteem.yueba.util.JsonUtil;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月31日&emsp;上午10:37:48
 * @use 幸运相遇
 */
public class LuckyMeetActivity extends BaseActivity {
	private EventListener mListener;
	private LuckyMeetPresentModel presentMode;
	private boolean isLoadComplete; // 事都行业、爱好都加载完成
	private View loadingView;
	private Button btnSure;
	private ListView lvData;
	private NearByFriendsListAdapter adapter;
	private List<PubFriendsModel> listLuckMeet;

	public LuckyMeetActivity() {
		super(-1);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		BinderFactory binderFactory = new BinderFactoryBuilder().build();
		ViewBinder vb = binderFactory.createViewBinder(this, true);
		presentMode = new LuckyMeetPresentModel(LuckyMeetActivity.this);
		View rootView = vb.inflateAndBind(R.layout.activity_lucky_meet,
				presentMode);
		setContentView(rootView);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void initViews() {
		loadingView = findViewById(R.id.lvLoading);
		loadingView.setVisibility(View.VISIBLE);
		presentMode.setAnchorHobby((TextView) findViewById(R.id.tvHobby));
		presentMode.setAnchorGender((TextView) findViewById(R.id.tvGender));
		presentMode.setAnchorIndustry((TextView) findViewById(R.id.tvIndustry));
		btnSure = (Button) findViewById(R.id.btnSure);
		lvData = (ListView) findViewById(R.id.lvData);
	}

	@Override
	public void initData() {
		// 获取爱好列表
		HttpRequest.getInstance().getHobbyList(new CustomAsyncResponehandler() {

			@Override
			public void onSuccess(ResponeModel baseModel) {
				if (isLoadComplete)
					presentMode.setSelectVisibilly(View.VISIBLE);
				if (baseModel != null && baseModel.isStatus()) {
					List<HobbyModel> popupData = (List<HobbyModel>) baseModel
							.getResultObject();
					presentMode.setListHobbyModel(popupData);
				} else
					presentMode.setNoneDataVisibilly(View.VISIBLE);
			}

			@Override
			public void onFinish() {
				if (isLoadComplete) {
					loadingView.setVisibility(View.GONE);
					return;
				}
				isLoadComplete = true;
			}

			@Override
			public void onFailure(Throwable error, String content) {
				if (isLoadComplete)
					presentMode.setNoneDataVisibilly(View.VISIBLE);
			}
		});

		// 获取行业列表
		HttpRequest.getInstance().getIndustryList(
				new CustomAsyncResponehandler() {

					@Override
					public void onSuccess(ResponeModel baseModel) {
						if (isLoadComplete)
							presentMode.setSelectVisibilly(View.VISIBLE);
						if (baseModel != null && baseModel.isStatus()) {
							List<IndustryModel> popupData = (List<IndustryModel>) baseModel
									.getResultObject();
							presentMode.setListIndustryModel(popupData);
						} else
							presentMode.setNoneDataVisibilly(View.VISIBLE);
					}

					@Override
					public void onFinish() {
						if (isLoadComplete) {
							loadingView.setVisibility(View.GONE);
							return;
						}
						isLoadComplete = true;
					}

					@Override
					public void onFailure(Throwable error, String content) {
						if (isLoadComplete)
							presentMode.setNoneDataVisibilly(View.VISIBLE);
					}
				});
	}

	@Override
	public void bindViews() {
		mListener = mListener == null ? new EventListener() : mListener;
		findViewById(R.id.container_main).setOnClickListener(mListener);
		findViewById(R.id.llContent).setOnClickListener(mListener);
		btnSure.setOnClickListener(mListener);
		lvData.setOnItemClickListener(mListener);
	}

	private class EventListener implements OnClickListener, OnItemClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.container_main:
				finish();
				break;

			case R.id.btnSure:
				loadLuckMeetList();
				break;
			}
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view,
				final int position, long id) {
			Intent intent = new Intent(LuckyMeetActivity.this,
					ChatActivity.class);
			intent.putExtra("userId", listLuckMeet.get(position)
					.getUserMobile());
			intent.putExtra("nickname", listLuckMeet.get(position)
					.getUserName());

			// 直接在跳转之前就在数据库插入信息
			new Thread(new Runnable() {
				@Override
				public void run() {
					DBFriendListDao dao = new DBFriendListDao(
							LuckyMeetActivity.this);
					UserCommonModel user = new UserCommonModel();
					user.setUserMobile(listLuckMeet.get(position)
							.getUserMobile());
					user.setUserId(listLuckMeet.get(position).getUserId());
					user.setUserName(listLuckMeet.get(position).getUserName());
					String imgUrl;
					if (listLuckMeet.get(position).getImgUrl() == null) {
						imgUrl = "null";
					} else {
						imgUrl = listLuckMeet.get(position).getImgUrl();
					}
					user.setUserLogoUrl(imgUrl);
					dao.saveContacter(user);
				}
			}).start();
			startActivity(intent);

		}
	}

	@SuppressLint("NewApi")
	private void loadLuckMeetList() {
		if (lvData.getAdapter() == null)
			lvData.setAdapter(adapter = adapter == null ? new NearByFriendsListAdapter(
					LuckyMeetActivity.this,
					listLuckMeet = listLuckMeet == null ? new LinkedList<NearbyPubFriendsModel.PubFriendsModel>()
							: listLuckMeet)
					: adapter);

		final LinearLayout llSelect = (LinearLayout) findViewById(R.id.llSelect);
		int[] location = new int[2];
		llSelect.getLocationOnScreen(location);
		int offsetX = DisplayUtils.getScreenWidht(LuckyMeetActivity.this)
				- location[0] + llSelect.getWidth();
		ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(llSelect,
				"translationX", 1F, -offsetX);
		objectAnimator.setInterpolator(new DecelerateInterpolator(1)); // Decelerate的增值器
		objectAnimator.setDuration(400).start();
		objectAnimator.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				llSelect.setVisibility(View.GONE);
			}
		});

		Integer userId = Integer.valueOf(AppContext.getApplication()
				.getUserInfo().getUserId());
		if (userId == null || userId < 0) {
			showToast(getString(R.string.hint_longinFirst));
			return;
		}

		// 随机获取4条记录，给人真正幸运相遇的感脚
		Random random = new Random(System.currentTimeMillis());
		int pageNumber = random.nextInt(100) + 1;

		HttpRequest.getInstance().getLuckMeetList(userId,
				presentMode.getCurrentHobbyId(),
				presentMode.getCurrentIndustryId(),
				presentMode.getCurrentGenderId(), pageNumber, 4,
				new CustomAsyncResponehandler() {

					@Override
					public void onStart() {
						loadingView.setVisibility(View.VISIBLE);
					}

					@Override
					public void onSuccess(ResponeModel baseModel) {
						if (baseModel != null && baseModel.isStatus()) {
							NearbyPubFriendsModel npf = new NearbyPubFriendsModel();
							npf = JsonUtil.convertJsonToObject(
									baseModel.getJson(),
									NearbyPubFriendsModel.class);
							if (npf != null && npf.getResult() != null) {
								listLuckMeet.addAll(npf.getResult());
								adapter.notifyDataSetChanged();
								return;
							}
						}
						presentMode.setNoneDataVisibilly(View.VISIBLE);
					}

					@Override
					public void onFailure(Throwable error, String content) {
						presentMode.setNoneDataVisibilly(View.VISIBLE);
					}

					@Override
					public void onFinish() {
						loadingView.setVisibility(View.GONE);
						lvData.setVisibility(View.VISIBLE);
					}
				});
	}
}
