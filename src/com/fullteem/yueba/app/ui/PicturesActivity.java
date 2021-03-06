package com.fullteem.yueba.app.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView.ScaleType;

import com.fullteem.yueba.R;
import com.fullteem.yueba.model.AlbumPhotoModel;
import com.fullteem.yueba.util.DisplayUtils;
import com.fullteem.yueba.util.ImageLoaderUtil;
import com.fullteem.yueba.widget.SmoothImageView;

/**
 * 图片浏览页面
 * 
 * @author ssy
 * @param <T>
 * 
 */
public class PicturesActivity<T> extends Activity {
	private int locationX;
	private int locationY;
	private String imgUrl;
	private SmoothImageView smoothImg;
	private int mWidth;
	private int mHeight;
	/** ========================================================= */
	private List<T> listAlbumUrl;
	private ViewPager viewpager;
	private MyPagerAdapter pagerAdapter;
	private List<SmoothImageView> imgList;
	private int position;
	private boolean isEditStatu;

	/** ========================================================= */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pictures);
		Intent intent = getIntent();
		/** ========================================================= */
		// Bundle b = intent.getBundleExtra("bundle");
		// listAlbumUrl = (List<AlbumPhotoModel>) b.getSerializable("list");
		listAlbumUrl = (List<T>) intent.getSerializableExtra("list");
		position = intent.getExtras().getInt("position", 0);
		/** ========================================================= */
		locationX = intent.getExtras().getInt("locationX");
		locationY = intent.getExtras().getInt("locationY");
		mWidth = getIntent().getIntExtra("width", 0);
		mHeight = getIntent().getIntExtra("height", 0);
		imgUrl = intent.getExtras().getString("images");

		/*
		 * smoothImg = new SmoothImageView(this);
		 * smoothImg.setOriginalInfo(mWidth, mHeight, locationX, locationY);
		 * smoothImg.transformIn(); smoothImg.setLayoutParams(new
		 * ViewGroup.LayoutParams(-1, -1));
		 * smoothImg.setScaleType(ScaleType.FIT_CENTER); // 展示图片
		 * ImageLoaderUtil.getImageLoader().displayImage(
		 * DisplayUtils.getAbsolutelyUrl(imgUrl), smoothImg);
		 * smoothImg.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { smoothImg.transformOut();
		 * quitHandler.sendEmptyMessageDelayed(0, 500); } });
		 */

		/** ========================================================= */
		imgList = new ArrayList<SmoothImageView>();

		if (listAlbumUrl != null && listAlbumUrl.size() > 0) {


			// 若list长度小于等于8说明最后一个是添加按钮，不展示
			String photoUrl = null;
			T t = listAlbumUrl.get(0);

			if (t instanceof AlbumPhotoModel) {

				if (null != ((AlbumPhotoModel) listAlbumUrl.get(listAlbumUrl
						.size() - 1)).getPhotoUrl()) {
					photoUrl = ((AlbumPhotoModel) listAlbumUrl.get(listAlbumUrl
							.size() - 1)).getPhotoUrl();
				}

				if (null == photoUrl && listAlbumUrl.size() <= 8) {
					listAlbumUrl.remove(listAlbumUrl.size() - 1);
				}
			}

			for (int i = 0; i < listAlbumUrl.size(); i++) {
				// 显示点击大图片的imageView
				smoothImg = new SmoothImageView(this);
				// 展示图片
				if (t instanceof AlbumPhotoModel) {
					ImageLoaderUtil
							.getImageLoader()
							.displayImage(
									DisplayUtils.getAbsolutelyUrl(((AlbumPhotoModel) listAlbumUrl
											.get(i)).getPhotoUrl()), smoothImg);
				} else if (t instanceof String) {
					ImageLoaderUtil
							.getImageLoader()
							.displayImage(
									DisplayUtils.getAbsolutelyUrl(((String) listAlbumUrl
											.get(i))), smoothImg);
				}
				smoothImg
						.setOriginalInfo(mWidth, mHeight, locationX, locationY);
				smoothImg.transformIn();
				smoothImg.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
				smoothImg.setScaleType(ScaleType.FIT_CENTER);
				smoothImg.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						smoothImg.transformOut();
						quitHandler.sendEmptyMessageDelayed(0, 500);
					}
				});
				// if (position == i) {
				// 添加到保存SmoothImageView的list中，用pagerView显示数据
				imgList.add(smoothImg);
				// } else {
				// SmoothImageView iv = new SmoothImageView(this);
				// System.out.println("imgUrl: "+listAlbumUrl.get(i).getPhotoUrl());
				// ImageLoaderUtil.getImageLoader().displayImage(listAlbumUrl.get(i).getPhotoUrl(),
				// iv);
				// imgList.add(iv);
				// }
			}
		}

		viewpager = (ViewPager) findViewById(R.id.viewpager);
		pagerAdapter = new MyPagerAdapter();
		viewpager.setAdapter(pagerAdapter);
		viewpager.setCurrentItem(position);
		/** ========================================================= */
	}

	@Override
	public boolean onKeyDown(int keycode, KeyEvent keyevent) {
		if (smoothImg == null) {
			return super.onKeyDown(keycode, keyevent);
		}
		if (keycode == KeyEvent.KEYCODE_BACK) {
			smoothImg.transformOut();
			quitHandler.sendEmptyMessageDelayed(0, 500);

		}
		return true;
		// super.onKeyDown(keycode, keyevent);
	}

	/**
	 * 退出handler
	 */
	Handler quitHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			finish();
			overridePendingTransition(0, 0);
		};
	};

	/** ========================================================= */

	class MyPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return imgList.size();
		}

		@Override
		public boolean isViewFromObject(View container, Object arg1) {
			return container == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(imgList.get(position));
			return imgList.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(imgList.get(position));
		}
	}
	/** ========================================================= */
}
