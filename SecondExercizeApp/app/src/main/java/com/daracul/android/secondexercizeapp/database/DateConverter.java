package com.daracul.android.secondexercizeapp.database;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;



public class DateConverter {

    @TypeConverter
    public Date toDate(long timeStamp){
        return new Date(timeStamp);
    }

    @TypeConverter
    public long toTimeStamp(Date date){
        return date.getTime();
    }
}
