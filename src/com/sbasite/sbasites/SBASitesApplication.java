package com.sbasite.sbasites;

import java.io.IOException;
import java.util.ArrayList;
import com.sbasite.sbasites.Controller.SitesSqliteOpenHelper;
import com.sbasite.sbasites.XMLHandler.XMLHandlerDelegate;
import com.sbasite.sbasites.model.DBMetadata;
import com.sbasite.sbasites.model.Site;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class SBASitesApplication extends com.activeandroid.Application implements XMLHandlerDelegate {

	public ArrayList<Site> currentSites = new ArrayList<Site>();
	public SQLiteDatabase database;
	public DBMetadata metadata;
	public String lastUpdate;
	public int take;
	public int skip;
	public int totalRecordsCount;
	public int totalRecordsUpdated;
	public final XMLHandler myExampleHandler = new XMLHandler(this, this);

	@Override
	public void onCreate() {
		super.onCreate();
		
		SitesSqliteOpenHelper helper = new SitesSqliteOpenHelper(this);
		
        try { helper.createDataBase(); }
        catch (IOException ioe) { throw new Error("Unable to create database"); }
 
        try { database = helper.openDataBase(); }
        catch (SQLException sqle) { throw sqle; }
        
        /*
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
            XMLHandler handler = new XMLHandler(this, this);
			xr.setContentHandler(handler);
           
            // Parse the xml-data from our URL. 
            xr.parse(new InputSource(url.openStream()));
            // Parsing has finished.
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (int i = 9000; i < totalRecordsCount; i+=500) {
			try {
				// Create a URL we want to load some xml-data from.

				String urlString = "http://map.sbasite.com/Mobile/GetData?LastUpdate=" + metadata.lastUpdate + "&Skip=" + i + "&Take=500&Version=2&Action=2";
				URL url = new URL(urlString);

				// Get a SAXParser from the SAXPArserFactory.
				SAXParserFactory spf = SAXParserFactory.newInstance();
				SAXParser sp = spf.newSAXParser();

				// Get the XMLReader of the SAXParser we created.
				XMLReader xr = sp.getXMLReader();
				// Create a new ContentHandler and apply it to the XML-Reader
				XMLHandler handler = new XMLHandler(this, this);
				xr.setContentHandler(handler);

				/// Parse the xml-data from our URL.
				xr.parse(new InputSource(url.openStream()));
				// Parsing has finished.
				Log.e("HTTP Request", urlString);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		*/
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

	public void didEndDocument() {
		/*new Thread(new Runnable() {
			public void run() {
				try {
					// Create a URL we want to load some xml-data from.

					String urlString = "http://map.sbasite.com/Mobile/GetData?LastUpdate=" + metadata.lastUpdate + "&Skip=" + metadata.skip + "&Take=" + metadata.take;
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
					metadata.skip += metadata.take;
					metadata.save();
					Log.e("HTTP Request", urlString);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		  }).start();*/
	}

	public void setTotalRecordsCount(int totalRecordsCount) {
		this.totalRecordsCount = totalRecordsCount;
	}

}
