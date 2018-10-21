package com.daracul.android.secondexercizeapp.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HomeDTO {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("num_results")
    @Expose
    private Integer numResults;
    @SerializedName("results")
    @Expose
    private List<ResultDTO> results = null;

    public String getStatus() {
        return status;
    }


    public Integer getNumResults() {
        return numResults;
    }


    public List<ResultDTO> getResults() {
        return results;
    }


}
