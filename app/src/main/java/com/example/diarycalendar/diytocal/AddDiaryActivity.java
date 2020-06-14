package com.example.diarycalendar.diytocal;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.diarycalendar.R;
import com.example.diarycalendar.diytocal.db.DiaryDatabaseHelper;
import com.example.diarycalendar.diytocal.utils.BackupUtil;
import com.example.diarycalendar.diytocal.utils.GetDate;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.example.diarycalendar.ui.WebDav.*;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.io.FileNotFoundException;
import java.io.IOException;

public class AddDiaryActivity extends Activity {
    private FloatingActionButton mSave;
    private FloatingActionButton mBack;
    private FloatingActionsMenu mFABMenu;
    private ImageView mBack2;
    private TextView mTopTitle;
    private DiaryDatabaseHelper mDiaryDatabaseHelper;
    private SQLiteDatabase mDb;
    private EditText mTitle;
    private EditText mContent;
    private AlertDialog mBackSure;
    private TextView mDate;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        setContentView(R.layout.activity_add_diary);
        initViews();
        mBack = new FloatingActionButton(getBaseContext());
        mBack.setColorNormal(R.color.fab_color);
        mBack.setIcon(R.drawable.ic_cancel_black_36dp);
        mFABMenu.addButton(mBack);
        mSave = new FloatingActionButton(getBaseContext());
        mSave.setColorNormal(R.color.fab_color);
        mSave.setIcon(R.drawable.ic_save_black_36dp);
        mFABMenu.addButton(mSave);
        mDiaryDatabaseHelper = new DiaryDatabaseHelper(this,"dtc_db",null,1);
        mDb = mDiaryDatabaseHelper.getWritableDatabase();
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBackSure =  new AlertDialog.Builder(AddDiaryActivity.this)
                        .setTitle("确定返回")
                        .setMessage("正在编辑的信息尚未保存")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .create();
                mBackSure.show();
            }
        });
        mBack2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBackSure =  new AlertDialog.Builder(AddDiaryActivity.this)
                        .setTitle("确定返回")
                        .setMessage("正在编辑的信息尚未保存")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .create();
                mBackSure.show();
            }
        });
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload myupload = new upload();
                myupload.start();
                Intent intent = new Intent(AddDiaryActivity.this, DiaryMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                AddDiaryActivity.this.finish();
            }
        });
    }
    private void initViews() {
        mFABMenu = findViewById(R.id.FAB_Menu);
        mBack2 = findViewById(R.id.common_iv_back);
        mTopTitle = findViewById(R.id.common_tv_title);
        mTitle = findViewById(R.id.add_diary_et_title);
        mContent = findViewById(R.id.add_diary_et_content);
        mDate = findViewById(R.id.add_diary_tv_date);
        mDate.setText("今天，"+GetDate.getDate().toString());
        mTopTitle.setText("添加日记");
    }

    @Override
    protected void onDestroy() {
        try{
            if (mDb!=null) {
                mDb.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            try{
                if (mDiaryDatabaseHelper!=null) {
                    mDiaryDatabaseHelper.close();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }
    class upload extends Thread{
        @Override
        public void run() {
            Looper.prepare();
            ContentValues values = new ContentValues();
            String title = mTitle.getText().toString();
            String content = mContent.getText().toString();
            String date = GetDate.getDate().toString();
            values.put("title",  title);
            values.put("content",  content);
            values.put("date", date);
            mDb.insert("Diary", null, values);
            BackupUtil.rewrite(mDb,"Diary",date,AddDiaryActivity.this);
//                SharedPreferences sp = getResources().getString(R.string.app_config);
            SharedPreferences sp = getSharedPreferences("config",MODE_PRIVATE);
            String  auto_update = sp.getString("auto_update", " ");

            if(auto_update.equals("true"))
            {
                String url = sp.getString("cloud_url", null);
                String uid = sp.getString("cloud_uid", null);
                String psd = sp.getString("cloud_psd", null);
                WebDavSardineUtil tool = new WebDavSardineUtil(url, uid, psd);
                try {
                    tool.mkdir("/Diary");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    System.out.println("开始上传");
                    tool.upload(BackupUtil.getDest("Diary", GetDate.getDate().toString()), BackupUtil.getSrc("Diary", AddDiaryActivity.this, GetDate.getDate().toString()));
                    System.out.println("结束上传");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                Toast.makeText(AddDiaryActivity.this, "您尚未启动网盘，请前往设置启动,否则无法备份到云端",Toast.LENGTH_LONG).show();
            }
            Looper.loop();
        }
    }
}
