package com.example.krushivignan;

import android.content.Intent;
import android.os.Bundle;

public class PackageGroupActivity extends TabGroupActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		startChildActivity("FarmerList", new Intent(this,
				PackageOfPracticesActivity.class));
	}
}