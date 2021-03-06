package com.fullteem.yueba.app.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.fullteem.yueba.R;
import com.fullteem.yueba.app.AppContext;
import com.fullteem.yueba.app.ui.CityActivity;
import com.fullteem.yueba.globle.GlobleConstant;
import com.fullteem.yueba.model.City;
import com.fullteem.yueba.util.LogUtil;

public class SearchCityAdapter extends BaseAdapter implements Filterable {

	private List<City> mResultCities;
	private LayoutInflater mInflater;
	private Context mContext;

	// private boolean[] isShowUserCity;

	// private String mFilterStr;

	public SearchCityAdapter(Context context) {
		mContext = context;
		mResultCities = new ArrayList<City>();
		mInflater = LayoutInflater.from(mContext);
		//
		// this.userCityName = new ArrayList<String>();
		// for (LeftCity city :
		// UserCityDBManger.getInstance().queryAllUserCity())
		// this.userCityName.add(city.getName());
	}

	@Override
	public int getCount() {
		return mResultCities.size();
	}

	@Override
	public City getItem(int position) {
		return mResultCities.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder mHolder;
		if (convertView == null) {
			mHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_search_city, null);
			mHolder.provinceTv = (TextView) convertView
					.findViewById(R.id.search_province);
			mHolder.cityTv = (TextView) convertView
					.findViewById(R.id.column_title);
			// mHolder.iv_isUserCity = (ImageView) convertView
			// .findViewById(R.id.iv_searchcity_isusercity);
			convertView.setTag(mHolder);
		} else
			mHolder = (ViewHolder) convertView.getTag();
		mHolder.provinceTv.setText(mResultCities.get(position).getPName());
		mHolder.cityTv.setText(mResultCities.get(position).getCName());
		// if (isShowUserCity[position])
		// mHolder.iv_isUserCity.setVisibility(View.VISIBLE);
		// else
		// mHolder.iv_isUserCity.setVisibility(View.GONE);
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String cityName = mResultCities.get(position).getCName();
				String cityId = mResultCities.get(position).getCID() + "";
				Intent intent = new Intent();
				intent.putExtra("cityName", cityName);
				intent.putExtra("cityId", cityId);
				((CityActivity) mContext).setResult(
						GlobleConstant.ACTION_HOT_CITY, intent);
				((CityActivity) mContext).finish();
			}
		});

		return convertView;
	}

	@Override
	public Filter getFilter() {
		Filter filter = new Filter() {
			@Override
			@SuppressWarnings("unchecked")
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				mResultCities = (ArrayList<City>) results.values;
				if (results.count > 0) {
					// isShowUserCity = new boolean[mResultCities.size()];
					// try {
					// for (int i = 0; i < userCityName.size(); i++) {
					// for (int j = mResultCities.size() - 1; j >= 0; j--) {
					// if (userCityName.get(i).equals(
					// mResultCities.get(j).getCity()))
					// isShowUserCity[j] = true;
					// }
					// }
					// } catch (NullPointerException e) {
					// }
					notifyDataSetChanged();
				} else {
					notifyDataSetInvalidated();
				}
			}

			@Override
			protected FilterResults performFiltering(CharSequence s) {
				String str = s.toString().toUpperCase();
				// mFilterStr = str;
				FilterResults results = new FilterResults();
				ArrayList<City> cityList = new ArrayList<City>();
				if (AppContext.allhotCitiesList != null
						&& AppContext.allhotCitiesList.size() >= 0) {
					for (City cb : AppContext.citiesList) {
						// 匹配全屏、首字母、和城市名中文
						// if (cb.getAllFristPY().indexOf(str) > -1 ||
						// cb.getAllPY().indexOf(str) > -1 ||
						// cb.getCity().indexOf(str) > -1) {
						// cityList.add(cb);
						// }
						// 匹配城市名称
						if (cb.getCName().indexOf(str) > -1) {
							cityList.add(cb);
						}

						LogUtil.printLog("city", cb.toString());
					}
				}
				results.values = cityList;
				results.count = cityList.size();
				return results;
			}
		};
		return filter;
	}

	private class ViewHolder {
		TextView provinceTv;
		TextView cityTv;
		// ImageView iv_isUserCity;
	}
}
