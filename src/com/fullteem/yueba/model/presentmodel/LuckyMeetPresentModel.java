package com.fullteem.yueba.model.presentmodel;

import java.util.LinkedList;
import java.util.List;

import org.robobinding.annotation.PresentationModel;
import org.robobinding.presentationmodel.HasPresentationModelChangeSupport;
import org.robobinding.presentationmodel.PresentationModelChangeSupport;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.fullteem.yueba.model.HobbyModel;
import com.fullteem.yueba.model.IndustryModel;
import com.fullteem.yueba.util.DisplayUtils;
import com.fullteem.yueba.widget.ListObjectPopupWindow;
import com.fullteem.yueba.widget.ListObjectPopupWindow.IEventListener;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月31日&emsp;下午1:41:14
 * @use 幸运相遇View Model
 */
@PresentationModel
public class LuckyMeetPresentModel implements HasPresentationModelChangeSupport {
	private PresentationModelChangeSupport mChangeSupport;
	private int noneDataVisibilly, selectVisibilly, errorStateVisibilly,
			lvVisibilly;
	private String hobby, industry, gender, errorState;
	private Context context;
	private ListObjectPopupWindow<IndustryModel> mPopupWindowIndustry; // 行业下拉选择
	private ListObjectPopupWindow<HobbyModel> mPopupWindowHobby; // 行业下拉选择
	private volatile ListObjectPopupWindow<String> mPopupWindowGender; // 性别下拉选择
	private TextView anchorIndustry, anchorHobby, anchorGender;
	private Integer currentHobbyId, currentIndustryId, currentGenderId;

	public LuckyMeetPresentModel(Context context) {
		mChangeSupport = new PresentationModelChangeSupport(this);
		this.context = context;
		noneDataVisibilly = View.GONE;
		selectVisibilly = View.GONE;
		lvVisibilly = View.GONE;
		errorStateVisibilly = View.INVISIBLE;
	}

	/**
	 * 弹出列表的锚视图
	 * 
	 * @param anchorIndustry
	 */
	public void setAnchorIndustry(TextView anchorIndustry) {
		this.anchorIndustry = anchorIndustry;
	}

	public void setAnchorHobby(TextView anchorHobby) {
		this.anchorHobby = anchorHobby;
	}

	public void setAnchorGender(TextView anchorGender) {
		this.anchorGender = anchorGender;
	}

	public int getNoneDataVisibilly() {
		return noneDataVisibilly;
	}

	public int getSelectVisibilly() {
		return selectVisibilly;
	}

	public int getErrorStateVisibilly() {
		return errorStateVisibilly;
	}

	public String getErrorState() {
		return errorState;
	}

	public String getHobby() {
		return hobby;
	}

	public String getIndustry() {
		return industry;
	}

	public String getGender() {
		return gender;
	}

	public void setHobby(String hobby) {
		this.hobby = hobby;
		mChangeSupport.firePropertyChange("hobby");
	}

	public void setIndustry(String industry) {
		this.industry = industry;
		mChangeSupport.firePropertyChange("industry");
	}

	public void setGender(String gender) {
		this.gender = gender;
		mChangeSupport.firePropertyChange("gender");
	}

	public void setErrorStateVisibilly(int errorStateVisibilly) {
		this.errorStateVisibilly = errorStateVisibilly;
		mChangeSupport.firePropertyChange("errorStateVisibilly");
	}

	public void setErrorState(String errorState) {
		this.errorState = errorState;
		mChangeSupport.firePropertyChange("errorState");
	}

	public void setNoneDataVisibilly(int noneDataVisibilly) {
		this.noneDataVisibilly = noneDataVisibilly;
		if (noneDataVisibilly == View.VISIBLE)
			setSelectVisibilly(View.GONE);
		mChangeSupport.firePropertyChange("noneDataVisibilly");
	}

	public void setSelectVisibilly(int selectVisibilly) {
		this.selectVisibilly = selectVisibilly;
		mChangeSupport.firePropertyChange("selectVisibilly");
	}

	public int getLvVisibilly() {
		return lvVisibilly;
	}

	public void setLvVisibilly(int lvVisibilly) {
		this.lvVisibilly = lvVisibilly;
	}

	/**
	 * 爱好点击事件
	 */
	public void onHobby() {
		if (mPopupWindowHobby != null)
			mPopupWindowHobby.showAsDropDown(anchorHobby, 0, 0);
	}

