package com.sbasite.sbasites;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SiteListItemView extends LinearLayout {
	
	private Site site;
	private ImageButton iconButton;
	private TextView siteNameTextView;

	public SiteListItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		iconButton = (ImageButton)findViewById(R.id.SiteIconImageButton);
		siteNameTextView = (TextView)findViewById(R.id.SiteNameTextView);
	}

	public void setSite(Site site) {
		this.site = site;
		/*
		checkbox.setText(site.siteName);
		checkbox.setChecked(true);
		address1Text.setText(site.siteCode);
		address1Text.setVisibility(View.VISIBLE);
		*/
	}

	public Site getSite() {
		return site;
	}

}
