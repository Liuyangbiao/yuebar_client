package com.fullteem.yueba.net.http;

import com.fullteem.yueba.model.ResponeModel;

public abstract class CustomAsyncResponehandler extends
		AsyncHttpResponseHandler {
	// private CustomAsyncResponehandler customAsyncResponehandler;

	public CustomAsyncResponehandler() {
		super();
		// customAsyncResponehandler = null;
	}

	/**
	 * 不需要进行界面操作的可以使用该构造方法，在请求类里面实现数据操作
	 * 
	 * @param customAsyncResponehandler
	 */
	public CustomAsyncResponehandler(
			CustomAsyncResponehandler customAsyncResponehandler) {
		super();
		// if (customAsyncResponehandler != null)
		// this.customAsyncResponehandler = customAsyncResponehandler;
		// customAsyncResponehandler.onStart();
	}

	// public void onSuccess(ResponeModel baseModel) {
	// System.err.println("任务完成：：：：：" + baseModel);
	// RequestSuccess(baseModel);
	// System.err.println("执行了：：：：：" + baseModel);
	// // if (customAsyncResponehandler != null)
	// // customAsyncResponehandler.onSuccess(baseModel);
	// }
	//
	@Override
	public void onStart() {
		super.onStart();
		// if (customAsyncResponehandler != null)
		// customAsyncResponehandler.onStart();
	}

	@Override
	public void onFinish() {
		super.onFinish();
		// if (customAsyncResponehandler != null)
		// customAsyncResponehandler.onFinish();
	}

	//
	// @Override
	// public void onFailure(Throwable error, String content) {
	// // TODO Auto-generated method stub
	// super.onFailure(error, content);
	// RequestFailure(error, content);
	// // if (customAsyncResponehandler != null)
	// // customAsyncResponehandler.onFailure(error, content);
	// }

	/** 请求开始 */
	// abstract public void onStart();

	/** 请求成功完成 */
	abstract public void onSuccess(ResponeModel baseModel);

	/** 请求出错 */
	@Override
	abstract public void onFailure(Throwable error, String content);

	/** 请求结束 */
	// abstract public void onFinish();

}
