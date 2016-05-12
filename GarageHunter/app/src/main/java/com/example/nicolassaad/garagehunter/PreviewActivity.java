package com.example.nicolassaad.garagehunter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

public class PreviewActivity extends AppCompatActivity {

    private static final String TAG = "PreviewActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView descText = (TextView) findViewById(R.id.preview_desc_text);
        TextView addressText = (TextView) findViewById(R.id.preview_address);
        TextView dayOfWeek = (TextView) findViewById(R.id.preview_DOW);
        ImageView image1 = (ImageView) findViewById(R.id.preview_image_holder1);
        ImageView image2 = (ImageView) findViewById(R.id.preview_image_holder2);
        ImageView image3 = (ImageView) findViewById(R.id.preview_image_holder3);

        final ArrayList<String> previewItems = getIntent().getStringArrayListExtra(PostFragment.PREVIEW_KEY);
        for (int j = 0; j < previewItems.size(); j++) {
            Log.d(TAG, previewItems.get(j));
        }

// TODO: 5/11/16 Refactor into its own Method setInfo()
        setTitle(previewItems.get(0));
        descText.setText(previewItems.get(1));
        addressText.setText(previewItems.get(2));
        dayOfWeek.setText(previewItems.get(3));
        URI uri1 = URI.create(previewItems.get(4));
        File f = new File(uri1);
      Picasso.with(this).load(f).centerCrop().resize(125,125).into(image1);
//        Picasso.with(this).load(f).into(image1);
//        Bitmap takenImage = BitmapFactory.decodeFile(uri1.getPath());
//        image1.setImageBitmap(takenImage);

        URI uri2 = URI.create(previewItems.get(5));
        File f2 = new File(uri2);
        Picasso.with(this).load(f2).centerCrop().resize(125, 125).into(image2);
//        Bitmap takenImage2 = BitmapFactory.decodeFile(uri2.getPath());
//        image2.setImageBitmap(takenImage2);
        URI uri3 = URI.create(previewItems.get(6));
        File f3 = new File(uri3);
        Picasso.with(this).load(f3).centerCrop().resize(125, 125).into(image3);
//        Bitmap takenImage3 = BitmapFactory.decodeFile(uri3.getPath());
//        image3.setImageBitmap(takenImage3);

        Button backButton = (Button) findViewById(R.id.preview_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setViews();
    }

    private void setViews() {

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
