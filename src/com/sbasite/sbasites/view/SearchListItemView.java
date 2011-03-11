package com.sbasite.sbasites.view;

import com.sbasite.sbasites.R;
import com.sbasite.sbasites.model.SearchResult;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;

public class SearchListItemView extends LinearLayout {
	
	private SearchResult result;
	private Button resultButton;

	public SearchListItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		resultButton = (Button)findViewById(R.id.SearchResultButton);
	}

	public void setSearchResult(SearchResult result) {
		this.result = result;
		Drawable icon = getResources().getDrawable(R.drawable.cw);
		resultButton.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
		resultButton.setText(this.result.title);
	}

	public SearchResult getResult() {
		return result;
	}

	public void setResult(SearchResult result) {
		this.result = result;
	}

	

}
