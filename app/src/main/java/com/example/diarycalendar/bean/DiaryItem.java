package com.example.diarycalendar.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class DiaryItem implements Parcelable {
    private int mId;
    private String mTitle;
    private String mContent;

    protected DiaryItem(Parcel in) {
        mId = in.readInt();
        mTitle = in.readString();
        mContent = in.readString();
        mDate = in.readString();
    }

    public static final Creator<DiaryItem> CREATOR = new Creator<DiaryItem>() {
        @Override
        public DiaryItem createFromParcel(Parcel in) {
            return new DiaryItem(in);
        }

        @Override
        public DiaryItem[] newArray(int size) {
            return new DiaryItem[size];
        }
    };

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public DiaryItem(String title, String content, String date) {
        mTitle = title;
        mContent = content;
        mDate = date;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    private String mDate;

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mTitle);
        dest.writeString(mContent);
        dest.writeString(mDate);
    }
}
