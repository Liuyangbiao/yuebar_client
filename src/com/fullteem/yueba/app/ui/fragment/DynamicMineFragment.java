package com.fullteem.yueba.app.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.fullteem.yueba.R;
import com.fullteem.yueba.app.AppContext;
import com.fullteem.yueba.app.adapter.DynamicCommonAdapter;
import com.fullteem.yueba.globle.GlobleConstant;
import com.fullteem.yueba.interfaces.ICommentOnClickListener;
import com.fullteem.yueba.model.BaseListModel;
import com.fullteem.yueba.model.MoodModel;
import com.fullteem.yueba.net.Urls;
import com.fullteem.yueba.net.http.AsyncHttpResponseHandler;
import com.fullteem.yueba.net.http.HttpRequestFactory;
import com.fullteem.yueba.util.AppUtil;
import com.fullteem.yueba.util.StringUtils;
import com.fullteem.yueba.widget.CircleImageView;
import com.fullteem.yueba.widget.ExpandGridView;
import com.fullteem.yueba.widget.XListView;
import com.fullteem.yueba.widget.XListView.IXListViewListener;

public class DynamicMineFragment extends BaseFragment implements
		ICommentOnClickListener {
	// private static DynamicMineFragment dff;
	private View view;
	private List<MoodModel> mineList;
	private XListView xListFriends;
	private AppContext appContext;
	private int pageSize = 10;
	private boolean isRefresh;
	private DynamicCommonAdapter adapter;

	public DynamicMineFragment() {

	}

	// public static Fragment getInstance() {
	// if (dff == null) {
	// dff = new DynamicMineFragment();
	// }
	// return dff;
	// }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_dynamic_friends, null);
		mineList = new ArrayList<MoodModel>();
		xListFriends = (XListView) view.findViewById(R.id.expandListFriends);
		xListFriends
				.setAdapter(adapter = adapter == null ? new DynamicCommonAdapter(
						getActivity(), mineList, 2, this) : adapter);

		appContext = (AppContext) getActivity().getApplication();
		// 发送请求
		getMyDynamicRequest();

		xListFriends.setXListViewListener(new IXListViewListener() {
			@Override
			public void onRefresh() {
				isRefresh = true;
				getMyDynamicRequest();
			}

			@Override
			public void onLoadMore() {
				isRefresh = false;
				getMyDynamicRequest();
			}
		});
		return view;
	}

	// class DynamicFriendAdapter extends BaseListAdapter<MoodModel> {
	//
	// public DynamicFriendAdapter(Activity context, List<MoodModel> list) {
	// super(context, list);
	// }
	//
	// @Override
	// public View getView(int position, View convertView, ViewGroup parent) {
	// ViewHolder vh;
	// if (convertView == null) {
	// convertView = LayoutInflater.from(getActivity()).inflate(
	// R.layout.adapter_dynamic_friends, null);
	// vh = new ViewHolder();
	// vh.tvName = (TextView) convertView.findViewById(R.id.tvName);
	// vh.tvCreateTime = (TextView) convertView
	// .findViewById(R.id.tvCreateTime);
	// vh.edtMood = (TextView) convertView.findViewById(R.id.edtMood);
	// vh.ImgViewHeader = (CircleImageView) convertView
	// .findViewById(R.id.ImgViewHeader);
	// vh.gridView = (ExpandGridView) convertView
	// .findViewById(R.id.exGridview);
	// convertView.setTag(vh);
	// } else {
	// vh = (ViewHolder) convertView.getTag();
	// }
	// vh.tvName.setText(appContext.getUserInfo().getUserName());
	// vh.tvCreateTime.setText(mineList.get(position).getCreateDate());
	// vh.edtMood.setText(mineList.get(position).getMoodRecordText());
	// ImageLoaderUtil.getImageLoader()
	// .displayImage(appContext.getUserInfo().getUserLogoUrl(),
	// vh.ImgViewHeader);
	// String urls = mineList.get(position).getMoodRecordImgUrl();
	// String[] urlsArray;
	// if (urls != null && urls.length() > 0 && urls.contains(",")) {
	// urlsArray = urls.split(",");
	// } else
	// urlsArray = new String[] { urls };
	//
	// List<String> strList = new ArrayList<String>();
	// for (String string : urlsArray) {
	// strList.add(string);
	// }
	// vh.gridView.setAdapter(new GridAdapter(getActivity(), strList));
	// return convertView;
	// }
	// }
	//
	// class GridAdapter extends BaseListAdapter<String> {
	// private List<String> imgUrlsList;
	//
	// public GridAdapter(Activity context, List<String> list) {
	// super(context, list);
	// imgUrlsList = list;
	// }
	//
	// @Override
	// public View getView(int position, View convertView, ViewGroup parent) {
	// ViewHolder vh;
	// if (convertView == null) {
	// vh = new ViewHolder();
	// convertView = LayoutInflater.from(getActivity()).inflate(
	// R.layout.adapter_dynamic_grid, null);
	// vh.imgMyPics = (ImageView) convertView
	// .findViewById(R.id.imgMyPics);
	// convertView.setTag(vh);
	// } else {
	// vh = (ViewHolder) convertView.getTag();
	// }
	// if (imgUrlsList != null)
	// ImageLoaderUtil.getImageLoader().displayImage(
	// imgUrlsList.get(position), vh.imgMyPics);
	//
	// return convertView;
	// }
	//
	// }

	class ViewHolder {
		CircleImageView ImgViewHeader;
		TextView tvName;
		TextView tvCreateTime;
		TextView edtMood;
		ExpandGridView gridView;
		ImageView imgMyPics;
	}

	/**
	 * 获取我的动态
	 */
	private void getMyDynamicRequest() {
		JSONObject job = new JSONObject();

		if (isRefresh) {
			job.put("userId", appContext.getUserInfo().getUserId());
			job.put("pageNumber", 1);
			job.put("pageSize", pageSize);
		} else {
			job.put("userId", appContext.getUserInfo().getUserId());
			job.put("pageNumber", AppUtil.getPageNum(mineList.size(), pageSize));
			job.put("pageSize", pageSize);
		}
		HttpRequestFactory.getInstance().postRequest(Urls.MY_DYNAMIC_MINE, job,
				requestHandler);
	}

	AsyncHttpResponseHandler requestHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onStart() {
			showLoadingDialog();
		};

		@Override
		public void onSuccess(String content) {
			BaseListModel<MoodModel> model = JSON.parseObject(content,
					new TypeReference<BaseListModel<MoodModel>>() {
					});
			if (model != null
					&& model.getCode().equalsIgnoreCase(
							GlobleConstant.REQUEST_SUCCESS)) {
				if (isRefresh) {
					mineList.clear();
				}
				mineList.addAll(model.getResult());
				adapter.notifyDataSetChanged();
				if (!StringUtils.isLoadMoreVisible(mineList.size(), pageSize)) {
					xListFriends.setPullLoadEnable(false);
				}
			}
		};

		@Override
		public void onFailure(Throwable error) {
		};

		@Override
		public void onFinish() {
			xListFriends.stopRefresh();
			xListFriends.stopLoadMore();
			dismissLoadingDialog();
		};
	};

	@Override
	public void reFreshPage() {
		isRefresh = true;
		getMyDynamicRequest();
	}

}
