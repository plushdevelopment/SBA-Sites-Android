package com.sbasite.sbasites.XML;

import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import com.sbasite.sbasites.model.Site;

public class NewSitesFeedParser extends BaseFeedParser {

	private static final String TAG = NewSitesFeedParser.class.getSimpleName();
	
	public NewSitesFeedParser(Context context, String feedUrl) {
		super(context, feedUrl);
	}

	public int parse() {
		XmlPullParser parser = Xml.newPullParser();
		int total = 0;
		try {
			// auto-detect the encoding from the stream
			parser.setInput(this.getInputStream(), null);
			int eventType = parser.getEventType();
			Site currentSite = null;
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
							currentSite = new Site(context);
						} else if (currentSite != null){
							if (name.equalsIgnoreCase(SITEMOBILEKEY)){
								currentSite.mobileKey = parser.nextText();
							} else if (name.equalsIgnoreCase(SITENAME)){
								currentSite.siteName = parser.nextText();
							} else if (name.equalsIgnoreCase(SITECODE)){
								currentSite.siteCode = parser.nextText();
							} else if (name.equalsIgnoreCase(SITELAYER)){
								currentSite.siteLayer = parser.nextText();
							} else if (name.equalsIgnoreCase(SITEDELETED)){
								currentSite.deleted = Integer.parseInt(parser.nextText());
							} else if (name.equalsIgnoreCase(SITELATITUDE)){
								currentSite.latitude = Double.parseDouble((parser.nextText()));
							} else if (name.equalsIgnoreCase(SITELONGITUDE)){
								currentSite.longitude = Double.parseDouble((parser.nextText()));
							}
						}
						break;
					case XmlPullParser.END_TAG:
						name = parser.getName();
						if (name.equalsIgnoreCase(SITE) && currentSite != null){
							Site existingSite = Site.siteForMobileKey(context, currentSite.mobileKey); 
							if (existingSite != null) {
								existingSite.delete();
							}
							if (currentSite.deleted == 0) {
								currentSite.save();
							}
						} else if (name.equalsIgnoreCase(SITES)){
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
