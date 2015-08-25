package com.bli.spotifystreamer;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.Array;
import java.util.ArrayList;

import kaaes.spotify.webapi.android.models.Tracks;

public class ParcelableTrack implements Parcelable{

    //Parcel keys
    private static final String KEY_TRACK_NAME = "trackName";
    private static final String KEY_ARTIST_NAME = "artistName";
    private static final String KEY_ALBUM_NAME = "albumName";
    private static final String KEY_THUMBNAIL_URL = "thumbnailUrl";
    private static final String KEY_PREVIEW_URL = "previewUrl";
    private static final String KEY_DURATION_MS = "durationMs";

    public String trackName, artistName, albumName, thumbnailUrl, previewUrl;
    public long durationMs;

    public ParcelableTrack(String trackName, String artistName, String albumName, String thumbnailUrl, String previewUrl, long durationMs){
        this.trackName = trackName;
        this.artistName = artistName;
        this.albumName = albumName;
        this.thumbnailUrl = thumbnailUrl;
        this.previewUrl = previewUrl;
        this.durationMs = durationMs;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //Create a bundle
        Bundle bundle = new Bundle();

        //Insert key value pairs to the bundle
        bundle.putString(KEY_TRACK_NAME, trackName);
        bundle.putString(KEY_ARTIST_NAME, artistName);
        bundle.putString(KEY_ALBUM_NAME, albumName);
        bundle.putString(KEY_THUMBNAIL_URL, thumbnailUrl);
        bundle.putString(KEY_PREVIEW_URL, previewUrl);
        bundle.putLong(KEY_DURATION_MS, durationMs);

        //Write bundle to parcel
        dest.writeBundle(bundle);
    }

    public static Creator<ParcelableTrack> CREATOR = new Creator<ParcelableTrack>() {
        @Override
        public ParcelableTrack createFromParcel(Parcel source) {
            Bundle bundle = source.readBundle();

            return new ParcelableTrack(bundle.getString(KEY_TRACK_NAME),
                    bundle.getString(KEY_ARTIST_NAME),
                    bundle.getString(KEY_ALBUM_NAME),
                    bundle.getString(KEY_THUMBNAIL_URL),
                    bundle.getString(KEY_PREVIEW_URL),
                    bundle.getLong(KEY_DURATION_MS));
        }

        @Override
        public ParcelableTrack[] newArray(int size) {
            return new ParcelableTrack[size];
        }
    };

    @Override
    public String toString(){
        return trackName + " " + albumName;
    }
}
