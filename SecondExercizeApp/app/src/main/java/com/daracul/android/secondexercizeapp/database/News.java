package com.daracul.android.secondexercizeapp.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "news")
public class News {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    @ColumnInfo(name = "image_url")
    private String imageUrl;
    private String category;
    @ColumnInfo(name = "publish_date")
    private String publishDate;
    @ColumnInfo(name = "preview_text")
    private String previewText;
    @ColumnInfo(name = "text_url")
    private String textUrl;

    public News(String title, String imageUrl, String category, String publishDate,
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

    public String getPublishDate() {
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

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public void setPreviewText(String previewText) {
        this.previewText = previewText;
    }


    public void setTextUrl(String textUrl) {
        this.textUrl = textUrl;
    }
}
