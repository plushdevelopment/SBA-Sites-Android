package com.sbasite.sbasites.parsers;

import java.util.HashMap;
import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import com.sbasite.sbasites.model.Site;

public class SiteDetailsFeedParser extends BaseFeedParser {

	private static final String TAG = SiteDetailsFeedParser.class.getSimpleName();
	
	public SiteDetailsFeedParser(Context context, String feedUrl) {
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
							currentSite.put(name, parser.nextText());
							/*
							} else if (name.equalsIgnoreCase(SITEADRESS)){
								currentSite.address = parser.nextText();
							} else if (name.equalsIgnoreCase(SITEAGL)){
								currentSite.agl = parser.nextText();
							} else if (name.equalsIgnoreCase(SITEBTA)){
								currentSite.bta = parser.nextText();
							} else if (name.equalsIgnoreCase(SITECITY)){
								currentSite.city = parser.nextText();
							} else if (name.equalsIgnoreCase(SITECONTACT)){
								currentSite.contact = parser.nextText();
							} else if (name.equalsIgnoreCase(SITEEMAIL)){
								currentSite.email = parser.nextText();
							} else if (name.equalsIgnoreCase(SITEHEIGHT)){
								currentSite.structureHeight = parser.nextText();
							} else if (name.equalsIgnoreCase(SITEID)){
								currentSite.structureID = parser.nextText();
							} else if (name.equalsIgnoreCase(SITELASTUPDATED)){
								currentSite.lastUpdated = parser.nextText();
							} else if (name.equalsIgnoreCase(SITEMTA)){
								currentSite.mta = parser.nextText();
							} else if (name.equalsIgnoreCase(SITEPHONE)){
								currentSite.phone = parser.nextText();
							} else if (name.equalsIgnoreCase(SITESTATE)){
								currentSite.stateProvince = parser.nextText();
							} else if (name.equalsIgnoreCase(SITESTATUS)){
								currentSite.siteStatus = parser.nextText();
							} else if (name.equalsIgnoreCase(SITETYPE)){
								currentSite.structureType = parser.nextText();
							} else if (name.equalsIgnoreCase(SITEZIP)){
								currentSite.zip = parser.nextText();
							}
							*/
						}
						break;
					case XmlPullParser.END_TAG:
						name = parser.getName();
						if (name.equalsIgnoreCase(SITE) && currentSite != null){
							if (currentSite.containsKey(SITEMOBILEKEY) && (null != currentSite.get(SITEMOBILEKEY))) {
								Site site = Site.siteForMobileKey(context, currentSite.get(SITEMOBILEKEY));
								site.address = currentSite.get(SITEADRESS);
								site.agl = currentSite.get(SITEADRESS);
								site.bta = currentSite.get(SITEADRESS);
								site.city = currentSite.get(SITEADRESS);
								site.contact = currentSite.get(SITEADRESS);
								site.email = currentSite.get(SITEADRESS);
								site.structureHeight = currentSite.get(SITEHEIGHT);
								site.structureID = currentSite.get(SITEID);
								site.lastUpdated = currentSite.get(SITELASTUPDATED);
								site.mta = currentSite.get(SITEMTA);
								site.phone = currentSite.get(SITEPHONE);
								site.stateProvince = currentSite.get(SITESTATE);
								site.siteStatus = currentSite.get(SITESTATUS);
								site.structureType = currentSite.get(SITETYPE);
								site.zip = currentSite.get(SITEZIP);
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

	public Site parseSite() {
		XmlPullParser parser = Xml.newPullParser();
		Site site = null;
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
						if (name.equalsIgnoreCase(SITE)){
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
								site = Site.siteForMobileKey(context, currentSite.get(SITEMOBILEKEY));
								site.address = currentSite.get(SITEADRESS);
								site.agl = currentSite.get(SITEAGL);
								site.bta = currentSite.get(SITEBTA);
								site.city = currentSite.get(SITECITY);
								site.contact = currentSite.get(SITECONTACT);
								site.email = currentSite.get(SITEEMAIL);
								site.structureHeight = currentSite.get(SITEHEIGHT);
								site.structureID = currentSite.get(SITEID);
								site.lastUpdated = currentSite.get(SITELASTUPDATED);
								site.mta = currentSite.get(SITEMTA);
								site.phone = currentSite.get(SITEPHONE);
								site.stateProvince = currentSite.get(SITESTATE);
								site.siteStatus = currentSite.get(SITESTATUS);
								site.structureType = currentSite.get(SITETYPE);
								site.zip = currentSite.get(SITEZIP);
								//site.save();
							}
						} else if (name.equalsIgnoreCase(SITES)){
							done = true;
						}
						break;
				}
				eventType = parser.next();
			}
		} catch (Exception e) {
		}
		return site;
	}
	
}
