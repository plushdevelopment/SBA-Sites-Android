	 
package com.sbasite.sbasites;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
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
	@SuppressWarnings("unused")
	private EditText searchText;
	private MyLocationOverlay me;
	private MapController mapController;
	private Handler messageHandler;
	private UpdateMapsOverlaysThread updateMapOverlaysThread;
	private SitesOverlay sitesOverlay;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    super.onCreateOptionsMenu(menu);
	    getMenuInflater().inflate(R.menu.menu, menu);
	    menu.findItem(R.id.instructions).setIntent(
	    		new Intent(this, Instructions.class));
	    menu.findItem(R.id.layers).setIntent(
	    		new Intent(this, Layers.class));
	    menu.findItem(R.id.list_view).setIntent(
	    		new Intent(this, ListView.class));
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
		
		//mapController.setCenter(getPoint(46.0730555556, -100.546666667));
		mapController.setCenter(getPoint(26.35049, -80.089004));
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

 	protected SBASitesApplication getSBASitesApplication() {
		return (SBASitesApplication)getApplication();
	}
 	
	private GeoPoint getPoint(double lat, double lon) {
		return(new GeoPoint((int)(lat*1000000.0),
													(int)(lon*1000000.0)));
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