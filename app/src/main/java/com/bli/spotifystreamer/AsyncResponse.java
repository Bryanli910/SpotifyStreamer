package com.bli.spotifystreamer;

import kaaes.spotify.webapi.android.models.ArtistsPager;

public interface AsyncResponse {
    void processFinish(ArtistsPager artistsPager);
}
