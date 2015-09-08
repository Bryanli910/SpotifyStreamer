package com.bli.spotifystreamer.activities;

import android.support.v4.app.Fragment;

public class TopTracksHostActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        String artistId = getIntent().getStringExtra("artistId");
        String artistName = getIntent().getStringExtra("artistName");
        return TopTracksFragment.newInstance(artistId, artistName);
    }
}
