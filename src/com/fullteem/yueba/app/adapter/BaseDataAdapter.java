package com.fullteem.yueba.app.adapter;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fullteem.yueba.R;
import com.fullteem.yueba.app.AppConfig;
import com.fullteem.yueba.app.AppContext;
import com.fullteem.yueba.util.DisplayUtils;
import com.fullteem.yueba.util.LogUtil;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月13日&emsp;下午9:54:21
 * @use 基础数据适配器
 */
public class BaseDataAdapter {
	private static View ItemView;

	/**
	 * NO_DEFAULT item有图但无默认图片，NO_IMAGE item没有图片，OTHER 有图有默认图情况,null也代表无图
	 * 
	 * @author roujun
	 * 
	 */
	public enum ImageType {
		NO_DEFAULT, NO_IMAGE, OTHER;
	}

	abstract public static class Build<T, VH extends BaseDataAdapter.ViewHolder>
			extends BaseAdapter {
		protected List<T> listData;// 列表List
		private ImageType imageType = ImageType.OTHER;
		protected static DisplayImageOptions imageOptions;
		protected static ImageLoader imageLoader;

		/**
		 * 无图片或者有图片没有默认图的构造方法
		 * 
		 * @param listData
		 * @param imageType
		 */
		public Build(List<T> listData, ImageType imageType) {
			if (imageType == null)
				imageType = ImageType.NO_IMAGE;
			this.imageType = imageType;
			init(listData, null, null, null);
		}

		/**
		 * 有图片有默认图的构造方法
		 * 
		 * @param listData
		 * @param loadingRes
		 * @param emptyRes
		 * @param failRes
		 */
		public Build(List<T> listData, Integer loadingRes, Integer emptyRes,
				Integer failRes) {
			this.imageType = ImageType.OTHER;
			init(listData, loadingRes, emptyRes, failRes);
		}

		/**
		 * 初始化数据
		 * 
		 * @param listData
		 * @param loadingRes
		 * @param emptyRes
		 * @param failRes
		 */
		private void init(List<T> listData, Integer loadingRes,
				Integer emptyRes, Integer failRes) {
			if (listData == null) {
				listData = new LinkedList<T>();
			}
			this.listData = listData;
			if (imageType == ImageType.NO_IMAGE) // 没有图片则不需要在向下初始化
				return;
			if (imageType == ImageType.NO_DEFAULT) {// 有图片但无默认图,使用提供的默认图
				imageOptions = getOptions(null, null, null);
			} else {
				imageOptions = getOptions(loadingRes, emptyRes, failRes);
			}
			if (!ImageLoader.getInstance().isInited())
				initImageLoader();
			if (imageLoader == null)
				imageLoader = ImageLoader.getInstance();
			imageLoader.clearMemoryCache();
		}

		private void initImageLoader() {
			// 缓存图片目录
			File cacheDir = new File(AppConfig.DEFAULT_SAVE_PATH,
					AppConfig.SAVE_IMAGE_PATH);

			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
					AppContext.getApplication())
					//
					// 保存每个缓存图片的最大长和宽
					.memoryCache(
							new UsingFreqLimitedMemoryCache(16 * 1024 * 1024))
					//
					// 线程池内加载的数量
					.threadPoolSize(3)
					//
					// 线程池的大小 这个其实默认就是3
					.diskCacheFileNameGenerator(new Md5FileNameGenerator())
					.threadPriority(Thread.NORM_PRIORITY - 2)
					//
					// 缓存目录
					.discCache(new UnlimitedDiscCache(cacheDir))
					//
					// 设置缓存的最大字节
					.memoryCacheSize(2 * 1024)
					//
					// 即保存的每个缓存文件的最大长宽
					.diskCacheSize(50 * 1024 * 1024)
					.memoryCacheExtraOptions(800, 1280)
					//
					// 缓存显示不同大小的同一张图片
					.denyCacheImageMultipleSizesInMemory()
					//
					// // 超时时间
					.imageDownloader(
							new BaseImageDownloader(
									AppContext.getApplication(), 5 * 1000,
									30 * 1000))
					.defaultDisplayImageOptions(imageOptions).build();
			ImageLoader.getInstance().init(config);
		}

