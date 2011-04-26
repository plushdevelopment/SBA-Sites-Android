package com.sbasite.sbasites.view;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Log;
import com.google.android.maps.MapView;
import com.readystatesoftware.mapviewballoons.BalloonItemizedOverlay;
import com.sbasite.sbasites.activity.SiteDetailActivity;
import com.sbasite.sbasites.model.Site;

public class LayerItemizedOverlay extends BalloonItemizedOverlay<SiteOverlayItem> {

	private static final String TAG = LayerItemizedOverlay.class.getSimpleName();
	private ArrayList<SiteOverlayItem> mapOverlays = new ArrayList<SiteOverlayItem>();
	private Context context;
	
	public LayerItemizedOverlay(Drawable defaultMarker, MapView mapView) {
		super(boundCenterBottom(defaultMarker), mapView);
		context = mapView.getContext();
	}
	
	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		super.draw(canvas, mapView, false);
	}

	public void removeOverlay(SiteOverlayItem overlay) {
	    mapOverlays.remove(overlay);
	    populate();
	}
	
	public void removeOverlays(ArrayList<SiteOverlayItem> overlays) {
	    mapOverlays.removeAll(overlays);
	    populate();
	}

	public void addOverlay(SiteOverlayItem overlay) {
	    mapOverlays.add(overlay);
	    populate();
	}

	@Override
	protected SiteOverlayItem createItem(int i) {
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
