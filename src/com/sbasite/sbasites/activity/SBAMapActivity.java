
package com.sbasite.sbasites.activity;

import greendroid.app.GDMapActivity;
import greendroid.widget.ActionBarItem;
import greendroid.widget.ActionBarItem.Type;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

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
	}

	@Override
	protected void onResume() {
		super.onResume();
		createAndShowMyItemizedOverlay();
	}

	/**
	 * Initialise the map and adds the zoomcontrols to the LinearLayout.
	 */
	private void initMap() {
		mapView=(SBAMapView)findViewById(R.id.map);
		mapView.setClickable(false);
		mapView.setSatellite(false);
		mapView.setTraffic(false);
		mapView.setBuiltInZoomControls(true);
		mapView.addListener(this);
		mapView.getController().setCenter(getPoint(46.0730555556, -100.546666667)); // Set center to the center of North America
		firstUpdate=true;
		mapView.getController().setZoom(4);
	}

	/**
	 * Initialise the location manager.
	 */
	private void initLocationManager() {
		locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		locListener = new LocationListener() {

			public void onLocationChanged(Location newLocation) {
				location = newLocation;
				createAndShowMyItemizedOverlay();
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

	/**
	 * This method will be called whenever a change of the current position
	 * is submitted via the GPS.
	 * @param newLocation
	 */
	protected void createAndShowMyItemizedOverlay() {
		progressBar.setVisibility(View.VISIBLE);
		mapView.setClickable(false);

		List<Overlay> overlays = mapView.getOverlays();

		// first remove old overlay
		if (overlays.size() != 0) {
			for (Iterator<Overlay> iterator = overlays.iterator(); iterator.hasNext();) {
				iterator.next();
				iterator.remove();
			}
		}
		
		List<String> providers = locManager.getProviders(true);

		for (int i=providers.size()-1; i>=0; i--) {
			location = locManager.getLastKnownLocation(providers.get(i));
			if (location != null) break;
		}
		
		if (location != null) {
			// transform the location to a geopoint
			GeoPoint geopoint = new GeoPoint(
					(int) (location.getLatitude() * 1E6), (int) (location
							.getLongitude() * 1E6));

			// initialize icon
			Drawable icon = getResources().getDrawable(R.drawable.marker);
			icon.setBounds(0, 0, icon.getIntrinsicWidth(), icon
					.getIntrinsicHeight());

			// create my overlay and show it
			MyItemizedOverlay overlay = new MyItemizedOverlay(icon);
			OverlayItem item = new OverlayItem(geopoint, "My Location", null);
			overlay.addItem(item);
			mapView.getOverlays().add(overlay);
		}
		
			
		Drawable marker = getResources().getDrawable(R.drawable.yellow_icon);
		marker.setBounds(0, 0, marker.getIntrinsicWidth(), marker.getIntrinsicHeight());

		LayerItemizedOverlay itemizedOverlay = new LayerItemizedOverlay(marker, mapView);

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
		SiteLayer[] layer = new SiteLayer[layers.size()];
		layer = layers.toArray(layer);
		String layersString = SiteLayer.activeLayersToString(layer);
		ArrayList<Site> sites = Site.loadSitesForRegionInLayer(getApplicationContext(), region.minLat, region.maxLat, region.minLong, region.maxLong, layersString);
		getSBASitesApplication().setCurrentSites(sites);

		if (sites.size() != 0) {
			for (Iterator<Site> iterator = sites.iterator(); iterator.hasNext();) {
				itemizedOverlay.addOverlay(new SiteOverlayItem(iterator.next()));
			}
		}

		mapView.getOverlays().add(itemizedOverlay);

		progressBar.setVisibility(View.GONE);
		mapView.setClickable(true);
		
		// redraw map
		mapView.invalidate();
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
			createAndShowMyItemizedOverlay();
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
				createAndShowMyItemizedOverlay();
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
			SearchResult result = data.getParcelableExtra(SearchListActivity.SEARCH_RESULT);
			welcomeImageView.setVisibility(View.GONE);
			mapView.setClickable(true);
			Log.d(TAG, String.format("Latitude: %d, Longitude: %d", result.coordinates.getLatitudeE6(), result.coordinates.getLongitudeE6()));
			navigateToLocation(result.coordinates.getLatitudeE6(), result.coordinates.getLongitudeE6(), mapView);
		} else if (CHOOSE_SITE_RESULT == requestCode && RESULT_OK == resultCode) {
			String mobileKey = data.getStringExtra("MobileKey");
			Site site = Site.siteForMobileKey(getApplicationContext(), mobileKey);
			if (null != site) {
				navigateToSite(site.latitude, site.longitude, mapView);
			}
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
		//updateDisplay();
		createAndShowMyItemizedOverlay();
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

	public void mapViewRegionDidChange() {
		int zoomLevel = mapView.getZoomLevel();
		if (!firstUpdate) {
			if (zoomLevel < 10) {
				mapView.getController().setZoom(10);
			}
			createAndShowMyItemizedOverlay();
		}
		firstUpdate = false;
	}
	
}