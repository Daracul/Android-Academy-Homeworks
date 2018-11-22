package com.daracul.android.secondexercizeapp.database;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;



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
