package com.fullteem.yueba.app.ui.fragment;

import android.support.v4.app.Fragment;

import com.fullteem.yueba.app.ui.BaseActivity;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年11月26日&emsp;下午3:48:46
 * @use 类说明
 */
public abstract class BaseFragment extends Fragment {
	public BaseActivity activity;

	@Override
	public void onResume() {
		super.onResume();
		activity = (BaseActivity) getActivity();
	}

	public void showLoadingDialog() {
		if (activity != null) {
			activity.showLoadingDialog();
		}
	}

	public void dismissLoadingDialog() {
		if (activity != null) {
			activity.dismissLoadingDialog();
		}
	}

	public boolean isLoadingDialogShowing() {
		if (activity != null) {
			return activity.isLoadingDialogShowing();
		}
		return false;
	}

	public void showToast(String str) {
		if (activity != null) {
			activity.showToast(str);
		}
	}

}
