package com.fullteem.yueba.model;

import java.io.Serializable;

/*
 * This model represent a single phono item in the Album.
 * Compared to AlbumModel, 'userPhotoImgUrl' here is just the one image url.
 */
public class AlbumPhotoModel implements Serializable {

	// 根据tag来决定当前图片是添加按钮还是相册图片
	private String typeTag;
	private String photoUrl;
	
	public String getTypeTag() {
		return typeTag;
	}

	public void setTypeTag(String typeTag) {
		this.typeTag = typeTag;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

}
