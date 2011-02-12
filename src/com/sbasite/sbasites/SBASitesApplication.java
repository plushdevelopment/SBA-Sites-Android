package com.sbasite.sbasites;

import java.io.IOException;
import java.util.ArrayList;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SBASitesApplication extends com.activeandroid.Application {

	public ArrayList<Site> currentSites;
	public SQLiteDatabase database;
	public String lastUpdate;
	public int totalRecordsCount;
	public int totalRecordsUpdated = 0;

	@Override
	public void onCreate() {
		super.onCreate();
		SitesSqliteOpenHelper helper = new SitesSqliteOpenHelper(this);
		currentSites = new ArrayList<Site>();
		
        try { helper.createDataBase(); }
        catch (IOException ioe) { throw new Error("Unable to create database"); }
 
        try { database = helper.openDataBase(); }
        catch (SQLException sqle) { throw sqle; }
        
		/*
		currentSites = new ArrayList<Site>();
		
		final XMLHandler myExampleHandler = new XMLHandler(this);
        myExampleHandler.delegate = this;
		
		try {
            // Create a URL we want to load some xml-data from.
			if(lastUpdate == null) {
				lastUpdate = "2008-09-24T15:05:04";
			}
			String urlString = "http://map.sbasite.com/Mobile/GetData?LastUpdate=" + lastUpdate + "&Skip=0&Take=0";
			Log.d("Request String", urlString);
            URL url = new URL(urlString);

            // Get a SAXParser from the SAXPArserFactory.
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();

            // Get the XMLReader of the SAXParser we created.
            XMLReader xr = sp.getXMLReader();
            // Create a new ContentHandler and apply it to the XML-Reader
            xr.setContentHandler(myExampleHandler);
           
            // Parse the xml-data from our URL. 
            xr.parse(new InputSource(url.openStream()));
            // Parsing has finished. 
            
            Log.v("Total Records", Integer.toString(totalRecordsCount));
		} catch (Exception e) {
            // Display any Error to the GUI. 
		}
		
		new Thread(new Runnable() {
		    public void run() {
		    	for (int i = 0; i < totalRecordsCount; i++) {
					try {
			            // Create a URL we want to load some xml-data from.
						String urlString = "http://map.sbasite.com/Mobile/GetData?LastUpdate=" + lastUpdate + "&Skip=" + i + "&Take=1";
			            URL url = new URL(urlString);

			            // Get a SAXParser from the SAXPArserFactory.
			            SAXParserFactory spf = SAXParserFactory.newInstance();
			            SAXParser sp = spf.newSAXParser();

			            // Get the XMLReader of the SAXParser we created.
			            XMLReader xr = sp.getXMLReader();
			            // Create a new ContentHandler and apply it to the XML-Reader
			            xr.setContentHandler(myExampleHandler);
			           
			            /// Parse the xml-data from our URL.
			            xr.parse(new InputSource(url.openStream()));
			            // Parsing has finished.
			            totalRecordsUpdated++;
			            Log.v("Updated Count", Integer.toString(i));
					} catch (Exception e) {
			            // Display any Error to the GUI.
					}
				}
		    }
		  }).start();
		  */
	}
	
	public void setCurrentSites(ArrayList<Site> currentSites) {
		this.currentSites = currentSites;
		Log.d("Application - Set Sites", this.currentSites.toString());
	}

	public ArrayList<Site> getCurrentSites() {
		Log.d("Application - Get Sites", this.currentSites.toString());
		return currentSites;
	}
	
	public void addSite(Site site) {
		
	}
	
}
