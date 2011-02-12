package com.sbasite.sbasites;

import java.util.ArrayList;

import com.sbasite.sbasites.R;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class SiteListActivity extends ListActivity {

	private Button doneButton;
	private SiteListAdapter listAdapter;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<Site> sites = getSBASitesApplication().getCurrentSites();
        Log.d("SiteListActivity", sites.toString());
        listAdapter = new SiteListAdapter(this, sites);
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
	
	protected SBASitesApplication getSBASitesApplication() {
		return (SBASitesApplication)getApplication();
	}
}
