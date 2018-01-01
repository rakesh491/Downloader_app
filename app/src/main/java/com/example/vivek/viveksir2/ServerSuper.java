package com.example.vivek.viveksir2;

import android.os.Environment;
import android.util.Log;
import android.webkit.URLUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Vivek on 18/10/2017.
 */

public class ServerSuper {

    String PATH = Environment.getExternalStorageDirectory().toString() + "/RakeshSuper";
    Thread thread2 = null;
    int i;
    String name_of_file;
    public int down(String string, boolean bool)
    {

        final File[] file = {new File(PATH)};
        if(file[0].mkdir()){
            Log.e("TAR","Directory Created");
        }
        else Log.e("TAR","Directory Not Created");
        try {
            final URL url = new URL(string);
            if(URLUtil.isValidUrl(string)){
                final URLConnection urlConnection = url.openConnection();
            final Thread thread = new Thread(){
                @Override
                public void run(){
                    try {
                        urlConnection.connect();
                        int length = urlConnection.getContentLength();
                        Log.e("TAR","Connected to URL == length:"+length);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("TAR","Not Connected to URL");
                    }
                }
            };
            thread.start();
            final InputStream[] input = new InputStream[1];
            final OutputStream[] output = new OutputStream[1];
                Log.e("TAR","Thread Started");
                //Something added
                    thread2 = new Thread(){
                        @Override
                        public void run(){
                            try {
                                input[0] = new BufferedInputStream(url.openStream(),8192);
                                output[0] = new FileOutputStream(PATH+"/"+name_of_file);
                                Log.e("TAR","Input Stream Generated");
                                byte data[] = new byte[1024];
                                int count;
                                if(input[0] !=null){
                                    int sum=0;
                                    while ((count = input[0].read(data)) != -1) {
                                        // writing data to file
                                        output[0].write(data, 0, count);
                                        //output[0].write(data);
                                        sum = sum+count;

                                    }
                                    Log.e("TAR","Reading/Writing done"+count);
                                    Log.e("TAR","size:"+sum);
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.e("TAR","Not Connected to URL");
                            }
                        }
                    };

                    if(bool)
                    thread2.start();

                    // flushing output
                    if(output[0]!=null)
                        output[0].flush();

                        // closing streams
                        if(output[0]!=null)
                            output[0].close();
                        if(input[0]!=null)
                        input[0].close();
                        Log.e("TAR","END");
            }else return 5;
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e("TAR","Malformed URL");
            return 5;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("TAR","Input/Output Error");
            return 6;
        }
        return 3;
    }

}
