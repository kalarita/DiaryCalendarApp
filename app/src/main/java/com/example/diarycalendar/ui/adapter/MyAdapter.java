package com.example.diarycalendar.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.diarycalendar.R;
import com.example.diarycalendar.bean.ItemInfos;

import java.util.List;

public class MyAdapter extends BaseAdapter {
    private List<ItemInfos> lst;
    private Context context;

    public MyAdapter(List<ItemInfos> lst, Context context) {
        this.lst = lst;
        this.context = context;
    }

    public MyAdapter(List<ItemInfos> lst) {
        this.lst = lst;
        this.context = context;
    }

    @Override
    public int getCount() {
        return lst.size();
    }

    @Override
    public Object getItem(int position) {
        return lst.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_today,parent,false);
            holder = new ViewHolder();
            holder.item_date = (TextView) convertView.findViewById(R.id.date_tv);
            holder.item_title = (TextView) convertView.findViewById(R.id.title_tv);
            holder.item_content = (TextView) convertView.findViewById(R.id.content_tv);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        ItemInfos itemInfos = lst.get(position);
        holder.item_date.setText(itemInfos.getDate());
        holder.item_title.setText(itemInfos.getTitle());
        holder.item_content.setText(itemInfos.getContent());
        return convertView;
    }

    class ViewHolder{
        TextView item_date;
        TextView item_title;
        TextView item_content;
    }
}
