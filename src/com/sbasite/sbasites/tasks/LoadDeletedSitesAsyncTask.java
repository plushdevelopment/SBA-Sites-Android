package com.sbasite.sbasites.tasks;

import com.sbasite.sbasites.parsers.DeletedSitesFeedParser;

import android.content.Context;
import android.os.AsyncTask;

public class LoadDeletedSitesAsyncTask extends AsyncTask<String, Void, LoadDeletedSitesAsyncTask.LoadDeletedSitesResult> {

	//private static final String TAG = LoadDeletedSitesAsyncTask.class.getSimpleName();
	private Context context;
	private LoadDeletedSitesResponder responder;

	public interface LoadDeletedSitesResponder {
		public void loadingDeletedSites();
		public void loadedDeletedSites(LoadDeletedSitesResult result);
	}

	public class LoadDeletedSitesResult {
		public int totalRecordsCount;
		public LoadDeletedSitesResult(int totalRecordsCount) {
			super();
			this.totalRecordsCount = totalRecordsCount;
		}
	}

	public LoadDeletedSitesAsyncTask(Context context, LoadDeletedSitesResponder responder) {
		super();
		this.context = context;
		this.responder = responder;
	}

	@Override
	protected LoadDeletedSitesResult doInBackground(String... params) {
		//String urlString = String.format("http://map.sbasite.com/Mobile/GetData?LastUpdate=%s&Skip=%d&take=%d", lastUpdate, skip, take);
		DeletedSitesFeedParser parser = new DeletedSitesFeedParser(context, params[0]);

		// Parsing has finished.
		LoadDeletedSitesResult result = new LoadDeletedSitesResult(parser.parse());
		return result;
	}

	@Override
	protected void onPostExecute(LoadDeletedSitesResult result) {
		super.onPostExecute(result);
		responder.loadedDeletedSites(result);
	}

	@Override
	public void onPreExecute() {
		responder.loadingDeletedSites();
	}

}
