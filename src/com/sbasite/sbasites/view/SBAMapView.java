package com.sbasite.sbasites.view;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.view.MotionEvent;

import com.google.android.maps.MapView;

public class SBAMapView extends MapView {
	
	private static final String TAG = "SBAMapView";
	
	private List<SBAMapViewListener> listeners = new ArrayList<SBAMapViewListener>();

	int oldZoomLevel=-1;

	public SBAMapView(android.content.Context context, android.util.AttributeSet attrs) {
		super(context, attrs);
	}

	public SBAMapView(android.content.Context context, android.util.AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public SBAMapView(android.content.Context context, java.lang.String apiKey) {
		super(context, apiKey);
	}

	public boolean onTouchEvent(MotionEvent ev) {
		if (ev.getAction()==MotionEvent.ACTION_UP) {
			//do your thing
		}
		return super.onTouchEvent(ev);
	}

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
