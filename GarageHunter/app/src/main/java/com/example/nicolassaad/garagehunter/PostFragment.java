package com.example.nicolassaad.garagehunter;

/**
 * Created by nicolassaad on 4/30/16.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.firebase.client.Firebase;

public class PostFragment extends Fragment {
    private Button backButton;
    private Button postButton;
    private EditText editTitle;
    private EditText editDesc;
    private EditText editAddress;
    private Spinner spinnerDay;

    Firebase mFirebaseRef;

    public PostFragment() {
    }

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);

        backButton = (Button) view.findViewById(R.id.post_back_button);
        postButton = (Button) view.findViewById(R.id.post);
        editAddress = (EditText) view.findViewById(R.id.post_address);
        editTitle = (EditText) view.findViewById(R.id.post_title);
        editDesc = (EditText) view.findViewById(R.id.post_desc);
        spinnerDay = (Spinner) view.findViewById(R.id.post_spinner);

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
                GarageSale garageSale = new GarageSale(editTitle.getText().toString(), editDesc.getText().toString(),
                editAddress.getText().toString(), spinnerDay.getSelectedItem().toString());
                mFirebaseRef.push().setValue(garageSale);

                clearEditTexts();

                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void clearEditTexts() {
        editTitle.setText("");
        editDesc.setText("");
        editAddress.setText("");
        spinnerDay.dispatchDisplayHint(View.VISIBLE);

    }
}