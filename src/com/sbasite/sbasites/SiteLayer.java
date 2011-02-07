package com.sbasite.sbasites;

import java.util.List;

import android.content.Context;

import com.activeandroid.ActiveRecordBase;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "SITE_LAYERS")	
public class SiteLayer extends ActiveRecordBase<SiteLayer> {
	
	public SiteLayer(Context context) { super(context); }
	
	@Column(name = "ACTIVATED")
	public boolean activated=true;
	
	@Column(name = "NAME")
	public String name;
	
	@Column(name = "PIN_ICON")
	public String pinIcon;
	
	public List<Site> sites(Context context) {
		return getMany(Site.class, "SITE_LAYER");
	}
}
