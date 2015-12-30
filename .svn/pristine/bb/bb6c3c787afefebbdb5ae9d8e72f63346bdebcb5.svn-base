package com.fullteem.yueba.engine.pay;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.fullteem.yueba.engine.pay.PayStateMachine.PayError;
import com.fullteem.yueba.engine.pay.PayStateMachine.PayState;
import com.fullteem.yueba.util.LogUtil;
import com.fullteem.yueba.util.UmengUtil;

public class AliPayment implements IPayment {

	private static final int SDK_PAY_FLAG = 1;
	private static final int SDK_CHECK_FLAG = 2;

	private Context context = null;
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			LogUtil.printPayLog("handle ali payment message");

			switch (msg.what) {
			case SDK_PAY_FLAG: {
				PayResult resultObj = new PayResult((String) msg.obj);
				String status = resultObj.getStatus();
				if("支付成功" == status){
					UmengUtil.onEvent(context, "payment_successful_return_hits");
					LogUtil.printUmengLog("payment_successful_return_hits");
				}
				Toast.makeText(context, status, Toast.LENGTH_SHORT).show();
				LogUtil.printPayLog("pay result status:" + status);

				if (resultObj.checkPayState()) {
					PayStateMachine.getInstance().changeState(
							PayState.PAY_SUCCEED);
				} else {
					PayStateMachine.getInstance().reportError(
							PayError.PAY_FAIL, "failed to pay:" + status);
				}

				break;
			}
			case SDK_CHECK_FLAG: {
				Toast.makeText(context, "检查结果为：" + msg.obj, Toast.LENGTH_SHORT)
						.show();
				break;
			}
			default:
				break;
			}
		};
	};

	public AliPayment(Context context) {
		this.context = context;
	}

	@Override
	public void pay(final IOrder order) {
		LogUtil.printPayLog("AliPayment: start pay order");

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				LogUtil.printPayLog("AliPayment: start thread to execute pay");
				PayTask alipay = new PayTask((Activity) context);
				// 调用支付接口
				String result = alipay.pay(order.getOrder());
				LogUtil.printPayLog("AliPayment: after executed pay");

				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	/**
	 * check whether the device has authentication alipay account.
	 * 
	 */
	@Override
	public void checkAccount() {
		LogUtil.printPayLog("enter check");

		Runnable checkRunnable = new Runnable() {

			@Override
			public void run() {
				LogUtil.printPayLog("start thread to execute check");

				PayTask payTask = new PayTask((Activity) context);
				boolean isExist = payTask.checkAccountIfExist();

				Message msg = new Message();
				msg.what = SDK_CHECK_FLAG;
				msg.obj = isExist;
				mHandler.sendMessage(msg);
			}
		};

		Thread checkThread = new Thread(checkRunnable);
		checkThread.start();

	}

}
