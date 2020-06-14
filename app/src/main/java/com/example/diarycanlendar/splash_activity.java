package com.example.diarycanlendar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class splash_activity extends AppCompatActivity {
    private Intent mIntent;
    private Handler mHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash_layout);

        //状态栏透明
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);



        SharedPreferences sharedPreferences = getSharedPreferences("login",MODE_PRIVATE);
        if (sharedPreferences!=null){
            if(!sharedPreferences.getString("loginstatus", "none").equals("islogin")){
                mIntent = new Intent(this,login_activity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                mHandler = new Handler();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(mIntent);
                    }
                }, 2000);
            }else{
                //直接在应用启动的时候直接获取今日图片
                new Thread(){
                    @Override
                    public void run() {
                        String urlString = "http://173.82.168.215:5000/todaypic";
                        try {
                            NetworkConn picConn = new NetworkConn(mHandler,urlString);
                            File filePictures = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                            String storage = filePictures.getAbsolutePath()+"/";
                            picConn.getpic(storage);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                new Thread(){
                    @Override
                    public void run() {
                        setidea();
                    }
                }.start();
            mIntent = new Intent(this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            mHandler = new Handler();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(mIntent);
                }
            }, 2000);
        }

        }
    }

    //
    private void setidea() {
            String urlString  = "http://173.82.168.215:5000/todayidea";
            try {
                NetworkConn networkConn = new NetworkConn(mHandler,urlString);
                String ideastr = networkConn.gettodayidea();
                SharedPreferences sharedPreferences = getSharedPreferences("idea",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                System.out.println(ideastr);
                ideastr = ideastr.replace("   ","");
                System.out.println(ideastr);
                String[] lst = ideastr.split("\\.");
                System.out.println(lst.length);
                editor.putString("en",lst[0]);
                editor.putString("zh",lst[1]);
                editor.commit();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
    }


}
