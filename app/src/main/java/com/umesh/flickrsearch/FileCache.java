package com.umesh.flickrsearch;

import android.content.Context;

import java.io.File;

public class FileCache {

    private File cacheDirectory;

    public FileCache(Context context){

        //Find the dir at SDCARD to save cached images
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            //if SDCARD is mounted (SDCARD is present on device and mounted)
            cacheDirectory = new File(
                    android.os.Environment.getExternalStorageDirectory(),"LazyList");
        }
        else {
            // if checking on simulator the create cache dir in your application context
            cacheDirectory =context.getCacheDir();
        }

        if(!cacheDirectory.exists()) {
            // create cache dir in your application context
            cacheDirectory.mkdirs();
        }
    }

    public File getFile(String url){
        //Identify images by hashcode or encode by URLEncoder.encode.
        String filename = String.valueOf(url.hashCode());

        File f = new File(cacheDirectory, filename);
        return f;

    }

    public void clearAll() {
        // get all files inside the cache directory
        File[] files= cacheDirectory.listFiles();
        if(files == null) {
            return;
        }
        //delete all those directory files
        for(File f : files)
            f.delete();
    }

}