package com.example.diarycalendar.diytocal;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.diarycalendar.R;
import com.example.diarycalendar.bean.DiaryItem;
import com.example.diarycalendar.diytocal.db.DiaryDatabaseHelper;
import com.example.diarycalendar.diytocal.utils.BackupUtil;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

public class UpdateDiaryActivity extends Activity {
    private static final String TAG = "UpdateDiaryActivity";
    private FloatingActionButton mSave;
    private FloatingActionButton mBack;
    private FloatingActionsMenu mFABMenu;
    private EditText mTitle;
    private EditText mContent;
    private ImageView mBack2;
    private TextView mTopTitle;
    private AlertDialog mBackSure;
    private DiaryDatabaseHelper mDiaryDatabaseHelper;
    private SQLiteDatabase mDb;
    private TextView mDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diary);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        initViews();
        mBack = new FloatingActionButton(getBaseContext());
        mBack.setColorNormal(R.color.fab_color);
        mBack.setIcon(R.drawable.ic_cancel_black_36dp);
        mFABMenu.addButton(mBack);
        mSave = new FloatingActionButton(getBaseContext());
        mSave.setColorNormal(R.color.fab_color);
        mSave.setIcon(R.drawable.ic_save_black_36dp);
        mFABMenu.addButton(mSave);
        Intent intent = getIntent();
        final DiaryItem diaryItem = intent.getParcelableExtra("diaryItem");
        mDiaryDatabaseHelper = new DiaryDatabaseHelper(this,"dtc_db",null,1);
        mDb = mDiaryDatabaseHelper.getWritableDatabase();
        mDate.setText(diaryItem.getDate());
        mTitle.setText(diaryItem.getTitle());
        mContent.setText(diaryItem.getContent());
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBackSure =  new AlertDialog.Builder(UpdateDiaryActivity.this)
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
                mBackSure =  new AlertDialog.Builder(UpdateDiaryActivity.this)
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
                ContentValues values = new ContentValues();
                values.put("title", mTitle.getText().toString());
                values.put("content", mContent.getText().toString());
                mDb.update("Diary",values,"id = ?", new String[]{String.valueOf(diaryItem.getId())});
                BackupUtil.rewrite(mDb, "Diary", mDate.getText().toString(), UpdateDiaryActivity.this);
//                SharedPreferences sp = getResources().getString(R.string.app_config);
//                boolean auto_update = sp.getBoolean("auto_update", false);
//                if(auto_update)
//                {
//                    String url = sp.getString("cloud_url", null);
//                    String uid = sp.getString("cloud_uid", null);
//                    String psd = sp.getString("cloud_psd", null);
//                    WebDavSardineTool tool = new WebDavSardineTool(url, uid, psd);
//                    tool.mkdir("/Diary");
//                    tool.delete("/Diary/"+mDate.getText().toString()+".txt");
//                    try {
//                          tool.upload(BackupUtil.getDest("Diary", mDate.getText().toString()), BackupUtil.getSrc("Diary", UpdateDiaryActivity.this, mDate.getText().toString()));
//                        } catch (FileNotFoundException e) {
//                           e.printStackTrace();
//                        }
//                }
//                else
//                {
//                    Toast.makeText(UpdateDiaryActivity.this, "您尚未启动网盘，请前往设置启动,否则无法备份到云端",Toast.LENGTH_LONG).show();
//                }
                Intent intent = new Intent(UpdateDiaryActivity.this, DiaryMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                UpdateDiaryActivity.this.finish();
            }
        });
    }

    private void initViews() {
        mFABMenu = findViewById(R.id.FAB_Menu);
        mTitle = findViewById(R.id.add_diary_et_title);
        mContent = findViewById(R.id.add_diary_et_content);
        mBack2 = findViewById(R.id.common_iv_back);
        mTopTitle = findViewById(R.id.common_tv_title);
        mTopTitle.setText("修改日记");
        mDate = findViewById(R.id.add_diary_tv_date);
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
}
