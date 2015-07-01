package com.bli.spotifystreamer;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Artists;
import kaaes.spotify.webapi.android.models.ArtistsPager;

public class ArtistSearchActivity extends AppCompatActivity implements AsyncResponse{

    private EditText searchArtistEditText;
    private Button searchArtistBtn;
    private ListView artistsListView;

    private String [] artists;

    public ArrayAdapter<Artist> artistsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_search);
        //searchArtistBtn = (Button)findViewById(R.id.searchArtistBtn);
        searchArtistEditText = (EditText)findViewById(R.id.searchArtistText);
        artistsListView = (ListView)findViewById(R.id.artistsListView);

        searchArtistEditText.setOnEditorActionListener(new TextView.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || event.getKeyCode() == KeyEvent.KEYCODE_ENTER){
                    beginSearch(v);
                    return true;
                }
                return false;
            }
        });
    }
    public void beginSearch(View view){
        GetArtistsTask getArtistsTask = new GetArtistsTask();
        getArtistsTask.delegate = this;
        getArtistsTask.execute(searchArtistEditText.getText().toString());
    }

    @Override
    public void processFinish(final ArtistsPager artistsPager) {

        Log.d("Artist Search", "Begin onPostExecute delegate");

        int artistListSize = artistsPager.artists.items.size();

        if(artistListSize > 0 ) {

            artistsAdapter = new ArtistArrayAdapter(getApplicationContext(), R.id.artistsListView, artistsPager);
            artistsListView.setAdapter(artistsAdapter);

            artistsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.d("ArtistList Activity","Position: " + Integer.toString(position) + " |ID: " + String.valueOf(id));

                    //Start the top tracks with an intent
                    Intent i = new Intent(getApplicationContext(), TopTracksActivity.class);
                    i.putExtra("artistId", artistsPager.artists.items.get(position).id);
                    i.putExtra("artistName", artistsPager.artists.items.get(position).name);
                    startActivity(i);

                }
            });
        }
        else{
            Toast toast = Toast.makeText(this, "Artist not found. Please try another one.",Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private class GetArtistsTask extends AsyncTask<String, Void, Void>{

        private ArtistsPager artistsPager;
        public AsyncResponse delegate = null;

        @Override
        protected Void doInBackground(String... artist){
            Log.d("Artist Search", "Begin spotify query");
            MusicService musicService = new  MusicService();

            this.artistsPager = musicService.getArtists(artist[0]);

            return null;
        }

        @Override
        protected void onPostExecute(Void v){
            Log.d("Artist Search", "Spotify query completed");
            delegate.processFinish(artistsPager);
        }
    }
}
