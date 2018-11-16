package com.daracul.android.secondexercizeapp.database;

import java.util.Date;

import androidx.room.TypeConverter;

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
