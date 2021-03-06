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
    public static final boolean IS_TESTING = true;

    @Override
    public void onCreate() {
        super.onCreate();
        setupParse();
    }

     public void setupParse(){
         // Initialize Parse
         if(!IS_TESTING) {
             Parse.initialize(this, APIKeys.IOS_APPLICATION_ID, APIKeys.IOS_APPLICATION_ID);
         } else {
             Parse.initialize(this, APIKeys.TEST_APPLICATION_ID, APIKeys.TEST_CLIENT_ID);
         }

         ParsePush.subscribeInBackground("TestingAppVersion", new SaveCallback() {
             @Override
             public void done(ParseException e) {
                 if (e == null) {
                     Log.d(TAG, "successfully subscribed to the broadcast channel.");
                 } else {
                     Log.e(TAG, "failed to subscribe for push", e);
                 }
             }
         });


     }
}
