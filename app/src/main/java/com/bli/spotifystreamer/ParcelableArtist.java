package com.bli.spotifystreamer;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class ParcelableArtist implements Parcelable{
    //Parcel keys
    private static final String KEY_ARTIST_NAME = "artistName";
    private static final String KEY_THUMBNAIL_URL = "thumbnailUrl";
    private static final String KEY_ARTIST_ID = "artistId";

    String artistName, thumbnailUrl, artistId;

    public ParcelableArtist(String artistName, String thumbnailUrl, String artistId){
        this.artistName = artistName;
        this.thumbnailUrl = thumbnailUrl;
        this.artistId = artistId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //Create a bundle for the key value pairs
        Bundle bundle = new Bundle();

        //Insert key value pairs to the bundle
        bundle.putString(KEY_ARTIST_NAME, artistName);
        bundle.putString(KEY_THUMBNAIL_URL, thumbnailUrl);
        bundle.putString(KEY_ARTIST_ID, artistId);

        //Write bundle to parcel
        dest.writeBundle(bundle);
    }

    public static final Parcelable.Creator<ParcelableArtist> CREATOR
            = new Parcelable.Creator<ParcelableArtist>() {
        public ParcelableArtist createFromParcel(Parcel source) {

            Bundle bundle = source.readBundle();

            return new ParcelableArtist(bundle.getString(KEY_ARTIST_NAME),
                    bundle.getString(KEY_THUMBNAIL_URL),
                    bundle.getString(KEY_ARTIST_ID));
        }

        public ParcelableArtist[] newArray(int size) {
            return new ParcelableArtist[size];
        }
    };
}
