package com.nunc.krushivignan.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public  class ConnectionDetector {
 
    private Context _context;
 
    public ConnectionDetector(Context context){
        this._context = context;
    }
 
   public boolean isOnline(){
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
          if (connectivity != null)
          {
              NetworkInfo[] info = connectivity.getAllNetworkInfo();
              if (info != null)
                  for (int i = 0; i < info.length; i++)
                      if (info[i].getState() == NetworkInfo.State.CONNECTED)
                      {
                          return true;
                      }
 
          }
          return false;
    }
   
   public void alertDialog(String str){
	   
	   AlertDialog.Builder alertbox = new AlertDialog.Builder(_context);
		alertbox.setMessage(str);

		alertbox.setPositiveButton("Ok",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						
						arg0.dismiss();
					}
				});

	    alertbox.show();
   }
   
	
	
	}