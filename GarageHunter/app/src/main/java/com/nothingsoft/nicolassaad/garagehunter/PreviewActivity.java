package com.nothingsoft.nicolassaad.garagehunter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.nothingsoft.nicolassaad.garagehunter.Fragments.PostFragment;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

/**
 * PreviewActivity allows the user to see how their post will look before they post it.
 * It grabs the data via an intent from the PostFragment and displays it.
 */
public class PreviewActivity extends AppCompatActivity {
    private TextView descText;
    private TextView addressText;
    private TextView dayOfWeek;
    private ImageView image1;
    private ImageView image2;
    private ImageView image3;
    private ArrayList<String> previewItems;
    private File f;
    private File f2;
    private File f3;


//    private ImagesAdapter mAdapter;
//    private RecyclerView recyclerView;
//    private List<String> imageList = new ArrayList<>();

    private static final String TAG = "PreviewActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
//        mAdapter = new ImagesAdapter(imageList);
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
//        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setAdapter(mAdapter);

        descText = (TextView) findViewById(R.id.preview_desc_text);
        addressText = (TextView) findViewById(R.id.preview_address);
        dayOfWeek = (TextView) findViewById(R.id.preview_DOW);
        image1 = (ImageView) findViewById(R.id.preview_image_holder1);
        image2 = (ImageView) findViewById(R.id.preview_image_holder2);
        image3 = (ImageView) findViewById(R.id.preview_image_holder3);

        setInfo();

//        Button backButton = (Button) findViewById(R.id.preview_back_button);
//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
        setViews();
    }

    /**
     * Takes data from the intent coming from PostFragment and populates this activity will text
     * and images
     */
    private void setInfo() {
        setTitle("");
        descText.setText("");
        addressText.setText("");
        dayOfWeek.setText("");
        clearImages(image1);
        clearImages(image2);
        clearImages(image3);
        previewItems = getIntent().getStringArrayListExtra(PostFragment.PREVIEW_KEY);
        for (int j = 0; j < previewItems.size(); j++) {
            Log.d(TAG, previewItems.get(j));
            setTitle(previewItems.get(0));
            descText.setText(previewItems.get(1));
            addressText.setText(previewItems.get(2));
            dayOfWeek.setText(previewItems.get(3));
            URI uri1 = URI.create(previewItems.get(4));
            f = new File(uri1);

            Picasso.with(this).load(f).centerCrop().resize(400, 375).into(image1);


//        Bitmap takenImage = BitmapFactory.decodeFile(uri1.getPath());
//        image1.setImageBitmap(takenImage);
//        imageList.add(previewItems.get(4));

            URI uri2 = URI.create(previewItems.get(5));
            f2 = new File(uri2);

            Picasso.with(this).load(f2).centerCrop().resize(400, 375).into(image2);

//        imageList.add(previewItems.get(5));
//        Bitmap takenImage2 = BitmapFactory.decodeFile(uri2.getPath());
//        image2.setImageBitmap(takenImage2);

            URI uri3 = URI.create(previewItems.get(6));
            f3 = new File(uri3);

            Picasso.with(this).load(f3).centerCrop().resize(400, 375).into(image3);

//        Bitmap takenImage3 = BitmapFactory.decodeFile(uri3.getPath());
//        image3.setImageBitmap(takenImage3);
//        imageList.add(previewItems.get(6));
//        mAdapter.notifyDataSetChanged();
        }
    }

    private void clearImages(ImageView image) {
        image.setBackground(getResources().getDrawable(R.drawable.image_holder));
    }

    private void setViews() {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        f.deleteOnExit();
        f2.deleteOnExit();
        f3.deleteOnExit();
    }
}
