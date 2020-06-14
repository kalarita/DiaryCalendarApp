package com.example.diarycalendar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.common.base.Joiner;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class signup_activity extends AppCompatActivity implements View.OnClickListener {
    private  Button mSignButton;
    private EditText mUsrNameText;
    private EditText mAccountText;
    private EditText mPwdText;
    private String mAccount;
    private String mPwd;
    private Boolean mMailMatched;
    private String mSignupUrlString;
    private Handler mHandler;
    public Intent mIntent;
    private String mUsrName;
    public Bundle mBundle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_layout);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        mHandler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case 0x3:
                        mIntent = new Intent(signup_activity.this,MainActivity.class);
                        mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        setlogin();
                        setAccount(msg);
                        startActivity(mIntent);
                        break;
                    case 0x4:
                        Toast.makeText(getBaseContext(), "用户已注册!跳转到登录", Toast.LENGTH_LONG).show();
                        mIntent = new Intent(signup_activity.this,login_activity.class);
                        mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(mIntent);
                        break;
                }
            }
        };
        init();

    }

    private void setAccount(@NonNull Message msg) {
        mBundle = msg.getData();
        SharedPreferences sharedPreferences = getSharedPreferences("account",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("account", mBundle.getString("account"));
        editor.commit();
    }

    private void setlogin() {
        SharedPreferences sharedPreferences = getSharedPreferences("login",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("loginstatus","islogin");
        editor.commit();
    }

    private void init() {
        mSignButton = findViewById(R.id.signup_btn);
        mUsrNameText = findViewById(R.id.usrname_input);
        mSignButton.setOnClickListener(this);
        mAccountText = findViewById(R.id.signaccount_input);
        mPwdText = findViewById(R.id.signpwd_input);

    }

    @Override
    public void onClick(View v) {
        mUsrName = mUsrNameText.getText().toString();
        mAccount = mAccountText.getText().toString();
        mPwd = mPwdText.getText().toString();
        switch (v.getId()){
            case R.id.signup_btn:
                        getpic();
                        mMailMatched = checkEmail(mAccount);
                        if (mUsrName.equals("")){
                            Toast.makeText(signup_activity.this,getString(R.string.usrnameisnull_string),Toast.LENGTH_SHORT).show();
                            break;
                        }
                        if (mAccount.equals("")){
                            Toast.makeText(signup_activity.this,getString(R.string.mailisnull_string),Toast.LENGTH_SHORT).show();
                            break;
                        }
                        if (mPwd.equals("")){
                            Toast.makeText(getBaseContext(),getString(R.string.pwdisnull_string),Toast.LENGTH_SHORT).show();
                            break;
                        }
                        if(!mMailMatched){
                            Toast.makeText(signup_activity.this,getString(R.string.mailnotmatch_string),Toast.LENGTH_SHORT).show();
                            break;
                        }else{
                            getidea();
                            new Thread(){
                                @Override
                                public void run() {
                                    gettodayinhis();
                                }
                            }.start();
                            mSignupUrlString = "http://173.82.168.215:5000/register/"+mUsrName+"+"+mAccount+"+"+mPwd;
                        }
                        new Thread(){
                            @Override
                            public void run() {
                                try {
                                    NetworkConn networkConn = new NetworkConn(mHandler,mSignupUrlString);
                                    networkConn.signup();
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();

                break;
        }
    }
    public  boolean checkEmail(String email) {
        boolean mFlag = false;
        try {
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            mFlag = matcher.matches();
        } catch (Exception e) {
            mFlag = false;
        }
        return mFlag;
    }


    private void getpic() {
        new Thread(){
            @Override
            public void run() {
                String urlString = "http://173.82.168.215:5000/todaypic";
                NetworkConn picConn = null;
                try {
                    picConn = new NetworkConn(mHandler,urlString);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                File filePictures = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                String storage = filePictures.getAbsolutePath()+"/";
                try {
                    picConn.getpic(storage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

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
        new Thread(){
            @Override
            public void run() {
                try {
                    String urlString  = "http://173.82.168.215:5000/todayidea";
                    NetworkConn networkConn = new NetworkConn(mHandler,urlString);
                    String ideastr = networkConn.gettodayidea();
                    SharedPreferences sharedPreferences = getSharedPreferences("idea",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    System.out.println(ideastr);
//                    ideastr = ideastr.replace("   ","");
//                    System.out.println(ideastr);
                    String[] lst = ideastr.split("   ");
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
        }.start();
    }
}
