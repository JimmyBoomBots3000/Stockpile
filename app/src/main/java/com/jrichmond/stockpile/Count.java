package com.jrichmond.stockpile;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "counts", foreignKeys = {
    @ForeignKey(
            entity = Item.class,
            parentColumns = "itemId",
            childColumns = "item"
    ),

    @ForeignKey(
            entity = Location.class,
            parentColumns = "locationId",
            childColumns = "location"
    )
})

public class Count {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long mId;

    @ColumnInfo(name = "item")
    private String mItem;

    @ColumnInfo(name = "location")
    private long mLocation;

    @ColumnInfo(name = "count")
    private int mCount;


    public void setId(long id) {
        mId = id;
    }

    public long getId() {
        return mId;
    }


    public String getItem() {
        return mItem;
    }

    public void setItem(String text) {
        mItem = text;
    }


    public long getLocation() {
        return mLocation;
    }

    public void setLocation(long locId) {
        mLocation = locId;
    }


    public int getCount() {
        return mCount;
    }

    public void setCount(int qty) {
        mCount = qty;
    }
}

