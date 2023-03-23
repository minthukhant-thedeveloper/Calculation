package com.cyber.calculation.Utils;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.cyber.calculation.Interfaces.DataDao;
import com.cyber.calculation.Models.Data;

@Database(entities =  {Data.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DataDao dataDao();
}
