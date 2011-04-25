package com.sbasite.sbasites.activity;

import java.util.ArrayList;

import com.sbasite.sbasites.R;
import com.sbasite.sbasites.SBASitesApplication;
import com.sbasite.sbasites.R.id;
import com.sbasite.sbasites.R.layout;
import com.sbasite.sbasites.adapter.LayerListAdapter;
import com.sbasite.sbasites.model.SiteLayer;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class Layers extends ListActivity {

	private static final String TAG = Layers.class.getSimpleName();
	private LayerListAdapter listAdapter;
	private ArrayList<SiteLayer> layers;
	private Button hybridButton;
	private Button mapButton;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layers = getSBASitesApplication().getLayers();
        Log.d("Layers", layers.toString());
        listAdapter = new LayerListAdapter(this, layers);
        setContentView(R.layout.layers);
        setListAdapter(listAdapter);
        
        
        
        hybridButton = (Button) findViewById(R.id.HybridButton);
        mapButton = (Button) findViewById(R.id.MapButton);
        
        hybridButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
			}
        });
        mapButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
			}
        });
    }

	/* (non-Javadoc)
	 * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		SiteLayer layer = listAdapter.getItem(position);
		Log.d(TAG, layer.toString());
		if (null != layer) {
			layer.activated = !layer.activated;
		}
		listAdapter.forceReload();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		listAdapter.forceReload();
	}
	
	protected SBASitesApplication getSBASitesApplication() {
		return (SBASitesApplication)getApplication();
	}
	
}
