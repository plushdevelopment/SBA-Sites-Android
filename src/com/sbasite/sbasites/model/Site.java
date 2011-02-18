package com.sbasite.sbasites.model;

import java.util.ArrayList;
import android.content.Context;

import com.activeandroid.ActiveRecordBase;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

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
	public String siteLayer;

	@Column(name = "SITE_NAME")
	public String siteName;
	
	/*
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
    */
    
	public static ArrayList<Site> loadSitesForRegion(Context context, double minLat, double maxLat, double minLong, double maxLong) {
		return Site.query(context, Site.class, new String[] { "SITE_NAME", "SITE_LATITUDE, SITE_LONGITUDE"}, "SITE_LATITUDE BETWEEN " + minLat + " AND " + maxLat + " AND SITE_LONGITUDE BETWEEN " + minLong + " AND " + maxLong, "SITE_NAME");
	}
	
	public static Site siteForMobileKey(Context context, String mobileKey) {
		return Site.querySingle(context, Site.class, null, String.format("SITE_MOBILEKEY = '%s'", mobileKey), null); 
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return siteName + " " + mobileKey;
	}
}