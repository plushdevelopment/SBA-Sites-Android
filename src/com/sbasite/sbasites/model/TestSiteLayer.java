package com.sbasite.sbasites.model;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.activeandroid.ActiveRecordBase;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "TestSiteLayers")
public class TestSiteLayer extends ActiveRecordBase<TestSiteLayer> {

	public TestSiteLayer(Context context) {
		super(context);
	}
	
	@Column(name = "NAME")
	public String name;
	
	@Column(name = "ACTIVATED")
	public boolean activated;
	
	@Column(name = "ICON")
	public Drawable icon;

}
