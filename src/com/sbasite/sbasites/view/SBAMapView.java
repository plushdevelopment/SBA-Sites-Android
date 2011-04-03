package com.sbasite.sbasites.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;

import com.google.android.maps.MapView;

public class SBAMapView extends MapView {
	
	private static final String TAG = "SBAMapView";

	public SBAMapView(Context arg0, AttributeSet arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}
	
	int oldZoomLevel=-1;
	 @Override
	 public void dispatchDraw(Canvas canvas) {
	  super.dispatchDraw(canvas);
	  if (getZoomLevel() != oldZoomLevel) {
	   Log.d(TAG, "ZOOOMED");
	   oldZoomLevel = getZoomLevel();
	  }
	 }

}
