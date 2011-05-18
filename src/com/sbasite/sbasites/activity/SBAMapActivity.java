
package com.sbasite.sbasites.activity;

import greendroid.app.GDMapActivity;
import greendroid.widget.ActionBarItem;
import greendroid.widget.ActionBarItem.Type;

import java.util.ArrayList;
import java.util.List;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.sbasite.sbasites.R;
import com.sbasite.sbasites.SBASitesApplication;
import com.sbasite.sbasites.model.SearchResult;
import com.sbasite.sbasites.model.Site;
import com.sbasite.sbasites.model.SiteLayer;
import com.sbasite.sbasites.view.LayerItemizedOverlay;
import com.sbasite.sbasites.view.MyItemizedOverlay;
import com.sbasite.sbasites.view.SBAMapRegion;
import com.sbasite.sbasites.view.SBAMapView;
import com.sbasite.sbasites.view.SBAMapViewListener;
import com.sbasite.sbasites.view.SiteOverlayItem;

public class SBAMapActivity extends GDMapActivity implements SBAMapViewListener {

	private static final String TAG = SBAMapActivity.class.getSimpleName();
	protected static final int CHOOSE_SEARCH_RESULT = 1;
	protected static final int CHOOSE_SITE_RESULT = 2;
	protected static final int CHOOSE_LAYER = 3;
	private SBAMapView mapView;
	private ImageView welcomeImageView;
	private LocationManager locManager;
	private LocationListener locListener;
	private ProgressBar progressBar;
	private static boolean firstUpdate=true;
	private Location location;

	private MapController mapController;
	private List<Overlay> overlays;
	private LayerItemizedOverlay layerItemizedOverlay;
	private MyLocationOverlay myLocationOverlay;
	private MyItemizedOverlay searchResultOverlay;
	
	GoogleAnalyticsTracker tracker;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		tracker = GoogleAnalyticsTracker.getInstance();

	    // Start the tracker in manual dispatch mode...
	    tracker.start("UA-19524393-2", this);
	    
	    tracker.trackEvent(
	            "Clicks",  // Category
	            "Button",  // Action
	            "clicked", // Label
	            77);  

		setActionBarContentView(R.layout.main);
		addActionBarItem(Type.LocateMyself);
		addActionBarItem(Type.Search);
		addActionBarItem(Type.Locate);

