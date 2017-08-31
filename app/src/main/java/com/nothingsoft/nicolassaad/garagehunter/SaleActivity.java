package com.nothingsoft.nicolassaad.garagehunter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.nothingsoft.nicolassaad.garagehunter.Fragments.MapsFragment;
import com.nothingsoft.nicolassaad.garagehunter.Models.GarageSale;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This activity is where the Garage sale data that is queried gets populated by making
 * a query to the Firebase server
 */
public class SaleActivity extends AppCompatActivity {

    private Query query;
    private Firebase mFirebase;
    private SaleImageAdapter mAdapter;
    private RecyclerView recyclerView;
    private List<String> imageList = new ArrayList<>();
    private RelativeLayout progressLayout;
    private ProgressBar progressBar;
    private TextView progressText;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> saleArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setProgressLayout();
        setRecyclerView();
        setUpListView();

        String saleTitle = getIntent().getStringExtra(MapsFragment.SALE_KEY1);
        setTitle(saleTitle);
        mFirebase = new Firebase(getString(R.string.firebase_address));
        query = mFirebase.orderByChild("title").equalTo(saleTitle);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                GarageSale daySearch = iterator.next().getValue(GarageSale.class);
                String address = daySearch.getAddress();
                String desc = daySearch.getDescription();
                String startDate = daySearch.getStartDate();
                String endDate = daySearch.getEndDate();

                String pic1 = daySearch.getImage1();
//                byte[] imageAsBytes = Base64.decode(pic1.getBytes(), Base64.DEFAULT);
//                image1.setImageBitmap(
//                        BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));

                String pic2 = daySearch.getImage2();
//                byte[] imageAsBytes2 = Base64.decode(pic2.getBytes(), Base64.DEFAULT);
//                image2.setImageBitmap(
//                        BitmapFactory.decodeByteArray(imageAsBytes2, 0, imageAsBytes2.length));

                String pic3 = daySearch.getImage3();
//                byte[] imageAsBytes3 = Base64.decode(pic3.getBytes(), Base64.DEFAULT);
//                image3.setImageBitmap(
//                        BitmapFactory.decodeByteArray(imageAsBytes3, 0, imageAsBytes3.length));

                // Description is required to post but just this is here in case someone tries to get around it
                if (desc.isEmpty()) {
                    desc = getString(R.string.no_desc_provided_text);
                }
                saleArrayList.add(getString(R.string.desc_sale_text) + "\n" + desc);
                saleArrayList.add(getString(R.string.address_sale_text) + "\n" + address);
                saleArrayList.add(getString(R.string.date_text_1) + " " + startDate + " " + getString(R.string.date_text_2) + " " + endDate + ".");
                arrayAdapter.notifyDataSetChanged();

                imageList.add(pic1);
                imageList.add(pic2);
                imageList.add(pic3);
                mAdapter.notifyDataSetChanged();
                hideProgressLayout();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void hideProgressLayout() {
// TODO: 6/3/16 ON ANIMATION END NEEDED HERE TO HIDE PROGRESS LAYOUT BUT ITS NOT WORKING PROPERLY
        progressLayout.animate().translationXBy(progressLayout.getWidth() + 15).setDuration(900);
//        progressBar.setVisibility(View.GONE);
//        progressLayout.setVisibility(View.GONE);
//        progressText.setVisibility(View.GONE);
    }

    private void showProgressLayout() {
        progressLayout.setVisibility(View.VISIBLE);
        progressLayout.animate().translationX(-progressLayout.getWidth() - 15).setDuration(1250);
        progressBar.setVisibility(View.VISIBLE);
        progressText.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);
    }

    private void setRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new SaleImageAdapter(imageList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }

    private void setProgressLayout() {
        progressLayout = (RelativeLayout) findViewById(R.id.progress_bar_layout);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressText = (TextView) findViewById(R.id.hunting_sales_text);
        progressBar.setMax(10);
        showProgressLayout();
    }

    private void setUpListView() {
        listView = (ListView) findViewById(R.id.sale_listview);
        saleArrayList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, saleArrayList);
        listView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
    }

}
