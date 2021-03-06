package com.fullteem.yueba.app.adapter;

import java.io.Serializable;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fullteem.yueba.R;
import com.fullteem.yueba.app.ui.BaseActivity;
import com.fullteem.yueba.app.ui.PicturesActivity;
import com.fullteem.yueba.model.AlbumPhotoModel;
import com.fullteem.yueba.util.CuttingPicturesUtil;
import com.fullteem.yueba.util.DisplayUtils;
import com.fullteem.yueba.util.ImageLoaderUtil;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月11日&emsp;下午9:54:21
 * @use 详细资料页面相册 Adapter
 */
public class PerssonalAlbumAdapter extends
		RecyclerView.Adapter<PerssonalAlbumAdapter.ViewHolder> {
	private List<AlbumPhotoModel> listAlbumUrl;
	private Context context;
	private Activity activity;

	public PerssonalAlbumAdapter(List<AlbumPhotoModel> listAlbumUrl) {
		this.listAlbumUrl = listAlbumUrl;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
		context = viewGroup.getContext();
		activity = (Activity) context;
		View itemView = View.inflate(viewGroup.getContext(),
				R.layout.adapter_perssonal_album, null);
		ViewHolder mHolder = new ViewHolder(itemView);
		return mHolder;
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int position) {

		// 判断是否添加按钮
		if ("add".equalsIgnoreCase(listAlbumUrl.get(position).getTypeTag())) {
			ImageLoaderUtil.getImageLoader().displayImage(
					"drawable://" + R.drawable.addgroup, viewHolder.ivAlbum);
			// RelativeLayout.LayoutParams ll = new RelativeLayout.LayoutParams(
			// 50, 50);
			// ll.leftMargin = 10;
			// ll.rightMargin = 10;
			// ll.topMargin = 70;
			// ll.bottomMargin = 10;
			// ll.addRule(RelativeLayout.CENTER_IN_PARENT);
			// viewHolder.ivAlbum.setLayoutParams(ll);
			String imgUrl = listAlbumUrl.get(position).getPhotoUrl();
			String tag = listAlbumUrl.get(position).getTypeTag();
			System.out.println("recyclerView相册上传图片url: " + imgUrl);
			System.out.println("recyclerView添加图片tag: " + tag);
			viewHolder.ivAlbum.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					addPic();
				}
			});
		} else {
			// RelativeLayout.LayoutParams ll = new RelativeLayout.LayoutParams(
			// 140, 120);
			// ll.leftMargin = 8;
			// ll.rightMargin = 8;
			// ll.topMargin = 25;
			// ll.bottomMargin = 10;
			// ll.addRule(RelativeLayout.CENTER_IN_PARENT);
			// viewHolder.ivAlbum.setLayoutParams(ll);
			ImageLoaderUtil.getImageLoader().displayImage(
					DisplayUtils.getAbsolutelyUrl(listAlbumUrl.get(position)
							.getPhotoUrl()), viewHolder.ivAlbum);
			imgOnClicked(viewHolder.ivAlbum, position);
		}
	}

	/**
	 * 添加相片
	 */
	private void addPic() {
		if (CuttingPicturesUtil.isSDCardExisd()) {
			CuttingPicturesUtil.searhcAlbum((Activity) context,
					CuttingPicturesUtil.UPLOAD_PIC);
		} else {
			Activity activity = (Activity) context;
			if (activity != null)
				((BaseActivity) activity).showToast(context
						.getString(R.string.sdcard_not_exsit));
		}
	}

	@Override
	public int getItemCount() {
		return listAlbumUrl.size();
	}

	class ViewHolder extends RecyclerView.ViewHolder {
		ImageView ivAlbum;

		public ViewHolder(View itemView) {
			super(itemView);
			ivAlbum = (ImageView) itemView.findViewById(R.id.iv_album);
		}
	}

	OnClickListener imgClickLister = new OnClickListener() {
		@Override
		public void onClick(View v) {
		}
	};

	private void imgOnClicked(final ImageView ivAlbum, final int position) {

		ivAlbum.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, PicturesActivity.class);
				// Bundle b = new Bundle();
				// b.putSerializable("list", (Serializable) listAlbumUrl);
				intent.putExtra("position", position);
				intent.putExtra("images", listAlbumUrl.get(position)
						.getPhotoUrl());// 非必须
				int[] location = new int[2];
				ivAlbum.getLocationOnScreen(location);
				intent.putExtra("locationX", location[0]);// 必须
				intent.putExtra("locationY", location[1]);// 必须
				intent.putExtra("width", ivAlbum.getWidth());// 必须
				intent.putExtra("height", ivAlbum.getHeight());// 必须
				// intent.putExtra("bundle", b);
				intent.putExtra("list", (Serializable) (listAlbumUrl));
				context.startActivity(intent);
				activity.overridePendingTransition(0, 0);

			}
		});

	}

}
