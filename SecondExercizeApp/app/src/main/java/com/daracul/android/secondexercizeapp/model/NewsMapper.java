package com.daracul.android.secondexercizeapp.model;

import android.accounts.NetworkErrorException;
import android.support.annotation.NonNull;

import com.daracul.android.secondexercizeapp.database.News;
import com.daracul.android.secondexercizeapp.network.DefaultResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class NewsMapper {
    public static List<News> convertDTOListToNewsItem(@NonNull Response<DefaultResponse<List<ResultDTO>>>  response) throws Exception{
        if (!response.isSuccessful()) {
            throw new NetworkErrorException("Response is not sucessful");
        }

        final DefaultResponse<List<ResultDTO>> body = response.body();
        if (body == null) {
            throw new NetworkErrorException("Response body is null");
        }

        final List<ResultDTO> data = body.getData();
        if (data == null||data.isEmpty()) {
            throw new Exception("Data is empty");
        }
        List<ResultDTO> resultDTOS = response.body().getData();
        List<News> newsList = new ArrayList<>(resultDTOS.size());
        for (ResultDTO resultDTO : resultDTOS){
            newsList.add(new News(resultDTO.getTitle(),resultDTO.checkAndReturnImageUrl(),
                    resultDTO.getSection(),resultDTO.getPublishedDate(),resultDTO.getShortText(),
                    resultDTO.getUrl()));
        }
        return newsList;
    }
}
