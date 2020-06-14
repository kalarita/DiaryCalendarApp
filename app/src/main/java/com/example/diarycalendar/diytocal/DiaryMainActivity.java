package com.example.diarycalendar.diytocal;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diarycalendar.R;
import com.example.diarycalendar.bean.DiaryItem;
import com.example.diarycalendar.diytocal.adapter.DiaryAdapter;
import com.example.diarycalendar.diytocal.db.DiaryDatabaseHelper;
import com.example.diarycalendar.diytocal.utils.GetDate;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class DiaryMainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private DiaryAdapter mDiaryRecycleAdapter;
    private List<DiaryItem> mDiaryItems = new ArrayList<>();
    private FloatingActionButton mAdd;
    private AlertDialog mDeleteSure;
    private static final String TAG = "DiaryMainActivity";
    private DiaryDatabaseHelper mDiaryDatabaseHelper;
    private SQLiteDatabase mDb;
    private Cursor mCursor;
    private DiaryAdapter.OnItemClickListener mOnItemClickListener = new DiaryAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View v, int position) {
            final DiaryItem diaryItem = mDiaryItems.get(position);

            if (v.getId() == R.id.iv_edit) {
                if(diaryItem.getId() > 0)
                {
                    Intent intent = new Intent(DiaryMainActivity.this, UpdateDiaryActivity.class);
                    intent.putExtra("diaryItem",diaryItem);
                    startActivity(intent);
                    DiaryMainActivity.this.finish();
                }
                else
                {
                    Toast.makeText(DiaryMainActivity.this, "提示信息不能作为日记修改",Toast.LENGTH_LONG).show();

                }

            }
            else
            {
                    mDeleteSure = new AlertDialog.Builder(DiaryMainActivity.this)
                            .setTitle("确定要删除此日记吗?")
                            .setMessage("此改动会同步到云端")
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String id = String.valueOf(diaryItem.getId());
                                    mDb.delete("Diary","id = ?",new String[]{id});
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

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_main);
        initData();
        initRecyclerView();
        mAdd = (FloatingActionButton) findViewById(R.id.fab_add);


        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DiaryMainActivity.this, AddDiaryActivity.class);
                startActivity(intent);
            }
        });
        mDiaryRecycleAdapter.setOnClickListener(mOnItemClickListener);
    }

    private void initData() {
        mDiaryDatabaseHelper = new DiaryDatabaseHelper(this, "dtc_db", null, 1);
         mDb = mDiaryDatabaseHelper.getWritableDatabase();
         mCursor = mDb.query("Diary",new String[]{"id","date", "title","content"},null,null,null,null,null);
        if (mCursor.getCount() == 0) {
            DiaryItem diaryItem = new DiaryItem("使用提示","您还未写过日记，快点击右下角的按钮开始写日记吧", GetDate.getDate().toString());
            mDiaryItems.add(diaryItem);
        }
        while(mCursor.moveToNext())
        {
            //title,content, date
            String title = mCursor.getString(mCursor.getColumnIndex("title"));
            String content = mCursor.getString(mCursor.getColumnIndex("content"));
            String date = mCursor.getString(mCursor.getColumnIndex("date"));
            DiaryItem diaryItem = new DiaryItem(title,content,date);
            diaryItem.setId(mCursor.getInt(mCursor.getColumnIndex("id")));
            Log.d(TAG, "initData: "+diaryItem.getId());
            mDiaryItems.add(diaryItem);
        }
    }

    private void initRecyclerView() {
        //找到recyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_diary);
        //创建适配器
        mDiaryRecycleAdapter = new DiaryAdapter(this, mDiaryItems);
        mRecyclerView.setAdapter(mDiaryRecycleAdapter);
        //设置布局管理器
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
    }
    public void refresh()
    {
        finish();
        Intent intent = new Intent(DiaryMainActivity.this,DiaryMainActivity.class);
        startActivity(intent);
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