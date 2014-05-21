package com.example.krushivignan;

import pdftron.PDF.PDFViewCtrl;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class PackageOfPracticesActivity extends Activity {
	private PDFViewCtrl content;

	private Button pro_pack, pest_pack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.production_selection);

		getComponentValue();
	}

	private void getComponentValue() {
		pro_pack = (Button) findViewById(R.id.prod_package);
		pro_pack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent edit = new Intent(getParent(), ProductionPackages.class);
				TabGroupActivity parentActivity = (TabGroupActivity) getParent();
				parentActivity.startChildActivity("production_packages", edit);
			}
		});

		pest_pack = (Button) findViewById(R.id.pest_package);
		pest_pack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent edit = new Intent(getParent(), PestControlPackages.class);
				TabGroupActivity parentActivity = (TabGroupActivity) getParent();
				parentActivity.startChildActivity("pest_control_packages", edit);
			}
		});
	}
}
