package com.sbasite.sbasites.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

public class SBASyncService extends Service {

	private static final String DEBUG_TAG = "SBASyncService";
	private DownloaderTask downloader;
	
	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
		URL url;
        try {
            url = new URL(intent.getDataString());
            downloader = new DownloaderTask();
            downloader.execute(url);
        } catch (MalformedURLException e) {
            Log.e(DEBUG_TAG, "Bad URL", e);
        }

        return Service.START_FLAG_REDELIVERY;
    }

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private class DownloaderTask extends AsyncTask<URL, Void, Boolean> {

		private static final String DEBUG_TAG = "DownloaderTask";
		
		@Override
        protected Boolean doInBackground(URL... params) {
			boolean succeeded = false;
		    URL downloadPath = params[0];
		 
		    if (downloadPath != null) {
		        succeeded = xmlParse(downloadPath);
		    }
		    return succeeded;
        }
 
        private boolean xmlParse(URL downloadPath) {
        	boolean succeeded = false;
        	/*
        	XmlPullParser parser;
        	
        	try {
        		parser = XmlPullParserFactory.newInstance().newPullParser();
        		parser.setInput(downloadPath.openStream(), null);
        		int eventType = -1;
        		// psuedo code--
                // for each found "item" tag, find "link" and "title" tags
                // before end tag "item"
        		
        		while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        String tagName = parser.getName();
                        if (tagName.equals("item")) {

                            ContentValues tutorialData = new ContentValues();
                            // inner loop looking for link and title
                            while (eventType != XmlPullParser.END_DOCUMENT) {
                                if (eventType == XmlPullParser.START_TAG) {
                                    if (parser.getName().equals("link")) {
                                        parser.next();
                                        Log.d(DEBUG_TAG,
                                                "Link: " + parser.getText());
                                       //tutorialData.put(TutListDatabase.COL_URL, parser.getText());
                                    } else if (parser.getName().equals("title")) {
                                        parser.next();
                                        //tutorialData.put(TutListDatabase.COL_TITLE, parser.getText());
                                    }
                                } else if (eventType == XmlPullParser.END_TAG) {
                                    if (parser.getName().equals("item")) {
                                        // save the data, and then continue with
                                        // the outer loop
                                        //getContentResolver().insert(TutListProvider.CONTENT_URI, tutorialData);
                                        break;
                                    }
                                }
                                eventType = parser.next();
                            }
                        }
                    }
                    eventType = parser.next();
                }
        	} catch (XmlPullParserException e) {
        		e.printStackTrace();
        	} catch (IOException e) {
        		e.printStackTrace();
        	}
        	*/
        	return succeeded;
        }
	}
	
}
