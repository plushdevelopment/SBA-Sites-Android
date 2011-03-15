package com.sbasite.sbasites.tasks;

import com.sbasite.sbasites.XML.SiteDetailsFeedParser;
import com.sbasite.sbasites.model.Site;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class LoadSiteDetailsAsyncTask extends AsyncTask<Void, Void, Site> {

	private static final String TAG = LoadSiteDetailsAsyncTask.class.getSimpleName();
	private Context context;
	private LoadSiteDetailsAsyncTaskResponder responder;
	private String mobileKey;

	public interface LoadSiteDetailsAsyncTaskResponder {
		public void siteLoading();
		public void siteLoadCancelled();
		public void siteLoaded(Site site);
	}

	public LoadSiteDetailsAsyncTask(Context context, LoadSiteDetailsAsyncTaskResponder responder, String mobileKey) {
		this.context = context;
		this.responder = responder;
		this.mobileKey = mobileKey;
	}

	@Override
	protected Site doInBackground(Void... params) {
		Site site = loadSiteDetails();
		return site;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		responder.siteLoading();
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		responder.siteLoadCancelled();
	}

	@Override
	protected void onPostExecute(Site result) {
		super.onPostExecute(result);
		responder.siteLoaded(result);
		Log.d(TAG, "onPostExecute()" + result.toString());
	}

	protected Site loadSiteDetails() {
		SiteDetailsFeedParser parser = new SiteDetailsFeedParser(context, getSiteRequestURL());
		Site site = parser.parseSite();
		Log.d(TAG, "loadSiteDetails()" + site.toString());
		return site;
	}

	private String getSiteRequestURL() {
		String urlString = "http://map.sbasite.com/Mobile/GetData?LastUpdate=2008-09-24&Skip=0&Take=1&Version=2&Action=4&MobileKey=" + mobileKey;
		Log.d(TAG, urlString);
		return urlString;
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onProgressUpdate(Progress[])
	 */
	@Override
	protected void onProgressUpdate(Void... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}

}
