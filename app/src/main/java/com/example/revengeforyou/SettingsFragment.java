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

    /*Switch swMusicSettings;
    SeekBar sbMusicSettings;
    MediaPlayer mpRevengeSettings;
    AudioManager amMusicSettings;*/

    private Switch swMusicSettings;
    private SeekBar sbMusicSettings;
    MediaPlayer mediaPlayerS;


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

        swMusicSettings = view.findViewById(R.id.swMusicSettings);
        swMusicSettings.setOnCheckedChangeListener(this);

        sbMusicSettings = view.findViewById(R.id.sbMusicSettings);
        sbMusicSettings.setOnSeekBarChangeListener(this);

        mediaPlayerS = MediaPlayer.create(getContext(), R.raw.music_for_app_revenge);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mediaPlayerS.release();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

        if (compoundButton.getId() == R.id.swMusicSettings) {
            if (b) {
                mediaPlayerS.start();
            } else {
                mediaPlayerS.pause();
            }
        }

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if (seekBar.getId() == R.id.sbMusicSettings) {
            float volume = (float) (1 - (Math.log(100 - i) / Math.log(100)));
            mediaPlayerS.setVolume(volume, volume);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

}