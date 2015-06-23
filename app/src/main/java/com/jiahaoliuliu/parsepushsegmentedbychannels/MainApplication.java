package com.jiahaoliuliu.parsepushsegmentedbychannels;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.SaveCallback;

/**
 * Created by Jiahao on 6/11/15.
 */
public class MainApplication extends Application {

    private static final String TAG = "MainApplication";

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize Parse
        if(!MainActivity.IS_TESTING) {
            //Parse.initialize(this, APIKeys.APPLICATION_ID, APIKeys.CLIENT_ID);
        } else {
            Parse.initialize(this, APIKeys.TEST_APPLICATION_ID, APIKeys.TEST_CLIENT_ID);
        }

        ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d(TAG, "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e(TAG, "failed to subscribe for push", e);
                }
            }
        });

        ParsePush.subscribeInBackground("jaboston_test");
    }
}
