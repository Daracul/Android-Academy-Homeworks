package com.daracul.android.secondexercizeapp.database;
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {News.class},version = 1)
public abstract class AppDataBase extends RoomDatabase {
    private static AppDataBase singleton;

    private static final String DATABASE_NAME = "news.db";

    public abstract NewsDao newsDao();

    public static AppDataBase getAppDatabase (Context context){
        if (singleton == null){
            singleton = Room.databaseBuilder(context.getApplicationContext(),
                    AppDataBase.class,
                    DATABASE_NAME)
                    .build();
        }
        return singleton;
    }
}
