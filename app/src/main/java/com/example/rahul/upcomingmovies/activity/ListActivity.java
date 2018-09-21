package com.example.rahul.upcomingmovies.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

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

public class ListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    public static final String EXTRA_DATA = "extraData";
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    // public static final String fragUrl = Constant.serverUrl;
    private ListView listView;
    //    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listactivity);

        listView = (ListView) findViewById(R.id.listView1);
        listView.setOnItemClickListener(this);
        new AsyncFetch().execute("https://api.themoviedb.org/3/movie/upcoming?api_key=b7cd3340a794e5a2f35e3abb820b497f&language=en-US&page=1");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.info_menu,menu);
       // getSupportActionBar().setIcon(R.drawable.ic_img_error);
        return super.onCreateOptionsMenu(menu);
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_home:
                Intent i=new Intent(getApplicationContext(),Information.class);
                startActivity(i);
                        return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long l) {

        Intent i = new Intent(this, ShowDetails.class);
        i.putExtra("MOVIEDETILS", (MovieModel) parent.getItemAtPosition(position));
        startActivity(i);
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
                conn.setDoOutput(true);

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

                    return ("unsuccessful");
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
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(s);
                ArrayList<MovieModel> movieModels = new ArrayList<>();

                JSONArray jsonArray = jsonObject.getJSONArray("results");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    MovieModel movieModel = new MovieModel();
                    movieModel.setId(object.getInt("id"));
                    movieModel.setTitle(object.getString("original_title"));
                    movieModel.setAdult(object.getString("adult"));
                    movieModel.setRelease_date(object.getString("release_date"));
                    movieModel.setPoster_path(object.getString("poster_path"));
                    movieModels.add(movieModel);
                }
                MovieArrayAdapter movieArrayAdapter = new MovieArrayAdapter(ListActivity.this, R.layout.row_listitem, movieModels);
                listView.setAdapter(movieArrayAdapter);

                Log.i("List", movieModels.get(2).getRelease_date());
                //text.setText(MoviesMap.get("The Predator"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
