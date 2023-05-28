package com.example.revengeforyou;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;


public class HomeFragment extends Fragment implements View.OnClickListener {

    FloatingActionButton btnFloatingActionButton;
    Dialog dCreateRevenge;
    Button btnCreateRevenge;
    EditText NameOfRevenge, WhoWillTakeRevenge, WhatTheRevenge, ReasonForRevenge;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public HomeFragment() {

    }


    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        btnFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.btnFloatingActionButton);
        btnFloatingActionButton.setOnClickListener(this);

        dCreateRevenge = new Dialog(getActivity());
        dCreateRevenge.setContentView(R.layout.custom_layout_create_revenge);
        dCreateRevenge.setCancelable(true);

        btnCreateRevenge = (Button) view.findViewById(R.id.btnCreateRevenge);
        btnCreateRevenge.setOnClickListener(this);

        NameOfRevenge = (EditText) view.findViewById(R.id.NameOfRevenge);
        WhoWillTakeRevenge = (EditText) view.findViewById(R.id.WhoWillTakeRevenge);
        WhatTheRevenge = (EditText) view.findViewById(R.id.WhatTheRevenge);
        ReasonForRevenge = (EditText) view.findViewById(R.id.ReasonForRevenge);

        return view;
    }


    @Override
    public void onClick(View view) {

        if(view == btnFloatingActionButton)
        {
            dCreateRevenge.show();

        }

        if(view == btnCreateRevenge)
        {

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("Revenge");

            Revenge revenge = new Revenge(NameOfRevenge.getText().toString(), WhoWillTakeRevenge.getText().toString(),
                    WhatTheRevenge.getText().toString(), ReasonForRevenge.getText().toString());

            myRef.setValue(revenge);


        }



    }



}