package edu.pitt.cs.cs1635.vsc5.group_project;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class Viewing extends AppCompatActivity {

    final String IMAGE_BASE_URL = "https://photopod.000webhostapp.com/";

    String capsuleOwner;
    String capsuleName;

    ArrayList<String> paths = new ArrayList<String>();
    ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();

    MyAdapter adapter;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlock);

        viewPager = (ViewPager) findViewById(R.id.view_pager);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            capsuleOwner = extras.getString("capsuleOwner");
            capsuleName = extras.getString("capsuleName");
        }

        //
        // Get paths from db
        //
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonResponse = new JSONArray(response);
                    System.out.println(response);
                    for(int i = 0; i < jsonResponse.length(); i++) {
                        JSONObject jsonObject = jsonResponse.getJSONObject(i);
                        String path = jsonObject.getString("path");
                        paths.add(path);
                    }

//                    Log.d("COLIN", paths.size() + "");
//                    for (int i=0; i<paths.size(); i++) {
//                        Log.d("COLIN", paths.get(i) + "");
//                    }
                    // Start downloading the images
                    for (int i=0; i<paths.size(); i++) {
                        new DownloadImage(paths.get(i), Viewing.this).execute();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        GetImages gottenImages = new GetImages(capsuleOwner, capsuleName, responseListener);
        RequestQueue queue = Volley.newRequestQueue(Viewing.this);
        queue.add(gottenImages);
    }

    private class DownloadImage extends AsyncTask<Void, Void, Bitmap>{
        String name;
        Context context;

        public DownloadImage(String n, Context c) {
            name = n;
            context = c;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            String url = IMAGE_BASE_URL + name;

            try {
                URLConnection connection = new URL(url).openConnection();
                connection.setConnectTimeout(1000 * 30);
                connection.setReadTimeout(1000 * 30);

                return BitmapFactory.decodeStream((InputStream) connection.getContent(), null, null);
            } catch(Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            if(bitmap != null) {
                bitmaps.add(bitmap);
                adapter = new MyAdapter(context, bitmaps);
                viewPager.setAdapter(adapter);
            }
        }
    }
}
