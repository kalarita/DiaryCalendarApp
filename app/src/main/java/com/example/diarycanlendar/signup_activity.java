package com.example.diarycanlendar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
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
}
