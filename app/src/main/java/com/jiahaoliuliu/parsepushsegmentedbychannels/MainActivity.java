package com.jiahaoliuliu.parsepushsegmentedbychannels;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.parse.ParseInstallation;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get subscribed channels
        List<String> subscribedChannels = ParseInstallation.getCurrentInstallation().getList("channels");
        for (String channel : subscribedChannels) {
            Log.v(TAG, "Channel: " + channel);
        }
    }
}
