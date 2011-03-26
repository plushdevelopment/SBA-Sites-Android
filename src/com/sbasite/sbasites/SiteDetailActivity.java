package com.sbasite.sbasites;

import java.net.MalformedURLException;
import java.net.URL;

import com.sbasite.sbasites.Activity.SiteImageViewActivity;
import com.sbasite.sbasites.model.Site;
import com.sbasite.sbasites.tasks.LoadImageAsyncTask;
import com.sbasite.sbasites.tasks.LoadImageAsyncTask.LoadImageAsyncTaskResponder;
import com.sbasite.sbasites.tasks.LoadSiteDetailsAsyncTask;
import com.sbasite.sbasites.tasks.LoadSiteDetailsAsyncTask.LoadSiteDetailsAsyncTaskResponder;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SiteDetailActivity extends Activity implements LoadSiteDetailsAsyncTaskResponder, LoadImageAsyncTaskResponder {
	
	private static final String TAG = SiteDetailActivity.class.getSimpleName();
	private Site site;
	private String mobileKey;
	private Button emailButton;
	private TextView siteNameTextView;
	private TextView siteCodeTextView;
	private TextView siteAddress1TextView;
	private TextView siteAddress2TextView;
	private TextView siteLayerTextView;
	private TextView siteCoordinatesTextView;
	private TextView siteTypeTextView;
	private TextView siteHeightTextView;
	private TextView siteElevationTextView;
	private TextView siteMTATextView;
	private TextView siteBTATextView;
	private ImageView siteImage;
	protected ProgressDialog progressDialog;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mobileKey = getIntent().getStringExtra("MobileKey");
		setupViews();
	}

	private void setupViews() {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		
		setContentView(R.layout.sitedetailactivity);
		
		siteNameTextView = (TextView)findViewById(R.id.TextView_SiteName);
		siteCodeTextView = (TextView)findViewById(R.id.TextView_SiteCode);
		siteAddress1TextView = (TextView)findViewById(R.id.TextView_SiteAddress1);
		siteAddress2TextView = (TextView)findViewById(R.id.TextView_SiteAddress2);
		siteLayerTextView = (TextView)findViewById(R.id.TextView_SiteLayer);
		siteCoordinatesTextView = (TextView)findViewById(R.id.TextView_SiteCoordinates);
		siteTypeTextView = (TextView)findViewById(R.id.TextView_SiteType);
		siteHeightTextView = (TextView)findViewById(R.id.TextView_SiteHeight);
		siteElevationTextView = (TextView)findViewById(R.id.TextView_SiteElevation);
		siteMTATextView = (TextView)findViewById(R.id.TextView_SiteMTA);
		siteBTATextView = (TextView)findViewById(R.id.TextView_SiteBTA);
        
        siteImage = (ImageView)findViewById(R.id.ImageView_SiteThumbnail);
        siteImage.setVisibility(View.INVISIBLE);
        
		
		emailButton = (Button) findViewById(R.id.Button_SubmitInquiry);
        emailButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
				String[] recipients = new String[]{site.email, "jsilberstein@sbasite.com", "bgottfried@sbasite.com", "",};
				emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, recipients);
				emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Site Inquiry");
				emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, site.toString());
				emailIntent.setType("text/plain");
				startActivity(Intent.createChooser(emailIntent, "Send mail..."));
				finish();
			}
        });
	}

	@Override
	  protected void onResume() {
	    super.onResume();
	    loadSiteDetailsIfNotLoaded();
	}

	private void loadSiteDetailsIfNotLoaded() {
		Log.d(TAG, "loadSiteDetailsIfNotLoaded");
		
		if (null == site) {
			site = Site.siteForMobileKey(getApplicationContext(), mobileKey);
		}
		
		if (!site.detailsLoaded() && null != mobileKey) {
			new LoadSiteDetailsAsyncTask(getApplicationContext(), this, mobileKey).execute();		
		} else {
			refreshViews();
		}
		
	}

	public void siteLoaded(Site site) {
		Log.d(TAG, "siteLoaded" + site.toString());
		setProgressBarIndeterminateVisibility(false);
		this.site = site;
		refreshViews();
		progressDialog.dismiss();
		loadSiteThumbnail();
	}

	private void refreshViews() {
		siteNameTextView.setText(site.siteName);
		siteCodeTextView.setText(site.siteCode);
		siteAddress1TextView.setText(site.address);
		siteAddress2TextView.setText(site.city + ", " + site.stateProvince + " " + site.zip);
		siteLayerTextView.setText(site.siteLayer.name);
		siteCoordinatesTextView.setText(Double.toString(site.latitude) + ", " + Double.toString(site.longitude));
		siteTypeTextView.setText(site.structureType);
		siteHeightTextView.setText(site.structureHeight);
		siteElevationTextView.setText(site.agl);
		siteMTATextView.setText(site.mta);
		siteBTATextView.setText(site.bta);
	}

	public void siteLoading() {
		Log.d(TAG, "siteLoading");
		setProgressBarIndeterminateVisibility(true);
		progressDialog = ProgressDialog.show(SiteDetailActivity.this, "Loading...", "Site details are loading");
	}

	public void siteLoadCancelled() {
		Log.d(TAG, "siteLoadCancelled");
		setProgressBarIndeterminateVisibility(false);
		refreshViews();
		progressDialog.dismiss();
	}
	
	private void loadSiteThumbnail() {
		try {
			String urlString = String.format("http://map.sbasite.com/Mobile/GetImage?SiteCode=%s&width=600&height=600", site.siteCode);
			URL requestURL = new URL(urlString);
			new LoadImageAsyncTask(this).execute(requestURL);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public void imageLoading() {
		Log.d(TAG, "imageLoading()");
	}

	public void imageLoadCancelled() {
		Log.d(TAG, "imageLoadCancelled()");
	}

	public void imageLoaded(Drawable drawable) {
		if (null == drawable) {
			Log.d(TAG, "drawable == null");
		} else {
			siteImage.setImageDrawable(drawable);
			siteImage.setVisibility(View.VISIBLE);
			siteImage.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Log.d(TAG, "Image Tapped");
				     Intent intent = new Intent(SiteDetailActivity.this, SiteImageViewActivity.class);
				     intent.putExtra("SiteCode", site.siteCode);
				     startActivity(intent);
				}
			});
		}
	}
	
}
