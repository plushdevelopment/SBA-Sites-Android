package com.sbasite.sbasites.parsers;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;

public abstract class BaseFeedParser implements FeedParserInterface {

	// names of the XML tags
	static final String TOTALRECORDSCOUNT = "TotalRecordCount";
	static final String SITES = "Sites";
	static final String SITE = "Site";
	static final  String SITENAME = "SiteName";
	static final  String SITECODE = "SiteCode";
	static final  String SITEMOBILEKEY = "MobileKey";
	static final  String SITELAYER = "Layer";
	static final  String SITELATITUDE = "Latitude";
	static final  String SITELONGITUDE = "Longitude";
	static final  String SITEDELETED = "Deleted";
	static final  String SITEAGL = "AGL";
	static final  String SITEADRESS = "Address1";
	static final  String SITEBTA = "BTA";
	static final  String SITECITY = "City";
	static final  String SITECONTACT = "Contact";
	static final  String SITECOUNTY = "County";
	static final  String SITEEMAIL = "Email";
	static final  String SITELASTUPDATED = "LastUpdated";
	static final  String SITEMTA = "MTA";
	static final  String SITEPHONE = "Phone";
	static final  String SITESTATUS = "SiteStatus";
	static final  String SITESTATE = "State";
	static final  String SITEHEIGHT = "StructureHeight";
	static final  String SITEID = "StructureID";
	static final  String SITETYPE = "StructureType";
	static final  String SITEZIP = "Zip";
	
	protected final Context context;
	private final URL feedUrl;
	
	public BaseFeedParser(Context context, String feedUrl) {
		this.context = context;
		try {
			this.feedUrl = new URL(feedUrl);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}
	
	protected InputStream getInputStream() {
		try {
			return feedUrl.openConnection().getInputStream();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
