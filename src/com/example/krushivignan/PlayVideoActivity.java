package com.example.krushivignan;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.nunc.image.loader.ImageLoader;
import com.nunc.krushivignan.core.VideoCore;
import com.nunc.krushivignan.threadcallback.VideoParserThread;

public class PlayVideoActivity extends Activity {
	private ListView listView;
	private VideoView myVideo;

	private ArrayList<VideoCore> list = new ArrayList<VideoCore>();
	private ProgressDialog _dialog;
	private ProgressDialog mProgressDialog;
	private int lastIndexpos = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playvideoactivity);

		myVideo = (VideoView) findViewById(R.id.myVideo);
		listView = (ListView) findViewById(R.id.mylist);
		ImageLoader.initialize(this);
		VideoParserThread thread = new VideoParserThread(this, list) {
			@Override
			public void onCompleter() {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						_dialog.dismiss();
						if (list.size() > 0) {
							listView.setAdapter(new LazyAdapter(list));
							listView.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> parent,
										View view, int position, long id) {
									// TODO Auto-generated method stub
									if (position != lastIndexpos) {
										lastIndexpos = position;
										VideoCore video = (VideoCore) parent
												.getAdapter().getItem(position);
										playVideo(video.getVideourl());
									}
								}
							});
						} else {
							Toast.makeText(PlayVideoActivity.this,
									"No Videos Available", Toast.LENGTH_SHORT)
									.show();
						}
					}
				});
			}

			@Override
			public void onError(final String msg) {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						_dialog.dismiss();
						Toast.makeText(PlayVideoActivity.this, msg,
								Toast.LENGTH_SHORT).show();
					}
				});
			}
		};
		thread.start();
		_dialog = ProgressDialog.show(this, "", "Please wait...", false, true);
	}

	static class ViewHolder {
		ImageView thumbnail;
		TextView title, duration;

	}

	class LazyAdapter extends BaseAdapter {

		private ArrayList<VideoCore> list;

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		public LazyAdapter(ArrayList<VideoCore> list) {
			this.list = list;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			ViewHolder holder = null;
			View v = convertView;
			if (v == null) {

				holder = new ViewHolder();
				LayoutInflater li = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
				v = li.inflate(R.layout.playvideo_listitem, null);
				holder.title = (TextView) v.findViewById(R.id.title);
				holder.duration = (TextView) v.findViewById(R.id.duration);
				holder.thumbnail = (ImageView) v.findViewById(R.id.thumbnail);
				v.setTag(holder);

			} else {
				holder = (ViewHolder) v.getTag();

			}
			VideoCore video = list.get(position);
			holder.title.setText(video.getTitle());
			holder.duration.setText(video.getLenght() + "");
			ImageLoader.start(video.getThumbnail(), holder.thumbnail, 0);
			return v;
		}

	}

	private void playVideo(String url) {
		try {
			mProgressDialog = ProgressDialog.show(PlayVideoActivity.this, "",
					"Loading...", false, true);

			Uri vidFile = Uri.parse(url);
			myVideo.setVideoURI(vidFile);
//			myVideo.setVideoPath(url);
			myVideo.setMediaController(new MediaController(this));
//			myVideo.requestFocus();
			myVideo.start();
			myVideo.setOnPreparedListener(new OnPreparedListener() {

				@Override
				public void onPrepared(MediaPlayer mp) {
					// TODO Auto-generated method stub
					mProgressDialog.dismiss();
				}
			});
			myVideo.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					// TODO Auto-generated method stub
					myVideo.stopPlayback();
				}
			});
		} catch (Exception e) {
			Log.i("Error", e + "");
		}
	}
}
