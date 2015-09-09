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

        ViewHolder holder;

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.artist_list_item, parent, false);
            holder = new ViewHolder();
            holder.artistName = (TextView)convertView.findViewById(R.id.artistNameText);
            holder.artistImage = (ImageView)convertView.findViewById(R.id.coverImage);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder)convertView.getTag();
        }
        Artist currentArtist = artistsPager.artists.items.get(position);

        holder.artistName.setText(currentArtist.name);

        if(currentArtist.images.size() > 0 ){

            String artistImageUrl = currentArtist.images.get(0).url;
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
