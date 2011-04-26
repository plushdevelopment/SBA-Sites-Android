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
	private final Double DISTANCE_CHANGE = 1.0; //in km
	int oldZoomLevel=10;
	private GeoPoint mapCenter = null;
	boolean firstUpdate = true;
	boolean userIsPanning = false;

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public SBAMapView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public SBAMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * @param context
	 * @param apiKey
	 */
	public SBAMapView(Context context, String apiKey) {
		super(context, apiKey);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		/*
		int actionId = event.getAction();
		 
		if (actionId == MotionEvent.ACTION_UP && userIsPanning) {

			userIsPanning = false;
			if(mapCenter == null || DistanceCalculator.calculateDistance(mapCenter, getMapCenter()) > DISTANCE_CHANGE) {
				mapCenter = getMapCenter();
				
			}
			
			listener.mapViewRegionDidChange();
			firstUpdate = false;
			
			return true;

		 } else if (actionId == MotionEvent.ACTION_MOVE) {
			 userIsPanning = true;
		 }

		return false;
		*/
		
		
		if (event.getAction()==MotionEvent.ACTION_UP) {
			if (mapCenter == null) {
				mapCenter = getMapCenter();
			}

			int zoomLevel = getZoomLevel();

			if (zoomLevel < 12) {
				getController().setZoom(oldZoomLevel);
			} else {

				//if (DistanceCalculator.calculateDistance(mapCenter, getMapCenter()) > DISTANCE_CHANGE) {
					mapCenter = getMapCenter();
					if (null != listener) {
						listener.mapViewRegionDidChange();
						Log.d(TAG, "Scroll event!");
					}
				//}
			}
		}
		return super.onTouchEvent(event);
		
	}

	@Override
	public void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		int zoomLevel = getZoomLevel();

		if (zoomLevel < 12 && firstUpdate == false) {
			getController().setZoom(oldZoomLevel);
		} else {

			if (zoomLevel != oldZoomLevel) {
				if (null != listener) {
					listener.mapViewRegionDidChange();
					Log.d(TAG, String.format("Zoom event! - Level: %d", getZoomLevel()));
				}
				
			}
		}
		oldZoomLevel = getZoomLevel();
		firstUpdate = false;
	}

	public void addListener(SBAMapViewListener listener) {
		this.listener = listener;
	}

	public void removeListener(SBAMapViewListener listener) {
		this.listener = null;
	}

}
