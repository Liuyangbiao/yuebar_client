package com.fullteem.yueba.app.ui;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.robobinding.ViewBinder;
import org.robobinding.binder.BinderFactory;
import org.robobinding.binder.BinderFactoryBuilder;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.fullteem.yueba.R;
import com.fullteem.yueba.app.AppContext;
import com.fullteem.yueba.app.adapter.GroupMembersAdapter;
import com.fullteem.yueba.app.adapter.PerssonalAlbumAdapter;
import com.fullteem.yueba.db.DBGroupsDao;
import com.fullteem.yueba.engine.upload.PhotoOwnerEnum;
import com.fullteem.yueba.engine.upload.PhotoUtil;
import com.fullteem.yueba.engine.upload.UploadManager;
import com.fullteem.yueba.model.AlbumPhotoModel;
import com.fullteem.yueba.model.Group;
import com.fullteem.yueba.model.PicModle;
import com.fullteem.yueba.model.ResponeModel;
import com.fullteem.yueba.model.UploadModel;
import com.fullteem.yueba.model.UploadModel.UploadDataModel;
import com.fullteem.yueba.model.presentmodel.GroupDeatilPresentModel;
import com.fullteem.yueba.net.http.CustomAsyncResponehandler;
import com.fullteem.yueba.net.http.HttpRequest;
import com.fullteem.yueba.net.http.HttpRequestFactory;
import com.fullteem.yueba.util.CuttingPicturesUtil;
import com.fullteem.yueba.util.DisplayUtils;
import com.fullteem.yueba.util.ImageLoaderUtil;
import com.fullteem.yueba.util.JsonUtil;
import com.fullteem.yueba.util.LogUtil;
import com.fullteem.yueba.util.UmengUtil;
import com.fullteem.yueba.widget.TopTitleView;

/**
 * @author jun
 * @version 1.0.0
 * @created 2015年1月12日&emsp;上午9:28:40
 * @use 群组详细资料
 */
public class GroupDetailActivity extends BaseActivity {
	private GroupDeatilPresentModel presentMode;
	private RecyclerView recyclerviewAlbum, recyclerviewMembers;
	private Button btnSureJoin;// 申请加入
	private PerssonalAlbumAdapter adapterAlbum;
	private GroupMembersAdapter adapterMembers;
	private List<AlbumPhotoModel> photoModelList;
	private List<PicModle> listMembers;
	private LinkedList<String> uploadedImgList;
	private String groupId;
	private EventListener mListener;
	private boolean isEdit;// 是否在编辑群资料

	private UploadModel upModel;

	private final int GROUP_PHOTO = 0xFF9900; // 群组相册集
	private List<String> localImgList;// 本地图片

	private enum BtnTag {
		TAG_NORMAL, // 默认情况
		TAG_JOINED, // 已加入该群
		TAG_MANAGE, // 群主
	}

