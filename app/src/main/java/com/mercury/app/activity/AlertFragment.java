package com.mercury.app.activity;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import com.mercury.app.R;

public class AlertFragment extends Fragment {

    ImageView alertButton;
    private SoundPool soundPool;
    private int soundID;
    boolean plays = false, loaded = false;
    float actVolume, maxVolume, volume;
    AudioManager audioManager;
    int counter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_alert, container, false);

        audioManager = (AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);
        actVolume = (float)audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        maxVolume = (float)audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volume = actVolume / maxVolume;

        //hardware buttons settings to adjust the media volume
        this.getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);

        //the counter will help recognize the stream id of the sound played now
        counter = 0;

        //load the sounds
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int i, int i1) {
                loaded = true;
            }
        });
        soundID = soundPool.load(getActivity(), R.raw.scream, 1);

        alertButton = (ImageView)rootView.findViewById(R.id.alertBtn);
        alertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playSound();
            }
        });
        return rootView;
    }

    public void playSound(){
        //check if the sound is loaded and does it play
        if(loaded && !plays){
            soundPool.play(soundID, maxVolume, maxVolume, 1, 0, 1f);
            counter = counter++;
            Toast.makeText(getActivity(), "Played sound", Toast.LENGTH_SHORT).show();
            plays = true;
        }
    }

    public void playLoop(View v){
        if(loaded && !plays){
            //sound will play forever if we put the loop parameter to -1
            soundPool.play(soundID, volume, volume, 1, -1, 1f);
            counter = counter++;
            Toast.makeText(getActivity(), "plays loop", Toast.LENGTH_SHORT).show();
            plays = true;
        }
    }

    public void pauseSound(View v){
        if(plays){
            soundPool.pause(soundID);
            soundID = soundPool.load(getActivity(), R.raw.scream, counter);
            Toast.makeText(getActivity(), "Pause sound", Toast.LENGTH_SHORT).show();
            plays = false;
        }
    }

    public void stopSound(View v){
        if(plays){
            soundPool.stop(soundID);
            soundID = soundPool.load(getActivity(), R.raw.scream, counter);
            Toast.makeText(getActivity(), "stop sound", Toast.LENGTH_SHORT).show();
            plays = false;
        }
    }
}
