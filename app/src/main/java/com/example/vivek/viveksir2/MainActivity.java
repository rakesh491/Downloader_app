package com.example.vivek.viveksir2;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {


    Button button;
    EditText editText;
    String[] FileName;
    String Filename1;
    String url;
    ServerSuper serverSuper;
    Button button2;
    TextView textView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final AlertDialog.Builder dialog;
        serverSuper = new ServerSuper();
        button = (Button)findViewById(R.id.button1);
        button2 = (Button)findViewById(R.id.downfol);
        textView = (TextView)findViewById(R.id.textView3);
        /////Initializing A Dialog For File Already Exixts.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            dialog = new AlertDialog.Builder(MainActivity.this);
        }

        dialog.setIcon(android.R.drawable.ic_dialog_alert);
        dialog.setTitle("File Already Exist. Download Anyways?");


        /////File Handler.
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what==0){
                    Toast.makeText(getApplicationContext(),"No File Explorer Found.",Toast.LENGTH_SHORT).show();
                }
                if(msg.what==1){
                    Toast.makeText(getApplicationContext(),"Opening File Explorer.",Toast.LENGTH_SHORT).show();
                }
                if(msg.what==2){
                    dialog.show();
                }
                if(msg.what==3){
                    Toast.makeText(getApplicationContext(),"Download Successful.",Toast.LENGTH_SHORT).show();
                }
                if(msg.what==4){
                    Toast.makeText(getApplicationContext(),"Download Canceled.",Toast.LENGTH_SHORT).show();
                }
                if(msg.what==5){
                    Toast.makeText(getApplicationContext(),"Malformed URL.",Toast.LENGTH_SHORT).show();
                }
                if(msg.what==4){
                    Toast.makeText(getApplicationContext(),"Writing Error.",Toast.LENGTH_SHORT).show();
                }
            }
        };

        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(url!=null)
                    serverSuper.down(url,true);
            }
        });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                    handler.sendEmptyMessage(4);
            } });

        final int[] j = new int[1];

        editText = (EditText)findViewById(R.id.editText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                    Log.e("TAR","HYPER:"+ s.toString());
                    textView.setText(URLUtil.guessFileName(editText.getText().toString(),null,null));
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editText = (EditText)findViewById(R.id.editText);
                url = editText.getText().toString();
                // FileName = url.split("/");
                Filename1 = URLUtil.guessFileName(url,null,null);
                //serverSuper.i = FileName.length;
                // serverSuper.name_of_file = FileName[serverSuper.i -1];
                serverSuper.name_of_file = Filename1;
                Log.e("TAR","URL:"+url);
                Log.e("TAR","Filename:"+serverSuper.name_of_file);
                File file = new File(serverSuper.PATH+"/"+serverSuper.name_of_file);
                if(file.exists() && j[0]!=5 && serverSuper.i!=0){
                    handler.sendEmptyMessage(2);
                    Log.e("TAR","IF STATED");
                }
                else{
                    int i = serverSuper.down(url,true);
                    j[0] = i;
                    handler.sendEmptyMessage(i);


                }
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(serverSuper.PATH+"/");
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri,"resource/folder");

                if (intent.resolveActivityInfo(getPackageManager(),0) != null)
                {
                    startActivity(Intent.createChooser(intent, "Open folder"));
                    handler.sendEmptyMessage(1);
                }
                else{
                    handler.sendEmptyMessage(0);
                    AlertDialog.Builder ps = new AlertDialog.Builder(MainActivity.this);
                    ps.setTitle("Proceed To Play Store?");
                    ps.setMessage("Install ES File Explorer for better result");
                    ps.setIcon(android.R.drawable.ic_dialog_info);
                    ps.setPositiveButton("YES",new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final String appPackageName = "com.estrongs.android.pop&hl=en"; // getPackageName() from Context or Activity object
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                Log.e("TAR","NO ERROR");
                            } catch (android.content.ActivityNotFoundException anfe) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                Log.e("TAR","ERROR");
                            }
                        }
                    });
                    ps.setNegativeButton("NO",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            handler.sendEmptyMessage(0);
                        } });
                    ps.show();
                }
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }




}
