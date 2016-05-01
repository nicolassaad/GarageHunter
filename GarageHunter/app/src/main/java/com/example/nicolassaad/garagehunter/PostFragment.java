package com.example.nicolassaad.garagehunter;

/**
 * Created by nicolassaad on 4/30/16.
 */
import android.os.Bundle;

import android.support.v4.app.Fragment;

import android.view.LayoutInflater;

import android.view.View;

import android.view.ViewGroup;

public class PostFragment extends Fragment {

    public PostFragment() {

    }

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_post, container, false);

        return view;

    }

}