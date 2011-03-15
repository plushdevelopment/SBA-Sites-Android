package com.sbasite.sbasites.tasks;

import com.sbasite.sbasites.model.DBMetadata;
import com.sbasite.sbasites.XML.BaseFeedParser;
import com.sbasite.sbasites.XML.NewSitesFeedParser;

import android.content.Context;
import android.os.AsyncTask;

public class LoadMoreSitesAsyncTask extends AsyncTask<Void, Void, Void> implements FeedParserResponder {

	private Context context;
	private LoadMoreSitesResponder responder;
	private String lastUpdate;
	private int skip;
	private int take;
	
	public interface LoadMoreSitesResponder {
		public void loadingMoreSites();
	}
	
	public class LoadMoreSitesResult {
		public int totalRecordsCount;
		public LoadMoreSitesResult(int totalRecordsCount) {
			super();
			this.totalRecordsCount = totalRecordsCount;
		}
	}
	
	
	public LoadMoreSitesAsyncTask(Context context, LoadMoreSitesResponder responder, DBMetadata metadata) {
		super();
		this.context = context;
		this.responder = responder;
		this.lastUpdate = metadata.lastUpdate;
		this.skip = metadata.skip;
		this.take = metadata.take;
	}
	
	public LoadMoreSitesAsyncTask(LoadMoreSitesResponder responder,
			String lastUpdate, int skip, int take) {
		this.responder = responder;
		this.lastUpdate = lastUpdate;
		this.skip = skip;
		this.take = take;
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		String urlString = String.format("http://map.sbasite.com/Mobile/GetData?LastUpdate=%s&Skip=%d&take=%d", lastUpdate, skip, take);
		NewSitesFeedParser parser = new NewSitesFeedParser(context, urlString);
			
			// Parsing has finished.
		parser.parse();
		return null;
	}
	
	@Override
	  public void onPreExecute() {
		responder.loadingMoreSites();
	}

	@Override
	  public void onPostExecute(Void arg0) {
		
	}

	public void totalRecordsCountUpdated(int total) {
		
	}
	
}
