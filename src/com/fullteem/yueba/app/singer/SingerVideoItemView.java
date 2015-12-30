package com.fullteem.yueba.app.singer;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.fullteem.yueba.R;
import com.fullteem.yueba.app.singer.model.SingerVideoListModel;
import com.fullteem.yueba.util.DisplayUtils;
import com.fullteem.yueba.util.ImageLoaderUtil;
import com.fullteem.yueba.util.LogUtil;

/**
 * @use 自定义view,用于viewpager不确定个数且View只加载一个xml页面文件
 */
public class SingerVideoItemView extends View {
	private Context context;

	public SingerVideoItemView(Context context) {
		super(context);
		this.context = context;
	}

	/**
	 * 创建一个item视图，布局文件为
	 * 
	 * @return
	 */
	public View createItemView(final SingerVideoListModel model) {
		View view = View
				.inflate(context, R.layout.adapter_singer_video_item, null);

		ImageView imgView = (ImageView) view
				.findViewById(R.id.imgView);


		String logoUrl = model.getVideoLogoUrl();
		String videoUrl = model.getVideoPlayUri();
		LogUtil.printSingerLog("logo:" + logoUrl + "\n video:" + videoUrl);
			
		ImageLoaderUtil.getImageLoader().displayImage(
				DisplayUtils.getAbsolutelyUrl(logoUrl),
						imgView,
				ImageLoaderUtil.getOptions(R.drawable.img_loading_default,
						R.drawable.img_loading_empty,
						R.drawable.img_loading_fail));


		return view;
	}
}
