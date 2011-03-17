package com.sbasite.sbasites.model;

import android.location.Address;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.maps.GeoPoint;

public class SearchResult implements Parcelable {

	protected final static int RESULT_SITE = 0;
	protected final static int RESULT_ADDRESS = 1;
	protected final static int RESULT_COORDINATES = 2;
	public int type;
	public String title;
	public GeoPoint coordinates;
	
	public SearchResult(int type, String title,
			GeoPoint coordinates) {
		super();
		this.type = type;
		this.title = title;
		this.coordinates = coordinates;
	}
	
	public SearchResult(Site site) {
		super();
		this.type = RESULT_SITE;
		this.title = site.siteName + " " + site.siteCode;
		this.coordinates = getPoint(site.latitude, site.longitude);
	}
	
	public SearchResult(Address address) {
		super();
		this.type = RESULT_ADDRESS;
		this.title = address.getAddressLine(0);
		this.coordinates = getPoint(address.getLatitude(), address.getLongitude());
	}

	public SearchResult(Double latitude, Double longitude) {
		super();
		this.type = RESULT_ADDRESS;
		GeoPoint point = getPoint(latitude, longitude);
		this.title = point.toString();
		this.coordinates = point;
	}
	
	public SearchResult(GeoPoint coordinates) {
		super();
		this.type = RESULT_COORDINATES;
		this.title = coordinates.toString();
		this.coordinates = coordinates;
	}
	
	private GeoPoint getPoint(double lat, double lon) {
		return(new GeoPoint((int)(lat*1000000.0), (int)(lon*1000000.0)));
	}
	
	public SearchResult(Parcel in) {
		type = in.readInt();
		title = in.readString();
		double latitude = in.readDouble();
		double longitude = in.readDouble();
		coordinates = getPoint(latitude, longitude);
	}

	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(type);
		dest.writeString(title);
		dest.writeDouble(coordinates.getLatitudeE6());
		dest.writeDouble(coordinates.getLongitudeE6());
	}
}
