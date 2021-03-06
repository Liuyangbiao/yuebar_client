package com.fullteem.yueba.app.singer.model;

import com.fullteem.yueba.model.CommentModel;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月17日&emsp;下午6:25:22
 * @use 视频评论bean
 */
public class VideoCommentModel extends CommentModel {
	private String header; // 评论人头像
	private String nickname;// 评论人昵称
	private String time;// 评论时间

	public String getHeader() {
		// if (TextUtils.isEmpty(header))
		// return "drawable://" + R.drawable.img_loading_empty;
		//
		// //不包含http://或者ftp://
		// if (!(header.contains("http://") || header.contains("ftp://")))
		// return Urls.SERVERHOST + header;
		//
		// //包含了看下前缀是不是http开头或者ftp
		// String urlHead = header.substring(0, 7);
		// if (!("http:/".equals(urlHead) || "ftp://".equals(urlHead)))
		// return Urls.SERVERHOST + header;
		//
		// if (header.length() <= 7)//错误地址
		// return "drawable://" + R.drawable.img_loading_fail;

		return header;
	}

	public String getNickname() {
		return nickname;
	}

	public String getTime() {
		return time;
	}

	public String getContent() {
		return getCommentommentContent();
	}

	public void setLogoUrl(String header) {
		this.header = header;
	}

	public void setUserName(String nickname) {
		this.nickname = nickname;
	}

	public void setCommentCreateDate(String time) {
		this.time = time;
	}

	public void setSingerVideoCommentContent(String content) {
		setCommentommentContent(content);
	}

	public void setSingerVideoDynamicCommentId(int dynamicCommentId) {
		setDynamicCommentId(dynamicCommentId);
	}

	public void setSingerVideoDynamicCommentType(int dynamicCommentType) {
		setDynamicCommentType(dynamicCommentType);
	}

}
