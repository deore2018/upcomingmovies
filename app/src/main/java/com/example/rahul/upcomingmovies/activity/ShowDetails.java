package com.example.rahul.upcomingmovies.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rahul.upcomingmovies.R;
import com.example.rahul.upcomingmovies.adapter.MovieArrayAdapter;
import com.example.rahul.upcomingmovies.model.MovieModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ShowDetails extends AppCompatActivity {
    public static final String EXTRA_DATA = "extraData";
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    // public static final String fragUrl = Constant.serverUrl;
    private ListView listView;
    ImageView imageView;
    TextView title1, overview;
    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageView = (ImageView) findViewById(R.id.image_movie);
        title1 = (TextView) findViewById(R.id.movietitle);
        overview = (TextView) findViewById(R.id.movie_overview);
        ratingBar = (RatingBar) findViewById(R.id.pop_ratingbar);

        MovieModel model = (MovieModel) getIntent().getExtras().getSerializable("MOVIEDETILS");
        int movieid = model.getId();

//        if(model !=null)
//        {
//            Glide.with(this).load("https://image.tmdb.org/t/p/w500/"+model.getPoster_path()).into(imageView);
//            title1.setText(model.getTitle());
//            overview.setText(model.getOverview());
//            ratingBar.setRating(model.getVote_average());
//        }

        new AsyncFetch().execute("https://api.themoviedb.org/3/movie/" + movieid + "?api_key=b7cd3340a794e5a2f35e3abb820b497f&language=en-US");

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(ShowDetails.this, ListActivity.class);
                startActivity(intent);
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private class AsyncFetch extends AsyncTask<String, Void, String> {

        // ProgressDialog pdLoading = new ProgressDialog(Home.this);
        HttpURLConnection conn;
        URL url = null;

        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
        /*    pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();
*/
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
                url = new URL(params[0]);

            } catch (MalformedURLException e) {

                e.printStackTrace();
                return e.toString();
            }

            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");

                // setDoOutput to true as we recieve data from json file
                // conn.setDoOutput(true);

            } catch (IOException e1) {

                e1.printStackTrace();
                return e1.toString();
            }


            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    String s = reader.readLine();
                    reader.close();


                    // Pass data to onPostExecute method
                    return s;

                } else {

                    return ("unsuccessfull");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("value return", "" + s);
            JSONObject jsonObject = null;
            try {

                jsonObject = new JSONObject(s);
                ArrayList<MovieModel> movieModels = new ArrayList<>();

                Log.i("value return", "" + jsonObject.getString("poster_path"));
                Log.i("value return",""+jsonObject.getString("id"));

                title1.setText(jsonObject.getString("title"));
                overview.setText(jsonObject.getString("overview"));
                ratingBar.setRating(jsonObject.getLong("vote_average"));
                Glide.with(getApplicationContext()).load("https://image.tmdb.org/t/p/w500/"+jsonObject.getString("poster_path")).into(imageView);
                /*  JSONArray jsonArray = jsonObject.getJSONArray("backdrops");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    Log.i("value return",  ""+object);
                    //String abcd=object.getJSONArray()


//            title1.setText(model.getTitle());
//            overview.setText(model.getOverview());
//            ratingBar.setRating(model.getVote_average());
                   /* MovieModel movieModel = new MovieModel();
                    movieModel.setId(object.getInt("id"));
                    movieModel.setTitle(object.getString("original_title"));
                    movieModel.setAdult(object.getString("adult"));
                    movieModel.setRelease_date(object.getString("release_date"));
                    movieModel.setPoster_path(object.getString("poster_path"));
                    movieModels.add(movieModel);
                }*/
               /* MovieArrayAdapter movieArrayAdapter=new MovieArrayAdapter(getApplicationContext(),R.layout.row_listitem,movieModels);
                listView.setAdapter(movieArrayAdapter);

                Log.i("List", movieModels.get(2).getRelease_date());
                Log.i("Id",""+movieModels.get(2).getId());
                //text.setText(MoviesMap.get("The Predator"));*/
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
