package com.sbasite.sbasites.view;

import com.sbasite.sbasites.R;
import com.sbasite.sbasites.model.Site;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SiteListItem extends RelativeLayout {

  private ImageView     pinIconView;
  private TextView      siteName;
  private TextView      siteCode;

  public SiteListItem(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public void setSite(Site site) {
    findViews();
    siteName.setText(site.siteName);
    siteCode.setText(site.siteCode);
    Drawable icon = getResources().getDrawable(site.getPinIcon());
    pinIconView.setImageDrawable(icon);
  }

  private void findViews() {
    pinIconView = (ImageView) findViewById(R.id.site_pin_icon);
    siteName = (TextView) findViewById(R.id.site_site_name);
    siteCode = (TextView) findViewById(R.id.site_site_code);
  }

}
