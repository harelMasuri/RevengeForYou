package com.example.revengeforyou;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static java.lang.Thread.sleep;


public class HomeFragment extends Fragment implements View.OnClickListener {

    FloatingActionButton btnFloatingActionButton;
    Dialog dCreateRevenge;
    Button btnCreateRevenge;
    EditText etNameOfRevenge, etWhoWillTakeRevenge, etWhatTheRevenge, etReasonForRevenge;
    TextView TVNameOfRevenge, TVWhoWillTakeRevenge, TVWhatTheRevenge, TVReasonForRevenge;
    // CheckBox checkBox1;
    Revenge revenge;
    ArrayList<Revenge> revenges;
    RevengeAdapter revengeAdapter;

    FirebaseDatabase database = FirebaseDatabase.getInstance("https://revengeforyou-4435b-default-rtdb.firebaseio.com/");
    DatabaseReference myRef = database.getReference("revenge/" + FirebaseAuth.getInstance().getUid());


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

        TVNameOfRevenge = view.findViewById(R.id.TVNameOfRevenge);
        TVWhoWillTakeRevenge = view.findViewById(R.id.TVWhoWillTakeRevenge);
        TVWhatTheRevenge = view.findViewById(R.id.TVWhatTheRevenge);
        TVReasonForRevenge = view.findViewById(R.id.TVReasonForRevenge);
        // checkBox1 = view.findViewById(R.id.checkBox1);


        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_revenges);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        revenges = new ArrayList<>();
        revengeAdapter = new RevengeAdapter(revenges);
        recyclerView.setAdapter(revengeAdapter);

        // Read Revenges from Firebase
//        database = FirebaseDatabase.getInstance("https://revengeforyou-4435b-default-rtdb.firebaseio.com/");
//        myRef = database.getReference("revenge/" + FirebaseAuth.getInstance().getUid());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                revenges.clear();
                for(DataSnapshot revengeSnapshot : snapshot.getChildren())
                {
                    Revenge currentRevenge = revengeSnapshot.getValue(Revenge.class);
                    Log.d("HomeFragment", "*************** READ: " + revengeSnapshot.getKey());
                    revenges.add(currentRevenge);
                }
                revengeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

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

            revenge = new Revenge(etNameOfRevenge.getText().toString(), etWhoWillTakeRevenge.getText().toString(), etWhatTheRevenge.getText().toString(), etReasonForRevenge.getText().toString());
            revenges.add(revenge);
            revengeAdapter = new RevengeAdapter(revenges);
            revengeAdapter.notifyDataSetChanged();

            if (!revenge.getEtNameOfRevenge().isEmpty() && !revenge.getEtWhoWillTakeRevenge().isEmpty() && !revenge.getEtWhatTheRevenge().isEmpty() && !revenge.getEtReasonForRevenge().isEmpty())
            {
//                FirebaseDatabase database = FirebaseDatabase.getInstance("https://revengeforyou-4435b-default-rtdb.firebaseio.com/");
//                DatabaseReference myRef = database.getReference("revenge/" + FirebaseAuth.getInstance().getUid());
                String key = myRef.push().getKey();
                myRef.child(key).setValue(revenge);
                dCreateRevenge.dismiss();

                etNameOfRevenge.setText("");
                etWhoWillTakeRevenge.setText("");
                etWhatTheRevenge.setText("");
                etReasonForRevenge.setText("");

            }
            else{
                Toast.makeText(getActivity(), "Please fill in all fields", Toast.LENGTH_LONG).show();
            }

        }
    }
}