package ru.geekbrains.mymenuauth.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class NoteData implements Parcelable {
    private String id;
    private String titel;
    private String text;
    private Date date;

    public NoteData(String titel, String text, Date date) {
        this.titel = titel;
        this.text = text;
        this.date = date;
    }

    protected NoteData(Parcel in) {
        titel = in.readString();
        text = in.readString();
        date = new Date(in.readLong());
    }

    public String getTitel() {
        return this.titel;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return this.date;
    }

    public static final Creator<NoteData> CREATOR = new Creator<NoteData>() {
        @Override
        public NoteData createFromParcel(Parcel in) {
            return new NoteData(in);
        }

        @Override
        public NoteData[] newArray(int size) {
            return new NoteData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(titel);
        dest.writeString(text);
        dest.writeLong(date.getTime());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
