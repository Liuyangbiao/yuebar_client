package com.fullteem.yueba.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fullteem.yueba.R;
import com.fullteem.yueba.util.DisplayUtils;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年11月27日&emsp;下午2:04:38
 * @use 顶部标题栏|菜单栏
 */
public class TopTitleView extends LinearLayout {
	private LinearLayout ll_title_left;
	public LinearLayout ll_title_right;
	private LinearLayout ll_title_center;
	private View view;
	private Context context;
	private Button statuBtn;
	private TextView rightText;

	public TopTitleView(Context context) {
		this(context, null);
	}

	public TopTitleView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	@SuppressLint("NewApi")
	public TopTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;
		initView(context);
	}

	private void initView(Context context) {
		view = View.inflate(context, R.layout.top_bar, null);
		ll_title_left = (LinearLayout) view.findViewById(R.id.ll_title_left);
		ll_title_right = ((LinearLayout) view.findViewById(R.id.ll_title_right));
		ll_title_center = (LinearLayout) view
				.findViewById(R.id.ll_title_center);
		LayoutParams lp = new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		lp.gravity = Gravity.CENTER_VERTICAL;
		addView(view, lp);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		// 固定高度为屏幕1/8
		setMeasuredDimension(DisplayUtils.getScreenWidht(context),
				DisplayUtils.getScreenWidht(context) * 3 / 19);

		int childWidthSize = getMeasuredWidth();
		int childHeightSize = getMeasuredHeight();

		widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize,
				MeasureSpec.EXACTLY);
		heightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeightSize,
				MeasureSpec.EXACTLY);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	/**
	 * 添加视图到左边
	 * 
	 * @param view
	 *            任意视图
	 */
	public void addViewToLeft(View view) {
		ll_title_left.addView(view);
	}

	/**
	 * 添加视图到又边
	 * 
	 * @param view
	 *            任意视图
	 */
	public void addViewToRight(View view) {
		ll_title_right.addView(view);
	}

	/**
	 * 添加视图到中间
	 * 
	 * @param view
	 *            任意视图
	 */
	public void addViewToCenter(View view) {
		ll_title_center.addView(view);
	}

	/**
	 * 显示标题栏左边
	 * 
	 * @param showContent
	 *            显示内容
	 * @param styleResid
	 *            style资源id，不需自定义传null即可
	 */
	public void showLeftText(String showContent, Integer styleResid) {
		TextView tvView = new TextView(context);

		// 设置textview样式
		tvView.setTextAppearance(context,
				styleResid == null ? R.style.toptitle_left : styleResid);
		tvView.setText(showContent == null ? "left" : showContent);
		ll_title_left.addView(tvView);
	}

	/**
	 * 显示标题栏左边同时设定点击事件
	 * 
	 * @param showContent
	 *            显示内容
	 * @param styleResid
	 *            style资源id，不需自定义传null即可
	 * @param clickListener
	 *            点击事件监听，不需要的话请使用showLeftText(String showContent, Integer
	 *            styleResid) 这个方法
	 */
	public void showLeftText(String showContent, Integer styleResid,
			OnClickListener clickListener) {
		TextView tvView = new TextView(context);
		tvView.setTextAppearance(context,
				styleResid == null ? R.style.toptitle_left : styleResid);
		tvView.setText(showContent == null ? "left" : showContent);
		if (clickListener != null) {
			tvView.setOnClickListener(clickListener);
		}
		ll_title_left.addView(tvView);
	}

	/**
	 * 显示标题栏右边
	 * 
	 * @param showContent
	 *            显示内容
	 * @param styleResid
	 *            style资源id，不需自定义传null即可
	 */
	public void showRightText(String showContent, Integer styleResid) {
		TextView tvView = new TextView(context);
		tvView.setTextAppearance(context,
				styleResid == null ? R.style.toptitle_right : styleResid);
		tvView.setText(showContent == null ? "right" : showContent);
		ll_title_right.addView(tvView);
	}

	/**
	 * 显示标题栏右边同时设定点击事件
	 * 
	 * @param showContent
	 *            显示内容
	 * @param styleResid
	 *            style资源id，不需自定义传null即可
	 * @param clickListener
	 *            点击事件监听，不需要的话请使用showRightText(String showContent, Integer
	 *            styleResid) 这个方法
	 */
	public void showRightText(String showContent, Integer styleResid,
			OnClickListener clickListener) {
		rightText = new TextView(context);
		rightText.setTextAppearance(context,
				styleResid == null ? R.style.toptitle_right : styleResid);
		rightText.setText(showContent == null ? "right" : showContent);
		if (clickListener != null) {
			rightText.setOnClickListener(clickListener);
		}
		ll_title_right.addView(rightText);
	}

	public void updateRightText(int resid) {
		if (rightText != null) {
			rightText.setText(resid);
		}
	}

	/**
	 * 显示标题栏中间
	 * 
	 * @param showContent
	 *            显示内容
	 * @param styleResid
	 *            style资源id，不需自定义传null即可
	 */
	public void showCenterText(String showContent, Integer styleResid) {
		TextView tvView = new TextView(context);
		tvView.setTextAppearance(context,
				styleResid == null ? R.style.toptitle_center : styleResid);
		tvView.setText(showContent == null ? "center" : showContent);
		// 保证只有一个子view
		if (ll_title_center.getChildCount() > 0) {
			ll_title_center.removeAllViews();
		}
		ll_title_center.addView(tvView);
	}

	/**
	 * 显示标题栏中间同时设定点击事件
	 * 
	 * @param showContent
	 *            显示内容
	 * @param styleResid
	 *            style资源id，不需自定义传null即可
	 * @param clickListener
	 *            点击事件监听，不需要的话请使用showCenterText(String showContent, Integer
	 *            styleResid) 这个方法
	 */
	public void showCenterText(String showContent, Integer styleResid,
			OnClickListener clickListener) {
		TextView tvView = new TextView(context);
		tvView.setTextAppearance(context,
				styleResid == null ? R.style.toptitle_center : styleResid);
		tvView.setText(showContent == null ? "center" : showContent);
		if (clickListener != null) {
			tvView.setOnClickListener(clickListener);
		}
		ll_title_center.addView(tvView);
	}

	/**
	 * 标题栏左边按钮
	 * 
	 * @param showContent
	 *            显示内容
	 * @param styleResid
	 *            显示内容style资源id，不需自定义传null即可
	 * @param bgResId
	 *            按钮背景图片,可传selector，不需自定义传null
	 * @param clickListener
	 *            点击事件监听
	 */
	public void showLeftButton(String showContent, Integer styleResid,
			Integer bgResId, OnClickListener clickListener) {
		Button btn = new Button(context);
		btn.setTextAppearance(context,
				styleResid == null ? R.style.toptitle_left : styleResid);
		btn.setText(showContent == null ? "left" : showContent);
		btn.setBackgroundResource(bgResId == null ? R.drawable.btn_toptitle_default_selector
				: bgResId);
		if (clickListener != null) {
			btn.setOnClickListener(clickListener);
		}
		ll_title_left.addView(btn);
	}

	/**
	 * 标题栏右边按钮
	 * 
	 * @param showContent
	 *            显示内容
	 * @param styleResid
	 *            显示内容style资源id，不需自定义传null即可
	 * @param bgResId
	 *            按钮背景图片,可传selector，不需自定义传null
	 * @param clickListener
	 *            点击事件监听
	 */
	public void showRightButton(String showContent, Integer styleResid,
			Integer bgResId, OnClickListener clickListener) {
		Button btn = new Button(context);
		btn.setTextAppearance(context,
				styleResid == null ? R.style.toptitle_right : styleResid);
		btn.setText(showContent == null ? "right" : showContent);
		btn.setBackgroundResource(bgResId == null ? R.drawable.btn_toptitle_default_selector
				: bgResId);
		if (clickListener != null) {
			btn.setOnClickListener(clickListener);
		}
		ll_title_right.addView(btn);
	}

	/**
	 * 标题栏中间按钮
	 * 
	 * @param showContent
	 *            显示内容
	 * @param styleResid
	 *            显示内容style资源id，不需自定义传null即可
	 * @param bgResId
	 *            按钮背景图片,可传selector，不需自定义传null
	 * @param clickListener
	 *            点击事件监听
	 */
	public void showCenterButton(String showContent, Integer styleResid,
			Integer bgResId, OnClickListener clickListener) {
		Button btn = new Button(context);
		btn.setTextAppearance(context,
				styleResid == null ? R.style.toptitle_center : styleResid);
		btn.setText(showContent == null ? "left" : showContent);
		btn.setBackgroundResource(bgResId == null ? R.drawable.btn_toptitle_default_selector
				: bgResId);
		if (clickListener != null) {
			btn.setOnClickListener(clickListener);
		}
		ll_title_center.addView(btn);
	}

	/**
	 * 标题栏左边显示图片
	 * 
	 * @param resId
	 *            图片资源id
	 * @param clickListener
	 *            点击事件监听
	 */
	public void showLeftImag(int resId, OnClickListener clickListener) {
		ImageView iv = new ImageView(context);
		iv.setScaleType(ScaleType.CENTER);
		iv.setImageResource(resId);
		if (clickListener != null) {
			iv.setOnClickListener(clickListener);
		}
		ll_title_left.addView(iv);
	}

	/**
	 * 标题栏右边显示图片
	 * 
	 * @param resId
	 *            图片资源id
	 * @param clickListener
	 *            点击事件监听
	 */
	public void showRightImag(int resId, OnClickListener clickListener) {
		ImageView iv = new ImageView(context);
		iv.setScaleType(ScaleType.CENTER);
		iv.setImageResource(resId);
		if (clickListener != null) {
			iv.setOnClickListener(clickListener);
		}
		ll_title_right.addView(iv);
	}

	/**
	 * 标题栏中间显示图片
	 * 
	 * @param resId
	 *            图片资源id
	 * @param clickListener
	 *            点击事件监听
	 */
	public void showCenterImag(int resId, OnClickListener clickListener) {
		ImageView iv = new ImageView(context);
		iv.setScaleType(ScaleType.CENTER);
		iv.setImageResource(resId);
		if (clickListener != null) {
			iv.setOnClickListener(clickListener);
		}
		ll_title_center.addView(iv);
	}

	/**
	 * 标题栏左边显示图片
	 * 
	 * @param bmp
	 *            位图资源
	 * @param clickListener
	 *            点击事件监听
	 */
	public void showLeftImag(Bitmap bmp, OnClickListener clickListener) {
		ImageView iv = new ImageView(context);
		iv.setScaleType(ScaleType.CENTER);
		iv.setImageBitmap(bmp);
		if (clickListener != null) {
			iv.setOnClickListener(clickListener);
		}
		ll_title_left.addView(iv);
	}

	/**
	 * 标题栏右边显示图片
	 * 
	 * @param bmp
	 *            位图资源
	 * @param clickListener
	 *            点击事件监听
	 */
	public void showRightImag(Bitmap bmp, OnClickListener clickListener) {
		ImageView iv = new ImageView(context);
		iv.setScaleType(ScaleType.CENTER);
		iv.setImageBitmap(bmp);
		if (clickListener != null) {
			iv.setOnClickListener(clickListener);
		}
		ll_title_right.addView(iv);
	}

	/**
	 * 标题栏中间显示图片
	 * 
	 * @param bmp
	 *            位图资源
	 * @param clickListener
	 *            点击事件监听
	 */
	public void showCenterImag(Bitmap bmp, OnClickListener clickListener) {
		ImageView iv = new ImageView(context);
		iv.setScaleType(ScaleType.CENTER);
		iv.setImageBitmap(bmp);
		if (clickListener != null) {
			iv.setOnClickListener(clickListener);
		}
		ll_title_center.addView(iv);
	}

	/**
	 * 移除左边所有view
	 */
	public void rmLeftAll() {
		if (ll_title_left.getChildCount() > 0) {
			ll_title_left.removeAllViews();
		}
	}

	/**
	 * 移除右边所有view
	 */
	public void rmRightAll() {
		if (ll_title_right.getChildCount() > 0) {
			ll_title_right.removeAllViews();
		}
	}

	/**
	 * 移除中间所有view
	 */
	public void rmCenterAll() {
		if (ll_title_center.getChildCount() > 0) {
			ll_title_center.removeAllViews();
		}
	}

	/*
	 * 移除所以View 标题栏三个线性布局不移除，请调用该方法
	 */
	public void rmAllViews() {
		rmCenterAll();
		rmLeftAll();
		rmRightAll();
	}

	/**
	 * 用户右侧可编辑按钮
	 * 
	 * @param resId
	 * @param onclickListener
	 */
	public void editPage(int resId, OnClickListener onclickListener) {
		statuBtn = new Button(context);
		LinearLayout.LayoutParams ll;
		ll = new LinearLayout.LayoutParams(DisplayUtils.dp2px(context, 25),
				DisplayUtils.dp2px(context, 25));
		if (resId != 0) {
			statuBtn.setBackgroundResource(resId);
		} else {
			statuBtn.setText("保存");
		}
		statuBtn.setLayoutParams(ll);
		statuBtn.setOnClickListener(onclickListener);
		ll_title_right.addView(statuBtn);
	}

	/**
	 * 必须在editpage方法之后
	 * 
	 * @return 编辑按钮
	 */
	public Button getEditBtn() {
		return statuBtn;
	}
}
