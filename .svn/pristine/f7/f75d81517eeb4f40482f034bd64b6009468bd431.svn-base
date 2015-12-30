package com.fullteem.yueba.model.presentmodel;

import org.robobinding.annotation.PresentationModel;
import org.robobinding.presentationmodel.HasPresentationModelChangeSupport;
import org.robobinding.presentationmodel.PresentationModelChangeSupport;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.fullteem.yueba.R;
import com.fullteem.yueba.app.ui.PerssonalInfoAcitivity;
import com.fullteem.yueba.model.Group;

/**
 * @author jun
 * @version 1.0.0
 * @created 2015年1月12日&emsp;上午11:27:17
 * @use 群组详情view bean
 */
@PresentationModel
public class GroupDeatilPresentModel implements
		HasPresentationModelChangeSupport {
	private Context mContext;
	private Group groupModel;
	private int albumNoneVisibility, albumVisibility, membersVisibility,
			membersNoneVisibility;
	private PresentationModelChangeSupport changeSupport;

	public GroupDeatilPresentModel(Context context) {
		changeSupport = new PresentationModelChangeSupport(this);
		this.groupModel = new Group();
		this.mContext = context;
		this.albumNoneVisibility = View.VISIBLE;
		this.albumVisibility = View.GONE;
		this.membersNoneVisibility = View.VISIBLE;
		this.membersVisibility = View.GONE;
	}

	public void setModel(Group model) {
		this.groupModel = model;
		changeSupport.firePropertyChange("groupName");
		changeSupport.firePropertyChange("groupDistance");
		changeSupport.firePropertyChange("groupPeoples");
		changeSupport.firePropertyChange("groupSignature");
		changeSupport.firePropertyChange("groupNum");
		changeSupport.firePropertyChange("groupLocation");
		changeSupport.firePropertyChange("groupHarmast");
		changeSupport.firePropertyChange("groupIntroduction");
	}

	public String getGroupName() {
		return groupModel.getGroupName();
	}

	public String getGroupDistance() {
		return groupModel.getDistance();
	}

	/**
	 * 群人数
	 * 
	 * @return
	 */
	public String getGroupPeoples() {
		return String.format(mContext.getString(R.string.group_peoples),
				groupModel.getNum(), groupModel.getTotal());
	}

	public String getGroupSignature() {
		return groupModel.getSignature();
	}

	public String getGroupNum() {
		return groupModel.getGroupId(); // 群号？
	}

	public String getGroupLocation() {
		return groupModel.getLocation();
	}

	/**
	 * 创建人昵称
	 * 
	 * @return
	 */
	public String getGroupHarmast() {
		return groupModel.getHarmastName();
	}

	public String getGroupIntroduction() {
		return groupModel.getGroupDesc();
	}

	public void setGroupIntroduction(String groupIntroduction) {
		groupModel.setGroupDesc(groupIntroduction);
	}

	public int getAlbumNoneVisibility() {
		return albumNoneVisibility;
	}

	public int getAlbumVisibility() {
		return albumVisibility;
	}

	private void setAlbumNoneVisibility(int albumNoneVisibility) {
		this.albumNoneVisibility = albumNoneVisibility;
		changeSupport.firePropertyChange("albumNoneVisibility");
	}

	public void setAlbumVisibility(int albumVisibility) {
		this.albumVisibility = albumVisibility;
		changeSupport.firePropertyChange("albumVisibility");
		if (albumVisibility == View.GONE && albumNoneVisibility != View.VISIBLE) {
			setAlbumNoneVisibility(View.VISIBLE);
			return;
		}
		if (albumVisibility == View.VISIBLE && albumNoneVisibility != View.GONE)
			setAlbumNoneVisibility(View.GONE);
	}

	public int getMembersVisibility() {
		return membersVisibility;
	}

	public int getMembersNoneVisibility() {
		return membersNoneVisibility;
	}

	public void setMembersVisibility(int membersVisibility) {
		this.membersVisibility = membersVisibility;
		changeSupport.firePropertyChange("membersVisibility");
		if (membersVisibility == View.GONE
				&& membersNoneVisibility != View.VISIBLE) {
			setMembersNoneVisibility(View.VISIBLE);
			return;
		}
		if (membersVisibility == View.VISIBLE
				&& membersNoneVisibility != View.GONE)
			setMembersNoneVisibility(View.GONE);
	}

	private void setMembersNoneVisibility(int membersNoneVisibility) {
		this.membersNoneVisibility = membersNoneVisibility;
		changeSupport.firePropertyChange("membersNoneVisibility");
	}

	/**
	 * 群主点击事件
	 */
	public void onHarmast() {
		if (groupModel.getUserId() == -1)
			return;
		Intent intent = new Intent(mContext, PerssonalInfoAcitivity.class);
		intent.putExtra("userId", groupModel.getUserId() + "");
		mContext.startActivity(intent);
	}

	@Override
	public PresentationModelChangeSupport getPresentationModelChangeSupport() {
		return changeSupport;
	}

}
