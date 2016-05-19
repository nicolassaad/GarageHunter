package com.nothingsoft.nicolassaad.garagehunter;

/**
 * Created by nicolassaad on 5/12/16.
 */

import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * RecyclerView Adapter that sets up and populates the recycler_image_layout with images that
 * are received as Strings from Firebase and converted to Bitmaps.
 */
    public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.MyViewHolder> {

        private List<String> imagesList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public ImageView image;

            public MyViewHolder(View view) {
                super(view);
                image = (ImageView) view.findViewById(R.id.recycler_image);
            }
        }

        public ImagesAdapter(List<String> imagesList) {
            this.imagesList = imagesList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_image_layout, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            String pic1 = imagesList.get(position);
            byte[] imageAsBytes = Base64.decode(pic1.getBytes(), Base64.DEFAULT);
            holder.image.setImageBitmap(
                    BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
        }

        @Override
        public int getItemCount() {
            return imagesList.size();
        }
    }