	public GroupDetailActivity() {
		super(-1);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		BinderFactory binderFactory = new BinderFactoryBuilder().build();
		ViewBinder vb = binderFactory.createViewBinder(this, true);
		presentMode = new GroupDeatilPresentModel(GroupDetailActivity.this);
		View rootView = vb.inflateAndBind(R.layout.activity_group_detail,
				presentMode);
		setContentView(rootView);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void initViews() {
		initTopTitle();
		recyclerviewAlbum = (RecyclerView) findViewById(R.id.recyclerview_album);
		recyclerviewMembers = (RecyclerView) findViewById(R.id.recyclerview_members);
		btnSureJoin = (Button) findViewById(R.id.btn_sureJoin);
		rightArrowMembers = (ImageView) findViewById(R.id.iv_arrowRightMembers);
		btnSureJoin.setTag(BtnTag.TAG_NORMAL);
		btnSureJoin.setVisibility(View.INVISIBLE);
	}

	private void initTopTitle() {
		TopTitleView topTitle = (TopTitleView) findViewById(R.id.top_title);
		topTitle.showLeftImag(R.drawable.back, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		topTitle.showCenterText(getString(R.string.discover_nearbyGroup), null);
	}

	@Override
	public void initData() {
		groupId = getIntent().getStringExtra("GROUP_ID");
		mListener = new EventListener();

		LinearLayoutManager[] layoutManager = new LinearLayoutManager[2];
		for (int i = 0; i < layoutManager.length; i++) {
			layoutManager[i] = new LinearLayoutManager(this);
			layoutManager[i].setOrientation(LinearLayoutManager.HORIZONTAL);
		}
		recyclerviewAlbum.setLayoutManager(layoutManager[0]);
		recyclerviewMembers.setLayoutManager(layoutManager[1]);
		
		btnSureJoin.setOnClickListener(mListener);

		recyclerviewAlbum
				.setAdapter(adapterAlbum = adapterAlbum == null ? new PerssonalAlbumAdapter(
						photoModelList = photoModelList == null ? new LinkedList<AlbumPhotoModel>()
								: photoModelList)
						: adapterAlbum);
		recyclerviewMembers
				.setAdapter(adapterMembers = adapterMembers == null ? new GroupMembersAdapter(
						listMembers = listMembers == null ? new LinkedList<PicModle>()
								: listMembers)
						: adapterMembers);
		System.out.println("群组相册个数： "+photoModelList.size());
		loadDate();
	}

	@Override
	public void bindViews() {
		//btnSureJoin.setOnClickListener(mListener);
		rightArrowMembers.setOnClickListener(mListener);
	}

	private class EventListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			if (v == rightArrowMembers) {
				Intent intent = new Intent(GroupDetailActivity.this,
						GroupMemberDetailActivity.class);
				intent.putExtra("groupId", groupId);
				startActivity(intent);
			}

			if (v == btnSureJoin) {
				if (btnSureJoin.getTag() == null)
					return;
				if (btnSureJoin.getTag() == BtnTag.TAG_NORMAL) {
					joinGroup();
					return;
				}
				if (btnSureJoin.getTag() == BtnTag.TAG_JOINED) {
					outGroup();
					return;
				}
				if (btnSureJoin.getTag() == BtnTag.TAG_MANAGE) {
					if (isEdit) {
						UpdateGroupIntroduction();
						if (photoModelList.size() > 0
								&& "add".equals(photoModelList.get(
										photoModelList.size() - 1).getTypeTag())) {
							photoModelList.remove(photoModelList.size() - 1);
							adapterAlbum.notifyItemChanged(photoModelList
									.size() - 1);
						}
						if (localImgList != null && localImgList.size() > 0) {
							for (String str : localImgList)
								//upLoadPic(str);
							UploadManager.getInstance(context).upLoadPic(str,GROUP_PHOTO,
									uploadHandler);
						}
					} else {
						isEdit = true;
						btnSureJoin.setText("保存");
						UploadManager.getInstance(context);
						UploadManager.addPictures(photoModelList, null);
						System.out.println("上传相册的个数： "+photoModelList.size());
						adapterAlbum
								.notifyItemChanged((photoModelList.size() - 1) > 0 ? photoModelList
										.size() - 1 : 0);
						findViewById(R.id.etGroupIntroduction).setEnabled(true);
					}
					if (photoModelList.size() <= 0)
						presentMode.setAlbumVisibility(View.GONE);
					else
						presentMode.setAlbumVisibility(View.VISIBLE);
					return;
				}
			}

		}
	}

