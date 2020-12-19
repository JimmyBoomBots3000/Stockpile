package com.jrichmond.stockpile;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "phoneNum")

public class PhoneNumber {

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    @NonNull
    private long mId;

    @ColumnInfo(name = "number")
    private String mPhoneNumber;


    public void setId(long id) {
        mId = id;
    }

    public long getId() {
        return mId;
    }


    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String text) { mPhoneNumber = text;}
}


