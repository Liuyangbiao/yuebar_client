package com.fullteem.yueba.app.adapter;

import java.util.List;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.fullteem.yueba.R;
import com.fullteem.yueba.app.AppContext;
import com.fullteem.yueba.app.adapter.BaseDataAdapter.ImageType;
import com.fullteem.yueba.app.ui.PerssonalInfoAcitivity;
import com.fullteem.yueba.db.DBFriendListDao;
import com.fullteem.yueba.globle.GlobleConstant;
import com.fullteem.yueba.model.DatePersonModel;
import com.fullteem.yueba.model.ResponeModel;
import com.fullteem.yueba.model.UserCommonModel;
import com.fullteem.yueba.net.http.CustomAsyncResponehandler;
import com.fullteem.yueba.net.http.HttpRequest;
import com.fullteem.yueba.util.StringUtils;
import com.fullteem.yueba.util.StringUtils.Gender;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月1日&emsp;下午4:22:25
 * @use 约会管理-想参与约会adapter
 */
public class WantPartakeAdapter extends
		BaseDataAdapter.Build<DatePersonModel, WantPartakeAdapter.ViewHolder> {
	private boolean isShowManage = true; // 是否显示管理
	private INotifyRefresh iRefresh;
	private AppContext appContext;
	private static EMConversation conversation;

	public WantPartakeAdapter(AppContext appContext,
			List<DatePersonModel> listData) {
		super(listData, ImageType.NO_DEFAULT);
		this.appContext = appContext;
	}

	public void setShowManage(boolean isShowManage) {
		this.isShowManage = isShowManage;
	}

	public void setRefreshListener(INotifyRefresh iRefresh) {
		this.iRefresh = iRefresh;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
		View itemView = View.inflate(viewGroup.getContext(),
				R.layout.adapter_date_manage_detail, null);
		return new ViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, final int position) {
		showImage(holder.ivUsrHeader, listData.get(position).getUserHeaderUrl());
		showText(holder.tvUsrNickname, listData.get(position).getUserNickname());
		showText(holder.tvUsrMood, listData.get(position)
				.getUserPersonalitySignature());
		showText(holder.tvUsrAge, listData.get(position).getUserAge());

		/*
		 * Drawable genderDrawable = null; if
		 * ("男".equals(listData.get(position).getUserGender())) genderDrawable =
		 * holder
		 * .tvUsrAge.getContext().getResources().getDrawable(R.drawable.sex_boy
		 * ); else genderDrawable =
		 * holder.tvUsrAge.getContext().getResources().getDrawable
		 * (R.drawable.sex_girl); genderDrawable.setBounds(0, 0,
		 * genderDrawable.getMinimumWidth(), genderDrawable.getMinimumHeight());
		 * holder.tvUsrAge.setCompoundDrawables(genderDrawable, null, null,
		 * null);
		 */

		if ("男".equals(listData.get(position).getUserGender())) {
			StringUtils.changeStyle(appContext, holder.tvUsrAge,
					Gender.GENDER_BOY);
		} else {
			StringUtils.changeStyle(appContext, holder.tvUsrAge,
					Gender.GENDER_GIRL);
		}

		if (!isShowManage)
			holder.rlManage.setVisibility(View.GONE);
		else
			holder.rlManage.setVisibility(View.VISIBLE);

		// 同意约会
		holder.btnArgee.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				HttpRequest.getInstance().getDatePersonManage(
						listData.get(position).getUserAppointmentJoinId(), 1,
						new CustomAsyncResponehandler() {

							@Override
							public void onSuccess(ResponeModel baseModel) {
								if (baseModel != null && baseModel.isStatus()) {
									sendText(position);
									if (iRefresh != null)
										iRefresh.onRefresh();
									return;
								}
								Toast.makeText(
										v.getContext(),
										v.getContext().getString(
												R.string.hint_operationError),
										Toast.LENGTH_SHORT).show();
							}

							@Override
							public void onFailure(Throwable error,
									String content) {
								Toast.makeText(
										v.getContext(),
										v.getContext().getString(
												R.string.hint_operationError),
										Toast.LENGTH_SHORT).show();
							}
						});
			}
		});

		// 拒绝约会
		holder.btnReject.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				HttpRequest.getInstance().getDatePersonManage(
						listData.get(position).getUserAppointmentJoinId(), 0,
						new CustomAsyncResponehandler() {

							@Override
							public void onSuccess(ResponeModel baseModel) {
								if (baseModel != null && baseModel.isStatus()) {
									if (iRefresh != null)
										iRefresh.onRefresh();
									return;
								}
								Toast.makeText(
										v.getContext(),
										v.getContext().getString(
												R.string.hint_operationError),
										Toast.LENGTH_SHORT).show();
							}

							@Override
							public void onFailure(Throwable error,
									String content) {
								Toast.makeText(
										v.getContext(),
										v.getContext().getString(
												R.string.hint_operationError),
										Toast.LENGTH_SHORT).show();
							}
						});
			}
		});

		holder.ivUsrHeader.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(),
						PerssonalInfoAcitivity.class);
				intent.putExtra("userId", listData.get(position).getUserId()
						+ "");
				v.getContext().startActivity(intent);
			}
		});

	}

	class ViewHolder extends BaseDataAdapter.ViewHolder {
		ImageView ivUsrHeader;
		TextView tvUsrNickname;
		TextView tvUsrAge;
		TextView tvUsrMood;
		Button btnArgee, btnReject;
		RelativeLayout rlManage;

		public ViewHolder(View itemView) {
			super(itemView);
			ivUsrHeader = (ImageView) itemView.findViewById(R.id.iv_usrheader);
			tvUsrNickname = (TextView) itemView
					.findViewById(R.id.tv_usrnickname);
			tvUsrAge = (TextView) itemView.findViewById(R.id.tv_usrsex);
			tvUsrMood = (TextView) itemView.findViewById(R.id.tv_usrmood);
			btnArgee = (Button) itemView.findViewById(R.id.btn_argee);
			btnReject = (Button) itemView.findViewById(R.id.btn_reject);
			rlManage = (RelativeLayout) itemView.findViewById(R.id.rlManage);
		}
	}

	/**
	 * 同意或拒绝用户后通知主界面进行数据刷新
	 * 
	 * @author roujun
	 * 
	 */
	public interface INotifyRefresh {
		void onRefresh();
	}

	private void sendText(final int position) {
		if (position >= listData.size())
			return;
		conversation = EMChatManager.getInstance().getConversation(
				listData.get(position).getImServerId());

		EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
		TextMessageBody txtBody = new TextMessageBody(listData.get(position)
				.getUserNickname()
				+ " 被 "
				+ appContext.getUserInfo().getUserName() + " 选为约会对象，聊聊约会细节吧！");
		// 设置消息body
		message.addBody(txtBody);
		// 设置要发给谁,用户username或者群聊groupid
		message.setReceipt(listData.get(position).getImServerId());

		// 添加默认的扩展字段,此处设置为他的nickname
		message.setAttribute("nickname", appContext.getUserInfo().getUserName());

		String imgurl;
		if (appContext.getUserInfo().getUserLogoUrl() == null) {
			imgurl = "null";
		} else {
			imgurl = appContext.getUserInfo().getUserLogoUrl();
		}
		message.setAttribute("imgurl", imgurl);
		message.setAttribute("userid", appContext.getUserInfo().getImServerId());
		message.setAttribute("type", GlobleConstant.MESSAGE_TYPE);
		// 把messgage加到conversation中
		// message.isAcked = false;
		conversation.addMessage(message);

		UserCommonModel user = new UserCommonModel();
		user.setUserMobile(listData.get(position).getUserMobile());
		user.setUserId(listData.get(position).getUserId() + "");
		user.setUserName(listData.get(position).getUserNickname());
		user.setImServerId(listData.get(position).getImServerId());
		
		String imgUrl;
		if (listData.get(position).getUserHeaderUrl() == null) {
			imgUrl = "null";
		} else {
			imgUrl = listData.get(position).getUserHeaderUrl();
		}
		user.setUserLogoUrl(imgUrl);
		saveToDB(user);
	}

	private void saveToDB(final UserCommonModel user) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				DBFriendListDao dao = new DBFriendListDao(appContext);
				dao.saveContacter(user);
			}
		}).start();
	}
}
