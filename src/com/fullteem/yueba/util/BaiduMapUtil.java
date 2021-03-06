package com.fullteem.yueba.util;

import java.math.BigDecimal;

import android.content.Context;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.MKMapViewListener;

public class BaiduMapUtil {
	public MKMapViewListener mMapListener = null;
	// 定位相关
	static LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	static BDLocation lastLocation = null;
	private static String TAG = "BaiduMapUtil";
	private IGetLocation getlocation;

	public BaiduMapUtil(Context context, IGetLocation getlocation) {
		mLocClient = new LocationClient(context);
		mLocClient.registerLocationListener(myListener);
		mLocClient.start();
		this.getlocation = getlocation;
	}

	/**
	 * 监听函数，有新位置的时候，格式化成字符串，输出到屏幕中
	 */
	public class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null) {
				return;
			}
			Log.d("map", "On location change received:" + location);
			Log.d("map", "addr:" + location.getAddrStr());
			// sendButton.setEnabled(true);
			// if (progressDialog != null) {
			// progressDialog.dismiss();
			// }

			if (lastLocation != null) {
				if (isLocationEqual(lastLocation, location)) {
					Log.d("map", "same location, skip refresh");
					getlocation.getlocation(lastLocation);
					mLocClient.stop();
					// mMapView.refresh(); //need this refresh?
					return;
				}
			}

			lastLocation = location;
			getlocation.getlocation(lastLocation);
			mLocClient.stop();
			// GeoPoint p1 = gcjToBaidu(location.getLatitude(),
			// location.getLongitude());
			// System.err.println("johnson change to baidu:" + p1);
			// GeoPoint p2 = baiduToGcj(location.getLatitude(),
			// location.getLongitude());
			// System.err.println("johnson change to gcj:" + p2);

			// OverlayItem addrItem = new OverlayItem(point, "title",
			// location.getAddrStr());
			// mAddrOverlay.removeAll();
			// mAddrOverlay.addItem(addrItem);
			// mMapView.getController().setZoom(17);
			// mMapView.refresh();
			// mMapController.animateTo(point);
		}

		@Override
		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null) {
				return;
			}
		}
	}

	public interface IGetLocation {

		/**
		 * 获取经纬度
		 */
		void getlocation(BDLocation lastLocation);
	}

	public static boolean isLocationEqual(BDLocation lastLocation,
			BDLocation newLocation) {
		boolean retVal = false;
		BigDecimal latitudeLast = new BigDecimal(lastLocation.getLatitude());
		BigDecimal latitudeNew = new BigDecimal(newLocation.getLatitude());

		BigDecimal longtitudeLast = new BigDecimal(lastLocation.getLongitude());
		BigDecimal longtitudeNew = new BigDecimal(newLocation.getLongitude());

		if (latitudeLast.equals(latitudeNew)
				&& longtitudeLast.equals(longtitudeNew)) {
			retVal = true;
		}

		LogUtil.printUserEntryLog("isLocationEqual:" + retVal);
		return retVal;
	}

}
