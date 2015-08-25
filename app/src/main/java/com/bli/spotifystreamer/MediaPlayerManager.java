package com.bli.spotifystreamer;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import com.bli.spotifystreamer.activities.MediaPlayerDialogFragment;

import java.io.IOException;

public class MediaPlayerManager {
    public static MediaPlayer sMediaPlayer;
    private static MediaPlayerManager sMediaPlayerManager;
    private String previewUrl;
    private MediaPlayerManager(){}

    public PreparedResponse preparedComplete = null;
    private MediaPlayerDialogFragment mpFragment;

    public static MediaPlayerManager getInstance(MediaPlayerDialogFragment mpFragment){
        if(sMediaPlayerManager == null){
            sMediaPlayerManager = new MediaPlayerManager();
        }
        sMediaPlayerManager.mpFragment = mpFragment;
        return sMediaPlayerManager;
    }

    private void newMediaPlayer(){
        sMediaPlayer = new MediaPlayer();
        sMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

    }

    public void playMusic(){
        sMediaPlayer.start();
    }

    public void pauseMusic(){
        sMediaPlayer.pause();
    }

    public void stopMusic(){
        sMediaPlayer.stop();
    }

    public void nextSong(String url){
        finish();
        try {
            this.previewUrl = url;
            sMediaPlayer.setDataSource(url);
            sMediaPlayer.prepare();
            sMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                public void onPrepared(MediaPlayer mp) {
                    //Log.d("PREPARED FROM MPM: ", String.valueOf(sMediaPlayer.getDuration()));
                    mpFragment.PreparedComplete();
                }
            });
            this.playMusic();

        } catch (IOException e) {

        }
    }

    private void finish() {
        if (sMediaPlayer != null) sMediaPlayer.release();
        newMediaPlayer();
    }
    public boolean isPlaying(){
        return sMediaPlayer.isPlaying();
    }
    public int getPosition(){
        if(sMediaPlayer != null) {
            return sMediaPlayer.getCurrentPosition();
        }
        else{
            return 0;
        }
    }
    public int getDuration(){
        if(sMediaPlayer != null) {
            return sMediaPlayer.getDuration();
        }
        else{
            return 0;
        }
    }

    public void seek(int position){
        sMediaPlayer.seekTo(position);
    }
}
