package com.sbasite.sbasites.layouts;

import com.sbasite.sbasites.R;
import com.sbasite.sbasites.model.SiteLayer;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LayerListItemView extends RelativeLayout {
	
	private ImageView     pinIconView;
	private TextView      layerName;
	private TextView      layerActivated;

	public LayerListItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setLayer(SiteLayer layer) {
		findViews();
	    layerName.setText(layer.name);
	    Drawable icon;
	    if (layer.activated == true) {
	    	layerActivated.setText("Enabled");
	    	icon = getResources().getDrawable(layer.getPinIcon());
	    } else {
	    	layerActivated.setText("Disabled");
	    	icon = getResources().getDrawable(R.drawable.disabled);
	    }
	    pinIconView.setImageDrawable(icon);
	}
	
	private void findViews() {
		pinIconView = (ImageView) findViewById(R.id.layer_pin_icon);
		layerName = (TextView)findViewById(R.id.layer_layer_name);
		layerActivated = (TextView)findViewById(R.id.layer_active);
	}

}
