package com.jrichmond.stockpile;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "locations")

public class Location {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "locationId")
    private long mId;

    @ColumnInfo(name = "locationName")
    private String mLocationName;

    @ColumnInfo(name = "locationDescription")
    private String mLocationDescription;

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getLocationName() {
        return mLocationName;
    }

    public void setLocationName(String text) {
        mLocationName = text;
    }


    public String getLocationDescription() {
        return mLocationDescription;
    }

    public void setLocationDescription(String text) {
        mLocationDescription = text;
    }
}

