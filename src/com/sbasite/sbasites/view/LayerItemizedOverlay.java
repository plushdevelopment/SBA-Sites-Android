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
	private ArrayList<SiteOverlayItem> myMapOverlays = new ArrayList<SiteOverlayItem>();
	private Context context;
	
	public LayerItemizedOverlay(Drawable defaultMarker, MapView mapView) {
		super(boundCenterBottom(defaultMarker), mapView);
		context = mapView.getContext();
		populate();
	}
	
	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		super.draw(canvas, mapView, false);
	}

	public void removeOverlay(SiteOverlayItem overlay) {
	    myMapOverlays.remove(overlay);
	    setLastFocusedIndex(-1);
	    populate();
	}
	
	public void removeOverlays(ArrayList<SiteOverlayItem> overlays) {
	    myMapOverlays.removeAll(overlays);
	    setLastFocusedIndex(-1);
	    populate();
	}

	public void addOverlay(SiteOverlayItem overlay) {
	    myMapOverlays.add(overlay);
	    setLastFocusedIndex(-1);
	    populate();
	}

	@Override
	protected SiteOverlayItem createItem(int i) {
		return myMapOverlays.get(i);
	}

	@Override
	public int size() {
		return myMapOverlays.size();
	}

	@Override
	protected boolean onBalloonTap(int index) {
		SiteOverlayItem item = myMapOverlays.get(index);
		Intent intent = new Intent(context, SiteDetailActivity.class);
		intent.putExtra("MobileKey", item.getSite().mobileKey);
		context.startActivity(intent);
		return true;
	}
	
}
