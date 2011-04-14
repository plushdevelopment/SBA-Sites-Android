package com.sbasite.sbasites.view;

public interface SBAMapViewListener {
	public void mapViewRegionWillChange(SBAMapView mapView, SBAMapRegion region);
	public void mapViewRegionDidChange(SBAMapView mapView, SBAMapRegion region);
}
