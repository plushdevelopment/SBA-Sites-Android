
package com.sbasite.sbasites.activity;

import greendroid.app.GDMapActivity;
import greendroid.widget.ActionBarItem;
import greendroid.widget.ActionBarItem.Type;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.sbasite.sbasites.R;
import com.sbasite.sbasites.SBASitesApplication;
import com.sbasite.sbasites.controller.SitesSqliteOpenHelper;
import com.sbasite.sbasites.model.SearchResult;
import com.sbasite.sbasites.model.Site;
import com.sbasite.sbasites.model.SiteLayer;
import com.sbasite.sbasites.view.LayerItemizedOverlay;
import com.sbasite.sbasites.view.SBAMapRegion;
import com.sbasite.sbasites.view.SBAMapView;
import com.sbasite.sbasites.view.SBAMapViewListener;

public class SBAMapActivity extends GDMapActivity implements LocationListener, SBAMapViewListener {

	private static final String TAG = SBAMapActivity.class.getSimpleName();
	protected static final int CHOOSE_SEARCH_RESULT = 1;
	protected static final int CHOOSE_SITE_RESULT = 2;
	protected static final int CHOOSE_LAYER = 3;
	private SBAMapView mapView;
	private MapController mapController;
	private ImageView welcomeImageView;
	private MyLocationOverlay me;
	private List<Overlay> mapOverlays;
	private LayerItemizedOverlay itemizedOverlay;
	Drawable marker;
	//private LocationManager locationManager=null;
	private ProgressBar progressBar;
	private boolean firstUpdate=true;

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
		// Call super always
		super.onCreate(savedInstanceState);
		// Inflate your view
		setActionBarContentView(R.layout.main);
		addActionBarItem(Type.LocateMyself);
		addActionBarItem(Type.Search);
		addActionBarItem(Type.Locate);
		setUpViews();
	}

	private void setUpViews() {
		// Assign ivars to elements listed in main.xml
		mapView=(SBAMapView)findViewById(R.id.map);
		mapView.setClickable(false);
		mapView.setSatellite(false);
		mapView.setTraffic(false);
		progressBar = (ProgressBar)findViewById(R.id.progressBar1);
		progressBar.setVisibility(View.GONE);

		mapView.addListener(this);
		mapController = mapView.getController();
		mapOverlays = mapView.getOverlays();
		marker = this.getResources().getDrawable(R.drawable.yellow_icon);
		marker.setBounds(0, 0, marker.getIntrinsicWidth(), marker.getIntrinsicHeight());
		me=new MyLocationOverlay(this, mapView);
		mapOverlays.add(me);

		mapController.setCenter(getPoint(46.0730555556, -100.546666667)); // Set center to the center of North America
		mapController.setZoom(4);

		welcomeImageView=(ImageView)findViewById(R.id.imageView1);
		welcomeImageView.setVisibility(View.VISIBLE);
	}

	@Override
	protected void onPause() {
		super.onPause();
		//locationManager.removeUpdates(this);
		me.disableMyLocation();
	}

	@Override
	protected void onResume() {
		super.onResume();
		//setUpLocation();
		me.enableMyLocation();
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
				//mapView.getController().animateTo(location);
				mapView.getController().setZoom(13);
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
		LocationManager lm = (LocationManager) getSystemService(
				Context.LOCATION_SERVICE);
		List<String> providers = lm.getProviders(true);

		Location l = null;

		for (int i=providers.size()-1; i>=0; i--) {
			l = lm.getLastKnownLocation(providers.get(i));
			if (l != null) break;
		}

		double[] gps = new double[2];
		if (l != null) {
			gps[0] = l.getLatitude();
			gps[1] = l.getLongitude();
		}

		return gps;
	}

	/*
	private void setUpLocation() {
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(
				LocationManager.GPS_PROVIDER,
				60,
				5,
				this);
	}
	 */

	public void onLocationChanged(Location location) {
		//latestLocation = location;
	}

	public void onProviderDisabled(String provider) { }

	public void onProviderEnabled(String provider) { }

	public void onStatusChanged(String provider, int status, Bundle extras) { }

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
	}

	public static void navigateToLocation (double latitude, double longitude, SBAMapView mv) {
		GeoPoint p = new GeoPoint((int) latitude, (int) longitude); //new GeoPoint
		MapController mc = mv.getController();
		mc.setCenter(p);
		mc.setZoom(15); //zoom
	}

	public static void navigateToSite (double latitude, double longitude, SBAMapView mv) {
		GeoPoint p = new GeoPoint((int)(latitude*1000000.0), (int)(longitude*1000000.0)); //new GeoPoint
		MapController mc = mv.getController();
		mc.setCenter(p); //move map to the given point
		mc.setZoom(15); 
	}

	protected SBASitesApplication getSBASitesApplication() {
		return (SBASitesApplication)getApplication();
	}

	private GeoPoint getPoint(double lat, double lon) {
		return(new GeoPoint((int)(lat*1000000.0), (int)(lon*1000000.0)));
	}

	public void mapViewRegionDidChange() {
		if (!firstUpdate) {
			new OverlayTask().execute();
		}
		firstUpdate = false;
	}

	class OverlayTask extends AsyncTask<Void, Void, Void> {
		
		@Override
		public void onPreExecute() {
			progressBar.setVisibility(View.VISIBLE);
			if (itemizedOverlay!=null) {
				mapOverlays.remove(itemizedOverlay);
				mapView.invalidate();	
				itemizedOverlay=null;
				
				
				
			}
		}

		@Override
		protected Void doInBackground(Void... unused) {
			
			itemizedOverlay=new LayerItemizedOverlay(marker, mapView);
			
			SBAMapRegion region = new SBAMapRegion();
			double latSpan = ((mapView.getLatitudeSpan() / 1000000.0));
			double longSpan = ((mapView.getLongitudeSpan() / 1000000.0));
			double latCenter = (mapView.getMapCenter().getLatitudeE6()/1000000.0);
			double longCenter = (mapView.getMapCenter().getLongitudeE6()/1000000.0);
			region.maxLat = (latCenter + latSpan);
			region.minLat = (latCenter -  latSpan);
			region.maxLong = (longCenter + longSpan);
			region.minLong = (longCenter - longSpan);
			
			ArrayList<SiteLayer> layers = SiteLayer.layers(getBaseContext());
			SiteLayer[] layer = new SiteLayer[layers.size()];
			layer = layers.toArray(layer);
			String layersString = SiteLayer.activeLayersToString(layer);
			ArrayList<Site> sites = Site.loadSitesForRegionInLayer(getBaseContext(), region.minLat, region.maxLat, region.minLong, region.maxLong, layersString);
			getSBASitesApplication().setCurrentSites(sites);
			if (!sites.isEmpty()) {
				itemizedOverlay.addOverlays(sites);
			}

			return null;
		}

		@Override
		public void onPostExecute(Void unused) {
			mapOverlays.add(itemizedOverlay);
			mapView.invalidate();
			progressBar.setVisibility(View.GONE);
		}
	}
}