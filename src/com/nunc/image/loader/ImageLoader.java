package com.nunc.image.loader;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

/**
 * Realizes an background image loader backed by a two-level FIFO cache. If the
 * image to be loaded is present in the cache, it is set immediately on the
 * given view. Otherwise, a thread from a thread pool will be used to download
 * the image in the background and set the image on the view as soon as it
 * completes.
 * 
 * @author Matthias Kaeppler
 */
public class ImageLoader implements Runnable {

	private static final ThreadPoolExecutor executor;

	private static final ImageCache imageCache;

	private static final int DEFAULT_POOL_SIZE = 3;

	static final int HANDLER_MESSAGE_ID = 0;

	static final String BITMAP_EXTRA = "droidfu:extra_bitmap";

	private static int numAttempts = 10;
	static {
		executor = (ThreadPoolExecutor) Executors
				.newFixedThreadPool(DEFAULT_POOL_SIZE);
		imageCache = new ImageCache();
	}

	/**
	 * @param numThreads
	 *            the maximum number of threads that will be started to download
	 *            images in parallel
	 */
	public static void setThreadPoolSize(int numThreads) {
		executor.setMaximumPoolSize(numThreads);
	}

	/**
	 * @param numAttempts
	 *            how often the image loader should retry the image download if
	 *            network connection fails
	 */
	public static void setMaxDownloadAttempts(int numAttempts) {
		ImageLoader.numAttempts = numAttempts;
	}

	/**
	 * @param count
	 *            how many images loaded from the web should be cached in
	 *            memory, i.e. in the 1st level cache. Be careful not to set
	 *            this too high, otherwise you will meet the dreaded
	 *            {@link OutOfMemoryError}.
	 */
	public static void setMaximumNumberOfImagesInMemory(int count) {
		ImageCache.firstLevelCacheSize = count;
	}

	/**
	 * @return how many images loaded from the web should be cached in memory,
	 *         i.e. in the 1st level cache. Be careful not to set this too high,
	 *         otherwise you will meet the dreaded {@link OutOfMemoryError}.
	 */
	public static int getMaximumNumberOfImagesInMemory() {
		return ImageCache.firstLevelCacheSize;
	}

	public static void initialize(Context context) {
		ImageCache.initialize(context);
	}

	public static boolean deleteImageFromCache(String url) {
		if (imageCache != null) {
			return imageCache.delete(url);
		} else {
			return false;
		}

	}

	public static void clearImageFromCache(String url) {
		if (imageCache != null) {
			imageCache.remove(url);
		}
	}

	public static String getCacheDir() {
		if (imageCache != null) {
			return imageCache.getCacheDir();
		} else {
			return null;
		}
	}

	private String imageUrl;

	private Handler handler;

	private ImageLoader(String imageUrl, ImageView imageView) {
		this.imageUrl = imageUrl;
		this.handler = new ImageLoaderHandler(imageView);
	}

	private ImageLoader(String imageUrl, ImageLoaderHandler handler) {
		this.imageUrl = imageUrl;
		this.handler = handler;
	}

	public static Bitmap getBitmapFromCache(String imageUrl) {
		Bitmap image = imageCache.get(imageUrl);
		return image;
	}

	public static String getImageFilePath(String url) {
		return imageCache.getFileNameFromCache(url);

	}

	public static void start(String imageUrl, ImageView imageView,
			int DefaultImage) {
		// Log.i("Image URL:::::::::::::::::", imageUrl + "");
		imageView.setImageResource(DefaultImage);
		ImageLoader loader = new ImageLoader(imageUrl, imageView);
		synchronized (imageCache) {
			Bitmap image = imageCache.get(imageUrl);
			if (image == null) {
				// fetch the image in the background
				executor.execute(loader);
			} else {
				// imageView.setBackgroundDrawable(new BitmapDrawable(image));//
				// ImageBitmap(image);
				if (!image.isRecycled())
					imageView.setImageBitmap(image);
			}
		}

	}

	public static void start(String imageUrl, ImageLoaderHandler handler) {
		ImageLoader loader = new ImageLoader(imageUrl, handler);
		synchronized (imageCache) {
			Bitmap image = imageCache.get(imageUrl);
			if (image == null) {
				// fetch the image in the background
				executor.execute(loader);
			} else {
				loader.notifyImageLoaded(image);
			}
		}
	}

	/**
	 * Clears the 1st-level cache (in-memory cache). A good candidate for
	 * calling in {@link Application#onLowMemory()}.
	 */
	public static void clearCache() {
		imageCache.clear();
	}

	private static class PatchInputStream extends FilterInputStream {
		public PatchInputStream(InputStream in) {
			super(in);
		}

		public long skip(long n) throws IOException {
			long m = 0L;
			while (m < n) {
				long _m = in.skip(n - m);
				if (_m == 0L)
					break;
				m += _m;
			}
			return m;
		}
	}

	public void run() {
		Bitmap bitmap = null;
		boolean downloadCompleteStatus = false;
		HttpResponse resp = null;
		InputStream is = null;
		// Log.i("Trying to DownLoad::::", imageUrl);
		while (!downloadCompleteStatus) {
			try {
				// URL url = new URL(imageUrl);
				// bitmap = BitmapFactory.decodeStream(url.openStream());

				// URL myImageURL = new URL(imageUrl);
				// HttpURLConnection connection = (HttpURLConnection) myImageURL
				// .openConnection();
				// connection.setDoInput(true);
				// connection.connect();
				// InputStream input = connection.getInputStream();
				HttpParams params = new BasicHttpParams();
				params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
						HttpVersion.HTTP_1_1);
				HttpGet uri = new HttpGet(imageUrl);

				// Set the timeout in milliseconds until a connection is
				// established.
				// The default value is zero, that means the timeout is not
				// used.
				int timeoutConnection = 5000;
				HttpConnectionParams.setConnectionTimeout(params,
						timeoutConnection);

				// Set the default socket timeout (SO_TIMEOUT)
				// in milliseconds which is the timeout for waiting for data.
				int timeoutSocket = 5000;
				HttpConnectionParams.setSoTimeout(params, timeoutSocket);

				DefaultHttpClient client = new DefaultHttpClient(params);
				resp = client.execute(uri);

				// Get the bitmap
				System.gc();
				is = resp.getEntity().getContent();
				bitmap = BitmapFactory.decodeStream(new PatchInputStream(is));
				synchronized (imageCache) {
					imageCache.put(imageUrl, bitmap);
				}

				downloadCompleteStatus = true;
				break;
			} catch (Throwable e) {
				// Log.w(ImageLoader.class.getSimpleName(), "download for "
				// + imageUrl + " failed (attempt " + timesTried + ")");
				try {
					Thread.sleep(20);
				} catch (InterruptedException e1) {
				}
				//
				// timesTried++;
			} finally {
				try {
					if (is != null) {
						is.close();
						is = null;
					}
					if (resp != null)
						resp.getEntity().consumeContent();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		if (bitmap != null) {
			notifyImageLoaded(bitmap);
		}
	}

	public void notifyImageLoaded(Bitmap bitmap) {
		Message message = new Message();
		message.what = HANDLER_MESSAGE_ID;
		Bundle data = new Bundle();
		data.putParcelable(BITMAP_EXTRA, bitmap);
		message.setData(data);

		handler.sendMessage(message);
	}

}