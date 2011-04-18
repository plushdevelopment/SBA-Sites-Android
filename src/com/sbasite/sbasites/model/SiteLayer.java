package com.sbasite.sbasites.model;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.activeandroid.ActiveRecordBase;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import com.sbasite.sbasites.R;

@Table(name = "SITE_LAYERS")	
public class SiteLayer extends ActiveRecordBase<SiteLayer> {
	
	private static final String TAG = SiteLayer.class.getSimpleName();
	
	public boolean activated=true;
	
	public SiteLayer(Context context) { super(context); }
	
	public SiteLayer(Context context, String name) { 
		super(context); 
		this.name = name;
	}
	
	public static void initialize(Context context) {
		SiteLayer canada = new SiteLayer(context, "Canada");
		canada.save();
		SiteLayer newConstruction = new SiteLayer(context, "New Construction");
		newConstruction.save();
		SiteLayer latAm = new SiteLayer(context, "Central America");
		latAm.save();
		SiteLayer managed = new SiteLayer(context, "Managed");
		managed.save();
		SiteLayer owned = new SiteLayer(context, "Owned");
		owned.save();
	}
	
	@Column(name = "NAME")
	public String name;
	
	public int getPinIcon() {
		int iconID;
		if (name.matches("Canada")) {
			iconID = R.drawable.canada;
		} else if (name.matches("New Construction")) {
			iconID = R.drawable.new_construction;
		} else if (name.matches("Central America")) {
			iconID = R.drawable.central_america;
		} else if (name.matches("Managed")) {
			iconID = R.drawable.managed;
		} else if (name.matches("Owned")) {
			iconID = R.drawable.owned;
		} else {
			iconID = R.drawable.yellow_icon;
		}
		return iconID;
	}

	public static ArrayList<SiteLayer> layers(Context context) {
		return SiteLayer.query(context, SiteLayer.class);
	}
	
	public static SiteLayer layerForName(Context context, String name) {
		return SiteLayer.querySingle(context, SiteLayer.class, null, String.format("NAME = '%s'", name), "NAME");
	}
	
	public List<Site> sites(Context context) {
		return getMany(Site.class, "SITE_LAYER");
	}
	
	public static String activeLayersToString(SiteLayer[] layer) {
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		for (int i = 0; i < layer.length; i++) {
			if (layer[i].activated == true) {
				if (i < (layer.length - 1)) {
					sb.append(layer[i].getId().toString() + ", ");
				} else {
					sb.append(layer[i].getId().toString());
				}
			}
		}
		sb.append(")");
		Log.d(TAG, sb.toString());
		return sb.toString();
	}
}
