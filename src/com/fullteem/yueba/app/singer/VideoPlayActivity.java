package com.fullteem.yueba.app.singer;

import java.util.LinkedList;
import java.util.List;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Message;
import android.os.RemoteException;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fullteem.yueba.R;
import com.fullteem.yueba.app.AppContext;
import com.fullteem.yueba.app.singer.VideoPlayerView.IFullScreenEvent;
import com.fullteem.yueba.app.singer.adapter.VideoCommentAdapter;
import com.fullteem.yueba.app.singer.model.VideoCommentModel;
import com.fullteem.yueba.app.singer.model.VideoDetailModel;
import com.fullteem.yueba.app.ui.BaseActivity;
import com.fullteem.yueba.app.ui.GiftActivity;
import com.fullteem.yueba.app.ui.PubDetailsActivity;
import com.fullteem.yueba.globle.GlobleConstant;
import com.fullteem.yueba.model.ResponeModel;
import com.fullteem.yueba.net.http.CustomAsyncResponehandler;
import com.fullteem.yueba.net.http.HttpRequest;
import com.fullteem.yueba.util.LogUtil;
import com.fullteem.yueba.util.TimeUtil;
import com.fullteem.yueba.util.VideoPlayerHelper;
import com.fullteem.yueba.widget.TopTitleView;
import com.fullteem.yueba.widget.XListView;
import com.fullteem.yueba.widget.XListView.IXListViewListener;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月17日&emsp;下午6:25:22
 * @use 视频播放界面
 */
public class VideoPlayActivity extends BaseActivity implements IFullScreenEvent {
	private VideoPlayerView viewVideoPlay;
	private TextView tvSinger;
	private LinearLayout llGift, llPraise;
	private CheckBox ckbPraise;
	private TextView tvPraise, tvGift;
	private TextView tvRegularPub;
	private Button btnDetail;
	private XListView lvComments;
	private EditText edtComment;
	private Button btnComment;
	private List<VideoCommentModel> listVideoComment;
	private VideoCommentAdapter adapterVideoComment;
	private EventListener mListener;
	private volatile int pubId = -1;// 驻唱酒吧id
	private String pubName;
	private int videoId;
	private int singerId = -1; // 歌手id，送礼物时用到

	private boolean isLoadMore = false;
	private boolean isRefresh = false;
	private int pageSize = 10;// 默认10条为1页
	private int pageNo = 1;
	private TopTitleView topTitle;

	public VideoPlayActivity() {
		super(R.layout.activity_video_play);
	}

	@Override
	public void initViews() {
		initTopTitle();
		viewVideoPlay = (VideoPlayerView) findViewById(R.id.viewVideoPlay);
		tvSinger = (TextView) findViewById(R.id.tvSinger);
		llPraise = (LinearLayout) findViewById(R.id.ll_praise);
		llGift = (LinearLayout) findViewById(R.id.ll_gift);
		ckbPraise = (CheckBox) findViewById(R.id.ckb_praise);
		tvPraise = (TextView) findViewById(R.id.tv_praise);
		tvGift = (TextView) findViewById(R.id.tv_gift);
		tvRegularPub = (TextView) findViewById(R.id.tvRegularPub);
		btnDetail = (Button) findViewById(R.id.btnDetail);
		lvComments = (XListView) findViewById(R.id.lvComments);
		edtComment = (EditText) findViewById(R.id.edtComment);
		btnComment = (Button) findViewById(R.id.btnComment);
		// lvComments.setPullLoadEnable(false);
	}

	private void initTopTitle() {
		topTitle = (TopTitleView) findViewById(R.id.top_title);
		topTitle.showLeftImag(R.drawable.back, new OnClickListener() {
			@Override
			public void onClick(View v) {
				AppContext.mp.release();
				finish();
			}
		});
		topTitle.showCenterText(getString(R.string.play), null);
	}

	@Override
	public void initData() {
		mListener = new EventListener();

		lvComments
				.setAdapter(adapterVideoComment = adapterVideoComment == null ? new VideoCommentAdapter(
						listVideoComment = listVideoComment == null ? new LinkedList<VideoCommentModel>()
								: listVideoComment)
						: adapterVideoComment);

		loadVideoDetail(videoId = getIntent().getIntExtra(
				GlobleConstant.VIDEO_ID, -1));

		// // --------------------------examples data--------------------------
		//
		// int totalData = new Random().nextInt(10);
		//
		// for (int i = 0; i < totalData; i++) {
		// VideoCommentModel videoCommentModel = new VideoCommentModel();
		// videoCommentModel.setHeader("drawable://" + R.drawable.pub_icon);
		// videoCommentModel.setNickname(getString(R.string.order_name_normal));
		// videoCommentModel.setTime(getString(R.string.date_time_normal));
		// videoCommentModel.setContent(getString(R.string.app_error_message));
		// listVideoComment.add(videoCommentModel);
		// }
		//
		// lvComments.setAdapter(new VideoCommentAdapter(listVideoComment));
		// viewVideoPlay.init(VideoPlayActivity.this, null, null, "drawable://"
		// + R.drawable.pub_image_icon);
		//
		// // --------------------------examples data--------------------------
	}

