
package com.sbasite.sbasites;

import greendroid.app.GDMapActivity;
import greendroid.widget.ActionBarItem;
import greendroid.widget.LoaderActionBarItem;
import greendroid.widget.NormalActionBarItem;
import greendroid.widget.ActionBarItem.Type;

import java.util.ArrayList;
import java.util.List;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.sbasite.sbasites.R;
import com.sbasite.sbasites.ItemizedOverlay.LayerItemizedOverlay;
import com.sbasite.sbasites.model.SearchResult;
import com.sbasite.sbasites.model.Site;
import com.sbasite.sbasites.model.SiteLayer;
import com.sbasite.sbasites.tasks.LoadSiteForRegionTask;
import com.sbasite.sbasites.tasks.LoadSiteForRegionTask.LoadSiteForRegionTaskResponder;
import com.sbasite.sbasites.view.SBAMapRegion;
import com.sbasite.sbasites.view.SBAMapView;
import com.sbasite.sbasites.view.SBAMapViewListener;

public class SBAMapActivity extends GDMapActivity implements LocationListener, LoadSiteForRegionTaskResponder, SBAMapViewListener {

	private static final String TAG = SBAMapActivity.class.getSimpleName();
	protected static final int CHOOSE_SEARCH_RESULT = 1;
	protected static final int CHOOSE_SITE_RESULT = 2;
	protected static final int CHOOSE_LAYER = 3;
	private SBAMapView mapView;
	private MapController mapController;
	private EditText searchText;
	private ImageView welcomeImageView;
	private MyLocationOverlay me;
	private Handler messageHandler;
	private UpdateMapsOverlaysThread updateMapOverlaysThread;
	private List<Overlay> mapOverlays;
	//private Button btnSearch;
	//private ImageButton useLocationButton;
	private LayerItemizedOverlay itemizedOverlay;
	Drawable marker;
	private LocationManager locationManager=null;
	private Location latestLocation;
	private boolean userIsPanning=false;
	private GeoPoint mapCenter = null;
	private final Double DISTANCE_CHANGE = 2.0; //in km

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
		setUpLocation();
	}

	@Override
	public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {

		switch (position) {
		case 0:
			double[] gps = getGPS();
			GeoPoint location = getPoint(gps[0], gps[1]);
			if (location != null) {
				
				//GeoPoint location = getPoint(latestLocation.getLatitude(), latestLocation.getLongitude());
				Log.i(TAG, String.format("%s", location.toString()));
				welcomeImageView.setVisibility(View.GONE);
				mapView.setClickable(true);
				mapView.getController().animateTo(location);
				//mapView.getController().setCenter(location);
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
			//startActivityForResult(new Intent(this, SearchListActivity.class), CHOOSE_SEARCH_RESULT);
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

	private void setUpLocation() {
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(
				LocationManager.GPS_PROVIDER,
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
		mapView=(SBAMapView)findViewById(R.id.map);
		mapView.setClickable(false);
		//mapView.setBuiltInZoomControls(true);
		mapView.setSatellite(false);
		/*
		mapView.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				int actionId = event.getAction();

				if (actionId == MotionEvent.ACTION_UP && userIsPanning) {

					userIsPanning = false;
					if(mapCenter == null || DistanceCalculator.calculateDistance(mapCenter, mapView.getMapCenter()) > DISTANCE_CHANGE) {
						mapCenter = mapView.getMapCenter();

						//TODO Update your overlays because the map has moved
						Toast.makeText(getBaseContext(), "Panned", Toast.LENGTH_SHORT).show();
					}

					return true;

				 } else if (actionId == MotionEvent.ACTION_MOVE) {
					 userIsPanning = true;
				 }

				return false;
			}
		});
		 */
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

		/*
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

		//updateMapOverlaysThread = new UpdateMapsOverlaysThread(mapView, messageHandler);
		//new Thread(updateMapOverlaysThread).start();
		 */
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
		//me.disableMyLocation();
	}

	@Override
	protected void onResume() {
		super.onResume();
		setUpLocation();
		//me.enableMyLocation();
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
		/*
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
		 */
		updateOverlays();
	}

	public static void navigateToLocation (double latitude, double longitude, SBAMapView mv) {
		GeoPoint p = new GeoPoint((int) latitude, (int) longitude); //new GeoPoint
		mv.displayZoomControls(true); //display Zoom (seems that it doesn't work yet)
		MapController mc = mv.getController();
		mc.animateTo(p); //move map to the given point
		//int zoomlevel = mv.getMaxZoomLevel(); //detect maximum zoom level
		mc.setZoom(15); //zoom
		//mv.setSatellite(false); //display only "normal" mapview	
	}

	public static void navigateToSite (double latitude, double longitude, SBAMapView mv) {
		GeoPoint p = new GeoPoint((int)(latitude*1000000.0), (int)(longitude*1000000.0)); //new GeoPoint
		mv.displayZoomControls(true); //display Zoom (seems that it doesn't work yet)
		MapController mc = mv.getController();
		mc.animateTo(p); //move map to the given point
		//int zoomlevel = mv.getMaxZoomLevel(); //detect maximum zoom level
		mc.setZoom(15); //zoom
		//mv.setSatellite(false); //display only "normal" mapview	
	}

	protected SBASitesApplication getSBASitesApplication() {
		return (SBASitesApplication)getApplication();
	}

	private GeoPoint getPoint(double lat, double lon) {
		return(new GeoPoint((int)(lat*1000000.0), (int)(lon*1000000.0)));
	}

	public void updateOverlays() {
		new OverlayTask().execute();
	}

	public void sitesLoading() {
		// TODO Auto-generated method stub

	}

	public void sitesLoadCancelled() {
		// TODO Auto-generated method stub

	}

	public void sitesLoaded(ArrayList<Site> sites) {

	}

	public void mapViewRegionWillChange(SBAMapView mapView, SBAMapRegion region) {
		// TODO Auto-generated method stub

	}

	public void mapViewRegionDidChange(SBAMapView mapView, SBAMapRegion region) {
		updateOverlays();
	}

	class OverlayTask extends AsyncTask<Void, Void, Void> {
		@Override
		public void onPreExecute() {
			if (itemizedOverlay!=null) {
				mapView.getOverlays().remove(itemizedOverlay);
				mapView.invalidate();	
				itemizedOverlay=null;

				double latSpan = ((mapView.getLatitudeSpan() / 1000000.0)/2.0);
				double longSpan = ((mapView.getLongitudeSpan() / 1000000.0)/2.0);
				double latCenter = (mapView.getMapCenter().getLatitudeE6()/1000000.0);
				double longCenter = (mapView.getMapCenter().getLongitudeE6()/1000000.0);
				double maxLat = (latCenter + latSpan);
				double minLat = (latCenter -  latSpan);
				double maxLong = (longCenter + longSpan);
				double minLong = (longCenter - longSpan);

				getSBASitesApplication().setCurrentSites(new ArrayList<Site>());

				for (SiteLayer layer : getSBASitesApplication().getLayers()) {
					if (layer.activated == true) {
						getSBASitesApplication().getCurrentSites().addAll(Site.loadSitesForRegionInLayer(getApplicationContext(), minLat, maxLat, minLong, maxLong, layer));
					}
				}
			}
		}

		@Override
		public Void doInBackground(Void... unused) {
			itemizedOverlay=new LayerItemizedOverlay(marker, mapView);
			return(null);
		}

		@Override
		public void onPostExecute(Void unused) {
			if (!getSBASitesApplication().getCurrentSites().isEmpty()) {
				itemizedOverlay.addOverlays(getSBASitesApplication().getCurrentSites());
			}
			mapView.getOverlays().add(itemizedOverlay);
			mapView.invalidate();			
		}
	}
}