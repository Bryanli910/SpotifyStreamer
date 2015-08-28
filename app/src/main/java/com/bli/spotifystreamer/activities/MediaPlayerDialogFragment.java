package com.bli.spotifystreamer.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bli.spotifystreamer.MediaPlayerManager;
import com.bli.spotifystreamer.ParcelableTrack;
import com.bli.spotifystreamer.PreparedResponse;
import com.bli.spotifystreamer.R;
import com.squareup.okhttp.internal.Util;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MediaPlayerDialogFragment extends DialogFragment{

    private final String SPOTIFY_DEFAULT_PREVIEW_DURATION = "00:30";
    public MediaPlayerManager mp;
    public TextView artistNameTitleTxt, albumNameTxt, songNameTxt, endTimeTxt, currentTimeTxt;
    public ImageView albumCoverImg;
    public List<ParcelableTrack> parcelableTrackList;
    public ParcelableTrack pTrack;
    public int currentPosition;
    public SeekBar scrubBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        this.setRetainInstance(true);

        Bundle args = getArguments();
        parcelableTrackList = args.getParcelableArrayList("track");
        currentPosition = args.getInt("currentPosition");
        pTrack = parcelableTrackList.get(currentPosition);

        View v = inflater.inflate(R.layout.fragment_media_player,container,false);
        artistNameTitleTxt = (TextView)v.findViewById(R.id.artistNameTxt);
        albumNameTxt = (TextView)v.findViewById(R.id.albumNameTxt);
        songNameTxt = (TextView)v.findViewById(R.id.songNameTxt);
        endTimeTxt = (TextView)v.findViewById(R.id.endTimeTxt);
        currentTimeTxt = (TextView)v.findViewById(R.id.currentTimeTxt);
        albumCoverImg = (ImageView)v.findViewById(R.id.albumCoverImg);
        scrubBar = (SeekBar)v.findViewById(R.id.scrubBar);

        setTrackInformation();

        final MediaPlayerManager mp = MediaPlayerManager.getInstance(this);
        if(savedInstanceState != null){
            mp.playMusic();
        }
        else{
            mp.nextSong(pTrack.previewUrl);
        }

        final ImageButton playBtn = (ImageButton)v.findViewById(R.id.playCtrlBtn);
        playBtn.setOnClickListener(new View.OnClickListener() {
            private MediaPlayerManager mp;
            private boolean paused = false;

            @Override
            public void onClick(View v) {
                if(mp.isPlaying()){
                    mp.pauseMusic();
                    playBtn.setImageResource(android.R.drawable.ic_media_play);
                    paused = true;
                }
                else {
                    mp.playMusic();
                    playBtn.setImageResource(android.R.drawable.ic_media_pause);
                    paused = false;
                }
            }

            private View.OnClickListener init(MediaPlayerManager mp) {
                this.mp = mp;
                return this;
            }
        }.init(mp));

        ImageButton nextBtn = (ImageButton)v.findViewById(R.id.nextCtrlBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            private MediaPlayerManager mp;
            @Override
            public void onClick(View v) {
                //TO DO: Find out a way to implement the next song when passing the value to the next song
                if(currentPosition+1 >= parcelableTrackList.size()){
                    //At end of list go back to the beginning
                    setCurrentPosition(0);
                    setTrackInformation();
                    mp.nextSong(pTrack.previewUrl);
                }
                else{
                    setCurrentPosition(currentPosition+1);
                    setTrackInformation();
                    mp.nextSong(pTrack.previewUrl);
                }
            }
            private View.OnClickListener init(MediaPlayerManager mp){
                this.mp = mp;
                return this;
            }
        }.init(mp));

        ImageButton prevBtn = (ImageButton)v.findViewById(R.id.previousCtrlBtn);
        prevBtn.setOnClickListener(new View.OnClickListener() {
            private MediaPlayerManager mp;
            @Override
            public void onClick(View v) {
                //TO DO: Find out a way to implement the next song when passing the value to the next song
                if(currentPosition-1 >= 0){
                    //At end of list go back to the beginning
                    setCurrentPosition(currentPosition-1);
                    setTrackInformation();
                    mp.nextSong(pTrack.previewUrl);
                }
                else{
                    setCurrentPosition(parcelableTrackList.size() - 1);
                    setTrackInformation();
                    mp.nextSong(pTrack.previewUrl);
                }
            }
            private View.OnClickListener init(MediaPlayerManager mp){
                this.mp = mp;
                return this;
            }
        }.init(mp));

        return v;
    }
    private void nextSong(){
        mp.nextSong(pTrack.previewUrl);
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    private void setCurrentPosition(int nextPosition){
        this.currentPosition = nextPosition;
        pTrack = parcelableTrackList.get(nextPosition);
    }

    private void setTrackInformation(){
        artistNameTitleTxt.setText(pTrack.artistName);
        albumNameTxt.setText(pTrack.albumName);
        songNameTxt.setText(pTrack.trackName);
        Picasso.with(getActivity()).load(pTrack.thumbnailUrl).into(albumCoverImg);
    }

    public void PreparedComplete() {
        final int currentPosition = mp.sMediaPlayer.getCurrentPosition();
        final int duration = mp.sMediaPlayer.getDuration();
        endTimeTxt.setText(Utility.convertToMinutes(duration));
        currentTimeTxt.setText(Utility.convertToMinutes(currentPosition));
        Log.d("PREPARED COMPLETE", "GOOD");
        scrubBar.setProgress(currentPosition);
        scrubBar.setMax(duration / 1000);
        scrubBar.setProgress(26);

        final Handler handler = new Handler();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mp.sMediaPlayer.isPlaying()) {
                    currentTimeTxt.setText(Utility.convertToMinutes(mp.sMediaPlayer.getCurrentPosition()));
                    scrubBar.setProgress(mp.sMediaPlayer.getCurrentPosition() / 1000);
                }
                handler.postDelayed(this, 1000);
            }
        });

        scrubBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("Progress Change Scrub:", String.valueOf(progress));
                if (mp.sMediaPlayer.isPlaying() && fromUser) {
                    mp.sMediaPlayer.seekTo(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance())
            getDialog().setDismissMessage(null);
        super.onDestroyView();
    }

}
