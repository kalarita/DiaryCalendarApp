package com.example.diarycalendar;

import android.annotation.SuppressLint;
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

public class login_activity extends AppCompatActivity implements View.OnClickListener {
    private Button mLoginButton ;
    private  Button mSignButton;
    private EditText mAccountText;
    private EditText mPwdText;
    private String mAccount;
    private String mPwd;
    private Boolean mMailMatched;
    private String mLoginUrlString;
    private Handler mHandler;
    public Intent mIntent;
    public Bundle mBundle;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        getpic();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        mHandler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case 0x1:
                        mIntent = new Intent(login_activity.this,MainActivity.class);
                        mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        setlogin();
                        setAccount(msg);
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(mIntent);
                            }
                        },750);
                        break;
                    case 0x2:
                        Toast.makeText(getBaseContext(), "登录失败,请检查账户密码!", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        };
        initview();


    }

    private void setAccount(@NonNull Message msg) {
        mBundle = msg.getData();
        SharedPreferences sharedPreferences = getSharedPreferences("account",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("account",mBundle.getString("account"));
        editor.commit();
    }

    private void initview() {
        mLoginButton = findViewById(R.id.login_btn);
        mLoginButton.setOnClickListener(this);
        mSignButton = findViewById(R.id.sign_btn);
        mSignButton.setOnClickListener(this);
        mAccountText = (EditText)findViewById(R.id.account_input);
        mPwdText = (EditText)findViewById(R.id.pwd_input);
    }

    @Override
    public void onClick(View v) {
        mAccount = mAccountText.getText().toString();
        mPwd = mPwdText.getText().toString();
        switch(v.getId()){
            case R.id.login_btn:
                getpic();
                if (mAccount.equals("")){
                Toast.makeText(login_activity.this,getString(R.string.mailisnull_string),Toast.LENGTH_SHORT).show();
                break;
            }
                if (mPwd.equals("")){
                    Toast.makeText(login_activity.this,getString(R.string.pwdisnull_string),Toast.LENGTH_SHORT).show();
                    break;
                }
                mMailMatched = checkEmail(mAccount);
                if(!mMailMatched){
                    Toast.makeText(login_activity.this,getString(R.string.mailnotmatch_string),Toast.LENGTH_SHORT).show();
                    break;
                }else{

                    getidea();
                    new Thread(){
                        @Override
                        public void run() {
                            gettodayinhis();
                        }
                    }.start();
                    mLoginUrlString = "http://173.82.168.215:5000/login/"+mAccount+"+"+mPwd;
                    new Thread(){
                        @Override
                        public void run() {
                            System.out.println("ui开始连接");
                            try {
                                NetworkConn networkConn = new NetworkConn(mHandler,mLoginUrlString);
                                networkConn.login();
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                };
                break;
            case R.id.sign_btn:
                mIntent = new Intent(login_activity.this,signup_activity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mIntent);
                break;
        }
    }

    private void setlogin() {
        SharedPreferences sharedPreferences = getSharedPreferences("login",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("loginstatus","islogin");
        editor.commit();
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