		private DisplayImageOptions getOptions(Integer loadingRes,
				Integer emptyRes, Integer failRes) {
			DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
					.showImageOnLoading(
							loadingRes == null ? R.drawable.img_loading_default
									: loadingRes)// 加载图片时的图片
					.showImageForEmptyUri(
							emptyRes == null ? R.drawable.img_loading_empty
									: emptyRes)// 没有图片资源时的默认图片
					.showImageOnFail(
							failRes == null ? R.drawable.img_loading_fail
									: failRes)// 加载失败时的图片
					.cacheInMemory(true)// 启用内存缓存
					.cacheOnDisk(true)// 启用外存缓存
					.considerExifParams(true)// 启用EXIF和JPEG图像格式  //SimpleBitmapDisplayer()
					.cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
					.displayer(new SimpleBitmapDisplayer())// 设置显示风格
															// -正常显示，RoundedBitmapDisplayer（int
															// roundPixels）设置圆角图片
					.displayer(new FadeInBitmapDisplayer(50))// 是否图片加载好后渐入的动画时间
					.bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型
					.imageScaleType(ImageScaleType.EXACTLY) // EXACTLY
															// :图像将完全按比例缩小的目标大小|
															// EXACTLY_STRETCHED:图片会缩放到目标大小完全
					.build();
			return displayImageOptions;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// LogUtil.LogDebug(null, "getView" + position, null);
			VH mHolder;
			if (convertView == null) {
				mHolder = onCreateViewHolder(parent, position);
				convertView = ItemView;
				convertView.setTag(mHolder);
			} else
				mHolder = (VH) convertView.getTag();
			onBindViewHolder(mHolder, position);
			return convertView;
		}

		@Override
		public int getCount() {
			return listData == null ? 0 : this.listData.size();
		}

		@Override
		public Object getItem(int position) {
			return listData == null ? null : this.listData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		/**
		 * 显示图片
		 * 
		 * @param imageView
		 * @param imgUrl
		 */
		public void showImage(final ImageView imageView, String imgUrl) {
			if (imageLoader == null || !ImageLoader.getInstance().isInited()) {
				LogUtil.LogError("BaseDataAdapter", "ImageLoader未初始化", null);
				return;
			}
			if (imageType == ImageType.NO_IMAGE) {
				LogUtil.LogError(
						"BaseDataAdapter",
						"请在构造中调用super()时传 ImageType.NO_DEFAULT或者ImageType.OTHER",
						null);
				return;
			}

			// 去掉xml文件指定的background
			//imageView.setBackgroundColor(Color.TRANSPARENT);
			imageView.setBackgroundColor(Color.TRANSPARENT);
			// 取得图片绝对地址
			imgUrl = DisplayUtils.getAbsolutelyUrl(imgUrl);

			if (TextUtils.isEmpty(imgUrl))
				return;
			if (imgUrl.equals(imageView.getTag()))
				return;
			imageLoader.displayImage(imgUrl, imageView, imageOptions);
			imageView.setTag(imgUrl);
			// imageLoader.loadImage(imgUrl, imageOptions,new
			// ImageLoadingListener() {
			//
			// @Override
			// public void onLoadingStarted(String arg0, View arg1) {
			// imageView.setImageResource(R.drawable.img_loading_default);
			// }
			//
			// @Override
			// public void onLoadingFailed(String arg0, View arg1, FailReason
			// arg2) {
			// imageView.setImageResource(R.drawable.img_loading_fail);
			// }
			//
			// @Override
			// public void onLoadingComplete(String arg0, View arg1, Bitmap
			// arg2) {
			// imageView.setImageBitmap(arg2);
			// }
			//
			// @Override
			// public void onLoadingCancelled(String arg0, View arg1) {
			// imageView.setImageResource(R.drawable.img_loading_default);
			// }
			// });
		}

		/**
		 * 显示文本
		 * 
		 * @param TextView
		 *            textView
		 * @param String
		 *            textContent
		 */
		public void showText(TextView textView, CharSequence textContent) {
			textView.setText(textContent);
		}

		abstract public VH onCreateViewHolder(ViewGroup viewGroup, int position);

		abstract public void onBindViewHolder(VH holder, int position);
	}

	public static class ViewHolder {
		public ViewHolder(View itemView) {
			ItemView = itemView;
		}
	}

}
