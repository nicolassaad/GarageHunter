package com.nothingsoft.nicolassaad.garagehunter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
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
    private ImagesAdapter mAdapter;
    private RecyclerView recyclerView;
    private List<String> imageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button backButton = (Button) findViewById(R.id.sale_back_button);
        final TextView saleDOW = (TextView) findViewById(R.id.sale_DOW);
        final TextView saleAddress = (TextView) findViewById(R.id.sale_address_text);
        final TextView saleDesc = (TextView) findViewById(R.id.sale_desc);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new ImagesAdapter(imageList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

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
                String weekday = daySearch.getWeekday();

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

                saleAddress.setText(address);
                saleDOW.setText(weekday);
                saleDesc.setText(desc);

                imageList.add(pic1);
                imageList.add(pic2);
                imageList.add(pic3);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

}
