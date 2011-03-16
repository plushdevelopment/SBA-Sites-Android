package com.sbasite.sbasites.ItemizedOverlay;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.readystatesoftware.mapviewballoons.BalloonItemizedOverlay;

public class LayerItemizedOverlay extends BalloonItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> mapOverlays = new ArrayList<OverlayItem>();
	private Context context;
	
	public LayerItemizedOverlay(Drawable defaultMarker, MapView mapView) {
		super(boundCenter(defaultMarker), mapView);
		context = mapView.getContext();
	}
	
	public void addOverlay(OverlayItem overlay) {
	    mapOverlays.add(overlay);
	    populate();
	}

	@Override
	protected OverlayItem createItem(int i) {
		return mapOverlays.get(i);
	}

	@Override
	public int size() {
		return mapOverlays.size();
	}

	@Override
	protected boolean onBalloonTap(int index) {
		Toast.makeText(context, "onBalloonTap for overlay index " + index,
				Toast.LENGTH_LONG).show();
		return true;
	}
	
}
