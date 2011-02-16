package com.sbasite.sbasites;

import com.sbasite.sbasites.model.Site;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SiteDetailActivity extends Activity implements OnClickListener {
	private Site site;
	private Button emailButton;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sitedetailactivity);
		
		this.emailButton = (Button) findViewById(R.id.Button10);
        this.emailButton.setOnClickListener(this);
        
	}

	public void onClick(View arg0) {
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"stevendavis@aplusrep.com"});
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "SBA Sites Support Request");
		emailIntent.setType("text/plain");
		startActivity(Intent.createChooser(emailIntent, "Send mail..."));
		finish();
	}
	
}
