package com.fullteem.yueba.entry;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

import com.fullteem.yueba.R;
import com.fullteem.yueba.app.ui.BaseActivity;
import com.fullteem.yueba.util.SharePreferenceUtil;

public class GuideActivity extends BaseActivity implements OnClickListener {

	private List<ImageView> imageViewList;
	private LinearLayout llPointGroup; // 点的组
	private View redPointView; // 红色的点
	private int basicWidth;// 点的间距
	private Button btnStartExperience; // 开始体验
	private ViewPager mViewPager;

	public GuideActivity() {
		super(R.layout.activity_guide);
	}

	@Override
	public void initViews() {
		mViewPager = (ViewPager) findViewById(R.id.vp_guide);
		btnStartExperience = (Button) findViewById(R.id.btn_start_experience);
		llPointGroup = (LinearLayout) findViewById(R.id.ll_guide_point_group);
		redPointView = findViewById(R.id.view_guide_red_point);

	}

	@Override
	public void initData() {

		int[] imageResIDs = { R.drawable.guide_date, R.drawable.guide_pub,
				R.drawable.guide_singer };

		imageViewList = new ArrayList<ImageView>();
		ImageView iv;
		for (int i = 0; i < imageResIDs.length; i++) {
			iv = new ImageView(this);
			iv.setBackgroundResource(imageResIDs[i]);
			imageViewList.add(iv);

			// 向线性布局中添加一个灰色的点
			View view = new View(this);
			view.setBackgroundResource(R.drawable.point_normal);
			LayoutParams params = new LayoutParams(10, 10);
			if (i != 0) {
				params.leftMargin = 10;
			}
			view.setLayoutParams(params);
			llPointGroup.addView(view);
		}

		GuidePagerAdapter mAdapter = new GuidePagerAdapter();
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());

		// measure -> layout -> draw

		// 获得视图树观察者, 观察当整个布局的layout时的事件
		redPointView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					/**
					 * 当全局开始布局layout时回调此方法
					 */
					@Override
					public void onGlobalLayout() {
						// 此方法只需要执行一次就可以: 把当前的监听事件从视图树中移除掉, 以后就不会在回调此事件了.
						redPointView.getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);

						// 点的间距 = 第1个点的左边 - 第0个点的左边;
						basicWidth = llPointGroup.getChildAt(1).getLeft()
								- llPointGroup.getChildAt(0).getLeft();
						//System.out.println("点的间距: " + basicWidth);
					}
				});

	}

	@Override
	public void bindViews() {
		btnStartExperience.setOnClickListener(this);
	}

	class GuidePagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return imageViewList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// 向ViewPager中添加一个ImageView
			ImageView iv = imageViewList.get(position);
			container.addView(iv);

			// 把添加的ImageView返回回去
			return iv;
		}
	}

	class MyOnPageChangeListener implements OnPageChangeListener {

		/**
		 * 当页面滚动时触发此方法
		 * 
		 * @param position
		 *            当前正在被选中的position
		 * @param positionOffset
		 *            页面移动的比值
		 * @param positionOffsetPixels
		 *            页面移动的像素
		 */
		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {

			// 点最终移动的距离 = 间距 * 比值;
			int leftMargin = (int) (basicWidth * (position + positionOffset));
			// System.out.println("红色的点移动的距离: " + leftMargin + ", 当前页面的索引: " +
			// position + ", 比值: " + positionOffset);

			RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) redPointView
					.getLayoutParams();
			lp.leftMargin = leftMargin;
			redPointView.setLayoutParams(lp);
		}

		/**
		 * 当页面选中时触发此方法
		 */
		@Override
		public void onPageSelected(int position) {
			if (position == imageViewList.size() - 1) {
				btnStartExperience.setVisibility(View.VISIBLE);
			} else {
				btnStartExperience.setVisibility(View.GONE);
			}
		}

		/**
		 * 当页面滚动状态改变时触发此方法
		 */
		@Override
		public void onPageScrollStateChanged(int state) {

		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		SharePreferenceUtil.getInstance(this).saveBooleanToShare("open", true);
		startActivity(new Intent(this, LoginActivity.class));
		finish();
	}
}