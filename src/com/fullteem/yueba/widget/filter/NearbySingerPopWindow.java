package com.fullteem.yueba.widget.filter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.PopupWindow;

import com.fullteem.yueba.R;
import com.fullteem.yueba.globle.GlobleConstant;
import com.fullteem.yueba.util.SharePreferenceUtil;

public class NearbySingerPopWindow {
	private final PopupWindow popupWindow;
	private Context mContext;
	private CheckBox[] checkBoxs;

	public NearbySingerPopWindow(Context mParent) {
		this.mContext = mParent;
		View menu = initMenuView(mContext);
		popupWindow = new PopupWindow(menu);
		popupWindow.setWidth(LayoutParams.MATCH_PARENT);
		popupWindow.setHeight(LayoutParams.MATCH_PARENT);
		popupWindow.setAnimationStyle(R.style.PopupAnimation);
	}

	/**
	 * 设置具体Menu按钮的监听
	 * 
	 * @param mParent
	 * @return
	 */
	private View initMenuView(Context mContext) {
		View menuView = View.inflate(mContext,
				R.layout.popwindow_nearby_singer, null);
		menuView.findViewById(R.id.container_main).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						popupWindow.dismiss();
					}
				});
		menuView.findViewById(R.id.container_01).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
					}
				});

		checkBoxs = new CheckBox[] {
				(CheckBox) menuView.findViewById(R.id.ckbOnlyGirl),
				(CheckBox) menuView.findViewById(R.id.ckbOnlyBoy),
				(CheckBox) menuView.findViewById(R.id.ckbSingerAll) };

		initCkbs();

		return menuView;
	}

	public void initCkbs() {
		int checkedIndex = SharePreferenceUtil.getInstance(mContext)
				.getIntFromShare(GlobleConstant.SINGER_FILTER,
						GlobleConstant.SINGER_FILTER_DEFAULT);
		for (int i = 0; i < checkBoxs.length; i++) {
			if (checkedIndex == i) {
				checkBoxs[i].setChecked(true);
				continue;
			}
			checkBoxs[i].setChecked(false);
		}
	}

	/**
	 * 返回View
	 * 
	 * @return
	 */
	public PopupWindow getMenu() {
		return popupWindow;
	}

	/**
	 * 设置checkbox的事件监听，分别左-右，上到下顺序
	 */
	public void setOnCkbEventListener(OnCkbEventListener... clickListeners) {
		for (int i = 0; i < clickListeners.length; i++) {
			if (i >= checkBoxs.length)
				return;
			checkBoxs[i].setOnClickListener(clickListeners[i]);
		}
	}

	public abstract class OnCkbEventListener implements OnClickListener {
		public OnCkbEventListener() {

		}

		public void onCheckedChanged(View v) {
			int checkedId = v.getId();
			for (int i = 0; i < checkBoxs.length; i++) {
				if (checkedId == checkBoxs[i].getId()) {
					checkBoxs[i].setChecked(true);
					SharePreferenceUtil.getInstance(mContext).saveIntToShare(
							GlobleConstant.SINGER_FILTER, i);
					continue;
				}
				checkBoxs[i].setChecked(false);
			}
			if (getMenu().isShowing())
				getMenu().dismiss();
		};

		@Override
		public void onClick(View v) {
			onCheckedChanged(v);
		}
	}
}