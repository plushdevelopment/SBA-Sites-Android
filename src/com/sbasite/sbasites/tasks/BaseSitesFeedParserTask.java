package com.sbasite.sbasites.tasks;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import com.sbasite.sbasites.model.Site;
import com.sbasite.sbasites.parsers.FeedParserInterface;

import android.os.AsyncTask;

public abstract class BaseSitesFeedParserTask extends AsyncTask<String, Void, Site[]> implements FeedParserInterface {
	
	String urlStr = "http://map.sbasite.com/Mobile/GetData?Format=";
	private final URL feedURL;
	public static final String FORMAT_JSON = "json";
	public static final String FORMAT_XML = "xml";
	
	// names of the XML tags
	static final String TOTALRECORDSCOUNT = "TotalRecordCount";
	static final String SITES = "Sites";
	protected static final String SITE = "Site";
	static final  String SITENAME = "SiteName";
	static final  String SITECODE = "SiteCode";
	static final  String SITEMOBILEKEY = "MobileKey";
	static final  String SITELAYER = "Layer";
	static final  String SITELATITUDE = "Latitude";
	static final  String SITELONGITUDE = "Longitude";
	static final  String SITEDELETED = "Deleted";
	
	public enum RequestVersion {
		OLD_VERSION, NEW_VERSION;
	}
	
	public enum RequestAction {
		SITES_ADDED, SITES_MODIFIED, SITES_DELETED, SITES_DETAILS;
	}
	
    protected BaseSitesFeedParserTask(String format, String lastUpdated, int skip, int take, RequestVersion version, RequestAction action, String mobileKey) {
        urlStr += format;
        if (null != lastUpdated) {
        	urlStr += "&LastUpdated=" + lastUpdated;
        }
        urlStr += "&Skip=" + skip;
        urlStr += "&Take=" + take;
        urlStr += "&Version=" + version;
        if (version == RequestVersion.NEW_VERSION) {
        	urlStr += "&Action=" + action;
        	if ((null != mobileKey) && action == RequestAction.SITES_DETAILS) {
            	urlStr += "&MobileKey=" + mobileKey;
            }
        }
        
        try {
			feedURL = new URL(urlStr);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
    }
    
    protected InputStream getInputStream() {
		try {
			return feedURL.openConnection().getInputStream();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(Site[] sites) {
		
	}

    
    
}
