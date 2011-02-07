package com.sbasite.sbasites;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;


import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import static com.sbasite.sbasites.SitesSqliteOpenHelper.*;
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
		
		
        try { helper.createDataBase(); }
        catch (IOException ioe) { throw new Error("Unable to create database"); }
 
        try { database = helper.openDataBase(); }
        catch (SQLException sqle) { throw sqle; }
        
		currentSites = new ArrayList<Site>();
		
		final XMLHandler myExampleHandler = new XMLHandler(this);
        myExampleHandler.delegate = this;
		
		try {
            // Create a URL we want to load some xml-data from.
			if(lastUpdate == null) {
				lastUpdate = "2008-09-24T15:05:04";
			}
			String urlString = "http://map.sbasite.com/Mobile/GetData?LastUpdate=" + lastUpdate + "&Skip=" + totalRecordsUpdated + "&Take=0";
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
						String urlString = "http://map.sbasite.com/Mobile/GetData?LastUpdate=" + lastUpdate + "&Skip=" + i + "&Take=1Ky";
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
	}
	
	public void setCurrentSites(ArrayList<Site> currentSites) {
		this.currentSites = currentSites;
	}

	public ArrayList<Site> getCurrentSites() {
		return currentSites;
	}
	
	public ArrayList<Site> loadSitesForRegion(double minLat, double maxLat, double minLong, double maxLong) {
		
		
		
		
	    // Execute query
		String where = String.format("SITE_LATITUDE > %f AND SITE_LATITUDE < %f AND SITE_LONGITUDE > %f AND SITE_LONGITUDE < %f", minLat, maxLat, minLong, maxLong);
		Log.d("DB", where);
		Cursor cursor = database.query(SITES_TABLE, null, where, null, null, null, String.format("%s", SITE_NAME));
		
		//  Create ArrayList to hold constructed results
		ArrayList<Site> sites = new ArrayList<Site>();
		
		//  Loop through the results
		cursor.moveToFirst();
		Site site;
		if (!cursor.isAfterLast()) {
			do {
				// Initialize site object
				site = new Site();
				site.latitude = cursor.getDouble(1);
				site.longitude = cursor.getDouble(2);
				site.deleted = cursor.getInt(3);
				site.address = cursor.getString(4);
				site.agl = cursor.getString(5);
				site.bta = cursor.getString(6);
				site.city = cursor.getString(7);
				site.contact = cursor.getString(8);
				site.county = cursor.getString(9);
				site.email = cursor.getString(10);
				site.lastUpdated = cursor.getString(11);
				site.mobileKey = cursor.getString(12);
				site.mta = cursor.getString(13);
				site.phone = cursor.getString(14);
				site.siteCode = cursor.getString(15);
				//site.siteLayer = cursor.getString(16);
				site.siteName = cursor.getString(17);
				site.siteStatus = cursor.getString(18);
				site.stateProvince = cursor.getString(19);
				site.structureHeight = cursor.getString(20);
				site.structureID = cursor.getString(21);
				site.structureType = cursor.getString(22);
				site.zip = cursor.getString(23);
				//  Add site to the array
				sites.add(site);
			} while (cursor.moveToNext());
		}
		Log.d("Sites", sites.toString());
		return sites;
	  }
	
	public void addSite(Site site) {
		
	}
	
}