	@Override
	public void bindViews() {
		btnDetail.setOnClickListener(mListener);
		btnComment.setOnClickListener(mListener);
		llPraise.setOnClickListener(mListener);
		llGift.setOnClickListener(mListener);
		lvComments.setXListViewListener(mListener);
	}

	private class EventListener implements OnClickListener, IXListViewListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ll_praise:
				if (videoId == -1) {
					showToast(getString(R.string.hint_dateLoading));
					break;
				}
				videoPriase(ckbPraise.isSelected());
				break;

			case R.id.ll_gift:
				if (singerId == -1) {
					showToast(getString(R.string.hint_dateLoading));
					break;
				}
				Intent intentGift = new Intent(VideoPlayActivity.this,
						GiftActivity.class);
				intentGift.putExtra(GlobleConstant.GIFT_GIVE, singerId);
				startActivity(intentGift);
				break;

			case R.id.btnDetail:
				if (pubId == -1) {
					showToast(getString(R.string.hint_dateLoading));
					break;
				}
				Intent intent = new Intent(VideoPlayActivity.this,
						PubDetailsActivity.class);
				intent.putExtra(GlobleConstant.PUB_ID, pubId);
				intent.putExtra(GlobleConstant.PUB_NAME, pubName);
				startActivity(intent);
				break;