	/**
	 * 加载群信息
	 */
	private void loadDate() {
		Integer userId = Integer.valueOf(AppContext.getApplication()
				.getUserInfo().getUserId());
		if (userId == null || userId == -1 || TextUtils.isEmpty(groupId))
			return;
		HttpRequest.getInstance().getGroupDetail(userId, groupId,
				new CustomAsyncResponehandler() {

					@Override
					public void onSuccess(ResponeModel baseModel) {
						if (baseModel == null || !baseModel.isStatus()) {
							showToast(getString(R.string.hint_groupError));
							return;
						}
						// 群成员
						List<PicModle> listResultPic = (List<PicModle>) baseModel
								.getListObject("userLogoUrlList",
										PicModle.class);
						if (listResultPic != null && listResultPic.size() > 0
								&& listResultPic.get(0) != null) {
							presentMode.setMembersVisibility(View.VISIBLE);
							if (listMembers.size() > 0)
								listMembers.clear();
							listMembers.addAll(listResultPic);
							adapterMembers.notifyItemRangeInserted(0,
									listMembers.size());
						}
						
						// 群图片
						UploadManager.getAlbumList(PhotoOwnerEnum.GROUP,
								baseModel, photoModelList);
						if (photoModelList.size() <= 0) {
							presentMode.setAlbumVisibility(View.GONE);
						} else {
							presentMode.setAlbumVisibility(View.VISIBLE);
						}

						adapterAlbum.notifyDataSetChanged();

						// 群信息
						btnSureJoin.setVisibility(View.VISIBLE);
						Group groupModel = (Group) baseModel.getObject(
								"barGroupList", Group.class);
						if (groupModel != null) {
							presentMode.setModel(groupModel);
							if (groupModel.isInGroup()) {
								btnSureJoin.setText("退出该群");
								btnSureJoin.setTag(BtnTag.TAG_JOINED);
							} else {
								btnSureJoin.setText("申请加入");
								btnSureJoin.setTag(BtnTag.TAG_NORMAL);
							}
							if (!TextUtils.isEmpty(groupModel.getGroupIcon())) {
								ImageView ivGroupHeader = (ImageView) findViewById(R.id.ivGroupHeader);
								ivGroupHeader
										.setBackgroundColor(Color.TRANSPARENT);
								ImageLoaderUtil
										.getImageLoader()
										.displayImage(
												DisplayUtils
														.getAbsolutelyUrl(groupModel
																.getGroupIcon()),
												ivGroupHeader,
												ImageLoaderUtil
														.getOptions(
																R.drawable.img_loading_default_copy,
																R.drawable.img_loading_empty_copy,
																R.drawable.img_loading_fail_copy));
							}

							if (!TextUtils.isEmpty(groupModel
									.getHarmastLogoUrl())) {
								ImageView ivHarmastHeader = (ImageView) findViewById(R.id.ivHarmastHeader);
								ImageLoaderUtil.getImageLoader().displayImage(
										DisplayUtils
												.getAbsolutelyUrl(groupModel
														.getHarmastLogoUrl()),
										ivHarmastHeader,
										ImageLoaderUtil.getOptions());
							}
							// TODO 如果群主id和当前用户一样，则可编辑
							Integer currentUserId = Integer
									.valueOf(AppContext.getApplication()
											.getUserInfo().getUserId());
							if (currentUserId != null)
								if (groupModel.getUserId() == currentUserId) {
									btnSureJoin.setText("管理群组");
									btnSureJoin.setTag(BtnTag.TAG_MANAGE);
									if (isEdit)
										isEdit = !isEdit;
								}

							// 将群信息插入数据库(直接存)
							DBGroupsDao dao = new DBGroupsDao(context);
							Group group = new Group();
							group.setGroupIcon(groupModel.getGroupIcon());
							group.setGroupId(groupModel.getGroupId());
							group.setGroupName(groupModel.getGroupName());
							dao.saveGroup(group);

						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
						LogUtil.LogDebug(null, "群组详细信息" + error.toString()
								+ "\n" + content, null);
						showToast(getString(R.string.hint_groupError));
					}
				});
	}

	/**
	 * 加入群组
	 */
	private void joinGroup() {
		String userMobile = AppContext.getApplication().getUserInfo()
				.getUserMobile();
		if (TextUtils.isEmpty(userMobile) || TextUtils.isEmpty(groupId))
			return;
		UmengUtil.onEvent(GroupDetailActivity.this, "apply_button_hits");
		LogUtil.printUmengLog("apply_button_hits");
		HttpRequest.getInstance(GroupDetailActivity.this).getJoinGroup(
				userMobile, groupId, new CustomAsyncResponehandler() {

					@Override
					public void onSuccess(ResponeModel baseModel) {
						if (baseModel != null && baseModel.isStatus()) {
							loadDate();
							return;
						}
						showToast(getString(R.string.hint_operationError));
					}

					@Override
					public void onFailure(Throwable error, String content) {
						showToast(getString(R.string.hint_operationError));
					}
				});
	}

	/**
	 * 退出群组
	 */
	private void outGroup() {
		String userMobile = AppContext.getApplication().getUserInfo()
				.getUserMobile();
		if (TextUtils.isEmpty(userMobile) || TextUtils.isEmpty(groupId))
			return;
		UmengUtil.onEvent(GroupDetailActivity.this, "quit_button_hits");
		LogUtil.printUmengLog("quit_button_hits");
		HttpRequest.getInstance(GroupDetailActivity.this).getOutGroup(
				userMobile, groupId, new CustomAsyncResponehandler() {

					@Override
					public void onSuccess(ResponeModel baseModel) {
						if (baseModel != null && baseModel.isStatus()) {
							loadDate();
							return;
						}
						showToast(getString(R.string.hint_operationError));
					}

					@Override
					public void onFailure(Throwable error, String content) {
						showToast(getString(R.string.hint_operationError));
					}
				});
	}

	/**
	 * 更新群简介
	 */
	private void UpdateGroupIntroduction() {
		if (TextUtils.isEmpty(groupId)) {
			showToast(getString(R.string.hint_operationError));
			return;
		}
		if (TextUtils.isEmpty(presentMode.getGroupIntroduction())) {
			showToast(getString(R.string.hint_inputIntroduction));
			return;
		}
		HttpRequest.getInstance(GroupDetailActivity.this)
				.getUpdateGroupIntroduction(groupId,
						presentMode.getGroupIntroduction(),
						new CustomAsyncResponehandler() {

							@Override
							public void onSuccess(ResponeModel baseModel) {
								if (baseModel != null && baseModel.isStatus()) {
									if (localImgList == null
											|| localImgList.size() <= 0)
										loadDate();
									findViewById(R.id.etGroupIntroduction)
											.setEnabled(false);
									return;
								}
								showToast(getString(R.string.hint_operationError));
							}

							@Override
							public void onFailure(Throwable error,
									String content) {
								showToast(getString(R.string.hint_operationError));
							}
						});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data == null)
			return;
		switch (requestCode) {
		case CuttingPicturesUtil.UPLOAD_PIC:
			System.out.println(data.getData());
			Uri originalUri = data.getData();

			String path = UploadManager.fetchImagePath(this, originalUri);
			if (!TextUtils.isEmpty(path)) {
				UploadManager.addPictures(photoModelList, path);
				if (localImgList == null)
					localImgList = new LinkedList<String>();
				localImgList.add(path);

				adapterAlbum.notifyDataSetChanged();
			}

			break;
		}
	}

