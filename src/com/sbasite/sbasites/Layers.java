package com.sbasite.sbasites;

import java.util.ArrayList;

import com.sbasite.sbasites.R;
import com.sbasite.sbasites.adapter.LayerListAdapter;
import com.sbasite.sbasites.model.SiteLayer;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class Layers extends ListActivity {

	private Button doneButton;
	private LayerListAdapter listAdapter;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<SiteLayer> layers = SiteLayer.layers(getApplicationContext());
        Log.d("SiteListActivity", layers.toString());
        listAdapter = new LayerListAdapter(this, layers);
        setContentView(R.layout.listview);
        setListAdapter(listAdapter);
        setUpViews();
    }

	/* (non-Javadoc)
	 * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
	}

	private void setUpViews() {
		this.doneButton = (Button)this.findViewById(R.id.Button01);
        this.doneButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
	            finish();
			}
        });
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		listAdapter.forceReload();
	}
	
}
