package com.example.krushivignan;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

public class HomeGroupActivity extends TabGroupActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		startChildActivity("FarmerList", new Intent(this, HomeActivity.class));
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		try {
			if (RegisterFarmerActivity.CAMERA_REQUEST == requestCode
					&& resultCode == RESULT_OK) {
				resizeBitmap(RegisterFarmerActivity.file + "");
				FileInputStream in = new FileInputStream(
						RegisterFarmerActivity.file);
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 10; // Downsample 10x
				Bitmap userImage = BitmapFactory
						.decodeStream(in, null, options);
				RegisterFarmerActivity.takePhoto.setImageBitmap(userImage);
			} else if (RegisterFarmerActivity.SELECT_PHOTO == requestCode
					&& resultCode == RESULT_OK) {

				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };

				Cursor cursor = getContentResolver().query(selectedImage,
						filePathColumn, null, null, null);
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String filePath = cursor.getString(columnIndex); 
				cursor.close();
				// Convert file path into bitmap image using below line.
				Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);

				RegisterFarmerActivity.takePhoto
						.setImageBitmap(yourSelectedImage);
				RegisterFarmerActivity.file = new File(filePath);
			}

		} catch (Exception e) {

		}

	}

	private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {

		// Decode image size
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(
				getContentResolver().openInputStream(selectedImage), null, o);

		// The new size we want to scale to
		final int REQUIRED_SIZE = 140;

		// Find the correct scale value. It should be the power of 2.
		int width_tmp = o.outWidth, height_tmp = o.outHeight;
		int scale = 1;
		while (true) {
			if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
				break;
			}
			width_tmp /= 2;
			height_tmp /= 2;
			scale *= 2;
		}

		// Decode with inSampleSize
		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize = scale;
		return BitmapFactory.decodeStream(
				getContentResolver().openInputStream(selectedImage), null, o2);

	}

	private void resizeBitmap(String path) {
		Bitmap b = BitmapFactory.decodeFile(path);
		Bitmap out = Bitmap.createScaledBitmap(b, b.getWidth() / 2,
				b.getHeight() / 2, false);

		File file = new File(path);
		FileOutputStream fOut;
		try {
			fOut = new FileOutputStream(file);
			out.compress(Bitmap.CompressFormat.JPEG, 95, fOut);
			fOut.flush();
			fOut.close();
			b.recycle();
			out.recycle();

		} catch (Exception e) { // TODO

		}
	}
}
