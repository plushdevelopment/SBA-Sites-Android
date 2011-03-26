package com.sbasite.sbasites.adapter;

import java.util.ArrayList;

import com.sbasite.sbasites.R;
import com.sbasite.sbasites.layouts.SiteListItem;
import com.sbasite.sbasites.model.Site;

import android.content.Context;
import android.util.Log;
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
		Log.d("SiteListAdapter", this.sites.toString());
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

	public View getView(int position, View convertView, ViewGroup parent) {
		
		SiteListItem siteListItem;
		if (null == convertView) {
			siteListItem = (SiteListItem)View.inflate(context, R.layout.site_list_item, null);
		} else {
			siteListItem = (SiteListItem)convertView;
		}
		siteListItem.setSite(sites.get(position));
		return siteListItem;
	}

	public void forceReload() {
		notifyDataSetChanged();
	}
}
