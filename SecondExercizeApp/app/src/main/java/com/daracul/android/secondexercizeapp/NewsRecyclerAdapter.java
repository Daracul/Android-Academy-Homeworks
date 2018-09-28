package com.daracul.android.secondexercizeapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.daracul.android.secondexercizeapp.data.NewsItem;
import com.daracul.android.secondexercizeapp.utils.Utils;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NewsRecyclerAdapter extends RecyclerView.Adapter<NewsRecyclerAdapter.ViewHolder>{
    private static final int IMAGE_DP = 110;
    @NonNull
    private final List<NewsItem> news;
    @NonNull
    private final LayoutInflater inflater;
    @NonNull
    private final OnItemClickListener clickListener;
    private final int picturePixels;


    public NewsRecyclerAdapter(@NonNull Context context, @NonNull List<NewsItem> news,
                               @NonNull OnItemClickListener clickListener) {
        this.news = news;
        this.inflater=LayoutInflater.from(context);
        this.picturePixels = Utils.convertDpToPixel(IMAGE_DP, context);
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.news_item, parent, false), clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(news.get(position));

    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView categoryTextView;
        private final TextView topicTextView;
        private final TextView previewTextView;
        private final ImageView pictureView;
        private final TextView dateTextView;

        ViewHolder(@NonNull View itemView, @NonNull final OnItemClickListener clickListener) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        clickListener.onItemClick(position);
                    }
                }
            });

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
            dateTextView.setText(Utils.convertDateToString(newsItem.getPublishDate()));
            Utils.loadImageAndSetToView(newsItem.getImageUrl(),pictureView, picturePixels);
        }
    }
}
