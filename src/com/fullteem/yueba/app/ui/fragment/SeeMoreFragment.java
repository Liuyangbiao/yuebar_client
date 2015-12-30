package com.fullteem.yueba.app.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;

import com.fullteem.yueba.R;
import com.fullteem.yueba.app.ui.BaseActivity;
import com.fullteem.yueba.util.UmengUtil;
import com.fullteem.yueba.widget.EmptyView;
import com.fullteem.yueba.widget.TopTitleView;
import com.fullteem.yueba.widget.XListView;
import com.fullteem.yueba.widget.XListView.IXListViewListener;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月9日&emsp;下午5:28:36
 * @use 查看更多封装类
 */
@SuppressLint("ValidFragment")
public class SeeMoreFragment<T> extends Fragment {
	private String topTitle;
	private XListView lvSeeMore;
	private View loadView;
	private BaseAdapter adapter;
	private OnItemClickListener itemClickListener;
	private IIXListViewEventListener xListViewListener;
	private boolean isInited;// 初始化完毕

	/**
	 * 宿主Activity必须继承于BaseActivity
	 * 
	 * @param topTitle
	 *            显示的标题，默认显示查看更多
	 * @param listDate
	 *            list数据
	 * @param adapter
	 *            listview的adapter
	 * @param itemClickListener
	 *            item事件
	 * @param xListViewListener
	 *            xlistview监听
	 */
	public SeeMoreFragment(String topTitle, BaseAdapter adapter,
			OnItemClickListener itemClickListener,
			IIXListViewEventListener xListViewListener) {
		this.topTitle = topTitle;
		this.adapter = adapter;
		this.itemClickListener = itemClickListener;
		this.xListViewListener = xListViewListener;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_see_more, container,
				false);
		initViews(rootView);
		initData();
		bindViews();
		isInited = true;
		return rootView;
	}

	private void initViews(View rootView) {
		// 防止你呢个点击到activity的东西
		rootView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});

		// for title
		TopTitleView topTitle = (TopTitleView) rootView
				.findViewById(R.id.top_title);
		topTitle.showLeftImag(R.drawable.back, new OnClickListener() {
			@Override
			public void onClick(View v) {
				((BaseActivity) getActivity()).getSupportFragmentManager()
						.popBackStack();
			}
		});
		topTitle.showCenterText(
				TextUtils.isEmpty(this.topTitle) ? getString(R.string.xlistview_footer_hint_normal)
						: this.topTitle, null);

		// for body
		lvSeeMore = (XListView) rootView.findViewById(R.id.lv_seeMore);
		EmptyView emptyView = new EmptyView(getActivity());
		emptyView.setVisibility(View.GONE);
		((ViewGroup) lvSeeMore.getParent()).addView(emptyView);
		loadView = rootView.findViewById(R.id.lvLoading);
	}

	private void initData() {
		if (xListViewListener != null)
			xListViewListener.onFirstLoad(loadView, lvSeeMore);
		if (adapter != null)
			lvSeeMore.setAdapter(adapter);
		lvSeeMore.setVisibility(View.VISIBLE);
		loadView.setVisibility(View.GONE);
	}

	private void bindViews() {
		if (itemClickListener != null)
			lvSeeMore.setOnItemClickListener(itemClickListener);
		if (xListViewListener != null)
			lvSeeMore.setXListViewListener(new IXListViewListener() {
				@Override
				public void onRefresh() {
					xListViewListener.onRefresh(lvSeeMore, adapter);
				}

				@Override
				public void onLoadMore() {
					xListViewListener.onLoadMore(lvSeeMore, adapter);
				}
			});
	}

	public boolean isInited() {
		return isInited;
	}

	public interface IIXListViewEventListener {

		void onFirstLoad(View loadView, XListView view);

		void onRefresh(XListView view, BaseAdapter adapter);

		void onLoadMore(XListView view, BaseAdapter adapter);
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
