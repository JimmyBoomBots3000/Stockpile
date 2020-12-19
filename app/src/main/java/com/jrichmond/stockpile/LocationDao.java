package com.jrichmond.stockpile;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface LocationDao {

    @Query("SELECT * FROM locations WHERE locationId = :id")
    Location getLocation(long id);


    @Query("SELECT * FROM locations ORDER BY locationName")
    List<Location> getLocations();



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertLocation(Location location);

    @Update
    void updateLocation(Location location);

    @Delete
    void deleteLocation(Location location);
}

