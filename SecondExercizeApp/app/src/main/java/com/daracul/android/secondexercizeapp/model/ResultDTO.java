package com.daracul.android.secondexercizeapp.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultDTO {
    @SerializedName("section")
    @Expose
    private String section;
    @SerializedName("subsection")
    @Expose
    private String subsection;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("abstract")
    @Expose
    private String shortText;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("byline")
    @Expose
    private String author;
    @SerializedName("published_date")
    @Expose
    private String publishedDate;
    @SerializedName("multimedia")
    @Expose
    private List<MultimediaDTO> multimedia = null;

    public String getSection() {
        return section;
    }


    public String getSubsection() {
        return subsection;
    }


    public String getTitle() {
        return title;
    }


    public String getShortText() {
        return shortText;
    }


    public String getUrl() {
        return url;
    }


    public String getAuthor() {
        return author;
    }


    public String getPublishedDate() {
        return publishedDate;
    }


    public List<MultimediaDTO> getMultimedia() {
        return multimedia;
    }

    public String checkAndReturnImageUrl() {
        if (getMultimedia().size() != 0) {
            for (MultimediaDTO multimedia : getMultimedia()) {
                if (multimedia.getFormat().equals("thumbLarge")) {
                    return multimedia.getUrl();
                }
            }
        }
        return "";
    }

}
