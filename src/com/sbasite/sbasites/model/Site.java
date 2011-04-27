package com.sbasite.sbasites.model;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.activeandroid.ActiveRecordBase;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.android.maps.GeoPoint;
import com.sbasite.sbasites.R;

@Table(name = "SITES")	
public class Site extends ActiveRecordBase<Site> {

	private static final String TAG = Site.class.getSimpleName();

	public Site(Context context) { super(context); }

	@Column(name = "SITE_LATITUDE")
	public double latitude;
	@Column(name = "SITE_LONGITUDE")
	public double longitude;
	public int deleted;
	@Column(name = "SITE_MOBILEKEY")
	public String mobileKey;
	@Column(name = "SITE_CODE")
	public String siteCode;
	@Column(name = "SITE_LAYER")
	public SiteLayer siteLayer;
	@Column(name = "SITE_NAME")
	public String siteName;
	public String address;
	public String agl;
	public String bta;
	public String city;
	public String contact;
	public String county;
	public String email;
	public String lastUpdated;
	public String mta;
	public String phone;
	public String siteStatus;
	public String stateProvince;
	public String structureHeight;
	public String structureID;
	public String structureType;
	public String zip;
	
	public String latitudeString() {
		return Double.toString(roundTwoDecimals(latitude));
	}
	
	public String longitudeString() {
		return Double.toString(roundTwoDecimals(longitude));
	}

	public static Site siteForMobileKey(Context context, String mobileKey) {
		return Site.querySingle(context, Site.class, null, String.format("SITE_MOBILEKEY = '%s'", mobileKey), null); 
	}

	public static int deleteSiteForMobileKey(Context context, String mobileKey) {
		return Site.delete(context, Site.class, String.format("SITE_MOBILEKEY = '%s'", mobileKey)); 
	}

	public static Site siteForName(Context context, String siteName) {
		return Site.querySingle(context, Site.class, null, String.format("SITE_NAME = '%s'", siteName), null); 
	}

	public static ArrayList<Site> sitesForSearchText(Context context, String addressInput) {
		return Site.query(context, Site.class, null, "SITE_NAME LIKE '%" + addressInput + "%' OR SITE_CODE LIKE '%" + addressInput + "%'", "SITE_NAME", "10");
	}

	public static ArrayList<Site> loadSitesForRegionInLayer(Context context, double minLat, double maxLat, double minLong, double maxLong, String layers) {
		return Site.query(context, Site.class, null, "SITE_LATITUDE BETWEEN " + minLat + " AND " + maxLat + " AND SITE_LONGITUDE BETWEEN " + minLong + " AND " + maxLong + " AND SITE_LAYER IN " + layers, "SITE_NAME", "20");
	}

	public int getPinIcon() {
		int iconID;
		if (siteLayer.name.matches("Canada")) {
			iconID = R.drawable.canada;
		} else if (siteLayer.name.matches("New Construction")) {
			iconID = R.drawable.new_construction;
		} else if (siteLayer.name.matches("Central America")) {
			iconID = R.drawable.central_america;
		} else if (siteLayer.name.matches("Managed")) {
			iconID = R.drawable.managed;
		} else if (siteLayer.name.matches("Owned")) {
			iconID = R.drawable.owned;
		} else {
			iconID = R.drawable.yellow_icon;
		}
		return iconID;
	}
	
	public GeoPoint getPoint() {
		return(new GeoPoint((int)(latitude*1000000.0), (int)(longitude*1000000.0)));
	}

	double roundTwoDecimals(double d) {
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		return Double.valueOf(twoDForm.format(d));
	}
	
	@Override
	public String toString() {
		String description = 	"\n\n\n" +
		"Name: " + siteName + "\n" + 
		"Code: " + siteCode + "\n" + 
		"Layer: " + siteLayer.name + "\n" + 
		"Status: " + siteStatus + "\n" + 
		"Address: " + address + "\n" + 
		"City: " + city + "\n" + 
		"County: " + county + "\n" + 
		"State/Province: " + stateProvince + "\n" + 
		"Zip: " + zip + "\n" + 
		"Latitude: " + Double.toString(latitude) + "\n" +
		"Longitude: " + Double.toString(longitude) + "\n" +
		"Structure ID: " + structureID + "\n" +
		"Structure Type: " + structureType + "\n" +
		"Height(ft): " + structureHeight + "\n" +
		"Grd Elev: " + agl + "\n" +
		"BTA: " + bta + "\n" +
		"MTA: " + mta + "\n" +
		"Contact: " + contact + "\n" +
		"Phone" + phone + "\n" +
		"Email: " + email + "\n";
		return description;
	}

}