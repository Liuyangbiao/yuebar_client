package com.fullteem.yueba.widget;

import java.lang.reflect.Method;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fullteem.yueba.R;

/**
 * @author jun
 * @version 1.0.0
 * @param <T>
 * @created 2014年12月31日&emsp;下午3:05:29
 * @use 列表形式弹窗，item存放的是范型对象,主要用于弹出下拉选择框一类
 */
public class ListObjectPopupWindow<T> {
	private ListView popupChlidCiew;
	private PopupWindow mPopupWindow;
	private BaseAdapter adapter;
	private Context context;
	private List<T> popupData;// 数据
	private IEventListener mEventListener;// 回调接口
	private int showWidth, showHeight;// popupdown显示的宽高
	private int animationStyle;// popupdown animationStyle
	private Class cls; // 通过反射查找对应方法
	private String contentMethod; // 默认adapter取得传进来范型的一个方法展示在ListView上面
	private View chlidView;

	public ListObjectPopupWindow(Context context, List<T> popupData,
			BaseAdapter adapter) {
		this.context = context;
		this.popupData = popupData;
		this.adapter = adapter;
		if (popupData != null && popupData.size() > 0)
			this.cls = popupData.get(0).getClass();
		initChlidView();
	}

	public ListObjectPopupWindow(Context context, List<T> popupData) {
		this(context, popupData, null);
	}

	private void initChlidView() {
		chlidView = LayoutInflater.from(context).inflate(
				R.layout.popwindow_list, null);
		popupChlidCiew = (ListView) chlidView.findViewById(R.id.lv_popupwdList);
		popupChlidCiew.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (mEventListener != null) {
					mEventListener.onItemClick(popupData.get(position),
							position);
				}
				mPopupWindow.dismiss();
			}
		});
		setAdapter(adapter);
		initPopupWindow();
	}

	/**
	 * 设置popupwindow内部listiew的adapter ，可不设置使用默认的
	 * 
	 * @param adapter
	 */
	public ListObjectPopupWindow setAdapter(BaseAdapter adapter) {
		this.adapter = adapter == null ? new PopupAdapter() : adapter;
		popupChlidCiew.setAdapter(this.adapter);
		return this;
	}

	/**
	 * 设置popupwindow内部listiew的item点击事件
	 * 
	 */
	public ListObjectPopupWindow<T> setEventListener(
			IEventListener eventListener) {
		this.mEventListener = eventListener;
		return this;
	}

	/**
	 * 初始化popupwindow
	 */
	private void initPopupWindow() {
		mPopupWindow = new PopupWindow(
				chlidView,
				showWidth <= 0 ? android.view.ViewGroup.LayoutParams.WRAP_CONTENT
						: showWidth,
				showHeight <= 0 ? android.view.ViewGroup.LayoutParams.WRAP_CONTENT
						: showHeight);
		mPopupWindow
				.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		mPopupWindow.setTouchable(true);
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setFocusable(true);
		mPopupWindow
				.setAnimationStyle(animationStyle <= 0 ? R.style.listpopupwindow_style
						: animationStyle);
	}

	public PopupWindow getPopupWindow() {
		return mPopupWindow;
	}

	/**
	 * 设置popupwindow宽度
	 * 
	 * @param width
	 */
	public ListObjectPopupWindow<T> setWidth(int width) {
		this.showWidth = width;
		mPopupWindow = null;
		initPopupWindow();
		return this;
	}

	/**
	 * 设置popupwindow高度
	 * 
	 * @param height
	 */
	public ListObjectPopupWindow setHeight(int height) {
		this.showHeight = height;
		mPopupWindow = null;
		initPopupWindow();
		return this;
	}

	public ListObjectPopupWindow setAnimationStyle(int animationStyle) {
		this.animationStyle = animationStyle;
		mPopupWindow = null;
		initPopupWindow();
		return this;
	}

	/**
	 * 设置显示在界面的方法名，如public String getName()传getName即可
	 * 
	 * @param contentMethod
	 */
	public ListObjectPopupWindow setShowContentMethod(String contentMethod) {
		this.contentMethod = contentMethod;
		return this;
	}

	/**
	 * 如果使用了没有传入回调接口的listener，使用该方法显示可收到item点击回调
	 * 
	 * @param anchor
	 * @param xoff
	 * @param yoff
	 * @param listrner
	 * @return
	 */
	public ListObjectPopupWindow showAsDropDown(View anchor, int xoff,
			int yoff, IEventListener listrner) {
		if (listrner != null)
			this.mEventListener = listrner;
		mPopupWindow.showAsDropDown(anchor, xoff, yoff);
		return this;
	}

	public ListObjectPopupWindow showAsDropDown(View anchor, int xoff, int yoff) {
		showAsDropDown(anchor, xoff, yoff, null);
		return this;
	}

	public abstract class IEventListener {
		public abstract void onItemClick(T obj, int positon);
	}

	/**
	 * 示例 adatper 只显示文字时可用该默认adapter
	 * 
	 * @author jun
	 */
	private class PopupAdapter extends BaseAdapter {

		@Override
		public int getCount() {

			return popupData.size();
		}

		@Override
		public Object getItem(int position) {
			return popupData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView tv;
			if (convertView == null) {
				tv = new TextView(context);
				convertView = new LinearLayout(context);

				tv.setPadding(4, 5, 4, 5);
				tv.setSingleLine(true);
				tv.setTextColor(0xbf101010);
				tv.setTextSize(14);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
				lp.gravity = Gravity.CENTER;
				tv.setLayoutParams(lp);
				tv.setGravity(Gravity.CENTER);

				((LinearLayout) convertView)
						.setOrientation(LinearLayout.VERTICAL);
				((LinearLayout) convertView).addView(tv);

				convertView.setTag(tv);
			} else
				tv = (TextView) convertView.getTag();
			tv.setText(getMethodResultStr(position));
			return convertView;
		}
	}

	private String getMethodResultStr(int position) {
		if (cls == null)
			return null;

		if (popupData.get(position) instanceof String)
			return (String) popupData.get(position);

		if (TextUtils.isEmpty(contentMethod))
			return null;

		try {
			Method method = cls.getMethod(contentMethod);
			// return "" + method.invoke(cls.get(position).newInstance());
			return "" + method.invoke(popupData.get(position));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void notifyDataSetChange(List<T> list) {
		popupData.clear();
		popupData.addAll(list);
		adapter.notifyDataSetChanged();
	}
}
