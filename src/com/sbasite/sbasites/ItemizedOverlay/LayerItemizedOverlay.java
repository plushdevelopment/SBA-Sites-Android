package com.sbasite.sbasites.ItemizedOverlay;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.readystatesoftware.mapviewballoons.BalloonItemizedOverlay;
import com.sbasite.sbasites.SiteDetailActivity;
import com.sbasite.sbasites.SiteOverlayItem;

public class LayerItemizedOverlay extends BalloonItemizedOverlay<SiteOverlayItem> {

	private ArrayList<SiteOverlayItem> mapOverlays = new ArrayList<SiteOverlayItem>();
	private Context context;
	
	public LayerItemizedOverlay(Drawable defaultMarker, MapView mapView) {
		super(boundCenterBottom(defaultMarker), mapView);
		context = mapView.getContext();
	}
	
	public void removeAllItems() {
		mapOverlays.removeAll(mapOverlays);
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
