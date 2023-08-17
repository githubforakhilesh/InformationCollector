package com.example.informationcollector.Activity;

import android.content.Context;
import android.os.Build;
import android.os.Environment;

import com.example.informationcollector.R;

import java.io.File;

public class Common {
    static Context contex;
    public static String getAppPath(Context context){
        contex=context;
        File dir;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1) {
            dir=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)+File.separator+context.getResources().getString(R.string.app_name)+File.separator);
           // dir=new File(getCacheDir(), "folder");
        }
        else
        {
            dir=new File(android.os.Environment.getExternalStorageDirectory()+File.separator+context.getResources().getString(R.string.app_name)+File.separator);
        }
   if(!dir.exists()){
       dir.mkdir();

   }
        return dir.getPath()+File.separator;
    }

    private static File getCacheDir() {

        return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)+File.separator+contex.getResources().getString(R.string.app_name)+File.separator);

    }
}
