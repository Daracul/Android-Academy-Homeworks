package com.daracul.android.secondexercizeapp.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.Date;



@Entity(tableName = "news")
@TypeConverters(DateConverter.class)
public class News {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    @ColumnInfo(name = "image_url")
    private String imageUrl;
    private String category;
    @ColumnInfo(name = "publish_date")
    private Date publishDate;
    @ColumnInfo(name = "preview_text")
    private String previewText;
    @ColumnInfo(name = "text_url")
    private String textUrl;

    public News(String title, String imageUrl, String category, Date publishDate,
                String previewText, String textUrl) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.category = category;
        this.publishDate = publishDate;
        this.previewText = previewText;
        this.textUrl = textUrl;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public String getPreviewText() {
        return previewText;
    }

    public String getTextUrl() {
        return textUrl;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public void setPreviewText(String previewText) {
        this.previewText = previewText;
    }

    public void setTextUrl(String textUrl) {
        this.textUrl = textUrl;
    }
}
