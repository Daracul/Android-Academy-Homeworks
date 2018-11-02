package com.daracul.android.secondexercizeapp.database;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import io.reactivex.Observable;

@Dao
public interface NewsDao {

    @Query("SELECT * FROM news")
    List<News> getAllNews();

    @Query("SELECT * FROM news")
    Observable<List<News>> getAllNewsObservable();

    @Query("SELECT * FROM news WHERE id = :id")
    News getNewsById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(News... news);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(News news);

    @Delete
    void delete(News news);

    @Query("DELETE FROM news")
    void deleteAll();

}
