package com.fullteem.yueba.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

/**
 * 异步线程加载图片工具类 使用说明： BitmapManager bmpManager; bmpManager = new
 * BitmapManager(BitmapFactory.decodeResource(context.getResources(),
 * R.drawable.loading)); bmpManager.loadBitmap(imageURL, imageView);
 * 
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-6-25
 */
public class BitmapManager {
	String tag = "BitmapManager";

	private static HashMap<String, SoftReference<Bitmap>> cache;
	private static ExecutorService pool;
	private static Map<ImageView, String> imageViews;
	private Bitmap defaultBmp;

	static {
		cache = new HashMap<String, SoftReference<Bitmap>>();
		pool = Executors.newFixedThreadPool(5); // 固定线程池
		imageViews = Collections
				.synchronizedMap(new WeakHashMap<ImageView, String>());
	}

	public BitmapManager() {

	}

	public BitmapManager(Bitmap def) {
		this.defaultBmp = def;
	}

	/**
	 * 设置默认图片
	 * 
	 * @param bmp
	 */
	public void setDefaultBmp(Bitmap bmp) {
		defaultBmp = bmp;
	}

	/**
	 * 加载图片
	 * 
	 * @param url
	 * @param imageView
	 */
	public void loadBitmap(String url, ImageView imageView) {
		loadBitmap(url, imageView, this.defaultBmp, 0, 0);
	}

	/**
	 * 加载图片-可设置加载失败后显示的默认图片
	 * 
	 * @param url
	 * @param imageView
	 * @param defaultBmp
	 */
	public void loadBitmap(String url, ImageView imageView, Bitmap defaultBmp) {
		if (StringUtils.isStringNone(url)) {
			imageView.setImageBitmap(defaultBmp);
		} else {
			loadBitmap(url, imageView, defaultBmp, 0, 0);
		}
	}

	/**
	 * 加载图片-可指定显示图片的高宽
	 * 
	 * @param url
	 * @param imageView
	 * @param width
	 * @param height
	 */
	public void loadBitmap(String url, ImageView imageView, Bitmap defaultBmp,
			int width, int height) {
		imageViews.put(imageView, url);
		Bitmap bitmap = getBitmapFromCache(url);

		if (bitmap != null) {
			// 显示缓存图片
			imageView.setImageBitmap(bitmap);
		} else {
			// 加载SD卡中的图片缓存
			String filename = FileUtils.getFileName(url);
			String filepath = imageView.getContext().getFilesDir()
					+ File.separator + filename;
			File file = new File(filepath);
			if (file.exists()) {
				// 显示SD卡中的图片缓存
				Bitmap bmp = ImageUtils.getBitmap(imageView.getContext(),
						filename);
				imageView.setImageBitmap(bmp);
			} else {
				// 线程加载网络图片
				imageView.setImageBitmap(defaultBmp);
				queueJob(url, imageView, width, height);
			}
		}
	}

	/**
	 * 从缓存中获取图片
	 * 
	 * @param url
	 */
	public Bitmap getBitmapFromCache(String url) {
		Bitmap bitmap = null;
		if (cache.containsKey(url)) {
			bitmap = cache.get(url).get();
		}
		return bitmap;
	}

	/**
	 * 从网络中加载图片
	 * 
	 * @param url
	 * @param imageView
	 * @param width
	 * @param height
	 */
	@SuppressLint("HandlerLeak")
	public void queueJob(final String url, final ImageView imageView,
			final int width, final int height) {
		/* Create handler in UI thread. */
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				String tag = imageViews.get(imageView);
				if (tag != null && tag.equals(url)) {
					if (msg.obj != null) {
						imageView.setImageBitmap((Bitmap) msg.obj);
						try {
							// 向SD卡中写入图片缓存
							ImageUtils.saveImage(imageView.getContext(),
									FileUtils.getFileName(url),
									(Bitmap) msg.obj);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		};

		pool.execute(new Runnable() {
			@Override
			public void run() {
				Message message = Message.obtain();
				message.obj = downloadBitmap(url, width, height);
				handler.sendMessage(message);
			}
		});
	}

	/**
	 * 下载图片-可指定显示图片的高宽
	 * 
	 * @param url
	 * @param width
	 * @param height
	 */
	public static Bitmap downloadBitmap(String url, int width, int height) {
		Bitmap bitmap = null;
		try {
			URL urls = new URL(url);
			HttpURLConnection con = (HttpURLConnection) urls.openConnection();
			con.setDoInput(true);
			con.connect();
			InputStream inputStream = con.getInputStream();

			bitmap = BitmapFactory.decodeStream(inputStream);
			inputStream.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		return bitmap;
	}

	/** 从Drawable中获取图片 **/
	public static Bitmap getBitmapFromDrawable(Context context, int id) {
		InputStream is = context.getResources().openRawResource(id);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		options.inInputShareable = true;
		options.inPurgeable = true;
		// 获取这个图片的宽和高
		Bitmap bitmap = BitmapFactory.decodeStream(is, null, options); // 此时返回bm为空
		options.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeStream(is, null, options);
		options = null;// 这里为null释放内存;
		return bitmap;
	}

}