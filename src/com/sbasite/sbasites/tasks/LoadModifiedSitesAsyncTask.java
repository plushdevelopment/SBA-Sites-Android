package com.sbasite.sbasites.tasks;

import com.sbasite.sbasites.parsers.NewSitesFeedParser;

import android.content.Context;
import android.os.AsyncTask;

public class LoadModifiedSitesAsyncTask extends AsyncTask<String, Void, LoadModifiedSitesAsyncTask.LoadModifiedSitesResult> {

	//private static final String TAG = LoadModifiedSitesAsyncTask.class.getSimpleName();
	private Context context;
	private LoadModifiedSitesResponder responder;

	public interface LoadModifiedSitesResponder {
		public void loadingModifiedSites();
		public void loadedModifiedSites(LoadModifiedSitesResult result);
	}

	public class LoadModifiedSitesResult {
		public int totalRecordsCount;
		public LoadModifiedSitesResult(int totalRecordsCount) {
			super();
			this.totalRecordsCount = totalRecordsCount;
		}
	}

	public LoadModifiedSitesAsyncTask(Context context, LoadModifiedSitesResponder responder) {
		super();
		this.context = context;
		this.responder = responder;
	}

	@Override
	protected LoadModifiedSitesResult doInBackground(String... params) {
		//String urlString = String.format("http://map.sbasite.com/Mobile/GetData?LastUpdate=%s&Skip=%d&take=%d", lastUpdate, skip, take);
		NewSitesFeedParser parser = new NewSitesFeedParser(context, params[0]);

		// Parsing has finished.
		LoadModifiedSitesResult result = new LoadModifiedSitesResult(parser.parse());
		return result;
	}

	@Override
	protected void onPostExecute(LoadModifiedSitesResult result) {
		super.onPostExecute(result);
		responder.loadedModifiedSites(result);
	}

	@Override
	public void onPreExecute() {
		responder.loadingModifiedSites();
	}

}
