	 
package com.sbasite.sbasites;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.OverlayItem;
import com.sbasite.sbasites.R;

public class SBAMapActivity extends MapActivity {
	private MapView map;
	private EditText searchText;
	private MyLocationOverlay me;
	private MapController mapController;
	private Handler messageHandler;
	private UpdateMapsOverlaysThread updateMapOverlaysThread;
	private SitesOverlay sitesOverlay;
	private Geocoder geoCoder;
	private Button btnSearch;
	private double lat;
	private double lon;
	private ArrayList<Site> sites;
	
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
		startActivity(item.getIntent());
		
		return true;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// Call super always
		super.onCreate(savedInstanceState);
		// Inflate your view
		setContentView(R.layout.main);
		// Assign ivars to elements listed in main.xml
		map=(MapView)findViewById(R.id.map);
		searchText=(EditText)findViewById(R.id.searchText);
		mapController = map.getController();
		btnSearch = (Button)findViewById(R.id.SearchButton);
		
		geoCoder = new Geocoder(this); //create new geocoder instance
		btnSearch.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String addressInput = searchText.getText().toString(); //Get input text

				try {

					List<Address> foundAdresses = geoCoder.getFromLocationName(addressInput, 1); //Search addresses
					if (foundAdresses.size() == 0) { //if no address found, display an error
						Dialog locationError = new AlertDialog.Builder(SBAMapActivity.this)
						.setIcon(0)
						.setTitle("Error")
						.setPositiveButton(R.string.ok, null)
						.setMessage("Sorry, your address doesn't exist.")
						.create();
						locationError.show(); 
					}
					else { //else display address on map
						for (int i = 0; i < foundAdresses.size(); ++i) {
							//Save results as Longitude and Latitude
							//@todo: if more than one result, then show a select-list
							Address x = foundAdresses.get(i);
							lat = x.getLatitude();
							lon = x.getLongitude();
						}
						navigateToLocation((lat * 1000000), (lon * 1000000), map); //display the found address
					}
				}
				catch (Exception e) {
					//@todo: Show error message
				}

			}
		});
		
		
		
		//mapController.setCenter(getPoint(46.0730555556, -100.546666667)); // Set center to the center of North America
		mapController.setCenter(getPoint(26.35049, -80.089004)); // Set center to the center of Boca Raton, FL
		mapController.setZoom(11);
		map.setBuiltInZoomControls(true);
		map.displayZoomControls(true);
		
		Drawable marker=getResources().getDrawable(R.drawable.owned);

		marker.setBounds(0, 0, marker.getIntrinsicWidth(),
														marker.getIntrinsicHeight());
		sitesOverlay = new SitesOverlay(marker);
		map.getOverlays().add(sitesOverlay);
		
		me=new MyLocationOverlay(this, map);
		map.getOverlays().add(me);
		
        messageHandler = new Handler() {
			public void handleMessage(Message msg) {
        		if(msg.obj.getClass() == OverlayItem.class) {
        			OverlayItem overlayItem = (OverlayItem)msg.obj;
        			sitesOverlay.addOverlay(overlayItem);
        		}
        		if(msg.obj.getClass() == ArrayList.class) {
					ArrayList<Site> overlays = (ArrayList<Site>)msg.obj;
					getSBASitesApplication().setCurrentSites(overlays);
        			sitesOverlay.addOverlays(overlays);
        		}
 
            }
 
        };
		
		//updateMapOverlaysThread is declared as a member variable 
		updateMapOverlaysThread = new UpdateMapsOverlaysThread(this, map, messageHandler, this.getSBASitesApplication());
				new Thread(updateMapOverlaysThread).start();
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
		me.enableMyLocation();
		me.enableCompass();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		me.disableMyLocation();
		me.disableCompass();
	}		
	
 	@Override
	protected boolean isRouteDisplayed() {
		return(false);
	}
 	
 	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_S) {
			map.setSatellite(!map.isSatellite());
			return(true);
		}
		else if (keyCode == KeyEvent.KEYCODE_Z) {
			map.displayZoomControls(true);
			return(true);
		}
		
		return(super.onKeyDown(keyCode, event));
	}
 	
 	 /**
 	  * Navigates a given MapView to the specified Longitude and Latitude
 	  */
 	  public static void navigateToLocation (double latitude, double longitude, MapView mv) {
 	    GeoPoint p = new GeoPoint((int) latitude, (int) longitude); //new GeoPoint
 	    mv.displayZoomControls(true); //display Zoom (seems that it doesn't work yet)
 	    MapController mc = mv.getController();
 	    mc.animateTo(p); //move map to the given point
 	    int zoomlevel = mv.getMaxZoomLevel(); //detect maximum zoom level
 	    mc.setZoom(zoomlevel - 1); //zoom
 	    mv.setSatellite(false); //display only "normal" mapview	
 	  }
 	
 	public void userLocationClicked(View view)  
 	{
 		if (me != null) {
 			mapController.setCenter(me.getMyLocation());
 			mapController.setZoom(13);
 		}
 	}  

 	protected SBASitesApplication getSBASitesApplication() {
		return (SBASitesApplication)getApplication();
	}
 	
	private GeoPoint getPoint(double lat, double lon) {
		return(new GeoPoint((int)(lat*1000000.0), (int)(lon*1000000.0)));
	}
		
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
				OverlayItem overlayItem = new OverlayItem(this.getPoint(site.latitude, site.longitude), site.siteName, site.siteCode);
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
		   OverlayItem item = items.get(i);
		   AlertDialog.Builder dialog = new AlertDialog.Builder(SBAMapActivity.this);
		   dialog.setTitle(item.getTitle());
		   dialog.setMessage(item.getSnippet());
		   dialog.setPositiveButton("Load", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int Click) {
		     Intent layersIntent = new Intent(SBAMapActivity.this, SiteDetailActivity.class);
		     startActivity(layersIntent);    }
		   });
		   dialog.show();
		   return true;
		  }
		
		@Override
		public int size() {
			return(items.size());
		}
	}
}