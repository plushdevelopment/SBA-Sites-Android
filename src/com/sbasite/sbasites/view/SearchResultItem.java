package com.sbasite.sbasites.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sbasite.sbasites.R;
import com.sbasite.sbasites.model.SearchResult;

public class SearchResultItem extends RelativeLayout {

	private TextView      siteName;
	private TextView      siteCode;

	public SearchResultItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setResult(SearchResult result) {
		findViews();
		siteName.setText(result.title);
		siteCode.setText(result.coordinates.toString());
	}

	private void findViews() {
		siteName = (TextView) findViewById(R.id.search_result_title);
		siteCode = (TextView) findViewById(R.id.search_result_subtitle);
	}
}