		initMap();
		initLocationManager();
		setUpViews();
	}

	@Override
	protected void onPause() {
		super.onPause();
		myLocationOverlay.disableCompass();
		myLocationOverlay.disableMyLocation();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mapView.setSatellite(getSBASitesApplication().getMapMode());
		myLocationOverlay.enableCompass();
		myLocationOverlay.enableMyLocation();
		manageOverlays();
	}

	/**
	 * Initialise the map and adds the zoomcontrols to the LinearLayout.
	 */
	private void initMap() {
		firstUpdate=true;
		mapView=(SBAMapView)findViewById(R.id.map);
		mapView.setClickable(false);
		
		mapView.setBuiltInZoomControls(false);
		mapView.addListener(this);

		mapController = mapView.getController(); 
		mapController.setCenter(getPoint(46.0730555556, -100.546666667)); // Set center to the center of North America
		mapController.setZoom(4);
		
		overlays = mapView.getOverlays();
		
		myLocationOverlay = new MyLocationOverlay(this, mapView);
		myLocationOverlay.enableMyLocation();
		myLocationOverlay.enableCompass();
		overlays.add(myLocationOverlay);
		
		Drawable marker = getResources().getDrawable(R.drawable.yellow_icon);
		marker.setBounds(0, 0, marker.getIntrinsicWidth(), marker.getIntrinsicHeight());
		layerItemizedOverlay = new LayerItemizedOverlay(marker, mapView);
		overlays.add(layerItemizedOverlay);
		
		/*
		Drawable icon = getResources().getDrawable(R.drawable.marker);
		icon.setBounds(0, 0, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
		searchResultOverlay = new MyItemizedOverlay(icon, mapView);
		overlays.add(searchResultOverlay);
		*/
	}

	/**
	 * Initialise the location manager.
	 */
	private void initLocationManager() {
		locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		locListener = new LocationListener() {

			public void onLocationChanged(Location newLocation) {
				location = newLocation;
				manageOverlays();
			}

			public void onProviderDisabled(String arg0) {
			}

			public void onProviderEnabled(String arg0) {
			}

			public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			}
		};
		locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
				locListener);

	}

	public void manageOverlays() {
		mapView.setClickable(false);
		layerItemizedOverlay.hideBalloon();
		removeOverlaysNotinView();
		removeDisabledLayers();
		
		if (myLocationOverlay.getMyLocation() == null) {
			if (overlays.contains(myLocationOverlay)) {
				overlays.remove(myLocationOverlay);
			}
			myLocationOverlay = new MyLocationOverlay(this, mapView);
			myLocationOverlay.enableMyLocation();
			myLocationOverlay.enableCompass();
			overlays.add(myLocationOverlay);
		}

		SBAMapRegion region = new SBAMapRegion();
		double latSpan = ((mapView.getLatitudeSpan() / 1000000.0)/2.0);
		double longSpan = ((mapView.getLongitudeSpan() / 1000000.0)/2.0);
		double latCenter = (mapView.getMapCenter().getLatitudeE6()/1000000.0);
		double longCenter = (mapView.getMapCenter().getLongitudeE6()/1000000.0);
		region.maxLat = (latCenter + latSpan);
		region.minLat = (latCenter -  latSpan);
		region.maxLong = (longCenter + longSpan);
		region.minLong = (longCenter - longSpan);

		ArrayList<SiteLayer> layers = getSBASitesApplication().getLayers();
		ArrayList<SiteLayer> activeLayers = new ArrayList<SiteLayer>();
		
		for (SiteLayer siteLayer : layers) {
			if (siteLayer.activated == true) {
				activeLayers.add(siteLayer);
			}
		}
		
		SiteLayer[] layer = new SiteLayer[activeLayers.size()];
		layer = activeLayers.toArray(layer);
		String layersString = SiteLayer.activeLayersToString(layer);
		ArrayList<Site> sites = Site.loadSitesForRegionInLayer(getApplicationContext(), region.minLat, region.maxLat, region.minLong, region.maxLong, layersString);
		getSBASitesApplication().setCurrentSites(sites);

		for (Site site : sites) {
			boolean addToMap = true;
			for (int i = 0; i < layerItemizedOverlay.size(); i++) {
				if (layerItemizedOverlay.getItem(i).getSite().mobileKey.equalsIgnoreCase(site.mobileKey)) {
					addToMap = false;
					break;
				}
			}
			if (addToMap == true) {
				layerItemizedOverlay.addOverlay(new SiteOverlayItem(site));
			}
		}
		mapView.invalidate();
		mapView.setClickable(true);
	}

	public void removeOverlaysNotinView() {
		ArrayList<SiteOverlayItem> overlaysToRemove = new ArrayList<SiteOverlayItem>();
		for (int i = 0; i < layerItemizedOverlay.size(); i++) {
			SiteOverlayItem siteOverlay = layerItemizedOverlay.getItem(i);
			if (isLocationVisible(siteOverlay.getPoint()) == false) {
				overlaysToRemove.add(siteOverlay);
			}
		}
		layerItemizedOverlay.removeOverlays(overlaysToRemove);
	}

	private void removeDisabledLayers() {
		ArrayList<SiteOverlayItem> overlaysToRemove = new ArrayList<SiteOverlayItem>();
		for (int i = 0; i < layerItemizedOverlay.size(); i++) {
			SiteOverlayItem siteOverlay = layerItemizedOverlay.getItem(i);
			if (siteOverlay.getSite().siteLayer.activated == false) {
				overlaysToRemove.add(siteOverlay);
			}
		}
		layerItemizedOverlay.removeOverlays(overlaysToRemove);
	}

	private void setUpViews() {
		progressBar = (ProgressBar)findViewById(R.id.progressBar1);
		progressBar.setVisibility(View.GONE);
		welcomeImageView=(ImageView)findViewById(R.id.imageView1);
		welcomeImageView.setVisibility(View.VISIBLE);
	}

	@Override
	public void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (intent.hasExtra(SearchListActivity.SEARCH_RESULT)) {
			SearchResult result = intent.getParcelableExtra(SearchListActivity.SEARCH_RESULT);
			welcomeImageView.setVisibility(View.GONE);
			mapView.setClickable(true);
			Log.d(TAG, String.format("Latitude: %d, Longitude: %d", result.coordinates.getLatitudeE6(), result.coordinates.getLongitudeE6()));
			navigateToLocation(result.coordinates.getLatitudeE6(), result.coordinates.getLongitudeE6(), mapView);
			manageOverlays();
			//OverlayItem resultOverlayItem = new OverlayItem(result.coordinates, result.title, " ");
			//searchResultOverlay.addItem(resultOverlayItem);
			//mapView.invalidate();
		}
	}

	@Override
	public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
		switch (position) {
		case 0:
			double[] gps = getGPS();
			GeoPoint location = getPoint(gps[0], gps[1]);
			if (location != null) {
				welcomeImageView.setVisibility(View.GONE);
				mapView.setClickable(true);
				mapView.getController().setCenter(location);
				mapView.getController().setZoom(13);
				manageOverlays();

			} else {
				AlertDialog.Builder dialog = new AlertDialog.Builder(SBAMapActivity.this);
				dialog.setTitle("Location Error");
				dialog.setMessage("Your location could not be found");
				dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int Click) {
						dialog.dismiss();
					}
				});
				dialog.show();
			}
			break;
		case 1:
			onSearchRequested();
			break;
		case 2:
			startActivityForResult(new Intent(this, SiteListActivity.class), CHOOSE_SITE_RESULT);
			break;
		default:
			return super.onHandleActionBarItemClick(item, position);
		}
		return true;
	}

	private double[] getGPS() {
		List<String> providers = locManager.getProviders(true);
		Location l = null;
		for (int i=providers.size()-1; i>=0; i--) {
			l = locManager.getLastKnownLocation(providers.get(i));
			if (l != null) break;
		}
		double[] gps = new double[2];
		if (l != null) {
			gps[0] = l.getLatitude();
			gps[1] = l.getLongitude();
		}
		return gps;
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	public void onActivityResult(int requestCode,int resultCode,Intent data) 
	{
		if (CHOOSE_SEARCH_RESULT == requestCode && RESULT_OK == resultCode) {
			
		} else if (CHOOSE_SITE_RESULT == requestCode && RESULT_OK == resultCode) {
			String mobileKey = data.getStringExtra("MobileKey");
			Site site = Site.siteForMobileKey(getApplicationContext(), mobileKey);
			if (null != site) {
				navigateToSite(site.latitude, site.longitude, mapView);
			}
			SiteOverlayItem siteOverlay = new SiteOverlayItem(site);
			layerItemizedOverlay.addOverlay(siteOverlay);
			mapView.invalidate();
			layerItemizedOverlay.setFocus(siteOverlay);
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
		manageOverlays();
	}

	public static void navigateToLocation (double latitude, double longitude, SBAMapView mv) {
		GeoPoint p = new GeoPoint((int) latitude, (int) longitude); //new GeoPoint
		MapController mc = mv.getController();
		mc.setCenter(p);
		mc.setZoom(13); //zoom
	}

	public static void navigateToSite (double latitude, double longitude, SBAMapView mv) {
		GeoPoint p = new GeoPoint((int)(latitude*1000000.0), (int)(longitude*1000000.0)); //new GeoPoint
		MapController mc = mv.getController();
		mc.setCenter(p); //move map to the given point
		mc.setZoom(13); 
	}

	protected SBASitesApplication getSBASitesApplication() {
		return (SBASitesApplication)getApplication();
	}

	private GeoPoint getPoint(double lat, double lon) {
		return(new GeoPoint((int)(lat*1000000.0), (int)(lon*1000000.0)));
	}

	private boolean isLocationVisible(GeoPoint geoPoint)
	{
		Rect currentMapBoundsRect = new Rect();
		Point currentPosition = new Point();

		mapView.getProjection().toPixels(geoPoint, currentPosition);
		mapView.getDrawingRect(currentMapBoundsRect);

		return currentMapBoundsRect.contains(currentPosition.x, currentPosition.y);

	}

	public void mapViewRegionDidChange() {
		int zoomLevel = mapView.getZoomLevel();
		if (!firstUpdate) {
			if (zoomLevel < 12) {
				mapView.getController().setZoom(10);
			}

			//createAndShowMyItemizedOverlay();
			manageOverlays();
		}
		firstUpdate = false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.menu, menu);
		menu.findItem(R.id.instructions).setIntent(
				new Intent(this, Instructions.class));
		menu.findItem(R.id.layers).setIntent(
				new Intent(this, Layers.class));
		menu.findItem(R.id.list_view).setIntent(
				new Intent(this, SiteListActivity.class));
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		super.onOptionsItemSelected(item);
		if (item.getItemId() == R.id.list_view) {
			startActivityForResult(item.getIntent(), CHOOSE_SITE_RESULT);
		} else if (item.getItemId() == R.id.layers) {
			startActivityForResult(item.getIntent(), CHOOSE_LAYER);
		} else {
			startActivity(item.getIntent());
		}

		return true;
	}

}