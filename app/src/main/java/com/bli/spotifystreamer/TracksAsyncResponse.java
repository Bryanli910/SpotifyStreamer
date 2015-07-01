package com.bli.spotifystreamer;

import kaaes.spotify.webapi.android.models.Tracks;

public interface TracksAsyncResponse {

    public void processFinish(Tracks tracks);
}
