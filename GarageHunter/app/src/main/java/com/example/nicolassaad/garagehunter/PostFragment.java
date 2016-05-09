package com.example.nicolassaad.garagehunter;

/**
 * Created by nicolassaad on 4/30/16.
 */

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.firebase.client.Firebase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PostFragment extends Fragment {
    private Button backButton;
    private Button postButton;

    private Button previewButton;

    private EditText editTitle;
    private EditText editDesc;
    private EditText editAddress;
    private Spinner spinnerDay;

    public static final String PREVIEW_KEY = "PrevKey";

    GarageSale garageSale;

    private String title;
    private String desc;
    private String address;
    private String dayOfWeek;

    ArrayList<String> previewItems;


    Firebase mFirebaseRef;

    public PostFragment() {
    }

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);

        backButton = (Button) view.findViewById(R.id.post_back_button);
        postButton = (Button) view.findViewById(R.id.post);

        previewButton = (Button) view.findViewById(R.id.preview_post);

        editAddress = (EditText) view.findViewById(R.id.post_address);
        editTitle = (EditText) view.findViewById(R.id.post_title);
        editDesc = (EditText) view.findViewById(R.id.post_desc);
        spinnerDay = (Spinner) view.findViewById(R.id.post_spinner);

        previewItems = new ArrayList<>();

        mFirebaseRef = new Firebase("https://garagesalehunter.firebaseio.com");

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double lat= 0.0, lng= 0.0;
                Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                address = editAddress.getText().toString();

                try {
                    List<Address> addresses = geocoder.getFromLocationName(address, 1);
                    Log.d("PostFragment", addresses.get(0).getLatitude() + "");
                    Log.d("PostFragment", addresses.get(0).getLongitude() + "");
                    lat = addresses.get(0).getLatitude();
                    lng = addresses.get(0).getLongitude();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                garageSale = new GarageSale(editTitle.getText().toString(), editDesc.getText().toString(),
                        editAddress.getText().toString(), lat, lng, spinnerDay.getSelectedItem().toString());
                mFirebaseRef.push().setValue(garageSale);

                clearEditTexts();

                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        previewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingPreviewIntent();

                Intent toPreviewIntent = new Intent(v.getContext(), PreviewActivity.class);
                toPreviewIntent.putStringArrayListExtra(PREVIEW_KEY, previewItems);
                startActivity(toPreviewIntent);
            }
        });

        return view;
    }

    private void settingPreviewIntent() {
        title = editTitle.getText().toString();
        desc = editDesc.getText().toString();
        address = editAddress.getText().toString();
        dayOfWeek = (String)spinnerDay.getSelectedItem();

        previewItems.add(0, title);
        previewItems.add(1, desc);
        previewItems.add(2, address);
        previewItems.add(3, dayOfWeek);
    }

    private void clearEditTexts() {
        editTitle.setText("");
        editDesc.setText("");
        editAddress.setText("");
        spinnerDay.dispatchDisplayHint(View.VISIBLE);

    }
}