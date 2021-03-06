package com.fullteem.yueba.app.adapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.fullteem.yueba.R;
import com.fullteem.yueba.app.AppContext;
import com.fullteem.yueba.app.ui.PicturesActivity;
import com.fullteem.yueba.interfaces.ICommentOnClickListener;
import com.fullteem.yueba.model.BaseModel;
import com.fullteem.yueba.model.MoodModel;
import com.fullteem.yueba.model.UserDynamicCommentModel;
import com.fullteem.yueba.net.Urls;
import com.fullteem.yueba.net.http.AsyncHttpResponseHandler;
import com.fullteem.yueba.net.http.HttpRequestFactory;
import com.fullteem.yueba.util.DateUtil;
import com.fullteem.yueba.util.DisplayUtils;
import com.fullteem.yueba.util.ImageLoaderUtil;
import com.fullteem.yueba.widget.CircleImageView;
import com.fullteem.yueba.widget.ExpandGridView;
import com.fullteem.yueba.widget.ShowMoreListview;

public class DynamicCommonAdapter extends BaseListAdapter<MoodModel> {
	Activity context;
	List<MoodModel> list;
	private int pageType;
	private AppContext appContext;
	List<UserDynamicCommentModel> commentList;
	boolean isReplyToSomeOne;
	int rePlyPosition;
	private ICommentOnClickListener commentListener;
	private HashMap<Integer, View> viewMap;// 保存adapter中position与Viewy映射关系的map
	private HashMap<Integer, View> ommentsViewMap;

