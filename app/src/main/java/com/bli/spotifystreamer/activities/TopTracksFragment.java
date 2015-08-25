package com.bli.spotifystreamer.activities;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.bli.spotifystreamer.MusicService;
import com.bli.spotifystreamer.ParcelableTrack;
import com.bli.spotifystreamer.ParcelableTracksArrayAdapter;
import com.bli.spotifystreamer.R;
import com.bli.spotifystreamer.TopTracksArrayAdapter;
import com.bli.spotifystreamer.TracksAsyncResponse;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;

public class TopTracksFragment extends Fragment implements TracksAsyncResponse{

    private static final String ARG_ARTIST_ID = "artistId";
    private static final String ARG_ARTIST_NAME = "artistName";

    public ListView tracksListView;
    public ArrayAdapter<Track> topTracksArrayAdapter;
    public final static String TAG = "TopTracksActivity";
    public Bundle savedInstanceState;
    public String artistName;
    ArrayList<ParcelableTrack> parcelableTracks;
    public ParcelableTracksArrayAdapter parcelableTracksArrayAdapter;
    public Toast toast;

    private Activity hostActivity;

    public static TopTracksFragment newInstance(String artistId, String artistName){
        Bundle args = new Bundle();
        args.putString(ARG_ARTIST_ID, artistId);
        args.putString(ARG_ARTIST_NAME, artistName);

        TopTracksFragment fragment = new TopTracksFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hostActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_top_tracks, container, false);

        tracksListView = (ListView)v.findViewById(R.id.tracksList);

        Bundle artistInfo = getArguments();

        if(savedInstanceState != null){
            artistName = savedInstanceState.getString("artistName");
            ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(artistName);

            parcelableTracks = savedInstanceState.getParcelableArrayList("tracks");

            Log.d("Parcelable Tracks: ", parcelableTracks.toString());

            if(parcelableTracks.size() > 0 ){
                parcelableTracksArrayAdapter = new ParcelableTracksArrayAdapter(hostActivity.getApplicationContext(),R.id.tracksList, parcelableTracks);
                tracksListView.setAdapter(parcelableTracksArrayAdapter);

                tracksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if(Utility.isNetworkAvailable(getActivity())) {
                            MediaPlayerDialogFragment mediaPlayerFragment = new MediaPlayerDialogFragment();
                            FragmentManager fm = getFragmentManager();
                            Bundle args = new Bundle();
                            args.putParcelableArrayList("track", parcelableTracks);
                            args.putInt("currentPosition", position);
                            mediaPlayerFragment.setArguments(args);

                            if (hostActivity.findViewById(R.id.detail_fragment_container) == null) {
                                FragmentTransaction transaction = fm.beginTransaction();
                                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                transaction.hide(getTargetFragment());
                                transaction.add(R.id.fragment_container, mediaPlayerFragment)
                                        .addToBackStack(null).commit();
                            } else {
                                mediaPlayerFragment.show(fm, "Media Player Fragment");
                            }
                        }
                        else{
                            if(toast != null){
                                toast.cancel();
                            }
                            toast = Toast.makeText(hostActivity.getApplicationContext(), "Please check to make sure device is connected to the internet.", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                });
            }

            tracksListView.setSelectionFromTop(savedInstanceState.getInt("listViewPosition"), savedInstanceState.getInt("listViewPositioniOffset"));

        }
        else if(artistInfo != null){
            artistName = artistInfo.getString("artistName");
            ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(artistName);

            GetTopTracksTask getTopTracksTask = new GetTopTracksTask();
            getTopTracksTask.delegate = this;
            Log.d("TEST", artistInfo.getString("artistId"));
            getTopTracksTask.execute(artistInfo.getString("artistId"));
        }

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putParcelableArrayList("tracks", parcelableTracks);
        outState.putString("artistName", artistName);

        int position = tracksListView.getFirstVisiblePosition();
        outState.putInt("listViewPosition", position);
        View v = tracksListView.getChildAt(0);
        int offset = (v == null) ? 0 : (v.getTop() - tracksListView.getPaddingTop());
        outState.putInt("listViewPositionOffset", offset);

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
            ParcelableTrack pTrack = new ParcelableTrack(track.name,track.artists.get(0).name, track.album.name, imageUrl, track.preview_url, track.duration_ms);
            Log.d("TEST", pTrack.toString());
            parcelableTracks.add(pTrack);
        }

        if(tracksListSize > 0){
            topTracksArrayAdapter = new TopTracksArrayAdapter(hostActivity.getApplicationContext(),R.id.tracksList, tracks);
            tracksListView.setAdapter(topTracksArrayAdapter);

            tracksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    if(Utility.isNetworkAvailable(getActivity())) {
                        MediaPlayerDialogFragment mediaPlayerFragment = new MediaPlayerDialogFragment();
                        FragmentManager fm = getFragmentManager();
                        Bundle args = new Bundle();
                        args.putParcelableArrayList("track", parcelableTracks);
                        args.putInt("currentPosition", position);
                        mediaPlayerFragment.setArguments(args);

                        if (hostActivity.findViewById(R.id.detail_fragment_container) == null) {
                            FragmentTransaction transaction = fm.beginTransaction();
                            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            transaction.add(android.R.id.content, mediaPlayerFragment)
                                    .addToBackStack(null).commit();
                        } else {
                            mediaPlayerFragment.show(fm, "Media Player Fragment");
                        }
                    }
                    else{
                        if(toast != null){
                            toast.cancel();
                        }
                        toast = Toast.makeText(hostActivity.getApplicationContext(), "Please check to make sure device is connected to the internet.", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            });
        }
    }

    private class GetTopTracksTask extends AsyncTask<String, Void, Void> {

        private Tracks tracks;
        public TracksAsyncResponse delegate = null;

        @Override
        protected void onPostExecute(Void aVoid) {
            delegate.processFinish(tracks);
        }

        @Override
        protected Void doInBackground(String... artist) {
            MusicService musicService = new MusicService();
            this.tracks = musicService.getTopTracks(artist[0]);
            return null;
        }
    }

    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }*/
}
