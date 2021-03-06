package com.fullteem.yueba.app.singer.model;

import com.fullteem.yueba.util.DisplayUtils;
import com.fullteem.yueba.util.LogUtil;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月22日&emsp;上午11:13:43
 * @use 歌手详情bean,
 */
public class SingerDetailModel extends SingerModel {
	private String singerStageName; // 艺名
	private int cityId = -1; // 城市id
	private int userId = -1; // 用户id
	private String singerDetail;
	private boolean isFollow; // 是否关注

	public String getSingerStageName() {
		return singerStageName;
	}

	public int getCityId() {
		return cityId;
	}

	public int getUserId() {
		return userId;
	}

	public String getSingerDetail() {
		return singerDetail;
	}

	public void setSingerStageName(String singerStageName) {
		this.singerStageName = singerStageName;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public void setSingerSex(String singerSex) {
		setSingerGender(singerSex);
	}

	public void setAge(String age) {
		setSingerAge(age);
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setSingerLogoUrl(String singerLogoUrl) {
		setLogoUrl(singerLogoUrl);
	}

	public void setSingerDetail(String singerDetail) {
		this.singerDetail = singerDetail;
	}

	public boolean isFollow() {
		return isFollow;
	}

	public void setIsFollow(boolean isFollow) {
		this.isFollow = isFollow;
	}

	public void setIsAttention(boolean isAttention) {
		setIsFollow(isAttention);
		LogUtil.LogDebug("", "是否关注" + isFollow, null);
	}

	/**
	 * 魅力值
	 * 
	 * @param total
	 */
	public void setTotal(int total) {
		setCharm(total);
		setSingerLevel(DisplayUtils.getLevelRes(total));
	}

	/**
	 * 浏览量
	 * 
	 * @param total
	 */
	public void setNum(int num) {
		setPageviews(num);
	}

}
