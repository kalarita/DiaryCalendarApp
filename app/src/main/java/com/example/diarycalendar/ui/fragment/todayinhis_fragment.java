package com.example.diarycalendar.ui.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.diarycalendar.MainActivity;
import com.example.diarycalendar.R;
import com.example.diarycalendar.ui.adapter.GoodsAdapter;
import com.example.diarycalendar.ui.data.GoodsInfo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link todayinhis_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class todayinhis_fragment extends Fragment {

    String[] lst;
    private List<GoodsInfo> goodsList;
    private GoodsAdapter goodsAdapter;


    public todayinhis_fragment() {
        // Required empty public constructor
    }


    public static todayinhis_fragment newInstance() {
        todayinhis_fragment fragment = new todayinhis_fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

            for (int i = 0; i < lst.length; i++) {
                GoodsInfo goodsInfo = new GoodsInfo();
                Log.i("内容",lst[i]);
                goodsInfo.setTitle(lst[i]);
                goodsList.add(goodsInfo);
            }


        Log.d("hand","-------" + goodsList.size() + "-------");
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.fragment_todayinhis_fragment,container,false);
        RecyclerView recyclerView = (RecyclerView)linearLayout.findViewById(R.id.recyclerView);
        // 创建一个垂直方向的线性布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), recyclerView.VERTICAL,false);
        // 设置recyclerView的布局管理器
        recyclerView.setLayoutManager(layoutManager);
        //一行两个
//        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

        goodsAdapter = new GoodsAdapter(getContext(), goodsList);
        recyclerView.setAdapter(goodsAdapter);
        return linearLayout;
    }


    /**
     * @param: null
     * @result: nul
     * @description:从sharedprefence中获取数据,
     */
    private void initData(){
        String[] num ;
        String defu = "网络出故障了!";
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("todayinhis",MODE_PRIVATE);
        String todayinhis_string = sharedPreferences.getString("TODAYINHIS",defu);
        if (todayinhis_string!=null) {
            lst = (String[])todayinhis_string.split("-");
        }
        goodsList = new ArrayList<GoodsInfo>();
    }
}