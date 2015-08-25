package com.bli.spotifystreamer.activities;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.bli.spotifystreamer.R;

public class TopTracksHostActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        String artistId = getIntent().getStringExtra("artistId");
        String artistName = getIntent().getStringExtra("artistName");
        return TopTracksFragment.newInstance(artistId, artistName);
    }
}
