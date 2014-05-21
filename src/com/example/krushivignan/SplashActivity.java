package com.example.krushivignan;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Window;

public class SplashActivity extends Activity {

	private final int welcomeScreenDisplay = 3000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash_layout);
		HomeActivity.mQuery = false;
		try {
			startThread();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void lunchApp() {

		setIntent();
	}

	private void startThread() {
		Thread welcomeThread = new Thread() {

			int wait = 0;

			@Override
			public void run() {
				try {
					super.run();
					/**
					 * use while to get the splash time. Use sleep() to increase
					 * the wait variable for every 100L.
					 */
					while (wait < welcomeScreenDisplay) {
						sleep(100);
						wait += 100;
					}
				} catch (Exception e) {
					System.out.println("EXc=" + e);
				} finally {
					/**
					 * Called after splash times up. Do some action after splash
					 * times up. Here we moved to another main activity class
					 */
					setIntent();
				}
			}
		};
		welcomeThread.start();
	}

	private void setIntent() {

		startActivity(new Intent(SplashActivity.this, LoginActivity.class));
		finish();

	}

	public void go() {
		MediaPlayer mp = MediaPlayer
				.create(SplashActivity.this, R.raw.applause);
		if (mp.isPlaying()) {
			mp.stop();
			try {
				mp.prepare();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mp.seekTo(0);
		} else {
			mp.start();

		}
	}

}
