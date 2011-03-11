package com.sbasite.sbasites.adapter;

import java.util.ArrayList;

import com.sbasite.sbasites.R;
import com.sbasite.sbasites.view.SearchListItemView;
import com.sbasite.sbasites.model.SearchResult;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class SearchResultListAdapter extends BaseAdapter {
	
	private Context context;
	private ArrayList<SearchResult> results;
	
	public SearchResultListAdapter(Context context, ArrayList<SearchResult> results) {
		super();
		this.context = context;
		this.results = results;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return results.size();
	}

	public Object getItem(int index) {
		return (null == results) ? null : results.get(index);
	}

	public long getItemId(int index) {
		return index;
	}

	
	 public View getView(int position, View convertView, ViewGroup parent) {
		
		SearchListItemView SearchListItemView;
		if (null == convertView) {
			SearchListItemView = (SearchListItemView)View.inflate(context, R.layout.search_result_list_item, null);
		} else {
			SearchListItemView = (SearchListItemView)convertView;
		}
		SearchListItemView.setResult(results.get(position));
		return SearchListItemView;
	}

	 public void forceReload() {
			notifyDataSetChanged();
	}
}
