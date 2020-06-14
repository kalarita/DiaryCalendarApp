package com.example.diarycalendar.diytocal;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.diarycalendar.R;
import com.example.diarycalendar.bean.EventItem;
import com.example.diarycalendar.diytocal.db.DiaryDatabaseHelper;
import com.example.diarycalendar.diytocal.utils.BackupUtil;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;

public class UpdateEventActivity extends Activity {
    private static final String TAG = "UpdateDiaryActivity";
    private FloatingActionButton mSave;
    private FloatingActionButton mBack;
    private FloatingActionsMenu mFABMenu;
    private ImageView mBack2;
    private TextView mTopTitle;
    private AlertDialog mBackSure;
    private DiaryDatabaseHelper mDiaryDatabaseHelper;
    private SQLiteDatabase mDb;
    private TextView mDate;
    private EditText mEvent;
    private ImageView mDateSelector;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
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
        final EventItem eventItem = intent.getParcelableExtra("eventItem");
        mDiaryDatabaseHelper = new DiaryDatabaseHelper(this,"dtc_db",null,1);
        mDb = mDiaryDatabaseHelper.getWritableDatabase();
        mDate.setText(eventItem.getDate());
        mEvent.setText(eventItem.getEvent());
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBackSure =  new AlertDialog.Builder(UpdateEventActivity.this)
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
                mBackSure =  new AlertDialog.Builder(UpdateEventActivity.this)
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
        final DateFormat dateFormat = DateFormat.getDateInstance(1,Locale.CHINA);
        final Calendar calendar = Calendar.getInstance(Locale.CHINA);
        mDateSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateEventActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR,year);
                        calendar.set(Calendar.MONTH,month);
                        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                        mDate.setText(dateFormat.format(calendar.getTime()));

                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.show();
            }

        }
        );
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put("event", mEvent.getText().toString());
                values.put("date", mDate.getText().toString());
                mDb.update("Event",values,"id = ?", new String[]{String.valueOf(eventItem.getId())});
                BackupUtil.rewrite(mDb, "Event", mDate.getText().toString(), UpdateEventActivity.this);

                Intent intent = new Intent(UpdateEventActivity.this, EventMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                UpdateEventActivity.this.finish();
            }
        });
    }

    private void initViews() {
        mFABMenu = findViewById(R.id.right_labels);
        mEvent = findViewById(R.id.update_event_et_event);
        mBack2 = findViewById(R.id.common_iv_back);
        mTopTitle = findViewById(R.id.common_tv_title);
        mTopTitle.setText("修改日程");
        mDate = findViewById(R.id.update_event_et_date);
        mDateSelector = findViewById(R.id.iv_date_selector);
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
