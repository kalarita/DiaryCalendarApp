package com.example.diarycalendar.ui.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import java.util.Calendar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.diarycalendar.DetailDairy;
import com.example.diarycalendar.ui.adapter.MyAdapter;
import com.example.diarycalendar.R;
import com.example.diarycalendar.bean.DiaryItem;
import com.example.diarycalendar.bean.ItemInfos;
import com.example.diarycalendar.diytocal.db.DiaryDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link todayinmyhis_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class todayinmyhis_fragment extends Fragment {
    private LinearLayout mLinerlayout;
    private ListView lst_view;
    private List<ItemInfos> lst;
    private DiaryDatabaseHelper mDiaryDatabaseHelper;
    private SQLiteDatabase mDb;
    private Cursor mCursor;
    private List<DiaryItem> mDiaryItems = new ArrayList<>();



    public todayinmyhis_fragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static todayinmyhis_fragment newInstance() {
        return new todayinmyhis_fragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);
        mLinerlayout = (LinearLayout)inflater.inflate(R.layout.items_of_today,container,false);

        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH)+1;
        int day = c.get(Calendar.DATE);
        String now_date = month+"月"+day+"日";
        lst = new ArrayList<ItemInfos>();
        lst_view = (ListView) mLinerlayout.findViewById(R.id.diary_lv);
        mDiaryDatabaseHelper = new DiaryDatabaseHelper(getContext(), "dtc_db", null, 1);
        mDb = mDiaryDatabaseHelper.getWritableDatabase();
        mCursor = mDb.query("Diary",new String[]{"id","date", "title","content"},null,null,null,null,null);
        if (mCursor.getCount() == 0) {
            lst.add(new ItemInfos("","使用提示","没有记录"));
        }
        while(mCursor.moveToNext())
        {
            //title,content, date
            String title = mCursor.getString(mCursor.getColumnIndex("title"));
            String content = mCursor.getString(mCursor.getColumnIndex("content"));
            String date = mCursor.getString(mCursor.getColumnIndex("date"));
            String mon_day = date.substring(5);
            if(now_date.equals(mon_day)){
                lst.add(new ItemInfos(date,title,content));
            }
        }

        MyAdapter myAdapter = new MyAdapter(lst,getContext());
        lst_view.setAdapter(myAdapter);

        lst_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItemInfos itemInfos = lst.get(position);
                String sp = "没有记录";
                if(!itemInfos.getContent().equals(sp)){
                    Intent intent1 = new Intent(getContext(), DetailDairy.class);
                    intent1.putExtra("date",itemInfos.getDate());
                    intent1.putExtra("title",itemInfos.getTitle());
                    intent1.putExtra("content",itemInfos.getContent());
                    startActivity(intent1);
                }
                else{
                    Toast.makeText(getActivity(), "空日记不能查看",Toast.LENGTH_LONG).show();
                }
            }
        });
        return mLinerlayout;
    }
}