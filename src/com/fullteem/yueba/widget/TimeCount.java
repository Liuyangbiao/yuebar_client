package com.fullteem.yueba.widget;

import android.os.CountDownTimer;

public class TimeCount extends CountDownTimer {
	public TimeCount(long millisInFuture, long countDownInterval) {
		super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
	}

	@Override
	public void onFinish() {// 计时完毕时触发
		// checking.setText("重新验证");
		// checking.setClickable(true);
	}

	@Override
	public void onTick(long millisUntilFinished) {// 计时过程显示
		// checking.setClickable(false);
		// checking.setText(millisUntilFinished / 1000 + "秒");
	}
}