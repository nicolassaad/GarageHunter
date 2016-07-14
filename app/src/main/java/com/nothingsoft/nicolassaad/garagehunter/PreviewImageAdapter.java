package com.nothingsoft.nicolassaad.garagehunter;

/**
 * Created by nicolassaad on 5/12/16.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * RecyclerView Adapter that sets up and populates the recycler_image_layout with images that
 * are received via an intent from the PostFragment and is displayed in the PreviewActivity
 */
public class PreviewImageAdapter extends RecyclerView.Adapter<PreviewImageAdapter.MyViewHolder> {

    private List<String> imagesList;
    private Context context;
    private static final String TAG = "PreviewImageAdapter";

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.recycler_image);
        }
    }

    public PreviewImageAdapter(List<String> imagesList, Context context) {
        this.imagesList = imagesList;
        this.context = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_image_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        URI pic1 = URI.create(imagesList.get(position));
        String path = pic1.getPath();
        Log.d(TAG, pic1.getPath());
        String absolutePath = "file://" + path;
        File file = null;
        try {
            file = new File(new URI(absolutePath));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        Picasso.with(context).load(file).centerCrop().resize(425, 570).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }
}

