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

import java.util.List;

import kaaes.spotify.webapi.android.models.Track;

public class ParcelableTracksArrayAdapter extends ArrayAdapter <ParcelableTrack>{
    private final Context context;
    private final List<ParcelableTrack> parcelableTracks;

    public ParcelableTracksArrayAdapter(Context context, int resource, List<ParcelableTrack> parcelableTracks) {
        super(context, resource, parcelableTracks);
        this.context = context;
        this.parcelableTracks = parcelableTracks;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.top_tracks_list_item, parent, false);

        ParcelableTrack currentTrack = parcelableTracks.get(position);

        TextView albumTextView = (TextView) rowView.findViewById(R.id.albumName);
        albumTextView.setText(currentTrack.albumName);

        TextView trackTextView = (TextView) rowView.findViewById(R.id.trackName);
        trackTextView.setText(currentTrack.trackName);

        ImageView albumImageView = (ImageView) rowView.findViewById(R.id.albumImage);
        String thumbnailUrl = currentTrack.thumbnailUrl;

        if(thumbnailUrl != "" || !(thumbnailUrl.equals(""))){

            Picasso.with(context).load(thumbnailUrl).into(albumImageView);
            Log.d("Array Adapter: ", thumbnailUrl);

        }

        return rowView;
    }
}
