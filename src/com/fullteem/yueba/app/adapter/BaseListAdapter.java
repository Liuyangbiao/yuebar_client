package com.fullteem.yueba.app.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import com.fullteem.yueba.R;
import com.fullteem.yueba.app.AppConfig;
import com.fullteem.yueba.app.AppContext;
import com.fullteem.yueba.app.ui.BaseActivity;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

/**
 * 
 * @ClassName: BaseListAdapter
 * @Description: 基本列表适配器封装
 * @author BaoHang baohang2011@gmail.com
 * @date
 * @param <T>
 */
public abstract class BaseListAdapter<T> extends BaseAdapter {
	protected List<T> mList;// 列表List
	protected LayoutInflater mInflater;// 布局管理
	protected DisplayImageOptions options;
	protected ImageLoader imageLoader;
	protected static final int NO_DEFAULT = -1;// 有图片但是没有默认图
	protected static final int NO_IMAGE = 0;// 没有图片
	protected BaseActivity baseActivity;
	protected Activity activity;

	/**
	 * 没有指定默认图的够造方法
	 * 
	 * @param context
	 * @param list
	 */
	public BaseListAdapter(BaseActivity context, List<T> list) {
		this(context, list, NO_DEFAULT);
		mInflater = LayoutInflater.from(context);
	}

	public BaseListAdapter(Activity context, List<T> list) {
		this(context, list, NO_DEFAULT);
		mInflater = LayoutInflater.from(context);
	}

	/**
	 * 
	 * @param context
	 * @param list
	 * @param defaultId
	 *            传0则表示适配器中没有图片需要显示,-1表示需要显示但没有默认图片
	 */
	protected BaseListAdapter(BaseActivity context, List<T> list, int defaultId) {
		baseActivity = context;
		init(list, defaultId);
	}

	/**
	 * 
	 * @param context
	 * @param list
	 * @param defaultId
	 *            传0则表示适配器中没有图片需要显示,-1表示需要显示但没有默认图片
	 */
	protected BaseListAdapter(Activity context, List<T> list, int defaultId) {
		activity = context;
		init(list, defaultId);
	}

	@SuppressWarnings("deprecation")
	private void init(List<T> list, int defaultId) {
		// TODO Auto-generated me
		if (list == null) {
			list = new ArrayList<T>();
		}
		mList = list;
		if (defaultId == NO_IMAGE) {// 没有图片
			return;
		} else if (defaultId == NO_DEFAULT) {// 有图片但是没有默认图
			options = new DisplayImageOptions.Builder()
					.showStubImage(R.drawable.default_image)
					.showImageForEmptyUri(R.drawable.default_image)
					.showImageOnFail(R.drawable.default_image)
					.cacheInMemory(true).cacheOnDisc(true)
					.bitmapConfig(Bitmap.Config.RGB_565).build();
		} else {// 有图片有默认图
			options = new DisplayImageOptions.Builder()
					.showStubImage(defaultId).showImageForEmptyUri(defaultId)
					.showImageOnFail(defaultId).cacheInMemory(true)
					.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565)
					.build();
		}
		if (!ImageLoader.getInstance().isInited()) {
			initImageLoader();
		}
		imageLoader = ImageLoader.getInstance();
	}

	private void initImageLoader() {
		// 缓存图片目录
		File cacheDir = new File(AppConfig.DEFAULT_SAVE_PATH,
				AppConfig.SAVE_IMAGE_PATH);
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				AppContext.getApplication())
				.memoryCache(new UsingFreqLimitedMemoryCache(16 * 1024 * 1024))
				// 保存每个缓存图片的最大长和宽
				.threadPoolSize(3)
				// 线程池内加载的数量
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.threadPriority(Thread.NORM_PRIORITY - 2)
				// 线程池的大小 这个其实默认就是3
				.discCache(new UnlimitedDiscCache(cacheDir))
				// 缓存目录
				.memoryCacheSize(2 * 1024)
				// 设置缓存的最大字节
				.diskCacheSize(50 * 1024 * 1024)
				.memoryCacheExtraOptions(800, 1280)
				// 即保存的每个缓存文件的最大长宽
				.denyCacheImageMultipleSizesInMemory()
				// 缓存显示不同大小的同一张图片
				.imageDownloader(
						new BaseImageDownloader(AppContext.getApplication(),
								5 * 1000, 30 * 1000))
				.defaultDisplayImageOptions(options)// 超时时间
				.build();
		ImageLoader.getInstance().init(config);
	}

	@Override
	public int getCount() {
		return (mList == null) ? 0 : mList.size();
	}

	@Override
	public Object getItem(int position) {
		return (mList == null) ? null : mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

}
