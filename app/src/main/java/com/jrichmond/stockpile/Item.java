package com.jrichmond.stockpile;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "items")

public class Item {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "itemId")
    private long mId;

    @ColumnInfo(name = "itemName")
    private String mItemName;

    @ColumnInfo(name = "itemNumber")
    private String mItemNumber;

    @ColumnInfo(name = "itemDescription")
    private String mItemDescription;

    public void setId(long id) {
        mId = id;
    }

    public long getId() {
        return mId;
    }


    public String getItemName() {
        return mItemName;
    }

    public void setItemName(String text) {
        mItemName = text;
    }


    public String getItemNumber() {
        return mItemNumber;
    }

    public void setItemNumber(String text) {
        mItemNumber = text;
    }


    public String getItemDescription() {
        return mItemDescription;
    }

    public void setItemDescription(String text) {
        mItemDescription = text;
    }
}

