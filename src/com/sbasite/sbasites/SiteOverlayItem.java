package com.sbasite.sbasites;

import android.graphics.drawable.Drawable;

import com.google.android.maps.OverlayItem;
import com.sbasite.sbasites.model.Site;

public class SiteOverlayItem extends OverlayItem {

	Site site=null;
	Drawable marker=null;
	
	public SiteOverlayItem(Site site) {
		super(site.getPoint(), site.siteCode, site.siteName);
		this.site = site;
		this.marker = getPinIcon();
	}

	@Override
	public Drawable getMarker(int stateBitset) {
		return getPinIcon();
	}
	
	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public Drawable getPinIcon() {
		int iconID;
		if (site.siteLayer.name.matches("Canada")) {
			iconID = R.drawable.canada;
		} else if (site.siteLayer.name.matches("New Construction")) {
			iconID = R.drawable.new_construction;
		} else if (site.siteLayer.name.matches("Central America")) {
			iconID = R.drawable.central_america;
		} else if (site.siteLayer.name.matches("Managed")) {
			iconID = R.drawable.managed;
		} else if (site.siteLayer.name.matches("Owned")) {
			iconID = R.drawable.owned;
		} else {
			iconID = R.drawable.yellow_icon;
		}
		Drawable aMarker = site.getContext().getResources().getDrawable(iconID);
		aMarker.setBounds(0, 0, aMarker.getIntrinsicWidth(), aMarker.getIntrinsicHeight());
		return aMarker;
	}

}
