package com.sbasite.sbasites;

import greendroid.app.GDApplication;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.sbasite.sbasites.controller.SitesSqliteOpenHelper;
import com.sbasite.sbasites.model.Site;
import com.sbasite.sbasites.model.SiteLayer;
import com.sbasite.sbasites.tasks.LoadDeletedSitesAsyncTask;
import com.sbasite.sbasites.tasks.LoadDeletedSitesAsyncTask.LoadDeletedSitesResponder;
import com.sbasite.sbasites.tasks.LoadDeletedSitesAsyncTask.LoadDeletedSitesResult;
import com.sbasite.sbasites.tasks.LoadModifiedSitesAsyncTask;
import com.sbasite.sbasites.tasks.LoadModifiedSitesAsyncTask.LoadModifiedSitesResult;
import com.sbasite.sbasites.tasks.LoadMoreSitesAsyncTask;
import com.sbasite.sbasites.tasks.LoadModifiedSitesAsyncTask.LoadModifiedSitesResponder;
import com.sbasite.sbasites.tasks.LoadMoreSitesAsyncTask.LoadMoreSitesResponder;
import com.sbasite.sbasites.tasks.LoadMoreSitesAsyncTask.LoadMoreSitesResult;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.SQLException;

public class SBASitesApplication extends GDApplication implements LoadMoreSitesResponder, LoadDeletedSitesResponder, LoadModifiedSitesResponder {

	//private static final String TAG = SBASitesApplication.class.getSimpleName();
	//private static final String APPLICATION_PREFERENCES = "app_prefs";
	public ArrayList<Site> currentSites = new ArrayList<Site>();
	private ArrayList<SiteLayer> layers;
	private SharedPreferences prefs;
	public boolean updatedDeletedSites = false;
	public boolean updatedModifiedSites = false;
	public boolean updatedAddedSites = false;
	public int totalAdds = 0;
	public int totalUpdates = 0;
	public int totalDeletes = 0;
	public int totalAdded = 0;
	public int totalUpdated = 0;
	public int totalDeleted = 0;
	public int take = 1;
	public String lastAddedUpdated;
	public String lastModifiedUpdated;
	public String lastDeletedUpdated;

	@Override
	public void onCreate() {
		super.onCreate();
		/*
		prefs = this.getSharedPreferences(APPLICATION_PREFERENCES, Context.MODE_PRIVATE);
		totalAdded = prefs.getInt("totalAdded", 0);
		totalUpdated = prefs.getInt("totalUpdated", 0);
		totalDeleted = prefs.getInt("totalDeleted", 0);
		lastAddedUpdated = prefs.getString("lastAddedUpdated", "2011-03-22T22:33:49.399Z");
		lastModifiedUpdated = prefs.getString("lastModifiedUpdated", "2011-03-22T23:43:54.122Z");
		lastDeletedUpdated = prefs.getString("lastDeletedUpdated", "2011-03-22T22:33:43.351Z");
		*/
		
		SitesSqliteOpenHelper helper = new SitesSqliteOpenHelper(this);
		
		try { helper.createDataBase(); }
        catch (IOException ioe) { throw new Error("Unable to create database"); }
 
        try { helper.openDataBase(); }
        catch (SQLException sqle) { throw sqle; }
        
        helper = null;
		
		// Starts loading deleted sites
		//String urlString = "http://map.sbasite.com/Mobile/GetData?LastUpdate=" + lastDeletedUpdated + "&Skip=" + totalDeleted + "&Take=" + take + "&Version=2&Action=3";
		//Log.i(TAG, urlString);
        //new LoadDeletedSitesAsyncTask(this, this).execute(urlString);
		
        /*
		// Starts loading new sites
		String urlString = "http://map.sbasite.com/Mobile/GetData?LastUpdate=2008-02-15&Skip=" + totalAdded + "&Take=" + take + "&Version=2&Action=1";
		Log.i(TAG, urlString);
        new LoadMoreSitesAsyncTask(this, this).execute(urlString);
        */
        
        /*
		// Starts loading modified sites
		String urlString = "http://map.sbasite.com/Mobile/GetData?LastUpdate=2008-02-15&Skip=" + totalUpdated + "&Take=" + take + "&Version=2&Action=2";
		Log.i(TAG, urlString);
        new LoadModifiedSitesAsyncTask(this, this).execute(urlString);
        */
        
        
	}
	
