package com.nothingsoft.nicolassaad.garagehunter;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.nothingsoft.nicolassaad.garagehunter.Fragments.PostFragment;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * PreviewActivity allows the user to see how their post will look before they post it.
 * It grabs the data via an intent from the PostFragment and displays it.
 */
public class PreviewActivity extends AppCompatActivity {

    private ArrayList<String> previewItems;
    private ArrayList<String> previewItemsCopy;
    private ArrayAdapter<String> previewArrayAdapter;
    private ListView previewListView;
    private String title;
    private String desc;
    private String address;
    private String startDate;
    private String endDate;

    private PreviewImageAdapter mAdapter;
    private RecyclerView recyclerView;
    private List<String> imageList = new ArrayList<>();

    private File f;
    private File f2;
    private File f3;

    private static final String TAG = "PreviewActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setUpListView();
        setRecyclerView();
        setInfo();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /**
     * Takes data from the intent coming from PostFragment and populates this activity will text
     * and images
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void setInfo() {
        setTitle("");
        previewItems = getIntent().getStringArrayListExtra(PostFragment.PREVIEW_KEY);
        for (int j = 0; j < previewItems.size(); j++) {
            Log.d(TAG, previewItems.get(j));
        }
            setTitle(previewItems.get(0));

            URI uri1 = URI.create(previewItems.get(5));
            f = new File(uri1);
            imageList.add(f.toString());

            URI uri2 = URI.create(previewItems.get(6));
            f2 = new File(uri2);
            imageList.add(f2.toString());

            URI uri3 = URI.create(previewItems.get(7));
            f3 = new File(uri3);
            imageList.add(f3.toString());
        }


    private void setRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.preview_recycler_view);
        mAdapter = new PreviewImageAdapter(imageList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Picasso.with(this).invalidate(f);
        Picasso.with(this).invalidate(f2);
        Picasso.with(this).invalidate(f3);
    }

    private void setUpListView() {
        previewListView = (ListView) findViewById(R.id.preview_listview);
        previewItems = getIntent().getStringArrayListExtra(PostFragment.PREVIEW_KEY);
        title = previewItems.get(0);
        desc = previewItems.get(1);
        address = previewItems.get(2);
        startDate = previewItems.get(3);
        Log.d("PreviewActivity", "startDate = " + startDate);
        endDate = previewItems.get(4);
        Log.d("PreviewActivity", "endDate = " + endDate);
        previewItemsCopy = new ArrayList<>();
        previewItemsCopy.add(getString(R.string.desc_sale_text) + "\n" + desc);
        previewItemsCopy.add(getString(R.string.address_sale_text) + "\n" + address);
        previewItemsCopy.add(getString(R.string.date_text_1) + " " + startDate + " " +  getString(R.string.date_text_2) + " " + endDate + ".");
        previewArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, previewItemsCopy);
        previewListView.setAdapter(previewArrayAdapter);
        previewArrayAdapter.notifyDataSetChanged();
    }
}
