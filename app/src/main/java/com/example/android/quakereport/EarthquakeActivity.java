/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Quake>>{

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    private static final String URL="https://earthquake.usgs.gov/fdsnws/event/1/query";
//    private static final String URL="";
    private static final int EARTHQUAKE_LOADER_ID = 1;
    QuakeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        ConnectivityManager cm =
                (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        TextView noNetTV = (TextView)findViewById(R.id.noNetTV);
        if(!isConnected) {
            ProgressBar pb=(ProgressBar) findViewById(R.id.progressBar);
            TextView loadingTV=(TextView)findViewById(R.id.loadingTV);
            pb.setVisibility(View.GONE);
            loadingTV.setVisibility(View.GONE);
        }
        else {
            noNetTV.setVisibility(View.GONE);
            LoaderManager loaderManager = getLoaderManager();

            // Find a reference to the {@link ListView} in the layout
            ListView earthquakeListView = (ListView) findViewById(R.id.list);
            earthquakeListView.setEmptyView(findViewById(R.id.emptyView));

            // Create a new {@link ArrayAdapter} of earthquakes
            adapter = new QuakeAdapter(this, new ArrayList<Quake>());

            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);

            // Set the adapter on the {@link ListView}
            // so the list can be populated in the user interface
            earthquakeListView.setAdapter(adapter);
            earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    Quake quake = adapter.getItem(position);

                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(quake.getUrl()));
                    startActivity(i);

                }
            });
        }
    }

    @Override
    public Loader<List<Quake>> onCreateLoader(int i, Bundle bundle) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String minMagnitude = sharedPrefs.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));
        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));
        Uri baseUri = Uri.parse(URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", "10");
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", orderBy);
        return new EarthquakeAsyncTaskLoader(EarthquakeActivity.this,uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(android.content.Loader<List<Quake>> loader, List<Quake> data) {
        ProgressBar pb=(ProgressBar) findViewById(R.id.progressBar);
        TextView loadingTV=(TextView)findViewById(R.id.loadingTV);
        pb.setVisibility(View.GONE);
        loadingTV.setVisibility(View.GONE);
        adapter.clear();
        if (data != null && !data.isEmpty()) {
            adapter.addAll(data);
        }
        TextView emptyView=(TextView)findViewById(R.id.emptyView);
        emptyView.setText(""+"No Earthquakes found.");
    }



    @Override
    public void onLoaderReset(android.content.Loader<List<Quake>> loader) {
        adapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
