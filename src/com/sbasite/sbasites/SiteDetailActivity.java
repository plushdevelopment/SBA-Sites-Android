package com.sbasite.sbasites;

import android.app.Activity;
import android.os.Bundle;

public class SiteDetailActivity extends Activity {
	private Site site;
	
	
	/* (non-javadoc)
	 * Custom Constructor
	 */
	SiteDetailActivity(Site site) {
		this.site = site;
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sitedetailactivity);
		
	}
	
}
