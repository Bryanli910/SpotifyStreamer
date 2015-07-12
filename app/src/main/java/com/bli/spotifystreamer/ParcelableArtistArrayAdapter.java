package com.bli.spotifystreamer;

import android.content.Context;
import android.util.Log;
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

public class ParcelableArtistArrayAdapter extends ArrayAdapter<ParcelableArtist>{
    private final Context context;
    private final List<ParcelableArtist> parcelableArtistList;

    public ParcelableArtistArrayAdapter(Context context,int resource, List<ParcelableArtist> parcelableArtistList) {
        super(context,resource,parcelableArtistList);
        this.context = context;
        this.parcelableArtistList = parcelableArtistList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.artist_list_item, parent, false);

        ParcelableArtist currentArtist = parcelableArtistList.get(position);

        TextView artistTextView = (TextView) rowView.findViewById(R.id.artistNameText);
        artistTextView.setText(currentArtist.artistName);

        ImageView artistImageView = (ImageView) rowView.findViewById(R.id.coverImage);

        if(!currentArtist.thumbnailUrl.equals("") || currentArtist.thumbnailUrl != "" ){

            String artistImageUrl = currentArtist.thumbnailUrl;
            Picasso.with(context).load(artistImageUrl).into(artistImageView);
            Log.d("Array Adapter: ", artistImageUrl);

        }

        else{

        }

        return rowView;
    }
}
