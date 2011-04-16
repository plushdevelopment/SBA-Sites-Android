package com.sbasite.sbasites.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.sbasite.sbasites.DistanceCalculator;

public class SBAMapView extends MapView {
	
	private static final String TAG = "SBAMapView";
	
	private List<SBAMapViewListener> listeners = new ArrayList<SBAMapViewListener>();

	private static final int INTERVAL = 5500;
	private static final int PAUSE_INTERVAL = 14500;
	private final Double DISTANCE_CHANGE = 4.0; //in km
	
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
			if(mapCenter == null || DistanceCalculator.calculateDistance(mapCenter, getMapCenter()) > DISTANCE_CHANGE) {
	    		mapCenter = getMapCenter();
	    		for (SBAMapViewListener listener : listeners) {
					SBAMapRegion region = new SBAMapRegion();
					double latSpan = ((getLatitudeSpan() / 1000000.0));
					double longSpan = ((getLongitudeSpan() / 1000000.0));
					double latCenter = (getMapCenter().getLatitudeE6()/1000000.0);
					double longCenter = (getMapCenter().getLongitudeE6()/1000000.0);
					region.maxLat = (latCenter + latSpan);
					region.minLat = (latCenter -  latSpan);
					region.maxLong = (longCenter + longSpan);
					region.minLong = (longCenter - longSpan);
					listener.mapViewRegionDidChange(this, region);
				}
			}
		}
		return super.onTouchEvent(ev);
	}
	@Override
	public void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		if (getZoomLevel() != oldZoomLevel) {
			for (SBAMapViewListener listener : listeners) {
				SBAMapRegion region = new SBAMapRegion();
				double latSpan = ((getLatitudeSpan() / 1000000.0));
				double longSpan = ((getLongitudeSpan() / 1000000.0));
				double latCenter = (getMapCenter().getLatitudeE6()/1000000.0);
				double longCenter = (getMapCenter().getLongitudeE6()/1000000.0);
				region.maxLat = (latCenter + latSpan);
				region.minLat = (latCenter -  latSpan);
				region.maxLong = (longCenter + longSpan);
				region.minLong = (longCenter - longSpan);
				listener.mapViewRegionDidChange(this, region);
			}
			oldZoomLevel = getZoomLevel();
		}
	}
	
	public void addListener(SBAMapViewListener listener) {
		synchronized (listeners) {
			listeners.add(listener);
		}
	}
	
	public void removeListener(SBAMapViewListener listener) {
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}

}
