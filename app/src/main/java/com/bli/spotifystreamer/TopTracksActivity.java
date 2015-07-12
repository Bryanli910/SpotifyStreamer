package com.bli.spotifystreamer;

//import android.app.ActionBar;

import android.app.Activity;
import android.os.AsyncTask;

import android.os.Bundle;
import android.os.Parcel;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Tracks;
import kaaes.spotify.webapi.android.models.Track;

public class TopTracksActivity extends AppCompatActivity implements TracksAsyncResponse{

    public ListView tracksListView;
    public ArrayAdapter<Track> topTracksArrayAdapter;
    public final static String TAG = "TopTracksActivity";
    public Bundle savedInstanceState;
    public String artistName;
    ArrayList<ParcelableTrack> parcelableTracks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_tracks);

        tracksListView = (ListView)findViewById(R.id.tracksList);

        Bundle artistInfo = getIntent().getExtras();

        if(savedInstanceState != null){
            ActionBar actionBar = getSupportActionBar();
            String artistName = savedInstanceState.getString("artistName");
            actionBar.setSubtitle(artistName);

            parcelableTracks = savedInstanceState.getParcelable("tracks");

            Toast toast = Toast.makeText(this, parcelableTracks.get(0).toString(), Toast.LENGTH_SHORT);
            toast.show();

            List<Track> tracksList = new ArrayList<>();

            Tracks tracks = new Tracks();
            tracks.tracks = tracksList;

            processFinish(tracks);
        }
        else if(artistInfo != null){

            ActionBar actionBar = getSupportActionBar();

            artistName = artistInfo.getString("artistName");
            actionBar.setSubtitle(artistName);

            GetTopTracksTask getTopTracksTask = new GetTopTracksTask();
            getTopTracksTask.delegate = this;
            getTopTracksTask.execute(artistInfo.getString("artistId"));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        outState.putParcelableArrayList("tracks", parcelableTracks);
        outState.putString("artistName", artistName);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void processFinish(Tracks tracks) {

        Log.d("Top Tracks Activity", "Process finish called");

        int tracksListSize = tracks.tracks.size();

        parcelableTracks = new ArrayList<>();

        for(Track track:tracks.tracks){
            String imageUrl = "";
            if(track.album.images.size() > 0){
                imageUrl = track.album.images.get(0).url;
            }
            ParcelableTrack pTrack = new ParcelableTrack(track.name,track.artists.get(0).name, track.album.name, imageUrl);
            Log.d("TEST", pTrack.toString());
            parcelableTracks.add(pTrack);
        }

        if(tracksListSize > 0){
            topTracksArrayAdapter = new TopTracksArrayAdapter(getApplicationContext(),R.id.tracksList, tracks);
            tracksListView.setAdapter(topTracksArrayAdapter);
        }
    }

    private class GetTopTracksTask extends AsyncTask<String, Void, Void>{

        private Tracks tracks;
        public TracksAsyncResponse delegate = null;

        @Override
        protected void onPostExecute(Void aVoid) {
            delegate.processFinish(tracks);
        }

        @Override
        protected Void doInBackground(String... artist) {
            MusicService musicService = new MusicService();
            Log.d("TESTESTESTESTSTEST",artist[0]);
            this.tracks = musicService.getTopTracks(artist[0]);
            return null;
        }
    }
}
