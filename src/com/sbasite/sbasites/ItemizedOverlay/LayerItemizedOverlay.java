package com.sbasite.sbasites.ItemizedOverlay;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import com.google.android.maps.MapView;
import com.readystatesoftware.mapviewballoons.BalloonItemizedOverlay;
import com.sbasite.sbasites.SiteDetailActivity;
import com.sbasite.sbasites.SiteOverlayItem;
import com.sbasite.sbasites.model.Site;

public class LayerItemizedOverlay extends BalloonItemizedOverlay<SiteOverlayItem> {

	private static final String TAG = LayerItemizedOverlay.class.getSimpleName();
	private ArrayList<SiteOverlayItem> mapOverlays = new ArrayList<SiteOverlayItem>();
	private Context context;
	
	public LayerItemizedOverlay(Drawable defaultMarker, MapView mapView) {
		super(boundCenterBottom(defaultMarker), mapView);
		context = mapView.getContext();
		populate();
	}
	
	public void removeAllItems() {
		mapOverlays.removeAll(mapOverlays);
		populate();
	}
	
	public void addOverlay(SiteOverlayItem overlay) {
	    mapOverlays.add(overlay);
	    populate();
	}
	
	public void addOverlays(ArrayList<Site> overlays) {
		mapOverlays.removeAll(mapOverlays);
		Log.d(TAG, String.format("mapOverlays.size() == %d", mapOverlays.size()));
		for (int i = 0; i < overlays.size(); i++) {
			Site site = overlays.get(i);
			SiteOverlayItem overlayItem = new SiteOverlayItem(site);
			mapOverlays.add(overlayItem);
			Log.d(TAG, String.format("mapOverlays.size() == %d", mapOverlays.size()));
		}
		Log.d(TAG, String.format("mapOverlays.size() == %d", mapOverlays.size()));
		populate();
		Log.d(TAG, String.format("mapOverlays.size() == %d", mapOverlays.size()));
	}

	@Override
	protected SiteOverlayItem createItem(int i) {
		Log.d(TAG, String.format("createItem(%d), mapOverlays.size() == %d", i, mapOverlays.size()));
		return mapOverlays.get(i);
	}

	@Override
	public int size() {
		return mapOverlays.size();
	}

	@Override
	protected boolean onBalloonTap(int index) {
		SiteOverlayItem item = mapOverlays.get(index);
		Intent intent = new Intent(context, SiteDetailActivity.class);
		intent.putExtra("MobileKey", item.getSite().mobileKey);
		context.startActivity(intent);
		return true;
	}
	
	
	
}
