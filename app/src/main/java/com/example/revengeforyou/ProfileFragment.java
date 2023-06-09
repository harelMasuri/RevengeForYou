package com.example.revengeforyou;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProfileFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    FirebaseDatabase database   = FirebaseDatabase.getInstance("https://revengeforyou-4435b-default-rtdb.firebaseio.com/");
    DatabaseReference myRef     = database.getReference("revenge/" + FirebaseAuth.getInstance().getUid());
    DatabaseReference myRefDel  = database.getReference("revengeDeletions/" + FirebaseAuth.getInstance().getUid());

    public static TextView tvDeletionCounter;
    public static TextView tvDoneCounter;


    public ProfileFragment() {

    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        tvDeletionCounter = view.findViewById(R.id.tvDeletionCounter);
        tvDoneCounter     = view.findViewById(R.id.tvDoneCounter);


        // Add Item listener (Read RevengesDeletions from Firebase)
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                int counterDone = 0;
                for(DataSnapshot revengeSnapshot : snapshot.getChildren())
                {
                    Revenge currentRevenge = revengeSnapshot.getValue(Revenge.class);
                    if (currentRevenge.getbIsDone())
                        counterDone++;
                }
                tvDoneCounter.setText(String.valueOf(counterDone));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("ProfileFragment", "myRefDel.addValueEventListener: Got onCancelled");
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
                    int counterRemoved = snapshot.getChildren().iterator().next().getValue(Integer.class);
                    tvDeletionCounter.setText(String.valueOf(counterRemoved));
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("ProfileFragment", "myRefDel.addValueEventListener: Got onCancelled");
            }
        });

        return view;
    }
}