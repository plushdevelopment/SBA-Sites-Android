package com.sbasite.sbasites;

import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.sbasite.sbasites.XMLHandler.XMLHandlerDelegate;
import com.sbasite.sbasites.model.DBMetadata;
import com.sbasite.sbasites.model.Site;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SiteDetailActivity extends Activity implements OnClickListener, XMLHandlerDelegate {
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
        
        try {
			// Create a URL we want to load some xml-data from.

			String urlString = "http://map.sbasite.com/Mobile/GetData?LastUpdate=" + site.lastUpdated + "&Skip=0&Take=1&Version=2&Action=4&MobileKey=" + site.mobileKey;
			URL url = new URL(urlString);

			// Get a SAXParser from the SAXPArserFactory.
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();

			// Get the XMLReader of the SAXParser we created.
			XMLReader xr = sp.getXMLReader();
			// Create a new ContentHandler and apply it to the XML-Reader
			XMLHandler handler = new XMLHandler(this, this);
			xr.setContentHandler(handler);

			/// Parse the xml-data from our URL.
			xr.parse(new InputSource(url.openStream()));
			// Parsing has finished.
			Log.e("HTTP Request", urlString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onClick(View arg0) {
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"stevendavis@aplusrep.com"});
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "SBA Sites Support Request");
		emailIntent.setType("text/plain");
		startActivity(Intent.createChooser(emailIntent, "Send mail..."));
		finish();
	}

	public void didEndDocument() {
		// TODO Auto-generated method stub
		
	}

	public void setTotalRecordsCount(int totalRecordsCount) {
		// TODO Auto-generated method stub
		
	}
	
}
