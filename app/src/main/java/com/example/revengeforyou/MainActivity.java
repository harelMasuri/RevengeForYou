package com.example.revengeforyou;

//import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener {

    private FirebaseAuth mAuth;

    Dialog dSignUp, dSignIn, dMusic;
    Button btnSignIn, btnSignUp, btnSave, btnNext;
    EditText etUserNameSignUp, etPassSignUp, etUserNameSignIn, etPassSignIn;
    Switch swMusic;
    MediaPlayer mpRevenge;
    SeekBar sbMusic;
    AudioManager amMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();


        dSignUp = new Dialog(this);
        dSignUp.setContentView(R.layout.custom_layout_signup);
        dSignUp.setCancelable(true);

        dSignIn = new Dialog(this);
        dSignIn.setContentView(R.layout.custom_layout_signin);
        dSignIn.setCancelable(true);

        dMusic = new Dialog(this);
        dMusic.setContentView(R.layout.custom_layout_music);
        dMusic.setCancelable(true);

        btnSignIn = (Button)findViewById(R.id.btnSignIn);
        btnSignUp = (Button)findViewById(R.id.btnSignUp);
        btnSave = (Button)dSignUp.findViewById(R.id.btnSave);
        btnNext = (Button)dSignIn.findViewById(R.id.btnNext);
        etUserNameSignUp = (EditText)dSignUp.findViewById(R.id.etUserNameSignUp);
        etPassSignUp = (EditText)dSignUp.findViewById(R.id.etPassSignUp);
        etUserNameSignIn = (EditText)dSignIn.findViewById(R.id.etUserNameSignIn);
        etPassSignIn = (EditText)dSignIn.findViewById(R.id.etPassSignIn);

        swMusic = (Switch)dMusic.findViewById(R.id.switchMusic);
        swMusic.setOnCheckedChangeListener(this);
        mpRevenge = MediaPlayer.create(this, R.raw.music_for_app_revenge);


        sbMusic = (SeekBar)dMusic.findViewById(R.id.seekbarMusic);
        sbMusic.setMax(16);
        sbMusic.setProgress(8);
        amMusic = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int max = amMusic.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
//        sbMusic.setMax(max/2);
        amMusic.setStreamVolume(AudioManager.STREAM_MUSIC, max/2, 0);
        sbMusic.setOnSeekBarChangeListener(this);

        btnSignIn.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnNext.setOnClickListener(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        int id = item.getItemId();
        if(id == R.id.Music1)
        {
            dMusic.show();
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View view)
    {
        if(view == btnSignUp)
        {
            dSignUp.show();
        }

        if(view == btnSave)
        {
            mAuth.createUserWithEmailAndPassword(etUserNameSignUp.getText().toString(), etPassSignUp.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(MainActivity.this, RevengeAcount.class));
                                dSignUp.dismiss();
                                Toast.makeText(MainActivity.this, "username and password saved", Toast.LENGTH_LONG).show();
                            }
                            else {
                                Toast.makeText(MainActivity.this, "Sign Up failed", Toast.LENGTH_LONG).show();
                                dSignUp.dismiss();
                            }

                        }
                    });

            etUserNameSignUp.setText("");
            etPassSignUp.setText("");
        }




        if(view == btnSignIn)
        {
            dSignIn.show();
        }

        if(view == btnNext) {

                mAuth.signInWithEmailAndPassword(etUserNameSignIn.getText().toString(), etPassSignIn.getText().toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    startActivity(new Intent(MainActivity.this, RevengeAcount.class));
                                    Toast.makeText(MainActivity.this, "your welcome!", Toast.LENGTH_LONG).show();
                                }
                                else {
                                    Toast.makeText(MainActivity.this, "Sign In failed", Toast.LENGTH_LONG).show();
                                }

                            }
                        });
            dSignIn.dismiss();
            etUserNameSignIn.setText("");
            etPassSignIn.setText("");
        }

        }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b)
    {

        if(compoundButton == swMusic)
        {
            if(b)
                mpRevenge.start();
            else
                mpRevenge.pause();
        }

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

        if(seekBar == sbMusic)
        {
            //float alpha = (float)i/100;
            amMusic.setStreamVolume(AudioManager.STREAM_MUSIC, i,0);
        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


}