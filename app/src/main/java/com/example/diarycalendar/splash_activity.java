package com.example.diarycalendar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import com.google.common.base.Joiner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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
                //直接在应用启动的时候直接获取今日图片以及今日的一句话
                new Thread(){
                    @Override
                    public void run() {
                        getpic();
                    }
                }.start();
                //获取历史上的今天数据
                new Thread(){
                    @Override
                    public void run() {
                        gettodayinhis();
                    }
                }.start();
                new Thread(){
                    @Override
                    public void run() {
                        getidea();
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

    private void getpic() {
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

    private void gettodayinhis() {
        final String url = "http://173.82.168.215:5000/todayinhis";
        HttpURLConnection conn;
        String[] lst;
        try {
            URL mLoginUrl = new URL(url);
            conn = (HttpURLConnection) mLoginUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            int code = conn.getResponseCode();
            if(code == 200){
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is,"utf-8"));
                StringBuilder response = new StringBuilder();
                String line = reader.readLine();
                while ((line = reader.readLine()) != null){
                    line = line.trim();
                    response.append(line);
                }
                System.out.println(response.toString());
                lst = response.toString().replace("\"", "").replace("n]", "").replace("\\", "").split("n,");
                String todayinhis_string = Joiner.on('-').join(lst);


                SharedPreferences sharedPreferences = getSharedPreferences("todayinhis",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("TODAYINHIS",todayinhis_string);
                editor.commit();
            }else{
                SharedPreferences sharedPreferences = getSharedPreferences("todayinhis",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("TODAYINHIS","网络错误!");
                editor.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //
    private void getidea() {
            String urlString  = "http://173.82.168.215:5000/todayidea";
            try {
                NetworkConn networkConn = new NetworkConn(mHandler,urlString);
                String ideastr = networkConn.gettodayidea();
                SharedPreferences sharedPreferences = getSharedPreferences("idea",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
//                System.out.println(ideastr);
//                ideastr = ideastr.replace("   ","");
//                System.out.println(ideastr);
                String[] lst = ideastr.split("   ");
                System.out.println(lst.length);
                editor.putString("en",lst[0]+"\n");
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
    public Byte[] inputstream2byte(InputStream inputStream){

        Byte[] bytes = new Byte[4096]; return bytes;
    }

}
