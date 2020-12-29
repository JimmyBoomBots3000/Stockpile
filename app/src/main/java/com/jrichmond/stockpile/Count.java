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
    private long mItem;

    @ColumnInfo(name = "location")
    private long mLocation;

    @ColumnInfo(name = "count")
    private int mQty;


    public void setId(long id) {
        mId = id;
    }

    public long getId() {
        return mId;
    }


    public long getItem() {
        return mItem;
    }

    public void setItem(long item) {
        mItem = item;
    }


    public long getLocation() {
        return mLocation;
    }

    public void setLocation(long loc) {
        mLocation = loc;
    }


    public int getQty() { return mQty; }

    public void setQty(int qty) {
        mQty = qty;
    }
}