	/**
	 * 构造函数
	 * 
	 * @param context
	 * @param list
	 * @param pageType
	 *            1:好友 2：自己
	 */
	public DynamicCommonAdapter(Activity context, List<MoodModel> list,
			int pageType, ICommentOnClickListener commentListener) {
		super(context, list);
		this.context = context;
		this.list = list;
		this.pageType = pageType;
		appContext = AppContext.getApplication();
		this.commentListener = commentListener;

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder vh;
		final List<UserDynamicCommentModel> LastUserDynamicList = new ArrayList<UserDynamicCommentModel>();
		viewMap = new HashMap<Integer, View>();
		View rootView = viewMap.get(position);
		if (rootView == null) {
			rootView = LayoutInflater.from(context).inflate(
					R.layout.adapter_dynamic_friends, null);
			vh = new ViewHolder();
			vh.ImgViewHeader = (CircleImageView) rootView
					.findViewById(R.id.ImgViewHeader);
			vh.tvName = (TextView) rootView.findViewById(R.id.tvName);
			vh.tvCreateTime = (TextView) rootView
					.findViewById(R.id.tvCreateTime);
			vh.tvComments = (TextView) rootView.findViewById(R.id.tvComments);
			vh.tvPraise = (TextView) rootView.findViewById(R.id.tvPraise);
			vh.edtMood = (TextView) rootView.findViewById(R.id.edtMood);
			vh.gridView = (ExpandGridView) rootView
					.findViewById(R.id.exGridview);
			vh.exListView = (com.fullteem.yueba.widget.ShowMoreListview) rootView
					.findViewById(R.id.exListView);
			vh.checkMore = (LinearLayout) rootView
					.findViewById(R.id.llLoadMore);

			vh.commentLinearLayout = (LinearLayout) rootView
					.findViewById(R.id.replareLayout);
			vh.editText = (EditText) rootView.findViewById(R.id.edtInput);
			vh.submitBtn = (Button) rootView.findViewById(R.id.btnSend);
			rootView.setTag(vh);
		} else {
			vh = (ViewHolder) rootView.getTag();
		}

		// 2:自己 1:好友
		String imgUrl = DisplayUtils.getAbsolutelyUrl(list.get(position)
				.getUserLogourl());

		// 自己
		if (pageType == 2) {
			ImageLoaderUtil.getImageLoader()
					.displayImage(appContext.getUserInfo().getUserLogoUrl(),
							vh.ImgViewHeader);
			vh.tvName.setText(appContext.getUserInfo().getUserName());
		} else {
			ImageLoaderUtil.getImageLoader().displayImage(imgUrl,
					vh.ImgViewHeader);
			vh.tvName.setText(list.get(position).getUserName());
		}

		vh.tvCreateTime.setText(DateUtil.formatTimeStr(list.get(position)
				.getCreateDate()));
		vh.edtMood.setText(list.get(position).getMoodRecordText());
		vh.tvPraise.setText(list.get(position).getPraise());
		vh.tvComments.setText(list.get(position).getCommentNum());

		String urls = list.get(position).getMoodRecordImgUrl();
		String[] urlsArray;
		if (urls != null && urls.length() > 0 && urls.contains(",")) {
			urlsArray = urls.split(",");
		} else
			urlsArray = new String[] { urls };

		List<String> strList = new ArrayList<String>();
		for (String string : urlsArray) {
			strList.add(string);
		}
		vh.gridView.setAdapter(new GridAdapter(context, strList));

		// 评论处理

		vh.exListAdapter = new CommentListAdapter(context, LastUserDynamicList,
				vh.editText, vh.submitBtn, vh.commentLinearLayout);
		commentList = list.get(position).getUserDynamicComment();
		if (commentList == null) {
			commentList = new ArrayList<UserDynamicCommentModel>();
		}

		if (!LastUserDynamicList.isEmpty()) {
			LastUserDynamicList.clear();
		}

		LastUserDynamicList.addAll(ReBuildList(true, commentList));

		if (vh.exListView.getHeaderViewsCount() > 0) {
			vh.exListView.removeHeaderView(vh.checkMore);
		}
		if (commentList.size() > ShowMoreListview.MAX_COUNT) {
			vh.checkMore.setVisibility(View.VISIBLE);
		} else {
			vh.checkMore.setVisibility(View.GONE);
		}

		vh.exListView.initListAdapter(vh.exListAdapter);
		// 点击评论弹出提交发表editText
		vh.tvComments.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				vh.commentLinearLayout.setVisibility(View.VISIBLE);
			}
		});

		// 查看更多点击事件
		vh.checkMore.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				LastUserDynamicList.clear();
				LastUserDynamicList.addAll(ReBuildList(vh.isNeedLoadMore, list
						.get(position).getUserDynamicComment()));
				vh.exListAdapter.notifyDataSetChanged();
				vh.isNeedLoadMore = !vh.isNeedLoadMore;
				// 更改查看/隐藏更多评论显示效果
				TextView tvLoadingMore = (TextView) vh.checkMore.getChildAt(0);
				if (vh.isNeedLoadMore) {
					tvLoadingMore.setText(R.string.hide_more_comments);
				} else {
					tvLoadingMore.setText(R.string.check_more_comments);
				}
			}
		});

		// 点赞点击事件
		vh.tvPraise.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sendPriaseRequest(appContext.getUserInfo().getUserId(), list
						.get(position).getMoodRecordId());
			}
		});

		/**
		 * 评论提交按钮
		 */
		vh.submitBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// showToast(vh.editText.getText().toString() + "  "
				// + list.get(position).getMoodRecordText());
				if (TextUtils.isEmpty(vh.editText.getText()))
					return;
				if (TextUtils.isEmpty(vh.editText.getText())) {
					isReplyToSomeOne = false;
				}

				JSONObject jo = new JSONObject();
				// 针对评论的评论
				if (isReplyToSomeOne) {
					// showToast(list.get(position).getUserDynamicComment()
					// .get(rePlyPosition).getUserDynamicGroup()+list.get(position).getUserDynamicComment()
					// .get(rePlyPosition).getUserName());
					jo.put("userDynamicId", list.get(position)
							.getUserDynamicComment().get(rePlyPosition)
							.getUserDynamicId());
					int type = list.get(position).getUserDynamicComment()
							.get(rePlyPosition).getUserDynamicCommentType();
					if (1 == type) {
						jo.put("commenterId", list.get(position)
								.getUserDynamicComment().get(rePlyPosition)
								.getCommenterId());
					} else if (2 == type) {
						jo.put("commenterId", list.get(position)
								.getUserDynamicComment().get(rePlyPosition)
								.getReplyerId());
					}
					jo.put("replyerId", appContext.getUserInfo().getUserId());
					jo.put("userDynamicCommentType", 2);
					jo.put("dynamicCommentContent", vh.editText.getText());
					jo.put("userDynamicGroup", list.get(position)
							.getUserDynamicComment().get(rePlyPosition)
							.getUserDynamicGroup());
					HttpRequestFactory.getInstance().postRequest(
							Urls.USER_DYNAMIC_COMMENT, jo, requestHandler);
				} else {
					jo.put("replyerId", appContext.getUserInfo().getUserId());
					jo.put("userDynamicId", list.get(position)
							.getMoodRecordId());

					jo.put("commenterId", list.get(position).getUserId());
					jo.put("userDynamicCommentType", 1);
					jo.put("dynamicCommentContent", vh.editText.getText());
					HttpRequestFactory.getInstance().postRequest(
							Urls.USER_DYNAMIC_COMMENT, jo, requestHandler);

				}
				vh.editText.setText("");
				vh.editText.setHint("");
				vh.commentLinearLayout.setVisibility(View.GONE);
			}
		});

		// if(commentList.size()>ShowMoreListview.MAX_COUNT){
		// vh.exListView.setFooterVisible();
		// }
		return rootView;
	}

	/**
	 * 评论
	 */
	// private void doReply(final EditText editText, Button submitBtn,
	// final int position) {
	// submitBtn.setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// if (TextUtils.isEmpty(editText.getText()))
	// return;
	// JSONObject jo = new JSONObject();
	// jo.put("userDynamicId", list.get(position).getMoodRecordId());
	// jo.put("commenterId", appContext.getUserInfo().getUserId());
	// jo.put("replyerId", list.get(position).getUserId());
	// jo.put("userDynamicCommentType", 1);
	// jo.put("dynamicCommentContent", editText.getText());
	// HttpRequestFactory.getInstance().getRequest(
	// Urls.USER_DYNAMIC_COMMENT, jo, requestHandler);
	// }
	// });
	//
	// }

	AsyncHttpResponseHandler requestHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(String content) {
			BaseModel<String> model = JSON.parseObject(content,
					new TypeReference<BaseModel<String>>() {
					});
			if ("200".equalsIgnoreCase(model.getCode())) {
				commentListener.reFreshPage();
			}
		};
	};

	/**
	 * 图片adapter
	 * 
	 * @author ssy
	 * 
	 */
	class GridAdapter extends BaseListAdapter<String> {
		private List<String> imgUrlsList;

		public GridAdapter(Activity context, List<String> list) {
			super(context, list);
			imgUrlsList = list;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder vh;
			if (convertView == null) {
				vh = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(
						R.layout.adapter_dynamic_grid, null);
				vh.imgMyPics = (ImageView) convertView
						.findViewById(R.id.imgMyPics);
				convertView.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}

			if (imgUrlsList != null)
				if (imgUrlsList.get(position) != null
						&& !"".equalsIgnoreCase(imgUrlsList.get(position))) {
					int width = DisplayUtils.getScreenWidht(context);
					LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(
							width / 4, width / 4);
					ll.setMargins(5, 10, 5, 10);
					ll.gravity = Gravity.CENTER;
					vh.imgMyPics.setLayoutParams(ll);
					ImageLoaderUtil.getImageLoader().displayImage(
							DisplayUtils.getAbsolutelyUrl(imgUrlsList
									.get(position)), vh.imgMyPics);
				}
			imgOnClicked(vh.imgMyPics, position);
			return convertView;

		}

		private void imgOnClicked(final ImageView ivAlbum, final int position) {

			ivAlbum.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, PicturesActivity.class);
					Bundle b = new Bundle();
					b.putSerializable("list", (Serializable) imgUrlsList);
					intent.putExtra("position", position);
					intent.putExtra("images", imgUrlsList.get(position));// 非必须
					int[] location = new int[2];
					ivAlbum.getLocationOnScreen(location);
					intent.putExtra("locationX", location[0]);// 必须
					intent.putExtra("locationY", location[1]);// 必须
					intent.putExtra("width", ivAlbum.getWidth());// 必须
					intent.putExtra("height", ivAlbum.getHeight());// 必须
					intent.putExtra("bundle", b);
					context.startActivity(intent);
					activity.overridePendingTransition(0, 0);
				}
			});

		}

	}

	/**
	 * 用户评论list
	 * 
	 * @author ssy
	 * 
	 */
	class CommentListAdapter extends BaseListAdapter<UserDynamicCommentModel> {
		private List<UserDynamicCommentModel> cCommentList;
		EditText editText;
		Button submitBtn;
		LinearLayout commentLinearLayout;

		public CommentListAdapter(Activity context,
				List<UserDynamicCommentModel> list, EditText editText,
				Button submitBtn, LinearLayout commentLinearLayout) {
			super(context, list);
			cCommentList = list;
			this.editText = editText;
			this.submitBtn = submitBtn;
			this.commentLinearLayout = commentLinearLayout;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final ViewHolder vh;
			ommentsViewMap = new HashMap<Integer, View>();
			View rootView = ommentsViewMap.get(position);
			if (rootView == null) {
				vh = new ViewHolder();
				rootView = LayoutInflater.from(context).inflate(
						R.layout.adapter_dynamic_comment, null);
				vh.ImgViewHeader = (CircleImageView) rootView
						.findViewById(R.id.ImgViewHeader);
				vh.tvName = (TextView) rootView.findViewById(R.id.tvName);
				vh.tvCreateTime = (TextView) rootView
						.findViewById(R.id.tvCreateTime);
				vh.tvContent = (TextView) rootView.findViewById(R.id.tvContent);
				vh.tvRepare = (TextView) rootView.findViewById(R.id.tvRepare);
				rootView.setTag(vh);
			} else {
				vh = (ViewHolder) rootView.getTag();
			}

			if (null != cCommentList.get(position).getCommenterUserLogoUrl()
					&& cCommentList.get(position).getUserDynamicCommentType() == 1) {
				ImageLoaderUtil.getImageLoader().displayImage(
						DisplayUtils.getAbsolutelyUrl(cCommentList
								.get(position).getCommenterUserLogoUrl()),
						vh.ImgViewHeader);
			} else {
				ImageLoaderUtil.getImageLoader().displayImage(
						DisplayUtils.getAbsolutelyUrl(cCommentList
								.get(position).getReplyerUserLogoUrl()),
						vh.ImgViewHeader);
			}
			vh.tvCreateTime.setText(DateUtil.formatTimeStr(cCommentList.get(
					position).getCreateDate()));
			vh.tvContent.setText(":"
					+ cCommentList.get(position).getDynamicCommentContent());
			String commentUserName = cCommentList.get(position)
					.getCommenterUserName();
			String commentReplyName = cCommentList.get(position)
					.getReplyerUserName();
			int commentType = cCommentList.get(position)
					.getUserDynamicCommentType();

			// 回复某人的回复
			if (commentUserName != null && commentReplyName != null
					&& commentType == 2) {
				vh.tvName.setText(commentReplyName + " 回复 " + commentUserName);
			} else if (commentUserName != null && commentReplyName == null) {
				vh.tvName.setText(commentUserName);
			} else {
				vh.tvName.setText(commentReplyName);
			}

			vh.tvRepare.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					commentLinearLayout.setVisibility(View.VISIBLE);
					String commentUserName = cCommentList.get(position)
							.getCommenterUserName();
					String commentReplyName = cCommentList.get(position)
							.getReplyerUserName();
					int commentType = cCommentList.get(position)
							.getUserDynamicCommentType();
					// 处理评论/回复评论的逻辑
					if (commentUserName != null && commentReplyName != null
							&& commentType == 2) {
						doReply(editText, submitBtn, position, commentReplyName);
					} else if (commentReplyName == null) {
						doReply(editText, submitBtn, position, commentUserName);
					} else {
						doReply(editText, submitBtn, position, commentReplyName);
					}
				}
			});
			return rootView;
		}

		@Override
		public Object getItem(int position) {
			return cCommentList.size();
		}

		@Override
		public void notifyDataSetChanged() {
			// TODO Auto-generated method stub
			super.notifyDataSetChanged();
		}

		/**
		 * 评论回复
		 * 
		 * @param footEditView
		 */
		private void doReply(final EditText mEdittext, Button mSubmitBtn,
				final int position, String toWhom) {
			mEdittext.requestFocus();
			if (toWhom != null && toWhom.length() > 0) {
				mEdittext.setHint("回复: " + toWhom);
				isReplyToSomeOne = true;
				rePlyPosition = position;
			} else {
				isReplyToSomeOne = false;
			}
			// submitBtn.setOnClickListener(new OnClickListener() {
			// @Override
			// public void onClick(View v) {
			// if (TextUtils.isEmpty(editText.getText()))
			// return;
			// }
			// });
		}
	}

	/**
	 * viewholder
	 * 
	 * @author ssy
	 * 
	 */
	class ViewHolder {
		CircleImageView ImgViewHeader;
		TextView tvName;
		TextView tvCreateTime;
		TextView tvComments;
		TextView tvPraise;
		TextView edtMood;
		ExpandGridView gridView;
		ImageView imgMyPics;
		TextView tvContent;
		TextView tvRepare;
		ShowMoreListview exListView;
		LinearLayout checkMore;
		boolean isNeedLoadMore;
		BaseAdapter exListAdapter;

		// // 评论部分
		LinearLayout commentLinearLayout;
		EditText editText;
		Button submitBtn;
	}

	/**
	 * 重构list
	 */
	private List<UserDynamicCommentModel> ReBuildList(boolean isNeedLoad,
			List<UserDynamicCommentModel> userList) {
		List<UserDynamicCommentModel> newUserList = new ArrayList<UserDynamicCommentModel>();
		if (userList == null)
			return new ArrayList<UserDynamicCommentModel>();
		if (userList.size() <= ShowMoreListview.MAX_COUNT) {
			return userList;
		} else {
			if (isNeedLoad) {
				for (int i = 0; i < ShowMoreListview.MAX_COUNT; i++) {
					newUserList.add(userList.get(i));
				}
				return newUserList;
			} else {
				return userList;
			}
		}
	}

	/**
	 * 发送动态点赞请求
	 */
	private void sendPriaseRequest(String userId, String userDynamicId) {
		JSONObject jo = new JSONObject();
		jo.put("userDynamicId", userDynamicId);
		jo.put("userId", userId);
		HttpRequestFactory.getInstance().postRequest(Urls.DYNAMIC_PRIASE, jo,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String content) {
						super.onSuccess(content);

						BaseModel<String> model = JSON.parseObject(content,
								new TypeReference<BaseModel<String>>() {
								});
						if ("200".equalsIgnoreCase(model.getCode())) {
							commentListener.reFreshPage();
						} else {
							showToast("你已经点过赞了");
						}
					}
				});
	}

	/**
	 * 展示toast
	 * 
	 * @param str
	 */
	private void showToast(String str) {
		Toast.makeText(context, str, Toast.LENGTH_SHORT).show();

	}
}
