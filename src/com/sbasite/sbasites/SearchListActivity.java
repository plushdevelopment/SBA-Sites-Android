package com.sbasite.sbasites;

import java.util.ArrayList;

import android.app.ListActivity;
import android.location.Address;

public class SearchListActivity extends ListActivity {
	private ArrayList<Address> addressList;
	private ArrayList<Site> siteList;
	private ArrayList<String> coordinateList;
	
	public SearchListActivity(ArrayList<Address> addressList,
			ArrayList<Site> siteList, ArrayList<String> coordinateList) {
		super();
		this.addressList = addressList;
		this.siteList = siteList;
		this.coordinateList = coordinateList;
	}
	
	
}
