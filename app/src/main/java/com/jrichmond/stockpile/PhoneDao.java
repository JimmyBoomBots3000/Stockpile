package com.jrichmond.stockpile;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PhoneDao {

    @Query("SELECT * FROM phoneNum WHERE number = :number")
    PhoneNumber getNumber(String number);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertNumber(PhoneNumber number);
}

