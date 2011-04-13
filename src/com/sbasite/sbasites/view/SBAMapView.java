package com.sbasite.sbasites.view;

import android.graphics.Canvas;
import android.view.MotionEvent;

import com.google.android.maps.MapView;

public class SBAMapView extends MapView {

	private static final String TAG = "SBAMapView";
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
			//do your thing
			oldZoomLevel = getZoomLevel();
		}
	}

}
