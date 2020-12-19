package com.jrichmond.stockpile;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ItemDao {

    @Query("SELECT * FROM items WHERE itemId = :id")
    Item getItem(long id);

    @Query("SELECT * FROM items ORDER BY itemNumber")
    List<Item> getItems();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertItem(Item item);

    @Update
    void updateItem(Item item);

    @Delete
    void deleteItem(Item item);
}

