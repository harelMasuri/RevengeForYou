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
    CheckBox cbIsDone;
    Revenge revenge;
    ArrayList<Revenge> revenges;
    RevengeAdapter revengeAdapter;
    int counterRemoved = 0;
    String counterKey = "NumOfDeletions";

    FirebaseDatabase database   = FirebaseDatabase.getInstance("https://revengeforyou-4435b-default-rtdb.firebaseio.com/");
    DatabaseReference myRef     = database.getReference("revenge/" + FirebaseAuth.getInstance().getUid());
    DatabaseReference myRefDel  = database.getReference("revengeDeletions/" + FirebaseAuth.getInstance().getUid());

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
        cbIsDone                = view.findViewById(R.id.cbIsDone);


        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_revenges);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        revenges        = new ArrayList<>();
        revengeAdapter  = new RevengeAdapter(revenges);
        recyclerView.setAdapter(revengeAdapter);

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

        // revengeAdapter Item listener (delete + isDone)
        revengeAdapter.setOnRevengeClickListener(new RevengeAdapter.OnRevengeClickListener() {
            @Override
            public void onRevengeClick(int position, int buttonNum) {

                if (buttonNum == 1)  // isDone
                {
                    // update Revenge to Firebase
                    Revenge r = revenges.get(position);
                    String key = r.getRevengeId();
                    r.setbIsDone(!r.getbIsDone());  // Toggle isDone state
                    myRef.child(key).setValue(r);
                }
                else {  // delete

                    // Delete Revenge
                    // 1. remove revenge from 'revenges ArrayList'
                    // 2. remove from revengeAdapter
                    // 3. delete revenge from Firebase
                    // 4. update deletion counter in Firebase

                    // Remove revenge from List
                    Revenge r = revenges.get(position);
                    String key = r.getRevengeId();
                    revenges.remove(position); // 1. remove revenge from 'revenges ArrayList'

                    // 2. remove from revengeAdapter
                    revengeAdapter.notifyItemRemoved(position);

                    // 3. delete revenge from Firebase
                    myRef.child(key).removeValue();

                    // update deletion counter
                    counterRemoved++;

                    // 4. update deletion counter in Firebase
                    myRefDel.child(counterKey).setValue(counterRemoved);
                }
            }
        });

        // Add Item listener (Read RevengesDeletions from Firebase)
        myRefDel.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                int childrenAmount = 0;
                for(DataSnapshot revengeSnapshot : snapshot.getChildren()) {
                    childrenAmount++;
                }
                if (childrenAmount > 0) {
                    DataSnapshot deletionSnapshot = snapshot.getChildren().iterator().next();
                    counterRemoved = deletionSnapshot.getValue(Integer.class);
                }
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
        // 1. create revenge and insert to 'revenges ArrayList'
        // 2. insert revenge to Firebase
        // 3. add to revengeAdapter
        if(view == btnCreateRevenge)
        {
            // 1. create revenge and insert to 'revenges ArrayList'
            revenge = new Revenge(etNameOfRevenge.getText().toString(), etWhoWillTakeRevenge.getText().toString(), etWhatTheRevenge.getText().toString(), etReasonForRevenge.getText().toString());
            revenges.add(revenge);

            if (!revenge.getEtNameOfRevenge().isEmpty() && !revenge.getEtWhoWillTakeRevenge().isEmpty() && !revenge.getEtWhatTheRevenge().isEmpty() && !revenge.getEtReasonForRevenge().isEmpty())
            {
                // Add Revenge to Firebase
                String key = myRef.push().getKey();
                revenge.setRevengeId(key);
                revenge.setbIsDone(false);
                myRef.child(key).setValue(revenge);     // 2. insert revenge to Firebase

                revengeAdapter.notifyDataSetChanged();  // 3. add to revengeAdapter

                // Close create window
                dCreateRevenge.dismiss();

                // Clean views
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


}