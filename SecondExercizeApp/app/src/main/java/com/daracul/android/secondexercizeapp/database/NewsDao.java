package com.daracul.android.secondexercizeapp.database;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import io.reactivex.Observable;

@Dao
public interface NewsDao {

    @Query("SELECT * FROM news")
    List<News> getAllNews();

    @Query("SELECT * FROM news ORDER BY publish_date DESC")
    Observable<List<News>> getAllNewsObservable();

    @Query("SELECT * FROM news WHERE id = :id")
    News getNewsById(int id);

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
