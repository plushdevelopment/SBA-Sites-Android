package com.sbasite.sbasites.adapter;

import java.util.ArrayList;

import com.sbasite.sbasites.R;
import com.sbasite.sbasites.layouts.LayerListItemView;
import com.sbasite.sbasites.model.SiteLayer;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class LayerListAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<SiteLayer> layers;
	
	public LayerListAdapter(Context context, ArrayList<SiteLayer> layers) {
		super();
		this.context = context;
		this.layers = layers;
		Log.d("SiteListAdapter", this.layers.toString());
	}

	public int getCount() {
		return layers.size();
	}

	public SiteLayer getItem(int index) {
		return (null == layers) ? null : layers.get(index);
	}

	public long getItemId(int index) {
		return index;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		
		LayerListItemView layerListItemView;
		if (null == convertView) {
			layerListItemView = (LayerListItemView)View.inflate(context, R.layout.layer_list_item, null);
		} else {
			layerListItemView = (LayerListItemView)convertView;
		}
		layerListItemView.setLayer(layers.get(position));
		return layerListItemView;
	}

	public void forceReload() {
		notifyDataSetChanged();
	}

}
