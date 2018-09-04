
package com.backgroundlocationtracker;

import android.content.Intent;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

public class sajjadBackgroundLocationTrackerModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

    public sajjadBackgroundLocationTrackerModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }


    /**
     * @param AuthKey              Authkey For Authentication In Server Type String
     * @param ControllerLink       Link Of Controller That supposed to get location Type String
     * @param NotificationTitle    Title Of Notification For Showing in Status bar Type String
     * @param NotificationSubtitle SubTitle Of Notification For Showing in Status bar Type String
     * @param LatitudeParamName    Parameter Name Of Latitude in the controller Type String
     * @param LongitudeParamName   Parameter Name Of Longitude in the controller Type String
     * @param AuthKeyParamName     Parameter Name Of Authkey in the controller Type String
     */
    @ReactMethod
    public void StartLocationService(
            String AuthKey,
            String ControllerLink,
            String NotificationTitle,
            String NotificationSubtitle,
            String LatitudeParamName,
            String LongitudeParamName,
            String AuthKeyParamName) {
        Intent i = new Intent(getReactApplicationContext(), LocationService.class);
        i.putExtra("AuthKey", AuthKey);
        i.putExtra("ControllerLink", ControllerLink);
        i.putExtra("AuthKeyParamName", AuthKeyParamName);
        i.putExtra("NotificationTitle", NotificationTitle);
        i.putExtra("LatitudeParamName", LatitudeParamName);
        i.putExtra("LongitudeParamName", LongitudeParamName);
        i.putExtra("NotificationSubtitle", NotificationSubtitle);

        getReactApplicationContext().startService(i);
    }


    @ReactMethod
    public void StopLocationService() {
        Intent i = new Intent(getReactApplicationContext(), LocationService.class);

        try {
            getReactApplicationContext().stopService(i);
        } catch (Exception ignored) {
        }
    }


    @Override
    public String getName() {
        return "sajjadBackgroundLocationTracker";
    }
}