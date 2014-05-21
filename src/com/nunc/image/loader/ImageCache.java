package com.nunc.image.loader;

import java.io.File;
import java.io.FileOutputStream;
import java.util.LinkedHashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * <p>
 * A simple 2-level cache for bitmap images consisting of a small and fast
 * in-memory cache (1st level cache) and a slower but bigger disk cache (2nd
 * level cache). For second level caching, the application's cache directory
 * will be used. Please note that Android may at any point decide to wipe that
 * directory.
 * </p>
 * <p>
 * When pulling from the cache, it will first attempt to load the image from
 * memory. If that fails, it will try to load it from disk. If that succeeds,
 * the image will be put in the 1st level cache and returned. Otherwise it's a
 * cache miss, and the caller is responsible for loading the image from
 * elsewhere (probably the Internet).
 * </p>
 * <p>
 * Pushes to the cache are always write-through (i.e., the image will be stored
 * both on disk and in memory).
 * </p>
 * 
 * @author Matthias Kaeppler
 */
@SuppressWarnings("serial")
class ImageCache extends LinkedHashMap<String, Bitmap> {

	static int firstLevelCacheSize = 5;

	private static int cachedImageQuality = 75;

	private static String secondLevelCacheDir;

	private CompressFormat compressedImageFormat = CompressFormat.JPEG;

	public static boolean isSDPresent() {
		return android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
	}

	public static void initialize(Context context) {
		if (isSDPresent()) {
			secondLevelCacheDir = context.getExternalCacheDir() + "/";
		} else {
			secondLevelCacheDir = context.getCacheDir() + "/";
		}
		new File(secondLevelCacheDir).mkdirs();
	}

	/**
	 * @param cachedImageQuality
	 *            the quality of images being compressed and written to disk
	 *            (2nd level cache) as a number in [0..100]
	 */
	public static void setCachedImageQuality(int cachedImageQuality) {
		ImageCache.cachedImageQuality = cachedImageQuality;
	}

	@Override
	public Bitmap get(Object key) {
		String imageUrl = (String) key;
		Bitmap bitmap = super.get(imageUrl);

		if (bitmap != null && !bitmap.isRecycled()) {
			// 1st level cache hit (memory)
			return bitmap;
		}

		File imageFile = getImageFile(imageUrl);
		if (imageFile.exists()) {
			// 2nd level cache hit (disk)
			bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
			super.put(imageUrl, bitmap);
			return bitmap;
		}
		// cache miss
		return null;
	}

	public String getFileNameFromCache(String url) {
		File imageFile = getImageFile(url);
		return imageFile + "";

	}

	@Override
	public Bitmap put(String imageUrl, Bitmap image) {
		// Log.v("Bitmap is::", "" + image);
		if (image == null) {
			return null;
		}
		File imageFile = getImageFile(imageUrl);
		try {
			imageFile.createNewFile();

			FileOutputStream ostream = new FileOutputStream(imageFile);

			image.compress(compressedImageFormat, cachedImageQuality, ostream);

			ostream.close();

		} catch (Exception e) {
			Log.e("io error is", "" + e);
			imageFile.delete();
		}

		return super.put(imageUrl, image);
	}

	private File getImageFile(String imageUrl) {
		// String fileName = Integer.toHexString(imageUrl.hashCode()) + "."
		// + compressedImageFormat.name();
		String fileName = getlastToken(imageUrl);
		// Log.v("Image URL is@@@@", imageUrl);
		// Log.d("The File Name is", fileName);
		return new File(secondLevelCacheDir + fileName);

	}

	public String getCacheDir() {
		return secondLevelCacheDir;
	}

	public boolean delete(String imageUrl) {
		// String fileName = Integer.toHexString(imageUrl.hashCode()) + "."
		// + compressedImageFormat.name();
		this.remove(imageUrl);
		String fileName = getlastToken(imageUrl);
		// Log.v("Image URL is@@@@", imageUrl);
		// Log.d("The File Name is", fileName);

		File file = new File(secondLevelCacheDir + fileName);
		if (file.exists()) {
			return file.delete();
		} else {
			return false;
		}

	}

	@Override
	protected boolean removeEldestEntry(
			java.util.Map.Entry<String, Bitmap> eldest) {
		return size() > firstLevelCacheSize;
	}

	private String getlastToken(String strValue) {
		String[] strArray;
		String strlttoken = null;

		strArray = strValue.split("/");

		strlttoken = strArray[strArray.length - 1];
		return strlttoken;

	}

}