package com.daracul.android.secondexercizeapp.data;

import com.daracul.android.secondexercizeapp.database.News;

import java.util.ArrayList;
import java.util.List;

public class DataConverter {
    public static List<News> convertDTOListToNewsItem(List<ResultDTO> resultDTOList){
        List<News> newsList = new ArrayList<>(resultDTOList.size());
        for (ResultDTO resultDTO : resultDTOList){
            newsList.add(new News(resultDTO.getTitle(),resultDTO.checkAndReturnImageUrl(),
                    resultDTO.getSection(),resultDTO.getPublishedDate(),resultDTO.getShortText(),
                    resultDTO.getUrl()));
        }
        return newsList;
    }
}
