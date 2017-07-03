package com.kappstudio.apps.instagramm.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kappstudio.apps.instagramm.R;

import java.util.List;

import com.kappstudio.apps.instagramm.pojo.ImageObject;
import com.kappstudio.apps.instagramm.utils.ImageLoader;

/**
 * Created by kanishk on 11/12/16.
 */

public class ImagesAdapter extends RecyclerView.Adapter {

    private List<ImageObject> list;
    Context c;

    public ImagesAdapter(Context context, List<ImageObject> imagelist, RecyclerView recyclerView) {
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
                R.layout.user_images_row, parent, false);
        return new ImagesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ImagesAdapter.ImagesViewHolder) {

            ImageObject imageObject = list.get(position);

            new ImageLoader(((ImagesViewHolder) holder).imageView).execute(imageObject.getImage());

            ((ImagesViewHolder) holder).list= imageObject;

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ImagesViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        public ImageView imageView;
        public ImageObject list;

        public ImagesViewHolder(View v) {

            super(v);
            imageView = (ImageView) v.findViewById(R.id.image);


        }

    }
}
