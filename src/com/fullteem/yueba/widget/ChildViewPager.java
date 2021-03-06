package com.fullteem.yueba.widget;

import java.lang.reflect.Field;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月2日&emsp;下午1:59:46
 * @use 自定义ViewPager,可以放在viewpager内部
 */
public class ChildViewPager extends ViewPager {
	// /** 触摸时按下的点 **/
	// private PointF downP = new PointF();
	// /** 触摸时当前的点 **/
	// private PointF curP = new PointF();
	private boolean isTouch = false; // 手是否按下
	// private Context context;
	private GestureDetector mGestureDetector;

	private OnSingleTouchListener onSingleTouchListener;

	public ChildViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		initGestureDetector(context);
	}

	private void initGestureDetector(Context context) {
		mGestureDetector = new GestureDetector(context,
				new OnGestureListener() {
					@Override
					public boolean onSingleTapUp(MotionEvent e) {
						onSingleTouch();
						return true;
					}

					@Override
					public void onShowPress(MotionEvent e) {
					}

					@Override
					public boolean onScroll(MotionEvent e1, MotionEvent e2,
							float distanceX, float distanceY) {
						return false;
					}

					@Override
					public void onLongPress(MotionEvent e) {
					}

					@Override
					public boolean onFling(MotionEvent e1, MotionEvent e2,
							float velocityX, float velocityY) {
						return false;
					}

					@Override
					public boolean onDown(MotionEvent e) {
						isTouch = true;
						return false;
					}
				});
	}

	public ChildViewPager(Context context) {
		this(context, null);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {

		// 当拦截触摸事件到达此位置的时候，返回true，
		// 说明将onTouch拦截在此控件，进而执行此控件的onTouchEvent
		// return true;
		return false;// 响应点击其他控件的事件
	}

	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		mGestureDetector.onTouchEvent(arg0);

		// 每次进行onTouch事件都记录当前的按下的坐标,并且标记isTouch为true停止自动滑动
		// curP.x = arg0.getX();
		// curP.y = arg0.getY();
		// isTouch = true;
		//
		// if (arg0.getAction() == MotionEvent.ACTION_DOWN) {
		//
		// // 记录按下时候的坐标
		// downP.x = arg0.getX();
		// downP.y = arg0.getY();
		//
		// // 通知父ViewPager现在进行的是本控件的操作
		// getParent().requestDisallowInterceptTouchEvent(true);
		// }
		//
		// if (arg0.getAction() == MotionEvent.ACTION_MOVE) {
		// if (getAdapter().getCount() <= 1) {
		// getParent().requestDisallowInterceptTouchEvent(false);
		// return false;
		// } else
		// getParent().requestDisallowInterceptTouchEvent(true);
		// }
		// if (getCurrentItem() == 0) // 第0项右移取消拦截
		// if (downP.x - arg0.getX() < 0) {
		// getParent().requestDisallowInterceptTouchEvent(false);
		// isTouch = false;
		// return false;
		// }
		//
		// if (getCurrentItem() == getAdapter().getCount())// 最后一项左移取消拦截
		// if (downP.x - arg0.getX() > 0) {
		// getParent().requestDisallowInterceptTouchEvent(false);
		// isTouch = false;
		// return false;
		// }
		//
		if (arg0.getAction() == MotionEvent.ACTION_UP) {
			isTouch = false;
		}
		//
		// // 在up时判断是否按下和松手的坐标为一个点, 如果是一个点，将执行点击事件
		// if (downP.x == curP.x && downP.y == curP.y) {
		// onSingleTouch();
		// return true;
		// }
		// 移动范围小于3像素也视为点击事件
		// if (Math.abs(downP.x - curP.x) < DisplayUtils.dp2px(context, 3) ||
		// Math.abs(downP.y - curP.y) < DisplayUtils.dp2px(context, 3)) {
		// onSingleTouch();
		// return true;
		// }
		//
		// }

		return super.onTouchEvent(arg0);
	}

	/**
	 * 单击
	 */
	public void onSingleTouch() {
		if (onSingleTouchListener != null) {
			onSingleTouchListener.onSingleTouch(getCurrentItem());
		}
	}

	/**
	 * 创建点击事件接口
	 * 
	 */
	public interface OnSingleTouchListener {
		public void onSingleTouch(int position);
	}

	public void setOnSingleTouchListener(
			OnSingleTouchListener onSingleTouchListener) {
		this.onSingleTouchListener = onSingleTouchListener;
	}

	@Override
	public void setAdapter(PagerAdapter arg0) {
		super.setAdapter(arg0);
		initAutoSliding();
		// setPageTransformer(true, new ChlidPageTransformer());
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			if (getAdapter().getCount() >= 2) {
				int moveToItem = getCurrentItem() == getAdapter().getCount() - 1 ? 0
						: getCurrentItem() + 1;
				if (!isTouch)
					if (moveToItem == 0
							|| moveToItem == getAdapter().getCount() - 2)
						setCurrentItem(moveToItem, false);
					else
						setCurrentItem(moveToItem);
			}
			initAutoSliding();
		}
	};

	/**
	 * 设置持续时间
	 * 
	 * @param context
	 * @param duration
	 */
	public void setDuration(Context context, int duration) {
		try {
			Field mScroller;
			mScroller = ViewPager.class.getDeclaredField("mScroller");
			mScroller.setAccessible(true);
			Interpolator sInterpolator = new AccelerateDecelerateInterpolator();
			ChildScroller scroller = new ChildScroller(context, sInterpolator);
			scroller.setDuration(duration);
			mScroller.set(ChildViewPager.this, scroller);
		} catch (NoSuchFieldException e) {
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		}
	}

	private void initAutoSliding() {
		// if (getAdapter().getCount() >= 2)
		mHandler.sendEmptyMessageDelayed(0, 3 * 1000);
	}

	// 自定义动画
	private class ChlidPageTransformer implements ViewPager.PageTransformer {
		private static final float MIN_SCALE = 0.75f;

		@SuppressLint("NewApi")
		@Override
		public void transformPage(View view, float position) {
			int pageWidth = view.getWidth();

			if (position < -1) {// [-Infinity,-1))
				view.setAlpha(0);

			} else if (position <= 0) { // [-1,0]
				view.setAlpha(1 - Math.abs(position));
				view.setTranslationX(pageWidth * -position);
				view.setScaleX(MIN_SCALE + (1 - MIN_SCALE)
						* (1 - Math.abs(position)));
				view.setScaleY(1);

			} else if (position <= 1) { // (0,1]
				// if (getCurrentItem() == 0){
				// view.setTranslationX(-pageWidth);
				// view.setAlpha(0);
				// return;
				// }
				view.setAlpha(1 - position);
				view.setTranslationX(pageWidth * -position);
				float scaleFactor = MIN_SCALE + (1 - MIN_SCALE)
						* (1 - Math.abs(position));
				view.setScaleX(scaleFactor);
				view.setScaleY(scaleFactor);

			} else { // (1,+无穷大(Infinity)]
				view.setAlpha(0);
			}
		}
	}

	// 自定义滑动时间
	private class ChildScroller extends Scroller {
		private int mDuration = 500;

		public ChildScroller(Context context) {
			this(context, null);
		}

		public ChildScroller(Context context, Interpolator interpolator) {
			super(context, interpolator);
		}

		public ChildScroller setDuration(int mDuration) {
			this.mDuration = mDuration;
			return this;
		}

		@Override
		public void startScroll(int startX, int startY, int dx, int dy) {
			startScroll(startX, startY, dx, dy, mDuration);
		}

		@Override
		public void startScroll(int startX, int startY, int dx, int dy,
				int duration) {
			super.startScroll(startX, startY, dx, dy, duration <= 0 ? mDuration
					: duration);
		}
	}

}