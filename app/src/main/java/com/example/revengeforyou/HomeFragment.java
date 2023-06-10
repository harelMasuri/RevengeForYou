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


public class HomeFragment extends Fragment implements View.OnClickListener {

    FloatingActionButton btnFloatingActionButton;
    Dialog dCreateRevenge;
    Button btnCreateRevenge;
    EditText etNameOfRevenge, etWhoWillTakeRevenge, etWhatTheRevenge, etReasonForRevenge;


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

        btnCreateRevenge = (Button) dCreateRevenge.findViewById(R.id.btnCreateRevenge);
        btnCreateRevenge.setOnClickListener(this);

        etNameOfRevenge = (EditText) dCreateRevenge.findViewById(R.id.etNameOfRevenge);
        etWhoWillTakeRevenge = (EditText) dCreateRevenge.findViewById(R.id.etWhoWillTakeRevenge);
        etWhatTheRevenge = (EditText) dCreateRevenge.findViewById(R.id.etWhatTheRevenge);
        etReasonForRevenge = (EditText) dCreateRevenge.findViewById(R.id.etReasonForRevenge);


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
            DatabaseReference myRef = database.getReference("revenge");
            Revenge revenge = new Revenge(etNameOfRevenge.getText().toString(), etWhoWillTakeRevenge.getText().toString(), etWhatTheRevenge.getText().toString(), etReasonForRevenge.getText().toString());
            myRef.setValue(revenge);
            dCreateRevenge.dismiss();

            /*if (!revenge.getEtNameOfRevenge().isEmpty() && !revenge.getEtWhoWillTakeRevenge().isEmpty() && !revenge.getEtWhatTheRevenge().isEmpty() && !revenge.getEtReasonForRevenge().isEmpty()) {

            }*/
        }



    }



}