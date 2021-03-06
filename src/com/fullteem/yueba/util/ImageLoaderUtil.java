package com.fullteem.yueba.util;

import java.io.File;

import android.graphics.Bitmap;

import com.fullteem.yueba.R;
import com.fullteem.yueba.app.AppConfig;
import com.fullteem.yueba.app.AppContext;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月2日&emsp;上午10:35:46
 * @use imageloader封装的工具类
 */
public class ImageLoaderUtil {
	/**
	 * 获得imageloader实例
	 * 
	 * @param reInit
	 *            是否需要重新初始化imageloader
	 * @param loadingRes
	 *            reInit为true时该项不能为null
	 * @param emptyRes
	 *            reInit为true时该项不能为null
	 * @param failRes
	 *            reInit为true时该项不能为null
	 * @return
	 */
	public static ImageLoader getImageLoader() {
		if (!ImageLoader.getInstance().isInited()) {
			initImageLoader(null, null, null);
		}
		return ImageLoader.getInstance();
	}

	private static void initImageLoader(Integer loadingRes, Integer emptyRes,
			Integer failRes) {
		// 缓存图片目录
		File cacheDir = new File(AppConfig.DEFAULT_SAVE_PATH,
				AppConfig.SAVE_IMAGE_PATH);

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				AppContext.getApplication())
				//
				// 保存每个缓存图片的最大长和宽
				.memoryCache(new UsingFreqLimitedMemoryCache(16 * 1024 * 1024))
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
				.memoryCacheExtraOptions(800, 1280)//
				// 缓存显示不同大小的同一张图片
				.denyCacheImageMultipleSizesInMemory()
				//
				// // 超时时间
				.imageDownloader(
						new BaseImageDownloader(AppContext.getApplication(),
								5 * 1000, 30 * 1000)).//
				defaultDisplayImageOptions(getOptions())//
				.build();
		ImageLoader.getInstance().init(config);
	}

	public static DisplayImageOptions getOptions(Integer loadingRes,
			Integer emptyRes, Integer failRes) {
		DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(
						loadingRes == null ? R.drawable.img_loading_default
								: loadingRes)// 加载图片时的图片
				.showImageForEmptyUri(
						emptyRes == null ? R.drawable.img_loading_empty
								: emptyRes)// 没有图片资源时的默认图片
				.showImageOnFail(
						failRes == null ? R.drawable.img_loading_fail : failRes)// 加载失败时的图片
				.cacheInMemory(true)// 启用内存缓存
				.cacheOnDisk(true)// 启用外存缓存
				.considerExifParams(true)// 启用EXIF和JPEG图像格式
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

	public static DisplayImageOptions getOptions() {
		DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.img_loading_default)// 加载图片时的图片
				.showImageForEmptyUri(R.drawable.img_loading_empty)// 没有图片资源时的默认图片
				.showImageOnFail(R.drawable.img_loading_fail)// 加载失败时的图片
				.cacheInMemory(true)// 启用内存缓存
				.cacheOnDisk(true)// 启用外存缓存
				.considerExifParams(true)// 启用EXIF和JPEG图像格式
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
}
