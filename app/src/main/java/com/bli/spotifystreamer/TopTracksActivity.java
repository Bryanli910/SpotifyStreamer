package com.bli.spotifystreamer;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Tracks;
import kaaes.spotify.webapi.android.models.Track;

public class TopTracksActivity extends AppCompatActivity implements TracksAsyncResponse{

    public ListView tracksListView;
    public ArrayAdapter<Track> topTracksArrayAdapter;
    public final static String TAG = "TopTracksActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_tracks);

        tracksListView = (ListView)findViewById(R.id.tracksList);

        Bundle artistInfo = getIntent().getExtras();

        if(artistInfo != null){
            String artistName = artistInfo.getString("artistName");
            CharSequence currentTitle = getTitle();
            setTitle(artistName + " - " + currentTitle);

            GetTopTracksTask getTopTracksTask = new GetTopTracksTask();
            getTopTracksTask.delegate = this;
            getTopTracksTask.execute(artistInfo.getString("artistId"));
        }
    }

    @Override
    public void processFinish(Tracks tracks) {

        Log.d("Top Tracks Activity", "Process finish called");

        int tracksListSize = tracks.tracks.size();

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
