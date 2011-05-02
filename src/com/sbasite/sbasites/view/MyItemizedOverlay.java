package com.sbasite.sbasites.view;

import java.util.ArrayList;
import java.util.List;
 
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
 
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.readystatesoftware.mapviewballoons.BalloonItemizedOverlay;
 
public class MyItemizedOverlay extends BalloonItemizedOverlay<OverlayItem> {
 
	private ArrayList<OverlayItem> items;
	private Drawable marker;
	private Context context;
	
	public MyItemizedOverlay(Drawable defaultMarker, MapView mapView) {
		super(boundCenterBottom(defaultMarker), mapView);
		context = mapView.getContext();
		items = new ArrayList<OverlayItem>();
		marker = defaultMarker;
	}
 
	@Override
	protected OverlayItem createItem(int index) {
		return (OverlayItem)items.get(index);
	}
 
	@Override
	public int size() {
		return items.size();
 
	}
	
	public void removeOverlays() {
		items.removeAll(items);
		populate();
	}
 
	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.google.android.maps.ItemizedOverlay#draw(android.graphics.Canvas,
	 * com.google.android.maps.MapView, boolean)
	 */
	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		super.draw(canvas, mapView, shadow);
		boundCenterBottom(marker);
 
	}
 
	public void addItem(OverlayItem item) {
		items.add(item);
		populate();
	}
 
}