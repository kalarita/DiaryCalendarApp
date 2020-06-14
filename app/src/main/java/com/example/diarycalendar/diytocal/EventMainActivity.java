package com.example.diarycalendar.diytocal;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diarycalendar.R;
import com.example.diarycalendar.bean.EventItem;
import com.example.diarycalendar.diytocal.adapter.EventAdapter;
import com.example.diarycalendar.diytocal.db.DiaryDatabaseHelper;
import com.example.diarycalendar.diytocal.utils.GetDate;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class EventMainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private EventAdapter mEventRecycleAdapter;
    private List<EventItem> mEventItems = new ArrayList<>();
    private FloatingActionButton mAdd;
    private AlertDialog mDeleteSure;
    private static final String TAG = "EventMainActivity";
    private DiaryDatabaseHelper mDiaryDatabaseHelper;
    private SQLiteDatabase mDb;
    private Cursor mCursor;
    private EventAdapter.OnItemClickListener mOnItemClickListener = new EventAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View v, int position) {
            final EventItem eventItem = mEventItems.get(position);
            //final EventItem eventItem = new EventItem("ceshiyixia","2020年06月11日");
            if (v.getId() == R.id.iv_edit) {
                if (eventItem.getId() > 0)
                {
                    Intent intent = new Intent(EventMainActivity.this, UpdateEventActivity.class);
                    intent.putExtra("eventItem",eventItem);
                    startActivity(intent);
                    EventMainActivity.this.finish();
                }
                else {
                    Toast.makeText(EventMainActivity.this, "提示信息不能作为日程修改",Toast.LENGTH_LONG).show();
                }

            }
            else
            {
                    mDeleteSure = new AlertDialog.Builder(EventMainActivity.this)
                            .setTitle("确定要删除此日程吗?")
                            .setMessage("此改动会同步到云端")
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String id = String.valueOf(eventItem.getId());
                                    mDb.delete("Event", "id = ?", new String[]{id});
                                    refresh();
                                }
                            })
                            .create();
                    mDeleteSure.show();

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        initData();
        initRecyclerView();
        mAdd = (FloatingActionButton) findViewById(R.id.fab_add);


        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventMainActivity.this, AddEventActivity.class);
                startActivity(intent);
                EventMainActivity.this.finish();
            }
        });
        mEventRecycleAdapter.setOnClickListener(mOnItemClickListener);
    }

    private void initData() {
        mDiaryDatabaseHelper = new DiaryDatabaseHelper(this, "dtc_db", null, 1);
         mDb = mDiaryDatabaseHelper.getWritableDatabase();
         mCursor = mDb.query("Event",new String[]{"id","date", "event"},null,null,null,null,null);
        if (mCursor.getCount() == 0) {
            EventItem eventItem = new EventItem("现在没有任何日程，快点击右下角按钮添加吧。", GetDate.getDate().toString());

            mEventItems.add(eventItem);
        }
        while(mCursor.moveToNext())
        {
            //title,content, date
            String event = mCursor.getString(mCursor.getColumnIndex("event"));
            String date = mCursor.getString(mCursor.getColumnIndex("date"));
            EventItem eventItem = new EventItem(event,date);
            eventItem.setId(mCursor.getInt(mCursor.getColumnIndex("id")));
            mEventItems.add(eventItem);
        }
    }

    private void initRecyclerView() {
        //找到recyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_diary);
        //创建适配器
        mEventRecycleAdapter = new EventAdapter(this, mEventItems);
        mRecyclerView.setAdapter(mEventRecycleAdapter);
        //设置布局管理器
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
    }
    public void refresh()
    {
        Intent intent = new Intent(EventMainActivity.this, EventMainActivity.class);
        startActivity(intent);
        EventMainActivity.this.finish();
    }

    @Override
    protected void onDestroy() {
        try{
            if(mCursor != null)
            {
                mCursor.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
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
        }
        super.onDestroy();
    }
}