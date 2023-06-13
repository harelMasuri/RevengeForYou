package com.example.revengeforyou;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;

public class SettingsFragment extends Fragment implements CompoundButton.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    Switch swMusicSettings;
    SeekBar sbMusicSettings;
    MediaPlayer mpRevengeSettings;
    AudioManager amMusicSettings;


    public SettingsFragment() {
    }

    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        swMusicSettings = (Switch)view.findViewById(R.id.swMusicSettings);
        swMusicSettings.setOnCheckedChangeListener(SettingsFragment.this);

        sbMusicSettings = (SeekBar)view.findViewById(R.id.sbMusicSettings);
        mpRevengeSettings = MediaPlayer.create(requireContext(), R.raw.music_for_app_revenge);
        //mpRevengeSettings.start();


        amMusicSettings = (AudioManager) requireContext().getSystemService(Context.AUDIO_SERVICE);
        int max = amMusicSettings.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        sbMusicSettings.setMax(16);
        sbMusicSettings.setProgress(8);
        amMusicSettings.setStreamVolume(AudioManager.STREAM_MUSIC, max/2, 0);
        sbMusicSettings.setOnSeekBarChangeListener(SettingsFragment.this);

        return view;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

        if(compoundButton == swMusicSettings)
        {
            if(b)
            {
                mpRevengeSettings.start();
            }
            else {
                mpRevengeSettings.pause();
            }

        }

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if(seekBar == sbMusicSettings){
            amMusicSettings.setStreamVolume(AudioManager.STREAM_MUSIC, i, 0);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}