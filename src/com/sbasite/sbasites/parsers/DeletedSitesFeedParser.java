package com.sbasite.sbasites.parsers;

import java.util.HashMap;

import org.xmlpull.v1.XmlPullParser;

import com.sbasite.sbasites.model.Site;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

public class DeletedSitesFeedParser extends BaseFeedParser {
	
	private static final String TAG = DeletedSitesFeedParser.class.getSimpleName();

	public DeletedSitesFeedParser(Context context, String feedUrl) {
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
								int deletedCount = Site.deleteSiteForMobileKey(context, currentSite.get(SITEMOBILEKEY));
								Log.d(TAG, String.format("%d sites deleted - %s", deletedCount, currentSite.get(SITEMOBILEKEY)));
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