			case R.id.btnComment:
				String commentContent = edtComment.getText().toString();
				if (TextUtils.isEmpty(commentContent.trim())) {
					showToast(getString(R.string.hint_commentContentNone));
					break;
				}
				sendComment(commentContent);
				break;

			}
		}

		@Override
		public void onRefresh() {
			isLoadMore = false;
			isRefresh = true;
			pageNo = 0;
			loadCommentData();
			lvComments.setRefreshTime(TimeUtil.getCurrentDateTime());
		}

		@Override
		public void onLoadMore() {
			isLoadMore = true;
			isRefresh = false;
			pageNo++;
			loadCommentData();
		}
	}

	/**
	 * 从服务器获取数据
	 */
	private void loadVideoDetail(int videoId) {
		if (videoId == -1) {
			showToast(getString(R.string.hint_videoLoadingError));
			return;
		}

		Integer userId = Integer.valueOf(AppContext.getApplication()
				.getUserInfo().getUserId());
		HttpRequest.getInstance(VideoPlayActivity.this).getSingerVideoDetait(
				videoId, userId, new CustomAsyncResponehandler() {

					@Override
					public void onSuccess(ResponeModel baseModel) {
						LogUtil.LogDebug(getClass().getName(),
								baseModel.toString(), null);
						if (baseModel == null || !baseModel.isStatus()) {
							showToast(getString(R.string.hint_videoLoadingError));
							return;
						}

						Integer totalPriase = baseModel
								.getTreeInResult("totalPriase");
						if (totalPriase != null)
							tvPraise.setText(totalPriase + "");
						Boolean isPriase = baseModel
								.getTreeInResult("isPriase");
						if (isPriase != null && isPriase)
							ckbPraise.setSelected(true);
						else
							ckbPraise.setSelected(false);

						VideoDetailModel videoDetail = (VideoDetailModel) baseModel
								.getObject("singerVideoMap",
										VideoDetailModel.class);
						if (videoDetail == null) {
							showToast(getString(R.string.hint_videoLoadingError));
							return;
						}
						if (!viewVideoPlay.isInited()) {
							viewVideoPlay
									.init(VideoPlayActivity.this,
											VideoPlayActivity.this,
											"http://db.atianqi.com:8889/app_3/_definst_/mp4:output/atianqi/4244/500/58156_20150106000318.mp4/playlist.m3u8",
											videoDetail.getVideoLogoUrl());
							// viewVideoPlay.init(VideoPlayActivity.this, null,
							// videoDetail.getVideoPlayUri(),
							// videoDetail.getVideoLogoUrl());
							tvSinger.setText(videoDetail.getSingerStageName());
							tvRegularPub.setText(videoDetail.getPubName());
							pubId = videoDetail.getPubId();
							pubName = videoDetail.getPubName();
							singerId = videoDetail.getSingerId();
						}

						// TODO 还差驻唱酒吧数据、点赞和礼物量

						if (listVideoComment == null
								|| listVideoComment.size() <= 0) {
							List<VideoCommentModel> listVideoCommentModels = (List<VideoCommentModel>) baseModel
									.getListObject("singerVideoCommentList",
											VideoCommentModel.class);
							if (listVideoCommentModels == null
									|| listVideoCommentModels.size() <= 0
									|| listVideoCommentModels.get(0) == null) {
								findViewById(R.id.tvCommentNone).setVisibility(
										View.VISIBLE);
								lvComments.setVisibility(View.GONE);
								return;
							}
							lvComments.setPullLoadEnable(true);
							listVideoComment.addAll(listVideoCommentModels);
							adapterVideoComment.notifyDataSetChanged();
							pageSize = listVideoComment.size();
						}
					}

					@Override
					public void onStart() {
					}

					@Override
					public void onFinish() {
					}

					@Override
					public void onFailure(Throwable error, String content) {
						showToast(getString(R.string.hint_videoLoadingError));
					}
				});
	}

	/**
	 * 从服务器读取评论
	 */
	private void loadCommentData() {
		HttpRequest.getInstance(VideoPlayActivity.this).getSingerVideoComment(
				videoId, pageNo, pageSize, new CustomAsyncResponehandler() {

					@Override
					public void onSuccess(ResponeModel baseModel) {
						if (baseModel == null || !baseModel.isStatus())
							return;
						List<VideoCommentModel> listVideoCommentModels = (List<VideoCommentModel>) baseModel
								.getResultObject();
						lvComments
								.setPullLoadEnable(listVideoCommentModels == null ? false
										: listVideoCommentModels.size() >= pageSize);
						if (listVideoCommentModels == null
								|| listVideoCommentModels.size() <= 0
								|| listVideoCommentModels.get(0) == null)
							return;
						if (!isLoadMore)
							listVideoComment.clear();
						listVideoComment.addAll(listVideoCommentModels);
						adapterVideoComment.notifyDataSetChanged();
					}

					@Override
					public void onStart() {
					}

					@Override
					public void onFinish() {
						if (isRefresh) {
							lvComments.stopRefresh();
							isRefresh = false;
						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
					}
				});
	}

	/**
	 * 发送评论
	 * 
	 * @param singerVideoCommentContent
	 */
	private void sendComment(String singerVideoCommentContent) {
		Integer userId = Integer.valueOf(AppContext.getApplication()
				.getUserInfo().getUserId());
		if (userId == null || userId <= -1)
			return;
		HttpRequest.getInstance(VideoPlayActivity.this).sendSingerVideoComment(
				videoId, userId, singerVideoCommentContent, 1,
				new CustomAsyncResponehandler() {

					@Override
					public void onSuccess(ResponeModel baseModel) {
						if (baseModel != null && baseModel.isStatus()) {
							edtComment.setText("");
							isLoadMore = false;
							isRefresh = true;
							pageNo = 1;
							pageSize = listVideoComment.size() > 2 ? listVideoComment
									.size() : pageSize;
							loadCommentData();
						} else
							showToast(getString(R.string.hint_commentError)
									+ "\n" + baseModel.getCode());
					}

					@Override
					public void onStart() {
					}

					@Override
					public void onFinish() {
					}

					@Override
					public void onFailure(Throwable error, String content) {
					}
				});
	}

	/**
	 * 视频点赞、取消点赞
	 * 
	 * @param isCancelPriase
	 *            true为取消点赞
	 */
	private void videoPriase(final boolean isCancelPriase) {
		Integer userId = Integer.valueOf(AppContext.getApplication()
				.getUserInfo().getUserId());
		if (userId == null) {
			showToast(getString(R.string.hint_operationError));
			ckbPraise.setSelected(!isCancelPriase);
			return;
		}
		HttpRequest.getInstance().getVideoPriase(videoId, userId,
				isCancelPriase, new CustomAsyncResponehandler() {

					@Override
					public void onSuccess(ResponeModel baseModel) {
						if (baseModel != null && baseModel.isStatus()) {
							loadVideoDetail(videoId);
							return;
						}
						showToast(getString(R.string.hint_operationError));
						ckbPraise.setSelected(!isCancelPriase);
					}

					@Override
					public void onFailure(Throwable error, String content) {
						showToast(getString(R.string.hint_operationError));
						ckbPraise.setSelected(!isCancelPriase);
					}
				});
	}

	@Override
	public void preFullScreen() {
		topTitle.setVisibility(View.GONE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
		findViewById(R.id.rlComment).setVisibility(View.GONE);
	}

	@Override
	public void preRestore() {
		topTitle.setVisibility(View.VISIBLE);
		findViewById(R.id.rlComment).setVisibility(View.VISIBLE);
	}

	@Override
	public void onPause() {
		if (AppContext.mp.getState() == VideoPlayerHelper.STATE_PLAYING) {
			AppContext.mp.pause();
		}
		super.onPause();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (viewVideoPlay == null) {
				AppContext.mp.release();
				return super.onKeyDown(keyCode, event);
			}
			if (viewVideoPlay.isFullScreen()) {
				try {
					Message tmsg = Message.obtain(null,
							VideoPlayerHelper.MSG_MEDIA_FULLSCREEN_TRIGGER);
					viewVideoPlay.getPlayerMsg().send(tmsg);
					return true;
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			} else {
				viewVideoPlay.onStop();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		if (viewVideoPlay != null)
			viewVideoPlay.onStop();
		super.onDestroy();
	}
}
