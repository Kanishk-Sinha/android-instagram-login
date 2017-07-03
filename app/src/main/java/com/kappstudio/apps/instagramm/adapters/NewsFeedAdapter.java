package com.kappstudio.apps.instagramm.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kappstudio.apps.instagramm.R;

import java.util.List;

import com.kappstudio.apps.instagramm.pojo.NewsFeedObject;
import com.kappstudio.apps.instagramm.utils.ImageLoader;

/**
 * Created by kanishk on 12/12/16.
 */

public class NewsFeedAdapter extends RecyclerView.Adapter {

    private List<NewsFeedObject> list;
    Context c;

    public NewsFeedAdapter(Context context, List<NewsFeedObject> imagelist, RecyclerView recyclerView) {

        list = imagelist;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();
            c = context;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder vh;

        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.news_feed_row, parent, false);
        return new NewsFeedViewHolder(v);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ImagesAdapter.ImagesViewHolder) {

            NewsFeedObject newsFeedObject = list.get(position);

            new ImageLoader(((ImagesAdapter.ImagesViewHolder) holder).imageView).execute(newsFeedObject.getNewsfeed_image_url());
            ((NewsFeedViewHolder) holder).textView.setText(newsFeedObject.getNewsfeed_user_name());

            ((NewsFeedAdapter.NewsFeedViewHolder) holder).list= newsFeedObject;

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class NewsFeedViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        public ImageView imageView;
        public TextView textView;
        public NewsFeedObject list;

        public NewsFeedViewHolder(View v) {

            super(v);
            imageView = (ImageView) v.findViewById(R.id.image);
            textView = (TextView) v.findViewById(R.id.newsfeed_username);

        }

    }
}
