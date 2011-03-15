package com.sbasite.sbasites.view;

import com.sbasite.sbasites.R;
import com.sbasite.sbasites.R.drawable;
import com.sbasite.sbasites.R.id;
import com.sbasite.sbasites.model.SiteLayer;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;

public class LayerListItemView extends LinearLayout {
	private SiteLayer layer;
	private Button iconButton;

	public LayerListItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		iconButton = (Button)findViewById(R.id.LayerIconImageButton);
	}

	public void setSite(SiteLayer layer) {
		this.layer = layer;
		Drawable icon = getResources().getDrawable(R.drawable.owned);
		iconButton.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
		iconButton.setText(this.layer.name);
	}

	public SiteLayer getSite() {
		return layer;
	}

}
