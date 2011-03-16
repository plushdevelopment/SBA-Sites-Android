	 
package com.sbasite.sbasites;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.sbasite.sbasites.R;
import com.sbasite.sbasites.ItemizedOverlay.LayerItemizedOverlay;
import com.sbasite.sbasites.model.Site;
import com.sbasite.sbasites.model.SiteLayer;
import com.sbasite.sbasites.tasks.LoadSiteDetailsAsyncTask;

import de.android1.overlaymanager.*;
import de.android1.overlaymanager.lazyload.*;

public class SBAMapActivity extends MapActivity {
	
	private static final String TAG = SBAMapActivity.class.getSimpleName();
	private MapView mapView;
	private MapController mapController;
	private EditText searchText;
	private ImageView welcomeImageView;
	private MyLocationOverlay me;
	private Handler messageHandler;
	private UpdateMapsOverlaysThread updateMapOverlaysThread;
	private List<Overlay> mapOverlays;
	private Button btnSearch;
	private LayerItemizedOverlay itemizedOverlay;
	Drawable marker;
	
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
			final int result=1;
			startActivityForResult(item.getIntent(), result);
		} else if (item.getItemId() == R.id.layers) {
			final int result=2;
			startActivityForResult(item.getIntent(), result);
		} else {
			startActivity(item.getIntent());
		}
		
		return true;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// Call super always
		super.onCreate(savedInstanceState);
		setupViews();	
	}

	private void setupViews() {
		// Inflate your view
		setContentView(R.layout.main);
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
		me.enableMyLocation();
		me.enableCompass();
		
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
		
		searchText=(EditText)findViewById(R.id.searchText);
		
		messageHandler = new Handler() {
			public void handleMessage(Message msg) {
				if (welcomeImageView.getVisibility() == View.GONE) {
					
					if (mapView.getZoomLevel() < 13) {
						mapView.getController().setZoom(13);
					} else if (msg.what == 1) {
						double latSpan = ((mapView.getLatitudeSpan() / 1000000.0)/2.0);
						double longSpan = ((mapView.getLongitudeSpan() / 1000000.0)/2.0);
						double latCenter = (mapView.getMapCenter().getLatitudeE6()/1000000.0);
						double longCenter = (mapView.getMapCenter().getLongitudeE6()/1000000.0);
						double maxLat = (latCenter + latSpan);
						double minLat = (latCenter -  latSpan);
						double maxLong = (longCenter + longSpan);
						double minLong = (longCenter - longSpan);
						//itemizedOverlay.removeAllItems();
						ArrayList<Site> sites = Site.loadSitesForRegion(getApplicationContext(), minLat, maxLat, minLong, maxLong);
						if (!sites.isEmpty()) {
							itemizedOverlay.addOverlays(sites);
							/*
							for (Site site : sites) {
								itemizedOverlay.addOverlay(new SiteOverlayItem(site));
							}
							*/
						}
					}
				}
            }
        };
        
		updateMapOverlaysThread = new UpdateMapsOverlaysThread(mapView, messageHandler);
		new Thread(updateMapOverlaysThread).start();
	}

	protected void startSearch() {
		String addressInput = searchText.getText().toString(); //Get input text
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(searchText.getWindowToken(), 0);
		Intent searchIntent = new Intent(SBAMapActivity.this, SearchListActivity.class);
		searchIntent.putExtra("search_text", addressInput);
		startActivity(searchIntent);
		searchText.setText("");
		searchText.setHint("Search");
	}	
	
 	@Override
	protected boolean isRouteDisplayed() {
		return(false);
	}
 	
 	@Override
    public void onActivityResult(int requestCode,int resultCode,Intent data) 
 	{
 		super.onActivityResult(requestCode, resultCode, data);
 		welcomeImageView.setVisibility(View.GONE);
 		mapView.setClickable(true);
 		if (requestCode == 1) {
 			String latString=data.getStringExtra("Latitude");
 			String longString=data.getStringExtra("Longitude");
 			navigateToLocation(Double.valueOf(latString), Double.valueOf(longString), mapView);
 		} else if (requestCode == 2) {

 		}
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
	/*	
	private class SitesOverlay extends ItemizedOverlay<OverlayItem> {
		private List<OverlayItem> items=new ArrayList<OverlayItem>();
		private Drawable marker=null;
		
		public SitesOverlay(Drawable marker) {
			super(marker);
			this.marker=marker;
			
			populate();
		}
		
		public void addOverlay(OverlayItem overlay) {
		    items.add(overlay);
		    populate();
		}
		
		public void addOverlays(ArrayList<Site> overlays) {
			items.removeAll(items);
			for (int i = 0; i < overlays.size(); i++) {
				Site site = overlays.get(i);
				OverlayItem overlayItem = new OverlayItem(this.getPoint(site.latitude, site.longitude), site.siteName, site.mobileKey);
				items.add(overlayItem);
			}
		    populate();
		}
		
		private GeoPoint getPoint(double lat, double lon) {
			return(new GeoPoint((int)(lat*1000000.0), (int)(lon*1000000.0)));
		}

		@Override
		protected OverlayItem createItem(int i) {
			return(items.get(i));
		}
		
		@Override
		public void draw(Canvas canvas, MapView mapView,
											boolean shadow) {
			super.draw(canvas, mapView, shadow);
			
			boundCenterBottom(marker);
		}
 		
		@Override
		  protected boolean onTap(int i) {
			
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(searchText.getWindowToken(), 0);
			
		   final OverlayItem item = items.get(i);
		   AlertDialog.Builder dialog = new AlertDialog.Builder(SBAMapActivity.this);
		   dialog.setTitle(item.getTitle());
		   dialog.setMessage(item.getSnippet());
		   dialog.setPositiveButton("Load", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int Click) {
		    	Log.d("Overlay Tapped", item.getSnippet());
		     Intent intent = new Intent(SBAMapActivity.this, SiteDetailActivity.class);
		     intent.putExtra("MobileKey", item.getSnippet());
		     startActivity(intent);
		     }
		   });
		   dialog.show();
		   return true;
		  }
		
		@Override
		public int size() {
			return(items.size());
		}
	}
	*/
}