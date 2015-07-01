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

import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;

public class TopTracksArrayAdapter extends ArrayAdapter<Track>{

    private final Context context;
    private final Tracks tracks;

    public TopTracksArrayAdapter(Context context, int resource, Tracks tracks) {
        super(context, resource, tracks.tracks);
        this.context = context;
        this.tracks = tracks;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.top_tracks_list_item, parent, false);

        Track currentTrack = tracks.tracks.get(position);

        TextView albumTextView = (TextView) rowView.findViewById(R.id.albumName);
        albumTextView.setText(currentTrack.album.name);

        TextView trackTextView = (TextView) rowView.findViewById(R.id.trackName);
        trackTextView.setText(currentTrack.name);

        ImageView albumImageView = (ImageView) rowView.findViewById(R.id.albumImage);

        if(currentTrack.album.images.size() > 0 ){

            String albumImageUrl = currentTrack.album.images.get(0).url;
            Picasso.with(context).load(albumImageUrl).into(albumImageView);
            Log.d("Array Adapter: ", albumImageUrl);

        }

        return rowView;
    }
}
