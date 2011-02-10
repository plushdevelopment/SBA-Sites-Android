package com.sbasite.sbasites;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class SiteListAdapter extends BaseAdapter {
	
	private Context context;
	private ArrayList<Site> sites;
	
	public SiteListAdapter(Context context, ArrayList<Site> sites) {
		super();
		this.context = context;
		this.sites = sites;
	}

	public int getCount() {
		return sites.size();
	}

	public Site getItem(int index) {
		return (null == sites) ? null : sites.get(index);
	}

	public long getItemId(int index) {
		return index;
	}

	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		return null;
	}

}
