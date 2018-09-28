package com.daracul.android.secondexercizeapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.daracul.android.secondexercizeapp.data.NewsItem;
import com.daracul.android.secondexercizeapp.utils.Utils;
import com.squareup.picasso.Picasso;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NewsRecyclerAdapter extends RecyclerView.Adapter<NewsRecyclerAdapter.ViewHolder>{
    @NonNull
    private final List<NewsItem> news;
    @NonNull
    private final LayoutInflater inflater;
    @NonNull
    private final int picturePixels;
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm 'at' dd.MM.yyyy",Locale.getDefault());

    public NewsRecyclerAdapter(@NonNull Context context, @NonNull List<NewsItem> news) {
        this.news = news;
        this.inflater=LayoutInflater.from(context);
        this.picturePixels = Utils.convertDpToPixel(110, context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.news_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(news.get(position));

    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView categoryTextView;
        private final TextView topicTextView;
        private final TextView previewTextView;
        private final ImageView pictureView;
        private final TextView dateTextView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryTextView = itemView.findViewById(R.id.category);
            topicTextView = itemView.findViewById(R.id.topic);
            previewTextView = itemView.findViewById(R.id.preview);
            pictureView = itemView.findViewById(R.id.news_picture);
            dateTextView = itemView.findViewById(R.id.date);
        }

         void bind(NewsItem newsItem) {
            categoryTextView.setText(newsItem.getCategory().getName());
            topicTextView.setText(newsItem.getTitle());
            previewTextView.setText(newsItem.getPreviewText());
            dateTextView.setText(sdf.format(newsItem.getPublishDate()));
            Picasso.get().load(newsItem.getImageUrl()).
                    resize(picturePixels,picturePixels).centerCrop().into(pictureView);

        }
    }
}
