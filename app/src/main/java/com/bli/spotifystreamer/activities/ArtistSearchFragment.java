package com.bli.spotifystreamer.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bli.spotifystreamer.ArtistArrayAdapter;
import com.bli.spotifystreamer.AsyncResponse;
import com.bli.spotifystreamer.MusicService;
import com.bli.spotifystreamer.ParcelableArtist;
import com.bli.spotifystreamer.ParcelableArtistArrayAdapter;
import com.bli.spotifystreamer.R;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import retrofit.RetrofitError;

public class ArtistSearchFragment extends Fragment implements AsyncResponse{

    private Callbacks callbacks;

    public interface Callbacks{
        void onArtistSelected(String artistId, String artistName);
    }

    private static final String TAG = "Artist Search Fragment";
    private static final String ARG_ARTIST_ID = "artistID";
    private static final String ARG_ARTIST_NAME = "artistName";
    private static final String KEY_PARCELABLE_ARTIST_LIST = "artists";
    private EditText searchArtistEditText;
    private ListView artistsListView;

    private ArrayList<ParcelableArtist> artists;
    public ArtistsPager artistsPager;
    public ArrayAdapter<Artist> artistsAdapter;
    public ParcelableArtistArrayAdapter parcelableArtistsAdapter;
    public Toast toast;

    private Activity hostActivity;

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        callbacks = (Callbacks)activity;
    }

    @Override
    public void onDetach(){
        super.onDetach();
        callbacks = null;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hostActivity = getActivity();
        hostActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState){

        //Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.activity_artist_search, container, false);

        searchArtistEditText = (EditText)v.findViewById(R.id.searchArtistText);
        artistsListView = (ListView)v.findViewById(R.id.artistsListView);



        searchArtistEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                    if (Utility.isNetworkAvailable(getActivity())) {
                        beginSearch(v);
                    } else {
                        Utility.callToast(hostActivity.getApplicationContext(), toast, "Please check to make sure device is connected to the internet.");
                    }
                    return true;
                }
                return false;
            }
        });

        if(savedInstanceState != null){
            artists = savedInstanceState.getParcelableArrayList(KEY_PARCELABLE_ARTIST_LIST);
            if(artists != null) {
                Log.d("ARTIST SEARCH ACTIVITY", artists.toString());

                parcelableArtistsAdapter = new ParcelableArtistArrayAdapter(hostActivity.getApplicationContext(), R.id.artistsListView, artists);
                artistsListView.setAdapter(parcelableArtistsAdapter);

                artistsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.d("ArtistList Activity", "Position: " + Integer.toString(position) + " |ID: " + String.valueOf(id));
                        if(Utility.isNetworkAvailable(getActivity())) {
                            callbacks.onArtistSelected(artists.get(position).artistId, artists.get(position).artistName);
                        }
                        else{
                            Utility.callToast(hostActivity.getApplicationContext(), toast, "Please check to make sure device is connected to the internet.");
                        }
                    }
                });

                artistsListView.setSelectionFromTop(savedInstanceState.getInt("listViewPosition"), savedInstanceState.getInt("listViewPositioniOffset"));
            }
        }

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //if(artistsListView.getAdapter().getCount() > 0) {
        outState.putParcelableArrayList(KEY_PARCELABLE_ARTIST_LIST, artists);

        int position = artistsListView.getFirstVisiblePosition();
        outState.putInt("listViewPosition", position);

        View v = artistsListView.getChildAt(0);
        int offset = (v == null) ? 0 : (v.getTop() - artistsListView.getPaddingTop());
        outState.putInt("listViewPositionOffset", offset);
        //}
        super.onSaveInstanceState(outState);
    }

    public void beginSearch(View view){
        String searchText = searchArtistEditText.getText().toString();
        if(searchText == "" || searchText.isEmpty() || searchText.equals("")){
            if(toast != null){
                toast.cancel();
            }
            toast = Toast.makeText(hostActivity, "Please enter an artist",Toast.LENGTH_SHORT);
            toast.show();
        }
        else {
            GetArtistsTask getArtistsTask = new GetArtistsTask();
            getArtistsTask.delegate = this;
            getArtistsTask.execute(searchText);

            // Remove the focus from the edit text and hide the keyboard
            InputMethodManager in = (InputMethodManager) hostActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            in.hideSoftInputFromWindow(searchArtistEditText.getWindowToken(), 0);
            searchArtistEditText.clearFocus();
            artistsListView.requestFocus();
        }

    }

    @Override
    public void processFinish(final ArtistsPager artistsPager) {

        Log.d("Artist Search Fragment", "Begin onPostExecute delegate");
        artists = new ArrayList<>();

        int artistListSize = artistsPager.artists.items.size();

        if(artistListSize > 0 ) {

            artistsAdapter = new ArtistArrayAdapter(hostActivity.getApplicationContext(), R.id.artistsListView, artistsPager);
            artistsListView.setAdapter(artistsAdapter);

            artistsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.d("ArtistList Activity", "Position: " + Integer.toString(position) + " |ID: " + String.valueOf(id));

                    //Start the top tracks with an intent
                    if(Utility.isNetworkAvailable(getActivity())) {
                        callbacks.onArtistSelected(artists.get(position).artistId, artists.get(position).artistName);
                    }
                    else{
                        Utility.callToast(hostActivity.getApplicationContext(), toast, "Please check to make sure device is connected to the internet.");
                    }
                }
            });
        }
        else{
            Utility.callToast(hostActivity.getApplicationContext(), toast, "Please check to make sure device is connected to the internet.");
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

    private class GetArtistsTask extends AsyncTask<String, Void, Void> {

        private ArtistsPager artistsPager;
        public AsyncResponse delegate = null;

        @Override
        protected Void doInBackground(String... artist){
            Log.d("Artist Search", "Begin spotify query");

            try{
                MusicService musicService = new  MusicService();
                this.artistsPager = musicService.getArtists(artist[0]);
            }
            catch(RetrofitError e){
                Log.d(TAG, e.getBody().toString());
                Utility.callToast(hostActivity.getApplicationContext(), toast, "Please check to make sure device is connected to the internet.");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v){
            Log.d("Artist Search", "Spotify query completed");
            delegate.processFinish(this.artistsPager);
        }
    }
}
