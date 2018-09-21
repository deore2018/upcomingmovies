package com.example.rahul.upcomingmovies.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rahul.upcomingmovies.R;
import com.example.rahul.upcomingmovies.model.MovieModel;

import java.util.List;

public class MovieArrayAdapter extends ArrayAdapter {

    private List<MovieModel> movieModelsDetails;
    private int resource;
    private Context context;

    public MovieArrayAdapter( Context context, int resource,List<MovieModel> movieModels) {
        super(context, resource, movieModels);

        this.context = context;
        this.resource = resource;
        this.movieModelsDetails = movieModels;
    }


    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view= LayoutInflater.from(context).inflate(resource,parent,false);

        MovieModel model=movieModelsDetails.get(position);
        ImageView image=(ImageView)view.findViewById(R.id.ivmovie1);
        TextView movie=(TextView)view.findViewById(R.id.textmovieName);
        TextView adult=(TextView)view.findViewById(R.id.textmovietype);
        TextView release=(TextView)view.findViewById(R.id.textreleasedate);

        Glide.with(context).load("https://image.tmdb.org/t/p/w500/"+model.getPoster_path()).into(image);
        movie.setText(model.getTitle());
        adult.setText(model.getAdult());
        release.setText(model.getRelease_date());
        return view;
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return movieModelsDetails.get(position);
    }

}