	@Override
    public synchronized Class<?> getHomeActivityClass() {
        return SBAMapActivity.class;
    }

	public synchronized void setCurrentSites(ArrayList<Site> currentSites) {
		this.currentSites = currentSites;
	}

	public synchronized ArrayList<Site> getCurrentSites() {
		if (currentSites == null) {
			currentSites = new ArrayList<Site>();
		}
		return currentSites;
	}

	public synchronized ArrayList<SiteLayer> getLayers() {
		if (null == layers) {
			layers = SiteLayer.layers(this);
		}
		return layers;
	}

	public void loadingMoreSites() {
		
	}

	public void loadedNewSites(LoadMoreSitesResult result) {
		totalAdds = result.totalRecordsCount;
		totalAdded+=take;
		Editor editor = prefs.edit();
		if (totalAdded < totalAdds) {
			editor.putInt("totalAdded", totalAdded);
			editor.commit();
			String urlString = "http://map.sbasite.com/Mobile/GetData?LastUpdate=" + lastAddedUpdated + "&Skip=" + totalAdded + "&Take=" + take + "&Version=2&Action=1";
	        new LoadMoreSitesAsyncTask(this, this).execute(urlString);
		} else {
			totalAdded = 0;
			editor.putInt("totalAdded", totalAdded);
			lastAddedUpdated = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(new Date());
			editor.putString("lastAddedUpdated", lastAddedUpdated);
			editor.commit();
			
			// Starts loading modified sites
			String urlString = "http://map.sbasite.com/Mobile/GetData?LastUpdate=" + lastModifiedUpdated + "&Skip=" + totalUpdated + "&Take=" + take + "&Version=2&Action=2";
	        new LoadModifiedSitesAsyncTask(this, this).execute(urlString);
		}
	}

	public void loadingDeletedSites() {
		
	}

	public void loadedDeletedSites(LoadDeletedSitesResult result) {
		totalDeletes = result.totalRecordsCount;
		totalDeleted+=take;
		Editor editor = prefs.edit();
		if (totalDeleted < totalDeletes) {
			editor.putInt("totalDeleted", totalDeleted);
			editor.commit();
			String urlString = "http://map.sbasite.com/Mobile/GetData?LastUpdate=" + lastDeletedUpdated + "&Skip=" + totalDeleted + "&Take=" + take + "&Version=2&Action=3";
	        new LoadDeletedSitesAsyncTask(this, this).execute(urlString);
		} else {
			totalDeleted = 0;
			editor.putInt("totalDeleted", totalDeleted);
			lastDeletedUpdated = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(new Date());
			editor.putString("lastDeletedUpdated", lastDeletedUpdated);
			editor.commit();
			
			// Starts loading new sites
			String urlString = "http://map.sbasite.com/Mobile/GetData?LastUpdate=" + lastAddedUpdated + "&Skip=" + totalAdded + "&Take=" + take + "&Version=2&Action=1";
	        new LoadMoreSitesAsyncTask(this, this).execute(urlString);
		}
	}

	public void loadingModifiedSites() {
		// TODO Auto-generated method stub
		
	}

	public void loadedModifiedSites(LoadModifiedSitesResult result) {
		totalUpdates = result.totalRecordsCount;
		totalUpdated+=take;
		Editor editor = prefs.edit();
		if (totalUpdated < totalUpdates) {
			editor.putInt("totalUpdated", totalUpdated);
			editor.commit();
			String urlString = "http://map.sbasite.com/Mobile/GetData?LastUpdate=" + lastModifiedUpdated + "&Skip=" + totalUpdated + "&Take=" + take + "&Version=2&Action=2";
	        new LoadModifiedSitesAsyncTask(this, this).execute(urlString);
		} else {
			totalUpdated = 0;
			editor.putInt("totalUpdated", totalUpdated);
			lastModifiedUpdated = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(new Date());
			editor.putString("lastModifiedUpdated", lastModifiedUpdated);
			editor.commit();
		}
	}

}
