package com.example.diarycalendar.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diarycalendar.R;
import com.example.diarycalendar.ui.data.GoodsInfo;

import java.util.List;

public class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.ItemHolder> {
    private List<GoodsInfo> mGoodsList;
    private Context context;

    public GoodsAdapter(Context context, List<GoodsInfo> goodsList) {
        this.context = context;
        this.mGoodsList = goodsList;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.goods_item, parent, false);
        ItemHolder itemHolder = new ItemHolder(view);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder itemHolder, int position) {
        GoodsInfo goodsInfo = mGoodsList.get(position);
        //ItemHolder itemHolder = (ItemHolder) viewHolder;
        itemHolder.title.setText(goodsInfo.getTitle());
    }

    @Override
    public int getItemCount() {
        return mGoodsList.size();
    }

    // 定义列表项的视图持有者
    static class ItemHolder extends RecyclerView.ViewHolder {
        TextView title;

        public ItemHolder(View view) {
            super(view);
            this.title = (TextView) view.findViewById(R.id.text_home);
        }
    }
}
