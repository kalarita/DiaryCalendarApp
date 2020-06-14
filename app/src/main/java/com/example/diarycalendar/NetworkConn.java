package com.example.diarycalendar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Handler;

public class NetworkConn {
    private String mURLString;
    private HttpURLConnection mURLConnection;
    private URL mURL;
    private Handler mHandler;

    //创建对象,构件URL
    public NetworkConn(Handler mHandler,String url) throws MalformedURLException {
        mURL = new URL(url);
        this.mHandler=mHandler;
        System.out.println("创建连接对象成功");
    }

    //登录活动
    void login() throws IOException, JSONException {
        connect();
        int responsecode = mURLConnection.getResponseCode();
        if(responsecode == 200) {
            StringBuilder response = getStringBuilder();
            JSONObject jsonObject = new JSONObject(response.toString());
            if(jsonObject.getString("login").equals("True")){

                Message msg = new Message();
                msg.what=0x1;
                Bundle bundle = new Bundle();
                bundle.putString("account",jsonObject.getString("account"));
                msg.setData(bundle);
                mHandler.sendMessage(msg);
            }else {
                Message msg = new Message();
                msg.what=0x2;
                mHandler.sendMessage(msg);
            }
        }
    }

    void signup() throws IOException, JSONException {
        connect();
        int responsecode = mURLConnection.getResponseCode();
        if(responsecode == 200){
            StringBuilder response = getStringBuilder();
            JSONObject jsonObject = new JSONObject(response.toString());
            if(jsonObject.getString("status").equals("registed")){
                Message msg = new Message();
                msg.what=0x3;
                Bundle bundle = new Bundle();
                bundle.putString("account",jsonObject.getString("account"));
                msg.setData(bundle);
                mHandler.sendMessage(msg);
            }else {
                Message msg = new Message();
                msg.what=0x4;
                mHandler.sendMessage(msg);
            }
        }else {
            System.out.println(responsecode);
        }

    }

    void getpic(String storage) throws IOException {

        final File pic = new File(storage,getdatestring());
        if (!pic.exists()) {
            pic.createNewFile();
            pic.canWrite();
            pic.canRead();
            getpicfile(pic);
        }else{
            System.out.println("文件已经存在!");
        }
    }

    private void getpicfile(File pic) throws IOException {
        connect();
        int responsecode = mURLConnection.getResponseCode();
        if(responsecode == 200){
            InputStream inputStream = mURLConnection.getInputStream();
            System.out.println("已经获取数据流");
            Bitmap picBM = BitmapFactory.decodeStream(inputStream);
            try {
                FileOutputStream out = new FileOutputStream(pic);
                picBM.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
                System.out.println("成功获取到了文件");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String gettodayidea() throws IOException, JSONException {
        connect();
        int responsecode = mURLConnection.getResponseCode();
        if (responsecode == 200){
            StringBuilder response = getStringBuilder();
            String resultstr = response.toString().replace("\"","")
                    .replace("\\n,","").replace("[","")
                    .replace("]","");
            System.out.println("这是刚刚处理过的str");
            System.out.println(resultstr);
            return resultstr;
        }
        return "网络错误.稍后再试";
    }

    private StringBuilder getStringBuilder() throws IOException {
        System.out.println("连接成功!");
        InputStream inputStream = mURLConnection.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
        StringBuilder response = new StringBuilder();
        String line;
        while((line =bufferedReader.readLine()) != null){
            response.append(line);
        }
        System.out.println(response.toString());
        return response;
    }


    private void connect() throws IOException {
        mURLConnection = (HttpURLConnection) mURL.openConnection();
        mURLConnection.connect();
        System.out.println("已经连接");
    }

    private String getdatestring(){
        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyyMMdd");// a为am/pm的标记
        Date date = new Date();// 获取当前时间
        System.out.println(sdf.format(date)+".jpg");
        System.out.println("这是连接类中的时间"+sdf.format(date));
        return sdf.format(date)+".jpg";
    }

}
