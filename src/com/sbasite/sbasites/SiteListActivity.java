package com.sbasite.sbasites;

import java.util.ArrayList;

import com.sbasite.sbasites.R;
import com.sbasite.sbasites.adapter.SiteListAdapter;
import com.sbasite.sbasites.model.Site;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class SiteListActivity extends ListActivity {

	private static final String TAG = SiteListActivity.class.getSimpleName();
	private Button doneButton;
	private SiteListAdapter listAdapter;
	private ArrayList<Site> sites;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sites = getSBASitesApplication().getCurrentSites();
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
		Site site = listAdapter.getItem(position);
		Log.d(TAG, site.toString());
	    if (null != site) {
			Intent intent = new Intent();
			intent.putExtra("MobileKey", site.mobileKey);
			setResult(RESULT_OK, intent);
		}
		finish();
	    
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
