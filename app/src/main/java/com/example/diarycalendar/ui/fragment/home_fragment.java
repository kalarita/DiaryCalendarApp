package com.example.diarycalendar.ui.fragment;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.diarycalendar.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class home_fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters

    public NestedScrollView nestedScrollView;
    public TextView textview;
    public ImageView imgview;
    public Handler mHandler;

    public home_fragment() {
        // Required empty public constructor
    }

    public static home_fragment newInstance() {
        home_fragment fragment = new home_fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        nestedScrollView = (NestedScrollView) inflater.inflate(R.layout.fragment_home_fragment, container, false);
        System.out.println("初始化fragment");
        imgview = (ImageView)nestedScrollView.findViewById(R.id.card1_img);
        try {
            Bitmap bitmap = BitmapFactory.decodeFile( "/storage/emulated/0/Android/data/com.example.diarycalendar/files/Pictures/"+getdatestring());
            System.out.println("设置bitmap");
            imgview.setImageBitmap(bitmap);
            System.out.println("设置图片成功!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("设置图片失败");
        }
        textview = nestedScrollView.findViewById(R.id.card2_text);
        SharedPreferences sharedPreferences =getActivity(). getSharedPreferences("idea",MODE_PRIVATE);
        if(sharedPreferences!=null){
            String ideastr = sharedPreferences.getString("en","呕吼!").trim()+"."+"\n"+sharedPreferences.getString("zh","网络崩溃了");
            textview.setText(ideastr);
        }

        return nestedScrollView;
    }
    private String getdatestring(){
        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyyMMdd");// a为am/pm的标记
        Date date = new Date();// 获取当前时间
        System.out.println("这是fragment中的时间"+sdf.format(date));
        return sdf.format(date)+".jpg";
    }
}