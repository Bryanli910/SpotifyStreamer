package com.bli.spotifystreamer;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Pager;

public class ArtistArrayAdapter extends ArrayAdapter<Artist>{

    private final Context context;
    private final ArtistsPager artistsPager;

    public ArtistArrayAdapter(Context context,int resource, ArtistsPager artistsPager) {
        super(context,resource,artistsPager.artists.items);
        this.context = context;
        this.artistsPager = artistsPager;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.artist_list_item, parent, false);

        Artist currentArtist = artistsPager.artists.items.get(position);

        TextView artistTextView = (TextView) rowView.findViewById(R.id.artistNameText);
        artistTextView.setText(currentArtist.name);

        ImageView artistImageView = (ImageView) rowView.findViewById(R.id.coverImage);

        if(currentArtist.images.size() > 0 ){

            String artistImageUrl = currentArtist.images.get(0).url;
            Picasso.with(context).load(artistImageUrl).into(artistImageView);
            Log.d("Array Adapter: ", artistImageUrl);

        }

        else{

        }

        return rowView;
    }
}
