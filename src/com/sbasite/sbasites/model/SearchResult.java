package com.sbasite.sbasites.model;

import android.location.Address;

import com.google.android.maps.GeoPoint;

public class SearchResult {

	public SearchResultType type;
	public String title;
	public GeoPoint coordinates;
	
	public SearchResult(SearchResultType type, String title,
			GeoPoint coordinates) {
		super();
		this.type = type;
		this.title = title;
		this.coordinates = coordinates;
	}
	
	public SearchResult(Site site) {
		super();
		this.type = SearchResultType.RESULT_SITE;
		this.title = site.siteName + " " + site.siteCode;
		this.coordinates = getPoint(site.latitude, site.longitude);
	}
	
	public SearchResult(Address address) {
		super();
		this.type = SearchResultType.RESULT_ADDRESS;
		this.title = address.getAddressLine(0);
		this.coordinates = getPoint(address.getLatitude(), address.getLongitude());
	}

	public SearchResult(Double latitude, Double longitude) {
		super();
		this.type = SearchResultType.RESULT_ADDRESS;
		GeoPoint point = getPoint(latitude, longitude);
		this.title = point.toString();
		this.coordinates = point;
	}
	
	public SearchResult(GeoPoint coordinates) {
		super();
		this.type = SearchResultType.RESULT_COORDINATES;
		this.title = coordinates.toString();
		this.coordinates = coordinates;
	}
	
	private GeoPoint getPoint(double lat, double lon) {
		return(new GeoPoint((int)(lat*1000000.0), (int)(lon*1000000.0)));
	}
}
