package com.example.diarycalendar.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class EventItem implements Parcelable {
    private int mId;
    private String mEvent;

    public EventItem(String event, String date) {
        mEvent = event;
        mDate = date;
    }

    protected EventItem(Parcel in) {
        mId = in.readInt();
        mEvent = in.readString();
        mDate = in.readString();
    }

    public static final Creator<EventItem> CREATOR = new Creator<EventItem>() {
        @Override
        public EventItem createFromParcel(Parcel in) {
            return new EventItem(in);
        }

        @Override
        public EventItem[] newArray(int size) {
            return new EventItem[size];
        }
    };

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getEvent() {
        return mEvent;
    }

    public void setEvent(String event) {
        mEvent = event;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    private String mDate;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mEvent);
        dest.writeString(mDate);
    }
}
