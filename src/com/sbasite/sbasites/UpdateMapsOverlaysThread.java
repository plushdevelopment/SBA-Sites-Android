package com.sbasite.sbasites;

import java.util.ArrayList;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
 
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.sbasite.sbasites.model.Site;
 
public class UpdateMapsOverlaysThread implements Runnable {
 
	private static final String TAG = "UpdateMapOverlaysThread";
	private static final int INTERVAL = 1500;
	private static final int PAUSE_INTERVAL = 7500;
	private final Double DISTANCE_CHANGE = .01; //in km
 
	private boolean enabled = true;
	private boolean interrupted = false;
	private boolean firstUpdate = true;
 
	private int zoomLevel = 0;
	private Handler mainThreadMessageHandler = null;
	private MapView mapView = null;
	private GeoPoint mapCenter = null;
 
	/**
	 * @param context
	 * @param mapView
	 */
	public UpdateMapsOverlaysThread(MapView mapView, Handler messageHandler) {
		this.mapView = mapView;
		this.mainThreadMessageHandler = messageHandler;
		firstUpdate = true;
	}
 
	public void run() {
 
		Log.d(TAG, "Start");
 
		boolean fireOverlayUpdater = false;
 
		while(!interrupted) {
 
			if(enabled) {
				//Log.v(TAG, "zoomLevel: " + zoomLevel + " map zoomLevel: " + mapView.getZoomLevel());
				if(zoomLevel !=  mapView.getZoomLevel() ){
		    		zoomLevel = mapView.getZoomLevel();
		    		fireOverlayUpdater = true;
		    	} 
 
				//Log.v(TAG, "mapCenter: " + mapCenter + " map mapCenter: " + mapView.getMapCenter());
				if(mapCenter == null || DistanceCalculator.calculateDistance(mapCenter, mapView.getMapCenter()) > DISTANCE_CHANGE) {
		    		mapCenter = mapView.getMapCenter();
		    		fireOverlayUpdater = true;
		    	}
 
		    	if(fireOverlayUpdater || firstUpdate) {
 
		    		Log.d(TAG, "Need to update overlays");
		    		
		    		//if(!sites.isEmpty()) {
		    			// Send the overlays to the UI Thread
		    			Message message = mainThreadMessageHandler.obtainMessage();
		    			message.what = 1;
		    			//message.obj = sites;
		    			mainThreadMessageHandler.sendMessage(message);
		    		//}
		    		
		    		//Log.d("Sites", sites.toString());
			    	// Reset
		    		fireOverlayUpdater = false;
		    		firstUpdate = false;
		    	}
 
		    	// Wait until time to fire again
				try {
					Thread.sleep(INTERVAL);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
 
			} else {
				// Wait until your turn to work again
				try {
					Thread.sleep(PAUSE_INTERVAL);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
 
			}
		}
 
	}

	/**
	 * @param enabled
	 */
	public void setEnabled (boolean enabled) {
		Log.d(TAG, "setEnabled: " + enabled);
		this.enabled = enabled;
	}
 
	/**
	 *
	 */
	public void killThread() {
		Log.d(TAG, "killThread");
		interrupted = true;
	}
 
}
