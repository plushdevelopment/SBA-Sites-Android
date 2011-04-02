package com.sbasite.sbasites.tasks;

import java.util.ArrayList;

import com.google.android.maps.MapView;
import com.sbasite.sbasites.model.Site;
import com.sbasite.sbasites.model.SiteLayer;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class LoadSiteForRegionTask extends AsyncTask<Void, Void, ArrayList<Site>> {
	
	private static final String TAG = LoadSiteForRegionTask.class.getSimpleName();
	private Context context;
	private LoadSiteForRegionTaskResponder responder;
	private static double MIN_LONG;
	private static double MAX_LONG;
	private static double MIN_LAT;
	private static double MAX_LAT;
	private double latCenter;
	private double longCenter;
	private double latSpan;
	private double longSpan;
	private static ArrayList<SiteLayer> layers;

	public interface LoadSiteForRegionTaskResponder {
		public void sitesLoading();
		public void sitesLoadCancelled();
		public void sitesLoaded(ArrayList<Site> sites);
	}
	
	public LoadSiteForRegionTask(Context context, LoadSiteForRegionTaskResponder responder, MapView mapView, ArrayList<SiteLayer> layers) {
		this.context = context;
		this.responder = responder;
		this.layers = layers;
		
		latSpan = ((mapView.getLatitudeSpan() / 1000000.0)/2.0);
		longSpan = ((mapView.getLongitudeSpan() / 1000000.0)/2.0);
		latCenter = (mapView.getMapCenter().getLatitudeE6()/1000000.0);
		longCenter = (mapView.getMapCenter().getLongitudeE6()/1000000.0);
		MAX_LAT = (latCenter + latSpan);
		MIN_LAT = (latCenter -  latSpan);
		MAX_LONG = (longCenter + longSpan);
		MIN_LONG = (longCenter - longSpan);
	}

	@Override
	protected ArrayList<Site> doInBackground(Void... params) {
		ArrayList<Site> sites = new ArrayList<Site>();
		for (SiteLayer layer : layers) {
			if (layer.activated == true) {
				sites.addAll(Site.loadSitesForRegionInLayer(context, MIN_LAT, MAX_LAT, MIN_LONG, MAX_LONG, layer));
			}
		}
		return sites;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		responder.sitesLoading();
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		responder.sitesLoadCancelled();
	}

	@Override
	protected void onPostExecute(ArrayList<Site> sites) {
		super.onPostExecute(sites);
		responder.sitesLoaded(sites);
		Log.d(TAG, "onPostExecute()" + sites.toString());
	}

	@Override
	protected void onProgressUpdate(Void... values) {
		super.onProgressUpdate(values);
	}

}
