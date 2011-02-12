package com.sbasite.sbasites;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;

public class SiteListItemView extends LinearLayout {
	
	private Site site;
	private Button iconButton;

	public SiteListItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		iconButton = (Button)findViewById(R.id.SiteIconImageButton);
	}

	public void setSite(Site site) {
		this.site = site;
		Drawable icon = getResources().getDrawable(R.drawable.owned);
		iconButton.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
		iconButton.setText(this.site.siteName);
	}

	public Site getSite() {
		return site;
	}

}
