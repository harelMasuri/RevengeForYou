package com.example.revengeforyou;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.Switch;

import java.util.Calendar;

public class SettingsFragment extends Fragment implements CompoundButton.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;


    private Switch swMusicSettings;
    private SeekBar sbMusicSettings;
    private MediaPlayer mediaPlayerS;
    private AudioManager audioManagerS;



    /////////////////////////////////
    private static final String CHANNEL_ID = "notification_channel";
    private static final int NOTIFICATION_ID = 1;
    private AlarmManager alarmManager;
    private PendingIntent alarmPendingIntent;
    /////////////////////////////////



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

        audioManagerS       = (AudioManager) requireActivity().getSystemService(Context.AUDIO_SERVICE);
        int currentVolume   = audioManagerS.getStreamVolume(AudioManager.STREAM_MUSIC);
        int maxVolume       = audioManagerS.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int progress        = (int) (currentVolume / (float) maxVolume * 100);
        sbMusicSettings.setProgress(progress);


        // Create a notification channel for Android 8.0 and above
        createNotificationChannel();

        // Get references to UI elements
        Button startAlarmButton = view.findViewById(R.id.btnAlarmManager);

        // Set click listener for the button
        startAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFrequencyPickerDialog();
            }
        });


        return view;
    }


    private void createNotificationChannel() {
        // Create the notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            Context context             = requireContext().getApplicationContext();
            CharSequence channelName    = "Revenge For You";
            String channelDescription   = "do not forget to revenge";
            int importance              = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);
            channel.setDescription(channelDescription);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    private void showFrequencyPickerDialog() {
        Context context = requireContext().getApplicationContext();
        final NumberPicker numberPicker = new NumberPicker(context);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(24);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Select Frequency (in hours)");
        builder.setView(numberPicker);
        builder.setPositiveButton("Start", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int frequencyInHours = numberPicker.getValue();
                scheduleAlarm(frequencyInHours);
            }
        });
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void scheduleAlarm(int frequencyInHours) {
        Context context = requireContext().getApplicationContext();

        // Get the AlarmManager instance
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Create an explicit intent for the alarm receiver
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        alarmIntent.setAction("ALARM_ACTION");

        // Create a PendingIntent to be triggered when the alarm fires
        alarmPendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE);

        // Calculate the time to trigger the alarm
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.HOUR_OF_DAY, frequencyInHours);

        // Set the repeating alarm using the AlarmManager
        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                frequencyInHours * 60 * 60 * 1000, // Convert frequency to milliseconds
                alarmPendingIntent
        );
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

            float volume        = (float) (1 - (Math.log(100 - i) / Math.log(100)));
            mediaPlayerS.setVolume(volume, volume);
            int maxVolume       = audioManagerS.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            int currentVolume   = (int) (i / 100f * maxVolume);

            audioManagerS.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
        }
    }


    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

}