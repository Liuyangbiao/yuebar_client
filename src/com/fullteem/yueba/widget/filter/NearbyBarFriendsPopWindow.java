package com.fullteem.yueba.widget.filter;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.PopupWindow;

import com.fullteem.yueba.R;
import com.fullteem.yueba.globle.GlobleConstant;
import com.fullteem.yueba.util.SharePreferenceUtil;

public class NearbyBarFriendsPopWindow {
	private final PopupWindow popupWindow;
	private Activity mParent;
	private CheckBox[] checkBoxs;

	public NearbyBarFriendsPopWindow(Activity mParent) {
		this.mParent = mParent;
		View menu = initMenuView(mParent);
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
	private View initMenuView(Activity mParent) {
		View menuView = mParent.getLayoutInflater().inflate(
				R.layout.popwindow_nearby_barfriends, null);
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
						// TODO Auto-generated method stub

					}
				});

		checkBoxs = new CheckBox[] {
				(CheckBox) menuView.findViewById(R.id.checkboxGirl),
				(CheckBox) menuView.findViewById(R.id.checkboxBoy),
				(CheckBox) menuView.findViewById(R.id.checkBoxAll) };

		initCkbs();

		return menuView;
	}

	public void initCkbs() {
		int checkedIndex = SharePreferenceUtil.getInstance(mParent)
				.getIntFromShare(GlobleConstant.BARFRIENDS_FILTER,
						GlobleConstant.BARFRIENDS_FILTER_DEFAULT);
		for (int i = 0; i < checkBoxs.length; i++) {
			if (checkedIndex == i) {
				checkBoxs[i].setChecked(true);
				continue;
			}
			checkBoxs[i].setChecked(false);
		}
	}

	// @Override
	// public void onClick(View view) {
	// switch (view.getId()) {
	// // 取消
	// case R.id.cancleBtn:
	//
	// //强制更新情况下若点击取消更新则退出客户端
	// if(GlobleVariable.isForceUpdate){
	// System.exit(0);
	// }
	// if (popupWindow != null && popupWindow.isShowing()) {
	// popupWindow.dismiss();
	// }
	// break;
	//
	// // 升级
	// case R.id.updateBtn:
	// UpdateManager manager = new UpdateManager(mParent);
	// // 检查软件更新
	// manager.checkUpdate();
	// break;
	//
	// }
	// }

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
					SharePreferenceUtil.getInstance(mParent).saveIntToShare(
							GlobleConstant.BARFRIENDS_FILTER, i);
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