package com.fullteem.yueba.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.baidu.mapapi.map.MapView;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.baidu.platform.comapi.map.Projection;
import com.fullteem.yueba.interfaces.IGetMapPressPlace;

public class CustemMapView extends MapView {
	private IGetMapPressPlace listener;

	public CustemMapView(Context context) {
		super(context);
	}

	public void setGetPressListener(IGetMapPressPlace listener) {
		this.listener = listener;
	}

	public CustemMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustemMapView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
			// 获得屏幕点击的位置
			int x = (int) event.getX();
			int y = (int) event.getY();
			System.out.println(x + "   " + y);
			Projection proj = getProjection();
			GeoPoint pt = proj.fromPixels(x, y);
			// 将像素坐标转为地址坐标
			if (listener != null) {
				listener.getPoint(pt);
			}
			break;

		default:
			break;
		}

		return super.onTouchEvent(event);
	}
}