	/**
	 * 上传图片
	 */
	public void upLoadPic(String path) {
		System.out.println("上传图片" + path);
		File file = new File(path);
		HttpRequestFactory.getInstance(GroupDetailActivity.this).uploadFile(
				GroupDetailActivity.this, file,
				new CustomAsyncResponehandler() {

					@Override
					public void onStart() {
					}

					@Override
					public void onSuccess(String content) {
						super.onSuccess(content);
					}

					@Override
					public void onFailure(Throwable error, String content) {
					}

					@Override
					public void onFinish() {
					}

					@Override
					public void onSuccess(ResponeModel baseModel) {
						upModel = new UploadModel();
						upModel = JsonUtil.convertJsonToObject(
								baseModel.getJson(), UploadModel.class);
						if (!TextUtils.isEmpty(upModel.getResult()
								.getSmallImageFile()))
							uploadHandler.obtainMessage(GROUP_PHOTO, upModel)
									.sendToTarget();
					}

				});
	}

	/**
	 * 上传结束后的handler处理事件
	 */
	private Handler uploadHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			UploadDataModel upModel = (UploadDataModel) msg.obj;
			if (msg.what == GROUP_PHOTO) {
				/*upModel = (UploadModel) msg.obj;
				if (uploadedImgList == null)
					uploadedImgList = new LinkedList<String>();
				uploadedImgList.add(upModel.getResult().getSmallImageFile());
				if (uploadedImgList.size() < localImgList.size())
					return;
				// 说明上传图片完毕

				String urls = UploadManager.getPhotoPaths(uploadedImgList,
						photoModelList);
				setGroupPhoto(urls);
				localImgList.clear();
				uploadedImgList.clear();*/
				
				upModel = (UploadDataModel) msg.obj;
				if (uploadedImgList == null) {
					uploadedImgList = new LinkedList<String>();
				}

				uploadedImgList.add(upModel.getSmallImageFile());

				if (uploadedImgList.size() < localImgList.size()) {
					PhotoUtil.printTrace("uploaded size is:"
							+ uploadedImgList.size());
					return;// they should be equal
				}

				String urls = UploadManager.getPhotoPaths(uploadedImgList,
						photoModelList);
				System.out.println("添加群组相册的url："+urls);
				setGroupPhoto(urls);
				localImgList.clear();
				uploadedImgList.clear();
				return;
			}
		}
	};
	private ImageView rightArrowMembers;

	/**
	 * 设置群组相册
	 */
	private void setGroupPhoto(String imgUrls) {
		if (TextUtils.isEmpty(groupId)) {
			showToast(getString(R.string.hint_operationError));
			return;
		}
		HttpRequest.getInstance(GroupDetailActivity.this).getGroupUpPhoto(
				groupId, imgUrls, new CustomAsyncResponehandler() {

					@Override
					public void onSuccess(ResponeModel baseModel) {
						if (baseModel != null && baseModel.isStatus())
							loadDate();
					}

					@Override
					public void onFailure(Throwable error, String content) {
						showToast("图片上传失败");
					}
				});
	}

}
