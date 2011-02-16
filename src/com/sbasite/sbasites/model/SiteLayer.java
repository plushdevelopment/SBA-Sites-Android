package com.sbasite.sbasites.model;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.activeandroid.ActiveRecordBase;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "SITE_LAYERS")	
public class SiteLayer extends ActiveRecordBase<SiteLayer> {
	
	public SiteLayer(Context context) { super(context); }
	
	public SiteLayer(Context context, String name) { 
		super(context); 
		this.name = name;
		activated = true;
		if (name.matches("Canada")) {
			pinIcon = "canada";
		} else if (name.matches("New Construction")) {
			pinIcon = "new_construction";
		} else if (name.matches("Central America")) {
			pinIcon = "central_america";
		} else if (name.matches("Managed")) {
			pinIcon = "managed";
		} else if (name.matches("Owned")) {
			pinIcon = "owned";
		}
	}
	
	@Column(name = "ACTIVATED")
	public boolean activated=true;
	
	@Column(name = "NAME")
	public String name;
	
	@Column(name = "PIN_ICON")
	public String pinIcon;
	
	public static ArrayList<SiteLayer> layers(Context context) {
		return SiteLayer.query(context, SiteLayer.class);
	}
	
	public static SiteLayer layerForName(Context context, String name) {
		return SiteLayer.querySingle(context, SiteLayer.class, null, String.format("NAME = '%s'", name), "NAME");
	}
	
	public List<Site> sites(Context context) {
		return getMany(Site.class, "SITE_LAYER");
	}
}
