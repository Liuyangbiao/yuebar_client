package com.fullteem.yueba.app.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fullteem.yueba.R;
import com.fullteem.yueba.app.ui.BaseActivity;
import com.fullteem.yueba.app.ui.CityActivity;
import com.fullteem.yueba.app.ui.MainActivity;
import com.fullteem.yueba.globle.GlobleConstant;
import com.fullteem.yueba.globle.GlobleVariable;
import com.fullteem.yueba.model.City;
import com.fullteem.yueba.util.AppUtil;
import com.fullteem.yueba.util.LogUtil;
import com.fullteem.yueba.util.UIHelper;

/**
 * @className：HotCityAdapter.java
 * @author: lmt
 * @Function: 热门城市列表适配器
 * @createDate: 2014-12-3 下午4:18:18
 * @update:
 */
@SuppressLint("InflateParams")
public class HotCityAdapter extends BaseListAdapter<City> {
	private String TAG = "HotCityAdapter";
	private boolean isTag = true;

	public HotCityAdapter(BaseActivity context, List<City> list) {
		super(context, list);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_select_hotcity, null);
			holder.imageViewPinMap = (ImageView) convertView
					.findViewById(R.id.city_pin_map);
			holder.imageViewSelected = (ImageView) convertView
					.findViewById(R.id.city_selected);
			holder.textViewCity = (TextView) convertView
					.findViewById(R.id.city_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		City data = (City) getItem(position);
		if (position == 0) {
			holder.textViewCity.setText(baseActivity
					.getResString(R.string.city_location));
			holder.imageViewPinMap.setVisibility(View.VISIBLE);
		} else {
			LogUtil.LogDebug(TAG, position + "____" + data.getCName()
					+ "*********" + data.isChoosed(), isTag);
			if (data.isChoosed()) {
				holder.imageViewSelected.setVisibility(View.VISIBLE);
			} else {
				holder.imageViewSelected.setVisibility(View.GONE);
			}
			holder.textViewCity.setText(AppUtil.parceCityName(data.getCName()));
			holder.imageViewPinMap.setVisibility(View.GONE);
		}

		setListener(convertView, data, position);
		return convertView;
	}

	private void setListener(View convertView, final City data,
			final int position) {
		// TODO Auto-generated method stub

		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (position == 0) {
					UIHelper.ShowMessage(baseActivity, "暂时无法定位！");
					// if (((CityActivity) baseActivity).comeFrom ==
					// CityActivity.FROM_APPSTART_TAG) {
					// baseActivity.JumpToActivity(MainActivity.class, false);
					// } else {
					// baseActivity.finish();
					// }
				} else {
					reLoactionCity(position);
					if (((CityActivity) baseActivity).comeFrom == CityActivity.FROM_APPSTART_TAG) {
						baseActivity.JumpToActivity(MainActivity.class, false);
					} else {
						String cityName = AppUtil.parceCityName(data.getCName());
						String cityId = AppUtil.parceCityName(data.getCID()
								+ "");
						Intent intent = new Intent();
						intent.putExtra("cityName", cityName);
						intent.putExtra("cityId", cityId);
						baseActivity.setResult(GlobleConstant.ACTION_HOT_CITY,
								intent);
						baseActivity.finish();
					}
				}
			}
		});
	}

	/**
	 * 城市的重新定位
	 * 
	 * @param position
	 */
	protected void reLoactionCity(int position) {
		// TODO Auto-generated method stub
		for (City city : mList) {
			if (city.isChoosed()) {
				city.setChoosed(false);
				break;
			}
		}
		mList.get(position).setChoosed(true);
		GlobleVariable.loc_City_Succed = true;
		GlobleVariable.currentCity = mList.get(position);
		notifyDataSetChanged();
	}

	class ViewHolder {
		ImageView imageViewPinMap, imageViewSelected;
		TextView textViewCity;
	}
}
