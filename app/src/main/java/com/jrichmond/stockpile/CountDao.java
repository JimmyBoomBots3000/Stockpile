package com.jrichmond.stockpile;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CountDao {

    @Query("SELECT * FROM counts WHERE item = :item")
    List<Count> getCountsOfItem(long item);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertCount(Count count);

    @Update
    void updateCount(Count count);

    @Delete
    void deleteCount(Count count);
}

