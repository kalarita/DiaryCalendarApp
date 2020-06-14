package com.example.diarycalendar.diytocal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.diarycalendar.R;
import com.example.diarycalendar.bean.EventItem;

import java.util.Collections;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<EventItem> mEventBeanList;

    public EventAdapter(Context context, List<EventItem> mEventBeanList){
        mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mEventBeanList = mEventBeanList;
        Collections.reverse(mEventBeanList);
    }
    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EventViewHolder(mLayoutInflater.inflate(R.layout.item_event, parent, false));
    }

    @Override
    public void onBindViewHolder(final EventViewHolder holder, final int position) {
        holder.mTvDate.setText(mEventBeanList.get(position).getDate());
        holder.mTvContent.setText(mContext.getString(R.string.spaces) + mEventBeanList.get(position).getEvent());
        holder.mIvEdit.setVisibility(View.INVISIBLE);
        holder.mIvDelete.setVisibility(View.INVISIBLE);

        /**
         * 当点击日程的内容时候，则显示出编辑按钮
         */
        if(mEventBeanList.get(position).getId() > 0)
        {
            holder.mLl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.mIvEdit.getVisibility() == View.INVISIBLE) {
                        holder.mIvEdit.setVisibility(View.VISIBLE);
                        holder.mIvDelete.setVisibility(View.VISIBLE);

                    }else {
                        holder.mIvEdit.setVisibility(View.INVISIBLE);
                        holder.mIvDelete.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mEventBeanList.size();
    }
    public interface OnItemClickListener{
        void onItemClick(View v, int position);
    }
    public OnItemClickListener mOnItemClickListener;
    public void setOnClickListener(OnItemClickListener onItemClickListener)
    {
        this.mOnItemClickListener = onItemClickListener;
    }
     class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView mIvDelete;
        TextView mTvDate;
        TextView mTvContent;
        ImageView mIvEdit;
        LinearLayout mLl;
        ImageView mIvCircle;
        LinearLayout mLlControl;
        RelativeLayout mRlEdit;

        EventViewHolder(View view){
            super(view);
            mIvCircle = (ImageView) view.findViewById(R.id.iv_image);
            mTvDate = (TextView) view.findViewById(R.id.tv_date);
            mTvContent = (TextView) view.findViewById(R.id.tv_content);
            mIvEdit = (ImageView) view.findViewById(R.id.iv_edit);
            mIvDelete = (ImageView) view.findViewById(R.id.iv_delete);
            mLl = (LinearLayout) view.findViewById(R.id.item_ll);
            mLlControl = (LinearLayout) view.findViewById(R.id.item_ll_control);
            mRlEdit = (RelativeLayout) view.findViewById(R.id.item_rl_edit);
            mIvEdit.setOnClickListener(this);
            mIvDelete.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            if(mOnItemClickListener != null)
            {
                mOnItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }
}
