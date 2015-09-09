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

        ViewHolder holder;

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.top_tracks_list_item, parent, false);
            holder = new ViewHolder();
            holder.albumName = (TextView)convertView.findViewById(R.id.albumName);
            holder.trackName = (TextView)convertView.findViewById(R.id.trackName);
            holder.coverImage = (ImageView)convertView.findViewById(R.id.albumImage);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder)convertView.getTag();
        }

        Track currentTrack = tracks.tracks.get(position);
        holder.albumName.setText(currentTrack.album.name);
        holder.trackName.setText(currentTrack.name);

        if(currentTrack.album.images.size() > 0 ){

            String albumImageUrl = currentTrack.album.images.get(0).url;
            Picasso.with(context).load(albumImageUrl).into(holder.coverImage);
            Log.d("Array Adapter: ", albumImageUrl);

        }

        return convertView;
    }

    class ViewHolder{
        TextView albumName, trackName;
        ImageView coverImage;
    }
}
