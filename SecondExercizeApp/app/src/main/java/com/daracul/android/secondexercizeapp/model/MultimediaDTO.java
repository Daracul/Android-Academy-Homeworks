package com.daracul.android.secondexercizeapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MultimediaDTO {
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("format")
    @Expose
    private String format;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("caption")
    @Expose
    private String caption;
    @SerializedName("copyright")
    @Expose
    private String copyright;

    public String getUrl() {
        return url;
    }


    public String getFormat() {
        return format;
    }


    public String getType() {
        return type;
    }


    public String getCaption() {
        return caption;
    }


    public String getCopyright() {
        return copyright;
    }

}
