package com.nunc.krushivignan.threadcallback;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTPClient;

import android.util.Log;

import com.nunc.krushivignan.core.AppInfo;

public class ImageUploadThread {
	private String IPAddress;
	private String userName;
	private String password;
	private String path;
	private FTPClient client;
	boolean status;

	public ImageUploadThread(String IPAddress, String userName,
			String password, String path) {
		// TODO Auto-generated constructor stub
		this.IPAddress = IPAddress;
		this.userName = userName;
		this.password = password;
		this.path = path;
	}

	public void run() {
		// TODO Auto-generated method stub
		uploadVideo();
	}
	


	public void finishUploadProcess() {
		try {
			client.completePendingCommand();
			client.logout();
			client.disconnect();
		} catch (Exception e) {
			Log.i("Error", e + "");
		}
	}
	public void InitiateUploadProcess() {
		try {
			client = new FTPClient();
			client.connect(IPAddress);
			if (client.isConnected()) {
				status = client.login(userName, password);
				if (status) {
					client.enterLocalPassiveMode();
					client.setFileType(FTPClient.BINARY_FILE_TYPE);
				}
			}
		} catch (Exception e) {
			Log.i("Error", e + "");
		}
	}
	public void uploadVideo() {
		InputStream fis = null;
		OutputStream os = null;
		try {
			InitiateUploadProcess();
			File imageFile = new File(path);
			if (imageFile.canRead()) {

//				client = new FTPClient();
//				client.connect(IPAddress);
//				if (client.isConnected()) {
//					 status = client.login(userName, password);
					if (status) {
						String dirTree = "public_html/fphotos/"
								+ AppInfo.CurrentUser.getDistrict();
						ftpCreateDirectoryTree(client, dirTree);
						client.enterLocalPassiveMode();
						client.setFileType(FTPClient.BINARY_FILE_TYPE);
						 fis = new FileInputStream(imageFile);
						
						 os = client
								.storeFileStream(getlastToken(path));

						byte buf[] = new byte[4096];
						int count = 0;
						int increment = 0;
						while ((count = fis.read(buf)) != -1) {
							increment += count;
						
							os.write(buf, 0, count);
						}

						fis.close();
						os.close();
						client.completePendingCommand();
						client.logout();
						client.disconnect();

					}
//				}
				
			}
			
		} catch (Exception e) {
			Log.i("Error", e + "");
		}
		 finally {
				if (fis != null) {
					try {
						fis.close();
					} catch (Exception e1) {

					}
				}
				if (os != null) {
					try {
						os.close();
					} catch (Exception e2) {

					}
				}

			}
			try {
				Thread.sleep(2000);
				finishUploadProcess();
			} catch (Exception e) {

			}

	}

	private static void ftpCreateDirectoryTree(FTPClient client, String dirTree)
			throws IOException {

		boolean dirExists = true;
		String[] directories = dirTree.split("/");
		for (String dir : directories) {
			if (!dir.isEmpty()) {
				if (dirExists) {
					dirExists = client.changeWorkingDirectory(dir);
				}
				if (!dirExists) {
					if (!client.makeDirectory(dir)) {
						throw new IOException(
								"Unable to create remote directory '" + dir
										+ "'.  error='"
										+ client.getReplyString() + "'");
					}
					if (!client.changeWorkingDirectory(dir)) {
						throw new IOException(
								"Unable to change into newly created remote directory '"
										+ dir + "'.  error='"
										+ client.getReplyString() + "'");
					}
				}
			}
		}
	}

	private String getlastToken(String strValue) {
		String[] strArray;
		String strlttoken = null;

		strArray = strValue.split("/");

		strlttoken = strArray[strArray.length - 1];
		return strlttoken;

	}
}
//Vasanth sir naku icharu thinamani but nenu neku vuncha .