package com.partho.zoomcar.zoomcarapp;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.partho.zoomcar.zoomcarapp.models.ZoomcarDatabaseHelper;

/**
 * Created by partho on 13/9/15.
 */
public class ZoomcarApplication extends Application {
    private static ZoomcarApplication sInstance;
    private RequestQueue appRequestQueue;
    private ZoomcarDatabaseHelper zoomcarDatabaseHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        appRequestQueue = Volley.newRequestQueue(getApplicationContext());
        if (sInstance == null)
        {
            sInstance = this;
            zoomcarDatabaseHelper = new ZoomcarDatabaseHelper(this);
        }
    }

    public synchronized static ZoomcarApplication getInstance()
    {
        return sInstance;
    }

    public RequestQueue getAppRequestQueue()
    {
        return appRequestQueue;
    }
    public ZoomcarDatabaseHelper getDbo()
    {
        return zoomcarDatabaseHelper;
    }
}
