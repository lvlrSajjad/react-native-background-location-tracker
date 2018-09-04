package com.backgroundlocationtracker;

import android.Manifest;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;


public class LocationService extends Service {
    private static final String TAG = "LocationService";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 10000;
    private static final float LOCATION_DISTANCE = 200f;
    private String provider;
    public static String mAuthKey;
    public static String mNotificationTitle;
    public static String mNotificationSubTitle;
    private static double mLat;
    private static double mLong;
    private String mControllerLink;
    private String mLongitudeParamName;
    private String mLatitudeParamName;
    private String mAuthKeyParamName;

    class SendDataToServer extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            String JsonResponse = null;
            String JsonDATA = params[0];
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(mControllerLink);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                // is output buffer writter
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setConnectTimeout(10000);
                //set headers and method
                Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
                writer.write(JsonDATA);
                // json data
                writer.close();
                InputStream inputStream = urlConnection.getInputStream();
                //input stream
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String inputLine;
                while ((inputLine = reader.readLine()) != null)
                    buffer.append(inputLine + "\n");
                if (buffer.length() == 0) {
                    // Stream was empty. No point in parsing.
                    return null;
                }
                JsonResponse = buffer.toString();

                //response data
                Log.i(TAG, JsonResponse);
                try {
                    //send to post execute
                    return JsonResponse;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;


            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(TAG, "Error closing stream", e);
                    }
                }
            }
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
        }

    }


    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;

        public LocationListener(String provider) {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);

        }

        @Override
        public void onLocationChanged(Location location) {
            Log.e(TAG, "onLocationChanged: " + location);
            mLastLocation.set(location);
            JSONObject post_dict = new JSONObject();

            try {
                mLat = location.getLatitude();
                mLong = location.getLongitude();
                post_dict.put(mLatitudeParamName, mLat);
                post_dict.put(mLongitudeParamName, mLong);
                post_dict.put(mAuthKeyParamName, mAuthKey);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (post_dict.length() > 0) {
                new SendDataToServer().execute(String.valueOf(post_dict));
            }


        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled: " + provider);

        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled: " + provider);

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG, "onStatusChanged: " + provider);

        }

    }


    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(provider),
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)

    };

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        super.onStartCommand(intent, flags, startId);
        mAuthKey = intent.getStringExtra("AuthKey");
        mControllerLink = intent.getStringExtra("ControllerLink");
        mNotificationTitle = intent.getStringExtra("NotificationTitle");
        mNotificationSubTitle = intent.getStringExtra("NotificationSubtitle");
        mLongitudeParamName = intent.getStringExtra("LongitudeParamName");
        mLatitudeParamName = intent.getStringExtra("LatitudeParamName");
        mAuthKeyParamName = intent.getStringExtra("AuthKeyParamName");

        showNotification();
        return START_REDELIVER_INTENT;
    }

    private void showNotification() {
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle(mNotificationTitle)
                .setTicker(mNotificationTitle)
                .setContentText(mNotificationSubTitle)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
//                        R.drawable.ic_launcher))
                .setOngoing(true).build();
        startForeground(120002,
                notification);

    }

    @Override
    public void onCreate() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        Log.e(TAG, "onCreate");
        initializeLocationManager();
        provider = mLocationManager.getBestProvider(criteria, true);

        try {
            mLocationManager.requestLocationUpdates(
                    provider,
                    LOCATION_INTERVAL,
                    LOCATION_DISTANCE,
                    mLocationListeners[0]
            );

        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);

        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());

        }

    }

    private void PostLocationAndStatusToServer() {
        JSONObject post_dict = new JSONObject();

        try {
            post_dict.put("lat", mLat);
            post_dict.put("long", mLong);
            post_dict.put("authKey", mAuthKey);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (post_dict.length() > 0) {
            new SendDataToServer().execute(String.valueOf(post_dict));
        }

    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (LocationListener mLocationListener : mLocationListeners) {
                try {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mLocationManager.removeUpdates(mLocationListener);
                    JSONObject post_dict = new JSONObject();
                    try {
                        post_dict.put("lat", mLat);
                        post_dict.put("long", mLong);
                        post_dict.put("auth_key", mAuthKey);
                        //   Toast.makeText(getApplicationContext(), String.valueOf(post_dict),Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (post_dict.length() > 0) {
                        new SendDataToServer().execute(String.valueOf(post_dict));
                    }
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listener, ignore", ex);
                }
            }
        }
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager - LOCATION_INTERVAL: " + LOCATION_INTERVAL + " LOCATION_DISTANCE: " + LOCATION_DISTANCE);
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }
}
