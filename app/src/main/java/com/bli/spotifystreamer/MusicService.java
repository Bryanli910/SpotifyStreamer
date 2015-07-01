package com.bli.spotifystreamer;


import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Tracks;

public class MusicService {

    private static SpotifyApi api;
    private static SpotifyService spotify;
    private Map<String, Object> options;

    public MusicService(){
        api = new SpotifyApi();
        spotify = api.getService();
        options = new HashMap<String, Object>();
    }

    public ArtistsPager getArtists(String artist){
        return spotify.searchArtists(artist);
    }

    public Tracks getTopTracks(String id){
        options.put("country","US");
        return spotify.getArtistTopTrack(id,options);
    }
}
