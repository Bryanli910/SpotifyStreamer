package com.bli.spotifystreamer;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcel;
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

import java.util.ArrayList;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Artists;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Pager;

public class ArtistSearchActivity extends AppCompatActivity implements AsyncResponse{

    private static final String KEY_PARCELABLE_ARTIST_LIST = "artists";
    private EditText searchArtistEditText;
    private ListView artistsListView;

    private ArrayList<ParcelableArtist> artists;
    public ArtistsPager artistsPager;
    public ArrayAdapter<Artist> artistsAdapter;
    public ParcelableArtistArrayAdapter parcelableArtistsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_search);

        searchArtistEditText = (EditText)findViewById(R.id.searchArtistText);
        artistsListView = (ListView)findViewById(R.id.artistsListView);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

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

        if(savedInstanceState != null){
            artists = savedInstanceState.getParcelableArrayList(KEY_PARCELABLE_ARTIST_LIST);
            Log.d("ARTIST SEARCH ACTIVITY", artists.toString());

            parcelableArtistsAdapter = new ParcelableArtistArrayAdapter(getApplicationContext(), R.id.artistsListView, artists);
            artistsListView.setAdapter(parcelableArtistsAdapter);

            artistsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.d("ArtistList Activity","Position: " + Integer.toString(position) + " |ID: " + String.valueOf(id));

                    //Start the top tracks with an intent
                    Intent i = new Intent(getApplicationContext(), TopTracksActivity.class);
                    i.putExtra("artistId", artists.get(position).artistId);
                    i.putExtra("artistName", artists.get(position).artistName);
                    startActivity(i);

                }
            });

            artistsListView.setSelectionFromTop(savedInstanceState.getInt("listViewPosition"), savedInstanceState.getInt("listViewPositioniOffset"));

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if(artists == null) {
            outState.putParcelableArrayList(KEY_PARCELABLE_ARTIST_LIST, artists);

            int position = artistsListView.getFirstVisiblePosition();
            outState.putInt("listViewPosition", position);

            View v = artistsListView.getChildAt(0);
            int offset = (v == null) ? 0 : (v.getTop() - artistsListView.getPaddingTop());
            outState.putInt("listViewPositionOffset", offset);
        }
        super.onSaveInstanceState(outState);
    }

    public void beginSearch(View view){
        String searchText = searchArtistEditText.getText().toString();
        if(searchText == "" || searchText.isEmpty() || searchText.equals("")){
            Toast toast = Toast.makeText(this, "Please enter an artist",Toast.LENGTH_SHORT);
            toast.show();
        }
        else {
            GetArtistsTask getArtistsTask = new GetArtistsTask();
            getArtistsTask.delegate = this;
            getArtistsTask.execute(searchText);

            // Remove the focus from the edit text and hide the keyboard
            InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            in.hideSoftInputFromWindow(searchArtistEditText.getWindowToken(), 0);
            searchArtistEditText.clearFocus();
            artistsListView.requestFocus();
        }

    }

    @Override
    public void processFinish(final ArtistsPager artistsPager) {

        Log.d("Artist Search", "Begin onPostExecute delegate");
        artists = new ArrayList<>();

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

        //Save the artists into the parcelableArtist list
        for(Artist artist:artistsPager.artists.items){
            String thumbnailUrl = "";
            if(artist.images.size() > 0 ){
                thumbnailUrl = artist.images.get(0).url;
            }
            ParcelableArtist pArtist = new ParcelableArtist(artist.name,thumbnailUrl, artist.id);
            artists.add(pArtist);
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
            delegate.processFinish(this.artistsPager);
        }
    }
}
