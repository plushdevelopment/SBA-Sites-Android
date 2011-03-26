package com.sbasite.sbasites.tasks;

import java.net.URL;

import com.sbasite.sbasites.parsers.NewSitesFeedParser;
import com.sbasite.sbasites.tasks.LoadMoreSitesAsyncTask.LoadMoreSitesResult;

import android.content.Context;
import android.os.AsyncTask;

public class LoadMoreSitesAsyncTask extends AsyncTask<String, Void, LoadMoreSitesAsyncTask.LoadMoreSitesResult> {

	private static final String TAG = LoadMoreSitesAsyncTask.class.getSimpleName();
	private Context context;
	private LoadMoreSitesResponder responder;

	public interface LoadMoreSitesResponder {
		public void loadingMoreSites();
		public void loadedNewSites(LoadMoreSitesResult result);
	}

	public class LoadMoreSitesResult {
		public int totalRecordsCount;
		public LoadMoreSitesResult(int totalRecordsCount) {
			super();
			this.totalRecordsCount = totalRecordsCount;
		}
	}

	public LoadMoreSitesAsyncTask(Context context, LoadMoreSitesResponder responder) {
		super();
		this.context = context;
		this.responder = responder;
	}

	@Override
	protected LoadMoreSitesResult doInBackground(String... params) {
		//String urlString = String.format("http://map.sbasite.com/Mobile/GetData?LastUpdate=%s&Skip=%d&take=%d", lastUpdate, skip, take);
		NewSitesFeedParser parser = new NewSitesFeedParser(context, params[0]);

		// Parsing has finished.
		LoadMoreSitesResult result = new LoadMoreSitesResult(parser.parse());
		return result;
	}

	@Override
	protected void onPostExecute(LoadMoreSitesResult result) {
		super.onPostExecute(result);
		responder.loadedNewSites(result);
	}

	@Override
	public void onPreExecute() {
		responder.loadingMoreSites();
	}

}
