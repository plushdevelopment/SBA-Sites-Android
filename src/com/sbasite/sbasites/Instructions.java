package com.sbasite.sbasites;

import com.sbasite.sbasites.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Instructions extends Activity {

	private Button doneButton;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instructions);
        this.doneButton = (Button)this.findViewById(R.id.Button01);
        this.doneButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
	            finish();
			}
        });

    }

}
