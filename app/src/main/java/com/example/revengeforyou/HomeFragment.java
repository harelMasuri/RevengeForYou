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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    FloatingActionButton btnFloatingActionButton;
    Dialog dCreateRevenge;
    Button btnCreateRevenge;
    EditText etNameOfRevenge, etWhoWillTakeRevenge, etWhatTheRevenge, etReasonForRevenge;
    TextView TVNameOfRevenge, TVWhoWillTakeRevenge, TVWhatTheRevenge, TVReasonForRevenge;
    // CheckBox checkBox1;
    Revenge revenge;
    ArrayList<Revenge> revenges;
    RevengeAdapter revengeAdapter;
    int counterRemoved = 0;
    String counterKey = "";

    FirebaseDatabase database = FirebaseDatabase.getInstance("https://revengeforyou-4435b-default-rtdb.firebaseio.com/");
    DatabaseReference myRef = database.getReference("revenge/" + FirebaseAuth.getInstance().getUid());
    DatabaseReference myRefDel = database.getReference("revengeDeletions/" + FirebaseAuth.getInstance().getUid());

    public HomeFragment() {

    }

    // Create HomeFragment object
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

        etNameOfRevenge         = (EditText) dCreateRevenge.findViewById(R.id.etNameOfRevenge);
        etWhoWillTakeRevenge    = (EditText) dCreateRevenge.findViewById(R.id.etWhoWillTakeRevenge);
        etWhatTheRevenge        = (EditText) dCreateRevenge.findViewById(R.id.etWhatTheRevenge);
        etReasonForRevenge      = (EditText) dCreateRevenge.findViewById(R.id.etReasonForRevenge);

        TVNameOfRevenge         = view.findViewById(R.id.TVNameOfRevenge);
        TVWhoWillTakeRevenge    = view.findViewById(R.id.TVWhoWillTakeRevenge);
        TVWhatTheRevenge        = view.findViewById(R.id.TVWhatTheRevenge);
        TVReasonForRevenge      = view.findViewById(R.id.TVReasonForRevenge);
        // checkBox1 = view.findViewById(R.id.checkBox1);


        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_revenges);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        revenges        = new ArrayList<>();
        revengeAdapter  = new RevengeAdapter(revenges);
        recyclerView.setAdapter(revengeAdapter);

        // Delete Item listener
        revengeAdapter.setOnRevengeClickListener(new RevengeAdapter.OnRevengeClickListener() {
            @Override
            public void onRevengeClick(int position) {

                // Remove revenge from List
                Revenge r   = revenges.get(position);
                String key  = r.getRevengeId();
                revenges.remove(position);
                revengeAdapter.notifyItemRemoved(position);

                // delete from Firebase
                myRef.child(key).removeValue();

                // update deletion counter
                counterRemoved++;

                // update deletion counter in Firebase
                myRefDel.child(counterKey).setValue(counterRemoved);

            }
        });

        // Add Item listener (Read again Revenges from Firebase)
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                revenges.clear();
                for(DataSnapshot revengeSnapshot : snapshot.getChildren())
                {
                    String key = revengeSnapshot.getKey();
                    Revenge currentRevenge = revengeSnapshot.getValue(Revenge.class);
                    currentRevenge.setRevengeId(key);
                    revenges.add(currentRevenge);
                }
                revengeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("HomeFragment", "myRef.addValueEventListener: Got onCancelled");
            }
        });

        // Add Item listener (Read RevengesDeletions from Firebase)
        myRefDel.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                DataSnapshot deletionSnapshot = snapshot.getChildren().iterator().next();
                counterKey = deletionSnapshot.getKey();
                counterRemoved = deletionSnapshot.getValue(Integer.class);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("HomeFragment", "myRefDel.addValueEventListener: Got onCancelled");
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

        // Create new Revenge
        if(view == btnCreateRevenge)
        {
            revenge = new Revenge(etNameOfRevenge.getText().toString(), etWhoWillTakeRevenge.getText().toString(), etWhatTheRevenge.getText().toString(), etReasonForRevenge.getText().toString());
            revenges.add(revenge);
            revengeAdapter = new RevengeAdapter(revenges);
            revengeAdapter.notifyDataSetChanged();

            if (!revenge.getEtNameOfRevenge().isEmpty() && !revenge.getEtWhoWillTakeRevenge().isEmpty() && !revenge.getEtWhatTheRevenge().isEmpty() && !revenge.getEtReasonForRevenge().isEmpty())
            {
                // Add Revenge to Firebase
                String key = myRef.push().getKey();
                revenge.setRevengeId(key);
                myRef.child(key).setValue(revenge);

                // Close create window
                dCreateRevenge.dismiss();

                // Clean controls
                etNameOfRevenge.setText("");
                etWhoWillTakeRevenge.setText("");
                etWhatTheRevenge.setText("");
                etReasonForRevenge.setText("");
            }
            else {
                Toast.makeText(getActivity(), "Please fill in all fields", Toast.LENGTH_LONG).show();
            }
        }
    }

    void deleteItem(String itemKey)
    {
        Query myQuery = myRef.child(itemKey);

        myQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot revengeSnapshot: dataSnapshot.getChildren()) {
                    // Log.d("HomeFragment", "*************** DELETE: " + revengeSnapshot.getKey());
                    revengeSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("HomeFragment", "deleteItem: onCancelled", databaseError.toException());
            }
        });
    }

}