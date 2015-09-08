package com.bli.spotifystreamer.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.bli.spotifystreamer.R;

public class ArtistSearchHostActivity extends SingleFragmentActivity implements ArtistSearchFragment.Callbacks{
    @Override
    public void onArtistSelected(String artistId, String artistName) {
        if(findViewById(R.id.detail_fragment_container) == null){
            Intent i = new Intent(this, TopTracksHostActivity.class);
            i.putExtra("artistId", artistId);
            i.putExtra("artistName", artistName);
            startActivity(i);
        }
        else{
            Fragment newDetail = TopTracksFragment.newInstance(artistId, artistName);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, newDetail)
                    .commit();
        }
    }

    @Override
    protected Fragment createFragment() {
        return new ArtistSearchFragment();
    }

    @Override
    protected int getLayoutResId(){
        return R.layout.activity_masterdetail;
    }
}
