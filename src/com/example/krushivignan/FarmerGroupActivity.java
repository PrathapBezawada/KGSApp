package com.example.krushivignan;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

public class FarmerGroupActivity extends TabGroupActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		startChildActivity("FarmerList", new Intent(this, FarmerActivity.class));
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		try {
			if (FarmerDetailsActivity.EditButtonClicked)
				RegisterFarmerActivity.ImageUpdateStatus = true;
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
				Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);

				RegisterFarmerActivity.takePhoto
						.setImageBitmap(yourSelectedImage);
				RegisterFarmerActivity.file = new File(filePath);
			}

		} catch (Exception e) {

		}

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
