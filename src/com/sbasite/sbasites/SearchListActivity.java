package com.sbasite.sbasites;

import java.util.ArrayList;
import com.sbasite.sbasites.adapter.SearchResultListAdapter;
import com.sbasite.sbasites.model.SearchResult;
import com.sbasite.sbasites.tasks.LoadSearchResultsAsyncTask;
import com.sbasite.sbasites.tasks.LoadSearchResultsAsyncTask.LoadSearchResults;
import com.sbasite.sbasites.tasks.LoadSearchResultsAsyncTask.LoadSearchResultsResponder;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class SearchListActivity extends ListActivity implements LoadSearchResultsResponder {

	private static final String TAG = SearchListActivity.class.getSimpleName();
	public static final String SEARCH_RESULT = "search_result";
	private Button doneButton;
	private SearchResultListAdapter listAdapter;
	public String searchString;
	protected ProgressDialog progressDialog;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchString = getIntent().getStringExtra("search_text");
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.search_list_activity);
        setUpViews();
    }
	
	@Override
	protected void onResume() {
		super.onResume();
		loadResultsIfNotLoaded();
	}

	private void loadResultsIfNotLoaded() {
		if (null == getListAdapter()) {
			new LoadSearchResultsAsyncTask(getApplicationContext(), this).execute(searchString);
		} else {
			listAdapter.forceReload();
		}
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		SearchResult result = listAdapter.getItem(position); 
		if (null != result) {
			Intent intent = new Intent();
			intent.putExtra(SEARCH_RESULT, result);
			setResult(RESULT_OK, intent);
		}
		finish();
	}

	private void setUpViews() {
		
		this.doneButton = (Button)this.findViewById(R.id.Button30);
        this.doneButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
	            finish();
			}
        });		
	}

	public void loadingSearchResults() {
		setProgressBarIndeterminateVisibility(true);
		progressDialog = ProgressDialog.show(
				SearchListActivity.this,
				"Searching...",
				"Performing search for '" + searchString + "'"
		);
	}

	public void searchResultsLoaded(LoadSearchResults results) {
		ArrayList<SearchResult> searchResults = results.searchResults;
		listAdapter = new SearchResultListAdapter(this, searchResults);
		setListAdapter(listAdapter);
		setProgressBarIndeterminateVisibility(false);
		progressDialog.dismiss();
		listAdapter.forceReload();
	}

}
