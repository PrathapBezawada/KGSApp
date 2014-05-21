package com.nunc.krushivignan.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

import com.demo.imageapp.imagezoom.ImageViewTouch;

public class AppInfo {
	public static User CurrentUser;
	public static String selectedImage = null;
	public static ArrayList<String> photoDetailsList = null;

	public static ImageViewTouch oldImageView = null;
	public static int position;

	public static String FTP_IP_ADDRESS = "109.73.166.7";
	public static String FTP_USER_NAME = "billzee";
	public static String FTP_PASSWORD = "*BZ2012*";
	public static String FTP_UPLOAD_DIRECTORY = "public_html/fphotos/";

	public static String downloadImage(Context context, String urlString) {
		String localpath = null;
		try {
			String filename = getlastToken(urlString);
			File outputFile = new File(context.getExternalCacheDir() + "/"
					+ filename);
			localpath = outputFile + "";
			if (!outputFile.exists()) {
				URL url = new URL(urlString.replace(" ", "%20"));
				HttpURLConnection c = (HttpURLConnection) url.openConnection();
				c.setRequestMethod("GET");
				c.setDoOutput(true);
				c.connect();
				int responseCode = c.getResponseCode();
				Log.i("111", urlString + "::" + responseCode);
				if (responseCode == HttpURLConnection.HTTP_OK) {

					FileOutputStream fos = new FileOutputStream(outputFile);
					InputStream is = c.getInputStream();

					byte[] buffer = new byte[4096];
					int len1 = 0;

					while ((len1 = is.read(buffer)) != -1) {
						fos.write(buffer, 0, len1);
					}

					fos.close();
					is.close();
				}
				return localpath;
			} else {
				return localpath;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

	private static String getlastToken(String strValue) {
		String[] strArray;
		String strlttoken = null;

		strArray = strValue.split("/");

		strlttoken = strArray[strArray.length - 1];
		return strlttoken;

	}

	public static Bitmap getResizedBitmap(Bitmap bm) {

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		Bitmap original = null;
		int srcWidth = bm.getWidth();
		int srcHeight = bm.getHeight();

		int desiredWidth = 0;
		int desiredHeight = 0;
		if (srcWidth > 2048) {
			desiredWidth = 2048;
			desiredHeight = (int)((float)srcHeight / srcWidth * 2048);
		} else if (srcHeight > 2048) {
			desiredHeight = 2048;
			desiredWidth = (int)((float)srcWidth / srcHeight * 2048);
		} else {

			return bm;
		}

		int inSampleSize = 1;
		while (srcWidth / 2 > desiredWidth) {
			srcWidth /= 2;
			srcHeight /= 2;
			inSampleSize *= 2;
		}

		float desiredWidthScale = (float) desiredWidth / srcWidth;
		float desiredHeightScale = (float) desiredHeight / srcHeight;

		// Decode with inSampleSize
		options.inJustDecodeBounds = false;
		options.inDither = false;
		options.inSampleSize = inSampleSize;
		options.inScaled = false;
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;

		Matrix matrix = new Matrix();
		matrix.postScale(desiredWidthScale, desiredHeightScale);
		original = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(),
				matrix, true);
		bm.recycle();
		return original;
	}
}
