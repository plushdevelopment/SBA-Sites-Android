package com.sbasite.sbasites.parsers;

import java.util.HashMap;

import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import com.sbasite.sbasites.model.Site;
import com.sbasite.sbasites.model.SiteLayer;

public class ModifiedSitesFeedParser extends BaseFeedParser {

private static final String TAG = NewSitesFeedParser.class.getSimpleName();
	
	public ModifiedSitesFeedParser(Context context, String feedUrl) {
		super(context, feedUrl);
	}

	public int parse() {
		XmlPullParser parser = Xml.newPullParser();
		int total = 0;
		try {
			// auto-detect the encoding from the stream
			parser.setInput(this.getInputStream(), null);
			int eventType = parser.getEventType();
			HashMap<String,String> currentSite = null;
			boolean done = false;
			while (eventType != XmlPullParser.END_DOCUMENT && !done){
				String name = null;
				switch (eventType){
					case XmlPullParser.START_DOCUMENT:
						break;
					case XmlPullParser.START_TAG:
						name = parser.getName();
						if (name.equalsIgnoreCase(TOTALRECORDSCOUNT)){
							total = Integer.parseInt(parser.nextText());
						}else if (name.equalsIgnoreCase(SITE)){
							currentSite = new HashMap<String,String>();
						} else if (currentSite != null){
							String textValue = parser.nextText();
							currentSite.put(name, textValue);
						}
						break;
					case XmlPullParser.END_TAG:
						name = parser.getName();
						if (name.equalsIgnoreCase(SITE) && currentSite != null){
							if (currentSite.containsKey(SITEMOBILEKEY) && (null != currentSite.get(SITEMOBILEKEY))) {
								Log.d(TAG, currentSite.get(SITEMOBILEKEY));
								Site site = Site.siteForMobileKey(context, currentSite.get(SITEMOBILEKEY));
								if (null == site) {
									site = new Site(context);
								} else {
									Log.e(TAG, "Site already exists");
								}
								site.mobileKey = currentSite.get(SITEMOBILEKEY);
								site.siteName = currentSite.get(SITENAME);
								site.siteCode = currentSite.get(SITECODE);
								site.siteStatus = currentSite.get(SITESTATUS);
								site.lastUpdated = currentSite.get(SITELASTUPDATED);
								site.latitude = Double.parseDouble(currentSite.get(SITELATITUDE));
								site.longitude = Double.parseDouble(currentSite.get(SITELONGITUDE));
								// Check to see if Layer exists
								// If it does, assign the layer to the site, else create a new one
								String layerName = currentSite.get(SITELAYER);
								SiteLayer layer = SiteLayer.layerForName(context, layerName);
								if (null == layer) {
									layer = new SiteLayer(context, layerName);
									layer.save();
								}
								site.siteLayer = layer;
								site.save();
								site = null;
								currentSite = null;
							}
						} else if (name.equalsIgnoreCase(TOTALRECORDSCOUNT)){
							done = true;
						}
						break;
				}
				eventType = parser.next();
			}
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
			throw new RuntimeException(e);
		}
		return total;
	}


}
