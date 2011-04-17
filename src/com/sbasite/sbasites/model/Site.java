package com.sbasite.sbasites.model;

import java.util.ArrayList;
import android.content.Context;
import com.activeandroid.ActiveRecordBase;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.android.maps.GeoPoint;
import com.sbasite.sbasites.R;

@Table(name = "SITES")	
public class Site extends ActiveRecordBase<Site> {
	
	public Site(Context context) { super(context); }
	
	@Column(name = "SITE_LATITUDE")
	public double latitude;
	
	@Column(name = "SITE_LONGITUDE")
	public double longitude;
	
	@Column(name = "SITE_DELETED")
	public int deleted;
	
	@Column(name = "SITE_MOBILEKEY")
	public String mobileKey;
	
	@Column(name = "SITE_CODE")
	public String siteCode;
	
	@Column(name = "SITE_LAYER")
	public SiteLayer siteLayer;

	@Column(name = "SITE_NAME")
	public String siteName;
	
	@Column(name = "SITE_ADDRESS")
	public String address;
	
	@Column(name = "SITE_AGL")
	public String agl;
	
	@Column(name = "SITE_BTA")
	public String bta;
	
	@Column(name = "SITE_CITY")
	public String city;
	
	@Column(name = "SITE_CONTACT")
	public String contact;
	
	@Column(name = "SITE_COUNTY")
	public String county;
	
	@Column(name = "SITE_EMAIL")
	public String email;
	
	@Column(name = "SITE_LASTUPDATED")
	public String lastUpdated;
	
	@Column(name = "SITE_MTA")
	public String mta;
	
	@Column(name = "SITE_PHONE")
	public String phone;
	
	@Column(name = "SITE_STATUS")
	public String siteStatus;
	
	@Column(name = "SITE_STATEPROVINCE")
	public String stateProvince;
	
	@Column(name = "SITE_STRUCTUREHEIGHT")
	public String structureHeight;
	
	@Column(name = "SITE_STRUCTUREID")
	public String structureID;
	
	@Column(name = "SITE_STRUCTURETYPE")
	public String structureType;
	@Column(name = "SITE_ZIP")
    public String zip;
    
	public static ArrayList<Site> loadSitesForRegion(Context context, double minLat, double maxLat, double minLong, double maxLong) {
		return Site.query(context, Site.class, null, "SITE_LATITUDE BETWEEN " + minLat + " AND " + maxLat + " AND SITE_LONGITUDE BETWEEN " + minLong + " AND " + maxLong, "SITE_NAME", "100");
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
		// SELECT * FROM SITES WHERE SITE_NAME LIKE '%a%' OR SITE_CODE LIKE '%a%'
		return Site.query(context, Site.class, null, "SITE_NAME LIKE '%" + addressInput + "%' OR SITE_CODE LIKE '%" + addressInput + "%'", "SITE_NAME", "10");
	}

	public boolean detailsLoaded() {
		if (null == address) { 
			return false;
		} else if (agl == null) {
			return false;
		} else if (bta == null) {
			return false;
		} else if (city == null) {
			return false;
		} else if (contact == null) {
			return false;
		} else if (county == null) {
			return false;
		} else if (email == null) {
			return false;
		} else if (lastUpdated == null) {
			return false;
		} else if (mta == null) {
			return false;
		} else if (phone == null) {
			return false;
		} else if (siteStatus == null) {
			return false;
		} else if (stateProvince == null) {
			return false;
		} else if (structureHeight == null) {
			return false;
		} else if (structureID == null) {
			return false;
		} else if (structureType == null) {
			return false;
		} else if (zip == null) {
			return false;
		}
		return true;
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

	public static ArrayList<Site> loadSitesForRegionInLayer(
			Context context, double minLat, double maxLat,
			double minLong, double maxLong, SiteLayer layer) {
		return Site.query(context, Site.class, null, "SITE_LATITUDE BETWEEN " + minLat + " AND " + maxLat + " AND SITE_LONGITUDE BETWEEN " + minLong + " AND " + maxLong + " AND SITE_LAYER = " + layer.getId(), "SITE_NAME");
	}

	public GeoPoint getPoint() {
		return(new GeoPoint((int)(latitude*1000000.0), (int)(longitude*1000000.0)));
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
	
}