	/**
	 * 行业点击事件
	 */
	public void onIndustry() {
		if (mPopupWindowIndustry != null)
			mPopupWindowIndustry.showAsDropDown(anchorIndustry, 0, 0);
	}

	/**
	 * 性别点击事件
	 */
	public void onGender() {
		if (mPopupWindowGender != null)
			mPopupWindowGender.showAsDropDown(anchorGender, 0, 0);
	}

	@SuppressWarnings("unchecked")
	public void setListHobbyModel(List<HobbyModel> hobbyModel) {
		if (hobbyModel.get(0) != null) {
			setHobby(hobbyModel.get(0).getHobbyName());
			currentHobbyId = hobbyModel.get(0).getHobbyId();
		}
		anchorHobby.invalidate();
		anchorHobby.requestLayout();
		mPopupWindowHobby = new ListObjectPopupWindow<HobbyModel>(context,
				hobbyModel);

		if (anchorHobby.getWidth() <= 0) {
			mPopupWindowHobby.setWidth(DisplayUtils.dp2px(context, 120));
		} else
			mPopupWindowHobby.setWidth(anchorHobby.getWidth());

		mPopupWindowHobby
				.setShowContentMethod("getHobbyName")
				.setEventListener(
						(IEventListener) mPopupWindowHobby.new IEventListener() {
							@Override
							public void onItemClick(HobbyModel obj, int positon) {
								setHobby(obj.getHobbyName());
								currentHobbyId = obj.getHobbyId();
							}
						});
		if (mPopupWindowGender == null)
			initGenderPopupWindow();
	}

	@SuppressWarnings("unchecked")
	@SuppressLint("NewApi")
	public void setListIndustryModel(List<IndustryModel> industryModel) {
		if (industryModel.get(0) != null) {
			setIndustry(industryModel.get(0).getIndustryName());
			currentIndustryId = industryModel.get(0).getIndustryId();
		}
		anchorIndustry.invalidate();
		anchorIndustry.requestLayout();
		mPopupWindowIndustry = new ListObjectPopupWindow<IndustryModel>(
				context, industryModel);

		// 适配API 16以下
		if (anchorIndustry.getWidth() <= 0) {
			// if (AndroidVersionCheckUtils.hasJellyBean())
			// mPopupWindowIndustry.setWidth(anchorIndustry.getMaxWidth());
			// else
			mPopupWindowIndustry.setWidth(DisplayUtils.dp2px(context, 120));
		} else
			mPopupWindowIndustry.setWidth(anchorIndustry.getWidth());

		mPopupWindowIndustry
				.setShowContentMethod("getIndustryName")
				.setEventListener(
						((IEventListener) mPopupWindowIndustry.new IEventListener() {
							@Override
							public void onItemClick(IndustryModel obj,
									int positon) {
								setIndustry(obj.getIndustryName());
								currentIndustryId = obj.getIndustryId();
							}
						}));
		if (mPopupWindowGender == null)
			initGenderPopupWindow();
	}

	@SuppressLint("NewApi")
	private void initGenderPopupWindow() {
		List<String> listGender = new LinkedList<String>();
		listGender.add("不限");
		listGender.add("男");
		listGender.add("女");
		setGender(listGender.get(0));
		mPopupWindowGender = new ListObjectPopupWindow<String>(context,
				listGender);
		if (anchorGender.getWidth() <= 0) {
			// if (AndroidVersionCheckUtils.hasJellyBean())
			// mPopupWindowGender.setWidth(anchorGender.getMaxWidth());
			// else
			mPopupWindowGender.setWidth(DisplayUtils.dp2px(context, 120));
		} else
			mPopupWindowGender.setWidth(anchorGender.getWidth());

		mPopupWindowGender
				.setEventListener(mPopupWindowGender.new IEventListener() {
					@Override
					public void onItemClick(String obj, int positon) {
						setGender(obj);
						currentGenderId = "男".equalsIgnoreCase(obj) ? 1 : "女"
								.equalsIgnoreCase(obj) ? 2 : null;
					}
				});
	}

	public Integer getCurrentHobbyId() {
		return currentHobbyId;
	}

	public Integer getCurrentIndustryId() {
		return currentIndustryId;
	}

	public Integer getCurrentGenderId() {
		return currentGenderId;
	}

	@Override
	public PresentationModelChangeSupport getPresentationModelChangeSupport() {
		return mChangeSupport;
	}

}
