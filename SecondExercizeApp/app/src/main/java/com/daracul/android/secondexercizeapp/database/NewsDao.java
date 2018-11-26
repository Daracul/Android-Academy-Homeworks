package com.daracul.android.secondexercizeapp.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;


import io.reactivex.Flowable;


@Dao
public interface NewsDao {

    @Query("SELECT * FROM news")
    List<News> getAllNews();

    @Query("SELECT * FROM news ORDER BY publish_date DESC")
    Flowable<List<News>> getAllNewsObservable();

    @Query("SELECT * FROM news WHERE id = :id")
    News getNewsById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(News... news);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(News news);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateNews(News news);

    @Delete
    void delete(News news);

    @Query("DELETE FROM news")
    void deleteAll();

}
