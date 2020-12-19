package com.jrichmond.stockpile;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")

public class User {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long mId;

    @ColumnInfo(name = "email")
    private String mEmail;

    @ColumnInfo(name = "password")
    private String mPassword;


    public void setId(long id) {
        mId = id;
    }

    public long getId() {
        return mId;
    }


    public String getEmail() { return mEmail; }

    public void setEmail(String text) {
        mEmail = text;
    }


    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String text) {
        mPassword = text;
    }
}

