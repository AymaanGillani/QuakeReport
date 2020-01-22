package com.example.android.quakereport;

import android.content.Context;
import android.content.AsyncTaskLoader;
import android.util.Log;

import java.util.List;

public class EarthquakeAsyncTaskLoader extends AsyncTaskLoader<List<Quake>> {
    public static final String LOG_TAG = EarthquakeAsyncTaskLoader.class.getSimpleName();
    String[] url;

    public EarthquakeAsyncTaskLoader(Context context, String... urls) {
        super(context);
        if (urls.length < 1 || urls[0] == null) {
                Log.e(LOG_TAG,"Url is null");
        }
        url = urls;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Quake> loadInBackground() {
        if (url == null) {
            return null;
        }
        List<Quake> result = QueryUtils.fetchEarthquakeData(url[0]);
        return result;
    }
}
