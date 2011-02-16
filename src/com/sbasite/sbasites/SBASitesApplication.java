package com.sbasite.sbasites;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.sbasite.sbasites.model.DBMetadata;
import com.sbasite.sbasites.model.Site;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SBASitesApplication extends com.activeandroid.Application {

	public ArrayList<Site> currentSites;
	public SQLiteDatabase database;
	public DBMetadata metadata;
	public String lastUpdate;
	public int take;
	public int skip;
	public int totalRecordsCount;
	public int totalRecordsUpdated;

	@Override
	public void onCreate() {
		super.onCreate();
		
		/*
		if (metadata == null) {
			metadata = new DBMetadata(this);
			metadata.skip = 0;
			metadata.take = 1;
			metadata.lastUpdate = "2008-09-24";
			metadata.save();
		}
		*/
		
		SitesSqliteOpenHelper helper = new SitesSqliteOpenHelper(this);
		
        try { helper.createDataBase(); }
        catch (IOException ioe) { throw new Error("Unable to create database"); }
 
        try { database = helper.openDataBase(); }
        catch (SQLException sqle) { throw sqle; }
		
		final XMLHandler myExampleHandler = new XMLHandler(this);
        myExampleHandler.delegate = this;
        
        metadata = DBMetadata.last(this, DBMetadata.class);
		
     // TODO 
		// Do something with this array initially
		currentSites = new ArrayList<Site>();
        
		try {
            // Create a URL we want to load some xml-data from.
			String urlString = "http://map.sbasite.com/Mobile/GetData?LastUpdate=" + metadata.lastUpdate + "&Skip=0&Take=0";
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		new Thread(new Runnable() {
		    public void run() {
		    	for (int i = metadata.skip; i < totalRecordsCount; i += metadata.take) {
					try {
			            // Create a URL we want to load some xml-data from.
						
						String urlString = "http://map.sbasite.com/Mobile/GetData?LastUpdate=" + metadata.lastUpdate + "&Skip=" + i + "&Take=" + metadata.take;
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
			            totalRecordsUpdated = i;
			            
			            Log.e("HTTP Request", urlString);
			            metadata.skip = i;
			            metadata.save();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
		    }
		  }).start();
	}
	
	/* (non-Javadoc)
	 * @see com.activeandroid.Application#onTerminate()
	 */
	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
	}

	public void setCurrentSites(ArrayList<Site> currentSites) {
		this.currentSites = currentSites;
	}

	public ArrayList<Site> getCurrentSites() {
		return currentSites;
	}
}
