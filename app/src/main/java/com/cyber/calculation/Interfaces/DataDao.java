package com.cyber.calculation.Interfaces;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.cyber.calculation.Models.Data;

import java.util.List;
@Dao
public interface DataDao {

    @Query("SELECT * FROM data")
    List<Data> getAll();

    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insert(Data data);

    @Insert
    public long insertData(Data data);

    @Delete
    void delete(Data data);

    @Update
    void update(Data data);


}
