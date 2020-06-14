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
import com.example.diarycalendar.bean.DiaryItem;

import java.util.Collections;
import java.util.List;

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<DiaryItem> mDiaryBeanList;

    public DiaryAdapter(Context context, List<DiaryItem> mDiaryBeanList){
        mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mDiaryBeanList = mDiaryBeanList;
        Collections.reverse(mDiaryBeanList);
    }
    @Override
    public DiaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DiaryViewHolder(mLayoutInflater.inflate(R.layout.item_diary, parent, false));
    }

    @Override
    public void onBindViewHolder(final DiaryViewHolder holder, final int position) {
        holder.mTvDate.setText(mDiaryBeanList.get(position).getDate());
        holder.mTvTitle.setText(mDiaryBeanList.get(position).getTitle());
        holder.mTvContent.setText(mContext.getString(R.string.spaces) + mDiaryBeanList.get(position).getContent());
        holder.mIvEdit.setVisibility(View.INVISIBLE);
        holder.mIvDelete.setVisibility(View.INVISIBLE);

        /**
         * 当点击日记的内容时候，则显示出编辑按钮
         */
        if(mDiaryBeanList.get(position).getId() > 0)
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
        return mDiaryBeanList.size();
    }
    public interface OnItemClickListener{
        void onItemClick(View v, int position);
    }
    public OnItemClickListener mOnItemClickListener;
    public void setOnClickListener(OnItemClickListener onItemClickListener)
    {
        this.mOnItemClickListener = onItemClickListener;
    }
     class DiaryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView mIvDelete;
        TextView mTvDate;
        TextView mTvTitle;
        TextView mTvContent;
        ImageView mIvEdit;
        LinearLayout mLlTitle;
        LinearLayout mLl;
        ImageView mIvCircle;
        LinearLayout mLlControl;
        RelativeLayout mRlEdit;

        DiaryViewHolder(View view){
            super(view);
            mIvCircle = (ImageView) view.findViewById(R.id.iv_image);
            mTvDate = (TextView) view.findViewById(R.id.tv_date);
            mTvTitle = (TextView) view.findViewById(R.id.tv_title);
            mTvContent = (TextView) view.findViewById(R.id.tv_content);
            mIvEdit = (ImageView) view.findViewById(R.id.iv_edit);
            mIvDelete = (ImageView) view.findViewById(R.id.iv_delete);
            mLlTitle = (LinearLayout) view.findViewById(R.id.main_ll_title);
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
