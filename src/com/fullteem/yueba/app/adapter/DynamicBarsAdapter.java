package com.fullteem.yueba.app.adapter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fullteem.yueba.R;
import com.fullteem.yueba.app.AppContext;
import com.fullteem.yueba.app.adapter.BaseDataAdapter.ImageType;
import com.fullteem.yueba.app.ui.PubDetailsActivity;
import com.fullteem.yueba.globle.GlobleConstant;
import com.fullteem.yueba.model.BarDynamicCommentModel;
import com.fullteem.yueba.model.BarDynamicModel;
import com.fullteem.yueba.model.PubModel;
import com.fullteem.yueba.model.ResponeModel;
import com.fullteem.yueba.net.http.CustomAsyncResponehandler;
import com.fullteem.yueba.net.http.HttpRequest;
import com.fullteem.yueba.util.DateUtil;
import com.fullteem.yueba.util.DisplayUtils;
import com.fullteem.yueba.widget.CircleImageView;
import com.fullteem.yueba.widget.ShowMoreListview;

public class DynamicBarsAdapter extends
		BaseDataAdapter.Build<PubModel, DynamicBarsAdapter.ViewHolder> {
	private IPraiseClickListener praiseClickListener;
	private boolean isReplyToSomeOne;
	private int rePlyPosition;

	public DynamicBarsAdapter(IPraiseClickListener praiseClickListener,
			List<PubModel> listData) {
		super(listData, ImageType.NO_DEFAULT);
		this.praiseClickListener = praiseClickListener;
	}

	@Override
	public int getCount() {
		int count = 0;
		for (int i = 0; i < listData.size(); i++) {
			for (int j = 0; j < listData.get(i).getBarDynamicList().size(); j++) {
				count++;
			}
		}
		return count;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
		View itemView = View.inflate(viewGroup.getContext(),
				R.layout.adapter_dynamic_bars, null);
		return new ViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, final int position) {
		// 重置resetViewHolder，让输入框消失，处理listView复用问题
		resetViewHolder(holder.commentLinearLayout);
		int count = -1;
		for (int i = 0; i < (position < listData.size() ? position + 1
				: listData.size()); i++) { // 遍历酒吧
			for (int j = 0; j < (position < listData.get(i).getBarDynamicList()
					.size() ? position + 1 : listData.get(i)
					.getBarDynamicList().size()); j++) { // 遍历每个酒吧对应动态
				if (++count == position) {
					showImage(holder.ivPubHeader, listData.get(i)
							.getBarLogoUrl());
					showText(holder.pubName, listData.get(i).getBarName());
					showText(holder.tvAddress, listData.get(i).getBarAddress());
					showText(holder.tvPhoneNum, listData.get(i)
							.getBarTelephone());
					showText(holder.tvComments, listData.get(i)
							.getBarDynamicList().get(j).getCommentNum()
							+ "");
					showText(holder.tvPraise, listData.get(i)
							.getBarDynamicList().get(j).getPraiseNum()
							+ "");
					showText(holder.tvDynamic, listData.get(i)
							.getBarDynamicList().get(j).getDynamicContent());
					DisplayUtils.praiseChanged(holder.tvPraise, listData.get(i)
							.getBarDynamicList().get(j).isPraise());
					onPraiseClickListener(holder.tvPraise, i, j);
					onPubHeaderListener(holder.ivPubHeader, listData.get(i)
							.getBarId(), listData.get(i).getBarName());
					onCommentsClickListener(holder.tvComments,
							holder.commentLinearLayout);

					final BarDynamicModel currentBarDynamic = listData.get(i)
							.getBarDynamicList().get(j);
					final List<BarDynamicCommentModel> dynamicCommentList = currentBarDynamic
							.getBarDynamicComment();
					if (dynamicCommentList != null
							&& dynamicCommentList.size() > 0
							&& dynamicCommentList.get(0) != null) {
						holder.exListView.setVisibility(View.VISIBLE);
						final List<BarDynamicCommentModel> LastUserDynamicList = new LinkedList<BarDynamicCommentModel>();
						if (!LastUserDynamicList.isEmpty()) {
							LastUserDynamicList.clear();
						}
						LastUserDynamicList.addAll(ReBuildList(true,
								dynamicCommentList));
						holder.exListAdapter = new CommentListAdapter(
								LastUserDynamicList, holder.editText,
								holder.submitBtn, holder.commentLinearLayout);
						if (dynamicCommentList.size() > ShowMoreListview.MAX_COUNT)
							holder.checkMore.setVisibility(View.VISIBLE);
						else
							holder.checkMore.setVisibility(View.GONE);
						holder.exListView.initListAdapter(holder.exListAdapter);

						// 查看更多点击事件
						holder.checkMore
								.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										LastUserDynamicList.clear();
										LastUserDynamicList.addAll(ReBuildList(
												holder.isNeedLoadMore,
												dynamicCommentList));
										holder.exListAdapter
												.notifyDataSetChanged();
										holder.isNeedLoadMore = !holder.isNeedLoadMore;
										// 更改查看/隐藏更多评论显示效果
										TextView tvLoadingMore = (TextView) holder.checkMore
												.getChildAt(0);
										if (holder.isNeedLoadMore) {
											tvLoadingMore
													.setText(R.string.hide_more_comments);
										} else {
											tvLoadingMore
													.setText(R.string.check_more_comments);
										}
									}
								});
					} else {
						holder.exListView.setVisibility(View.GONE);
						holder.checkMore.setVisibility(View.GONE);
					}

					onCommentListener(holder.commentLinearLayout,
							holder.submitBtn, holder.editText,
							currentBarDynamic);
				}
			}
		}
	}

	private void resetViewHolder(LinearLayout commentLinearLayout) {
		commentLinearLayout.setVisibility(View.GONE);
	}

	/**
	 * 评论点击事件
	 * 
	 * @param tvComments
	 * @param commentLinearLayout
	 */
	private void onCommentsClickListener(TextView tvComments,
			final LinearLayout commentLinearLayout) {
		tvComments.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				commentLinearLayout.setVisibility(View.VISIBLE);
				System.out.println("评论按钮被点击了");
			}
		});
	}

	/**
	 * 点赞点击事件
	 * 
	 * @param tvPraise
	 * @param i
	 * @param j
	 */
	private void onPraiseClickListener(TextView tvPraise, final int i,
			final int j) {
		tvPraise.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Integer userId = Integer.valueOf(AppContext.getApplication()
						.getUserInfo().getUserId());
				if (userId == null)
					return;
				HttpRequest.getInstance().getPubPriase(
						listData.get(i).getBarDynamicList().get(j)
								.getDynamicId(), userId,
						listData.get(i).getBarDynamicList().get(j).isPraise(),
						new CustomAsyncResponehandler() {
							@Override
							public void onSuccess(ResponeModel baseModel) {
								if (baseModel != null && baseModel.isStatus())
									if (praiseClickListener != null)
										praiseClickListener
												.onPraiseClickListener();
							}

							@Override
							public void onFailure(Throwable error,
									String content) {
							}
						});
			}
		});
	}

	private void onCommentListener(final LinearLayout commentLinearLayout,
			final Button submitBtn, final EditText editText,
			final BarDynamicModel currentBarDynamic) {
		/**
		 * 评论提交按钮
		 */
		submitBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (TextUtils.isEmpty(editText.getText()))
					return;
				if (TextUtils.isEmpty(editText.getText())) {
					isReplyToSomeOne = false;
				}

				List<BarDynamicCommentModel> dynamicCommentList = currentBarDynamic
						.getBarDynamicComment();

				int barDynamicId = isReplyToSomeOne ? dynamicCommentList.get(
						rePlyPosition).getUserDynamicId() : currentBarDynamic
						.getDynamicId();
				Integer commenterId = (commenterId = isReplyToSomeOne ? (dynamicCommentList
						.get(rePlyPosition).getUserDynamicCommentType() == 2 ? dynamicCommentList
						.get(rePlyPosition).getReplyerId() : dynamicCommentList
						.get(rePlyPosition).getCommenterId())
						: currentBarDynamic.getBarId()) == -1 ? null
						: commenterId;
				Integer replyerId = Integer.valueOf(AppContext.getApplication()
						.getUserInfo().getUserId());
				int barDynamicCommentType = isReplyToSomeOne ? 2 : 1;
				String dynamicCommentContent = editText.getText().toString();
				String barDynamicGroup = isReplyToSomeOne ? dynamicCommentList
						.get(rePlyPosition).getUserDynamicGroup() : null;

				HttpRequest.getInstance().addBarDynamicComment(barDynamicId,
						commenterId, replyerId, barDynamicCommentType,
						dynamicCommentContent, barDynamicGroup,
						new CustomAsyncResponehandler() {

							@Override
							public void onSuccess(ResponeModel baseModel) {
//								System.out.println("酒吧提交成功baseModel："
//										+ baseModel);
								if (praiseClickListener != null)
									praiseClickListener.onPraiseClickListener();
							}

							@Override
							public void onFailure(Throwable error,
									String content) {
								System.err.println("评论" + error.toString()
										+ "\tcontent" + content);
							}
						});

				editText.setText("");
				editText.setHint("");
				// 点击“发表”评论框消失
				commentLinearLayout.setVisibility(View.GONE);
			}
		});
	}

	/**
	 * 点击酒吧头像事件
	 * 
	 * @param ivPubHeader
	 * @param pubName
	 */
	private void onPubHeaderListener(final ImageView ivPubHeader,
			final int pubId, final String pubName) {
		ivPubHeader.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ivPubHeader.getContext(),
						PubDetailsActivity.class);
				intent.putExtra(GlobleConstant.PUB_ID, pubId);
				intent.putExtra(GlobleConstant.PUB_NAME, pubName);
				ivPubHeader.getContext().startActivity(intent);
			}
		});

	}

	class ViewHolder extends BaseDataAdapter.ViewHolder {
		ImageView ivPubHeader;
		TextView pubName;
		TextView tvAddress;
		TextView tvPhoneNum;
		TextView tvPraise;
		TextView tvComments;
		TextView tvDynamic;
		BaseAdapter exListAdapter;
		ShowMoreListview exListView;
		LinearLayout checkMore;
		boolean isNeedLoadMore;

		// 评论部分
		EditText editText;
		Button submitBtn;
		LinearLayout commentLinearLayout;// 评论编辑框

		public ViewHolder(View itemView) {
			super(itemView);
			ivPubHeader = (ImageView) itemView.findViewById(R.id.ivPubHeader);
			pubName = (TextView) itemView.findViewById(R.id.pubName);
			tvAddress = (TextView) itemView.findViewById(R.id.tvAddress);
			tvPhoneNum = (TextView) itemView.findViewById(R.id.tvPhoneNum);
			tvPraise = (TextView) itemView.findViewById(R.id.tvPraise);
			tvComments = (TextView) itemView.findViewById(R.id.tvComments);
			tvDynamic = (TextView) itemView.findViewById(R.id.edtDynamic);
			exListView = (ShowMoreListview) itemView
					.findViewById(R.id.exListView);
			editText = (EditText) itemView.findViewById(R.id.edtInput);
			checkMore = (LinearLayout) itemView.findViewById(R.id.llLoadMore);
			submitBtn = (Button) itemView.findViewById(R.id.btnSend);
			commentLinearLayout = (LinearLayout) itemView
					.findViewById(R.id.replareLayout);
		}
	}

	public interface IPraiseClickListener {
		void onPraiseClickListener();
	}

	/**
	 * 用户评论list
	 * 
	 * @author ssy
	 * 
	 */
	class CommentListAdapter
			extends
			BaseDataAdapter.Build<BarDynamicCommentModel, CommentListAdapter.CommentListViewHolder> {
		// private List<BarDynamicCommentModel> cCommentList;
		EditText editText;
		Button submitBtn;
		LinearLayout commentLinearLayout;

		public CommentListAdapter(List<BarDynamicCommentModel> listData,
				EditText editText, Button submitBtn,
				LinearLayout commentLinearLayout) {
			super(listData, ImageType.NO_DEFAULT);
			this.editText = editText;
			this.submitBtn = submitBtn;
			this.commentLinearLayout = commentLinearLayout;
		}

		@Override
		public CommentListViewHolder onCreateViewHolder(ViewGroup viewGroup,
				int position) {
			View itemView = View.inflate(viewGroup.getContext(),
					R.layout.adapter_dynamic_comment, null);
			return new CommentListViewHolder(itemView);
		}

		@Override
		public void onBindViewHolder(CommentListViewHolder holder,
				final int position) {
			// if (null != listData.get(position).getCommenterUserLogoUrl()) {
			// showImage(holder.ImgViewHeader,
			// listData.get(position).getCommenterUserLogoUrl());
			// } else {
			// showImage(holder.ImgViewHeader,
			// listData.get(position).getReplyerUserLogoUrl());
			// }
			holder.tvCreateTime.setText(DateUtil.formatTimeStr(listData.get(
					position).getCreateDate()));
			holder.tvContent.setText(":"
					+ listData.get(position).getDynamicCommentContent());
			String commentUserName = listData.get(position)
					.getCommenterUserName();
			String commentReplyName = listData.get(position)
					.getReplyerUserName();
			int commentType = listData.get(position)
					.getUserDynamicCommentType();

			// 回复某人的回复
			if (commentUserName != null && commentReplyName != null
					&& commentType == 2) {
				holder.tvName.setText(commentReplyName + " 回复 "
						+ commentUserName);
				showImage(holder.ImgViewHeader, listData.get(position)
						.getReplyerUserLogoUrl());
			} else if (commentUserName != null && commentReplyName == null) {
				holder.tvName.setText(commentUserName);
				showImage(holder.ImgViewHeader, listData.get(position)
						.getCommenterUserLogoUrl());
			} else {
				holder.tvName.setText(commentReplyName);
				showImage(holder.ImgViewHeader, listData.get(position)
						.getCommenterUserLogoUrl());
			}
			if (position == 0) { // 存在0的位置
				if (TextUtils.isEmpty(listData.get(position)
						.getCommentContent())) {
					editText.setHint(listData.get(position).getCommentHint());
					editText.setText("");
				} else {
					editText.setText(listData.get(position).getCommentContent());
					editText.setHint("");
				}
			}

			holder.tvRepare.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					commentLinearLayout.setVisibility(View.VISIBLE);
					String commentUserName = listData.get(position)
							.getCommenterUserName();
					String commentReplyName = listData.get(position)
							.getReplyerUserName();
					int commentType = listData.get(position)
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
			// editText.addTextChangedListener(new TextWatcher() {
			//
			// @Override
			// public void onTextChanged(CharSequence s, int start, int before,
			// int count) {
			// }
			//
			// @Override
			// public void beforeTextChanged(CharSequence s, int start, int
			// count, int after) {
			// }
			//
			// @Override
			// public void afterTextChanged(Editable s) {
			// listData.get(0).setCommentContent(editText.getText().toString());
			// }
			// });

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
			listData.get(0).setCommentHint(editText.getHint().toString());
		}

		class CommentListViewHolder extends BaseDataAdapter.ViewHolder {
			CircleImageView ImgViewHeader;
			TextView tvName;
			TextView tvCreateTime;
			TextView tvContent;
			TextView tvRepare;

			public CommentListViewHolder(View itemView) {
				super(itemView);
				ImgViewHeader = (CircleImageView) itemView
						.findViewById(R.id.ImgViewHeader);
				tvName = (TextView) itemView.findViewById(R.id.tvName);
				tvCreateTime = (TextView) itemView
						.findViewById(R.id.tvCreateTime);
				tvContent = (TextView) itemView.findViewById(R.id.tvContent);
				tvRepare = (TextView) itemView.findViewById(R.id.tvRepare);
			}
		}
	}

	/**
	 * 重构list
	 */
	private List<BarDynamicCommentModel> ReBuildList(boolean isNeedLoad,
			List<BarDynamicCommentModel> userList) {
		List<BarDynamicCommentModel> newUserList = new ArrayList<BarDynamicCommentModel>();
		if (userList == null)
			return new ArrayList<BarDynamicCommentModel>();
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
}
