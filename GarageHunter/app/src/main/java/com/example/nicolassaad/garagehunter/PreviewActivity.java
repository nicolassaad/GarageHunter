package com.example.nicolassaad.garagehunter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class PreviewActivity extends AppCompatActivity {

    private static final String TAG = "PreviewActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView titleText = (TextView) findViewById(R.id.preview_title_text);
        TextView descText = (TextView) findViewById(R.id.preview_desc_text);
        TextView addressText = (TextView) findViewById(R.id.preview_address);
        TextView dayOfWeek = (TextView) findViewById(R.id.preview_DOW);


        final ArrayList<String> previewItems = getIntent().getStringArrayListExtra(PostFragment.PREVIEW_KEY);
        for (int j = 0; j < previewItems.size(); j++) {
            Log.d(TAG, previewItems.get(j));
        }

        setTitle(previewItems.get(0));
        descText.setText(previewItems.get(1));
        addressText.setText(previewItems.get(2));
        dayOfWeek.setText(previewItems.get(3));

        Button backButton = (Button) findViewById(R.id.preview_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
//                Intent toPostIntent = new Intent(PreviewActivity.this, MainActivity.class);
//                toPostIntent.putStringArrayListExtra(PostFragment.PREVIEW_KEY, previewItems);
//                startActivity(toPostIntent);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
