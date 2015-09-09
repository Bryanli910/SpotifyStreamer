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
    private transient List<ParcelableArtist> parcelableArtistList;

    public ParcelableArtistArrayAdapter(Context context,int resource, List<ParcelableArtist> parcelableArtistList) {
        super(context,resource,parcelableArtistList);
        this.context = context;
        this.parcelableArtistList = parcelableArtistList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.artist_list_item, parent, false);
            holder = new ViewHolder();
            holder.artistName = (TextView) convertView.findViewById(R.id.artistNameText);
            holder.artistImage = (ImageView) convertView.findViewById(R.id.coverImage);
            convertView.setTag(holder);
        }

        else{
            holder = (ViewHolder) convertView.getTag();
        }

        ParcelableArtist currentArtist = parcelableArtistList.get(position);
        holder.artistName.setText(currentArtist.artistName);

        String artistImageUrl = currentArtist.thumbnailUrl;

        if( !(artistImageUrl.equals("")) ){

            Picasso.with(context).load(artistImageUrl).into(holder.artistImage);
            Log.d("Array Adapter: ", artistImageUrl);

        }

        return convertView;
    }

    class ViewHolder {
        TextView artistName;
        ImageView artistImage;
    }

}
