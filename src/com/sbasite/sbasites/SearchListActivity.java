package com.sbasite.sbasites;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.sbasite.sbasites.adapter.SearchResultListAdapter;
import com.sbasite.sbasites.model.SearchResult;
import com.sbasite.sbasites.model.Site;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class SearchListActivity extends ListActivity {
	private Button doneButton;
	private SearchResultListAdapter listAdapter;
	private Geocoder geoCoder;
	public String searchString;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchString = getIntent().getStringExtra("search_text");
        setContentView(R.layout.search_list_activity);
        listAdapter = new SearchResultListAdapter(this, searchForResults());
        setListAdapter(listAdapter);
        setUpViews();
    }

	private ArrayList<SearchResult> searchForResults() {
		ArrayList<SearchResult> results = new ArrayList<SearchResult>();
		searchForMatchingSites(searchString, results);
		checkForCoordinates(searchString, results);
		searchForMatchingAddresses(searchString, results);
		//listAdapter.forceReload();
		return results;
	}


	private void searchForMatchingAddresses(String addressInput,
			ArrayList<SearchResult> results) {
		/*
		 * Geocode search text to get address matches
		 */
		try {
			List<Address> foundAdresses = geoCoder.getFromLocationName(addressInput, 1); //Search addresses
			if (foundAdresses.size() == 0) { //if no address found, display an error
				Dialog locationError = new AlertDialog.Builder(SearchListActivity.this)
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
					SearchResult result = new SearchResult(x);
					results.add(result);
				}
				//navigateToLocation((lat * 1000000), (lon * 1000000), map); //display the found address
			}
		}
		catch (Exception e) {
			//@todo: Show error message
			e.printStackTrace();
		}
	}

	private void searchForMatchingSites(String addressInput,
			ArrayList<SearchResult> results) {
		/*
		 * Search for matching sites
		 */
		ArrayList<Site> sites = Site.sitesForSearchText(getApplicationContext(), addressInput);
		
		for (Site site : sites) {
			SearchResult result = new SearchResult(site);
			results.add(result);
		}
	}

	private void checkForCoordinates(String addressInput,
			ArrayList<SearchResult> results) {
		/*
		 * Check to see if the user entered coordinates
		 */
		StringTokenizer tokenizer = new StringTokenizer(addressInput, ",");
		String coordLat = tokenizer.nextToken();
		String coordLong = tokenizer.nextToken();
		
		if ((containsOnlyNumbers(coordLat)) && (containsOnlyNumbers(coordLong))) {
			Double latitude = Double.valueOf(coordLat);
			Double longitude = (Double.valueOf(coordLong));
			if (longitude > 0.0) {
				longitude *= -1.0;
			}
			SearchResult result = new SearchResult(latitude, longitude);
			results.add(result);
		}
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
	}

	private void setUpViews() {

		geoCoder = new Geocoder(this); //create new geocoder instance
		this.doneButton = (Button)this.findViewById(R.id.Button30);
        this.doneButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
	            finish();
			}
        });
        /*
        btnSearch = (Button)findViewById(R.id.SearchButton);
		searchText=(EditText)findViewById(R.id.searchText);
		btnSearch.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String addressInput = searchText.getText().toString(); //Get input text
				//ArrayList<SearchResult> results = new ArrayList<SearchResult>();
				searchForMatchingSites(addressInput, results);
				checkForCoordinates(addressInput, results);
				searchForMatchingAddresses(addressInput, results);
				listAdapter.forceReload();
			}

		});
		*/
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		listAdapter.forceReload();
	}
	
	protected SBASitesApplication getSBASitesApplication() {
		return (SBASitesApplication)getApplication();
	}
	
	/**
     * This method checks if a String contains only numbers
     */
    public boolean containsOnlyNumbers(String str) {
        
        //It can't contain only numbers if it's null or empty...
        if (str == null || str.length() == 0)
            return false;
        
        for (int i = 0; i < str.length(); i++) {

            //If we find a non-digit character we return false.
            if (!Character.isDigit(str.charAt(i)))
                return false;
        }
        
        return true;
    }

}
