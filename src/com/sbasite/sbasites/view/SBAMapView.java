package com.sbasite.sbasites.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.sbasite.sbasites.activity.SBAMapActivity;
import com.sbasite.sbasites.util.DistanceCalculator;

public class SBAMapView extends MapView {
	
	private static final String TAG = SBAMapView.class.getSimpleName();
	private SBAMapViewListener listener;
	private final Double DISTANCE_CHANGE = 6.0; //in km
	int oldZoomLevel=-1;
	private GeoPoint mapCenter = null;

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public SBAMapView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public SBAMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param apiKey
	 */
	public SBAMapView(Context context, String apiKey) {
		super(context, apiKey);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (ev.getAction()==MotionEvent.ACTION_UP) {
			if (mapCenter == null) {
				mapCenter = getMapCenter();
			}
			
			if (DistanceCalculator.calculateDistance(mapCenter, getMapCenter()) > DISTANCE_CHANGE) {
				mapCenter = getMapCenter();
				if (null != listener) {
					listener.mapViewRegionDidChange();
					Log.d(TAG, "Scroll event!");
				}
			}
		}
		return super.onTouchEvent(ev);
	}
	
	@Override
	public void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		if (getZoomLevel() != oldZoomLevel) {
			if (null != listener) {
				listener.mapViewRegionDidChange();
				Log.d(TAG, String.format("Zoom event! - Level: %d", getZoomLevel()));
			}
			oldZoomLevel = getZoomLevel();
		} 
	}
	
	public void addListener(SBAMapViewListener listener) {
		this.listener = listener;
	}
	
	public void removeListener(SBAMapViewListener listener) {
		this.listener = null;
	}

}
