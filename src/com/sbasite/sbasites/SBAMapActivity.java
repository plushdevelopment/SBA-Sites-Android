
package com.sbasite.sbasites;

import java.util.ArrayList;
import java.util.List;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.sbasite.sbasites.R;
import com.sbasite.sbasites.ItemizedOverlay.LayerItemizedOverlay;
import com.sbasite.sbasites.model.SearchResult;
import com.sbasite.sbasites.model.Site;
import com.sbasite.sbasites.model.SiteLayer;
import com.sbasite.sbasites.tasks.LoadSiteForRegionTask;
import com.sbasite.sbasites.tasks.LoadSiteForRegionTask.LoadSiteForRegionTaskResponder;

public class SBAMapActivity extends MapActivity implements LocationListener, LoadSiteForRegionTaskResponder {

	private static final String TAG = SBAMapActivity.class.getSimpleName();
	protected static final int CHOOSE_SEARCH_RESULT = 1;
	protected static final int CHOOSE_SITE_RESULT = 2;
	protected static final int CHOOSE_LAYER = 3;
	private MapView mapView;
	private MapController mapController;
	private EditText searchText;
	private ImageView welcomeImageView;
	private MyLocationOverlay me;
	private Handler messageHandler;
	private UpdateMapsOverlaysThread updateMapOverlaysThread;
	private List<Overlay> mapOverlays;
	private Button btnSearch;
	private ImageButton useLocationButton;
	private LayerItemizedOverlay itemizedOverlay;
	Drawable marker;
	private LocationManager locationManager=null;
	private Location latestLocation;

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
		setContentView(R.layout.main);
		setUpViews();
		setUpLocation();
		//locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
	}

	private void setUpLocation() {
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                60,
                5,
                this);
	}
	
	public void onLocationChanged(Location location) {
		latestLocation = location;
	}
	
	public void onProviderDisabled(String provider) { }

	public void onProviderEnabled(String provider) { }

	public void onStatusChanged(String provider, int status, Bundle extras) { }

	private void setUpViews() {
		// Assign ivars to elements listed in main.xml
		mapView=(MapView)findViewById(R.id.map);
		mapView.setClickable(false);
		mapView.setBuiltInZoomControls(true);
		mapView.setSatellite(true);
		mapController = mapView.getController();
		mapOverlays = mapView.getOverlays();
		marker = this.getResources().getDrawable(R.drawable.yellow_icon);
		marker.setBounds(0, 0, marker.getIntrinsicWidth(), marker.getIntrinsicHeight());
		itemizedOverlay = new LayerItemizedOverlay(marker, mapView);
		mapView.getOverlays().add(itemizedOverlay);
		me=new MyLocationOverlay(this, mapView);
		mapOverlays.add(me);
		mapView.invalidate();

		mapController.setCenter(getPoint(46.0730555556, -100.546666667)); // Set center to the center of North America
		mapController.setZoom(4);

		welcomeImageView=(ImageView)findViewById(R.id.imageView1);
		welcomeImageView.setVisibility(View.VISIBLE);

		btnSearch = (Button)findViewById(R.id.SearchButton);
		btnSearch.setEnabled(true);
		btnSearch.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (searchText.getTextSize() > 0.0) {
					startSearch();
				}
			}
		});
		
		useLocationButton = (ImageButton)findViewById(R.id.SiteIconImageButton);
		useLocationButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(searchText.getWindowToken(), 0);
				
				if (latestLocation != null) {
					GeoPoint location = getPoint(latestLocation.getLatitude(), latestLocation.getLongitude());
					Log.i(TAG, String.format("%s", location.toString()));
						welcomeImageView.setVisibility(View.GONE);
						mapView.setClickable(true);
						mapView.getController().setCenter(location);
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
			}
		});

		searchText=(EditText)findViewById(R.id.searchText);

		messageHandler = new Handler() {
			public void handleMessage(Message msg) {
				if (welcomeImageView.getVisibility() == View.GONE) {

					if (mapView.getZoomLevel() < 11) {
						mapView.getController().setZoom(11);
					} else if (msg.what == 1) {
						updateOverlays();
						//new LoadSiteForRegionTask(getApplicationContext(), SBAMapActivity.this, mapView, getSBASitesApplication().getLayers()).execute();
					}
				}
			}
		};

		updateMapOverlaysThread = new UpdateMapsOverlaysThread(mapView, messageHandler);
		new Thread(updateMapOverlaysThread).start();
	}

	@Override
	protected boolean isLocationDisplayed() {
		return true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		//updateMapOverlaysThread.setEnabled(false);
		locationManager.removeUpdates(this);
		me.disableMyLocation();
	}

	@Override
	protected void onResume() {
		super.onResume();
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60, 5, this);
		me.enableMyLocation();
		//updateMapOverlaysThread.setEnabled(true);
	}

	protected void startSearch() {
		String addressInput = searchText.getText().toString(); //Get input text
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(searchText.getWindowToken(), 0);
		Intent searchIntent = new Intent(this, SearchListActivity.class);
		searchIntent.putExtra("search_text", addressInput);
		startActivityForResult(searchIntent, CHOOSE_SEARCH_RESULT);
		searchText.setText("");
		searchText.setHint("Search");
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
			Log.d(TAG, String.format("%s", mobileKey));
			Site site = Site.siteForMobileKey(getApplicationContext(), mobileKey);
			Log.d(TAG, site.toString());
			if (null != site) {
				navigateToSite(site.latitude, site.longitude, mapView);
			}
		} else if (CHOOSE_LAYER == requestCode && RESULT_OK == resultCode) {

		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
		updateOverlays();
	}

	public static void navigateToLocation (double latitude, double longitude, MapView mv) {
		GeoPoint p = new GeoPoint((int) latitude, (int) longitude); //new GeoPoint
		mv.displayZoomControls(true); //display Zoom (seems that it doesn't work yet)
		MapController mc = mv.getController();
		mc.animateTo(p); //move map to the given point
		//int zoomlevel = mv.getMaxZoomLevel(); //detect maximum zoom level
		mc.setZoom(15); //zoom
		//mv.setSatellite(false); //display only "normal" mapview	
	}
	
	public static void navigateToSite (double latitude, double longitude, MapView mv) {
		GeoPoint p = new GeoPoint((int)(latitude*1000000.0), (int)(longitude*1000000.0)); //new GeoPoint
		mv.displayZoomControls(true); //display Zoom (seems that it doesn't work yet)
		MapController mc = mv.getController();
		mc.animateTo(p); //move map to the given point
		//int zoomlevel = mv.getMaxZoomLevel(); //detect maximum zoom level
		mc.setZoom(15); //zoom
		//mv.setSatellite(false); //display only "normal" mapview	
	}

	public void userLocationClicked(View view)  
	{
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(searchText.getWindowToken(), 0);
		
		if (me != null) {
			GeoPoint location = me.getMyLocation();
			if (null == location) {
				AlertDialog.Builder dialog = new AlertDialog.Builder(SBAMapActivity.this);
				dialog.setTitle("Location Error");
				dialog.setMessage("Your location could not be found");
				dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int Click) {
						dialog.dismiss();
					}
				});
				dialog.show();
			} else {
				welcomeImageView.setVisibility(View.GONE);
				mapView.setClickable(true);
				mapView.getController().setCenter(location);
				mapView.getController().setZoom(13);
			}
		}
	}  

	protected SBASitesApplication getSBASitesApplication() {
		return (SBASitesApplication)getApplication();
	}

	private GeoPoint getPoint(double lat, double lon) {
		return(new GeoPoint((int)(lat*1000000.0), (int)(lon*1000000.0)));
	}

	public void updateOverlays() {
		
		double latSpan = ((mapView.getLatitudeSpan() / 1000000.0)/2.0);
		double longSpan = ((mapView.getLongitudeSpan() / 1000000.0)/2.0);
		double latCenter = (mapView.getMapCenter().getLatitudeE6()/1000000.0);
		double longCenter = (mapView.getMapCenter().getLongitudeE6()/1000000.0);
		double maxLat = (latCenter + latSpan);
		double minLat = (latCenter -  latSpan);
		double maxLong = (longCenter + longSpan);
		double minLong = (longCenter - longSpan);
		itemizedOverlay.removeAllItems();
		ArrayList<Site> sites = new ArrayList<Site>();
		for (SiteLayer layer : getSBASitesApplication().getLayers()) {
			if (layer.activated == true) {
				sites.addAll(Site.loadSitesForRegionInLayer(getApplicationContext(), minLat, maxLat, minLong, maxLong, layer));
			}
		}
		getSBASitesApplication().setCurrentSites(sites);
		if (!sites.isEmpty()) {
			itemizedOverlay.addOverlays(sites);
			for (Site site : sites) {
				itemizedOverlay.addOverlay(new SiteOverlayItem(site));
			}
		}
		
		
	}

	public void sitesLoading() {
		// TODO Auto-generated method stub
		
	}

	public void sitesLoadCancelled() {
		// TODO Auto-generated method stub
		
	}

	public void sitesLoaded(ArrayList<Site> sites) {
		
	}
}