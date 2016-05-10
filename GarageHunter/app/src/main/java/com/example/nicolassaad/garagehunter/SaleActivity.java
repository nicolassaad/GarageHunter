package com.example.nicolassaad.garagehunter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.Iterator;

public class SaleActivity extends AppCompatActivity {

    private Query query;
    private Firebase mFirebase;

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

        String saleTitle = getIntent().getStringExtra(MapsFragment.SALE_KEY1);
        setTitle(saleTitle);

        mFirebase = new Firebase("https://garagesalehunter.firebaseio.com/");

        query = mFirebase.orderByChild("title").equalTo(saleTitle);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                    GarageSale daySearch = iterator.next().getValue(GarageSale.class);
                    String address = daySearch.getAddress();
                    String desc = daySearch.getDescription();
                    String weekday = daySearch.getWeekday();

                    saleAddress.setText(address);
                    saleDOW.setText(weekday);
                    saleDesc.setText(desc);

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
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

}
