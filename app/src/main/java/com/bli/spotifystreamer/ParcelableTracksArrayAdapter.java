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

public class ParcelableTracksArrayAdapter extends ArrayAdapter <ParcelableTrack>{
    private final Context context;
    private transient List<ParcelableTrack> parcelableTracks;

    public ParcelableTracksArrayAdapter(Context context, int resource, List<ParcelableTrack> parcelableTracks) {
        super(context, resource, parcelableTracks);
        this.context = context;
        this.parcelableTracks = parcelableTracks;
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

        ParcelableTrack currentTrack = parcelableTracks.get(position);
        holder.albumName.setText(currentTrack.albumName);
        holder.trackName.setText(currentTrack.trackName);
        String thumbnailUrl = currentTrack.thumbnailUrl;

        if( !(thumbnailUrl.equals("")) ){
            Picasso.with(context).load(thumbnailUrl).into(holder.coverImage);
            Log.d("Array Adapter: ", thumbnailUrl);
        }
        return convertView;
    }
    class ViewHolder{
        TextView albumName, trackName;
        ImageView coverImage;
    }
}
