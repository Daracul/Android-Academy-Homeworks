package com.daracul.android.secondexercizeapp.ui.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daracul.android.secondexercizeapp.R;
import com.daracul.android.secondexercizeapp.database.News;
import com.daracul.android.secondexercizeapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NewsRecyclerAdapter extends RecyclerView.Adapter<NewsRecyclerAdapter.ViewHolder> {
    @NonNull
    private List<News> news;
    @NonNull
    private final LayoutInflater inflater;
    @NonNull
    private final OnItemClickListener clickListener;


    public NewsRecyclerAdapter(@NonNull Context context,
                               @NonNull OnItemClickListener clickListener) {
        news = new ArrayList<>();
        this.inflater = LayoutInflater.from(context);
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.news_item, parent, false);
        return new ViewHolder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(news.get(position));

    }


    @Override
    public int getItemCount() {
        return news.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int itemId);
    }

    public void swapData(List<News> newsList) {
        this.news = newsList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView categoryTextView;
        private final TextView topicTextView;
        private final TextView previewTextView;
        private final ImageView pictureView;
        private final TextView dateTextView;
        private int id;

        ViewHolder(@NonNull View itemView, @NonNull final OnItemClickListener clickListener) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(id);
                }
            });

            categoryTextView = itemView.findViewById(R.id.category);
            topicTextView = itemView.findViewById(R.id.topic);
            previewTextView = itemView.findViewById(R.id.preview);
            pictureView = itemView.findViewById(R.id.news_picture);
            dateTextView = itemView.findViewById(R.id.date);
        }

        void bind(News newsItem) {
            categoryTextView.setText(newsItem.getCategory());
            topicTextView.setText(newsItem.getTitle());
            previewTextView.setText(newsItem.getPreviewText());
            dateTextView.setText(Utils.formatDateTime(itemView.getContext(),newsItem.getPublishDate()));
            String imageUrl = newsItem.getImageUrl();
            if (!imageUrl.isEmpty()) {
                Utils.loadImageAndSetToView(imageUrl, pictureView);
            } else pictureView.setImageResource(R.drawable.placeholder);
            id = newsItem.getId();
        }
    }
}
