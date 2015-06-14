package com.jiahaoliuliu.parsepushsegmentedbychannels;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.SendCallback;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final boolean IS_TESTING = true;

    private static final String DEFAULT_CAHNNEL_1 = "dubizzle";
    private static final String DEFAULT_CHANNEL_2 = "samsung";
    private static final String DEFUALT_MESSAGE = "Default message";

    // views
    private EditText channel1EditText;
    private EditText channel2EditText;
    private Button calculateButton;
    private EditText messageEditText;
    private Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Link the views
        channel1EditText = (EditText) findViewById(R.id.channel_1_edit_text);
        channel2EditText = (EditText) findViewById(R.id.channel_2_edit_text);

        calculateButton = (Button) findViewById(R.id.calculate_button);
        calculateButton.setOnClickListener(onClickListener);

        messageEditText = (EditText) findViewById(R.id.message_edit_text);

        sendButton = (Button) findViewById(R.id.send_button);
        sendButton.setOnClickListener(onClickListener);

//        // Get subscribed channels
//        List<String> subscribedChannels = ParseInstallation.getCurrentInstallation().getList("channels");
//        for (String channel : subscribedChannels) {
//            Log.v(TAG, "Channel: " + channel);
//        }

        if (IS_TESTING) {
            channel1EditText.setText(DEFAULT_CAHNNEL_1);
            channel2EditText.setText(DEFAULT_CHANNEL_2);
            messageEditText.setText(DEFUALT_MESSAGE);
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.calculate_button:
                    calculateNumberUsers();
                    break;
                case R.id.send_button:
                    sendMessage();
                    break;
            }
        }
    };

    /**
     * Calculate number of users by both channels
     */
    private void calculateNumberUsers() {
        if (!areChannelFieldsOk()) {
            return;
        }

        String channel1 = channel1EditText.getText().toString();
        String channel2 = channel2EditText.getText().toString();

        // TODO Calculate the number of users
    }

    /**
     * Check if all the fields are ok
     * @return
     *      true if all the fields related with channel are ok
     *      false otherwise
     */
    private boolean areChannelFieldsOk() {
        boolean areChannelFieldsOk = true;

        // Check channel1
        String channel1 = channel1EditText.getText().toString();
        if (TextUtils.isEmpty(channel1)) {
            areChannelFieldsOk = false;
            channel1EditText.setError(getString(R.string.error_empty_field));
        }

        String channel2 = channel2EditText.getText().toString();
        if (TextUtils.isEmpty(channel2)) {
            areChannelFieldsOk = false;
            channel2EditText.setError(getString(R.string.error_empty_field));
        }

        return areChannelFieldsOk;
    }

    /**
     * Send message to the users
     */
    private void sendMessage() {
        if (!areChannelFieldsOk()) {
            return;
        }

        if (!isMessageFieldOk()) {
            return;
        }

        String channel1 = channel1EditText.getText().toString();
        String channel2 = channel2EditText.getText().toString();
        String message = messageEditText.getText().toString();

        ParseQuery parsePushQuery = ParseInstallation.getQuery();
        List<String> channelsList = new ArrayList<String>();
        channelsList.add(channel1);
        channelsList.add(channel2);
        parsePushQuery.whereContainsAll("channels", channelsList);

        ParsePush parsePush = new ParsePush();
        parsePush.setQuery(parsePushQuery);
        parsePush.setMessage(message);
        parsePush.sendInBackground(new SendCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.v(TAG, "Push send correctly");
                } else {
                    Log.e(TAG, "Error sending push", e);
                }
            }
        });
    }

    /**
     * Check if the message field is ok
     * @return
     *      true if the message field is ok
     *      false otherwise
     */
    private boolean isMessageFieldOk() {
        boolean isMessageFieldOk = true;

        // Check message
        String message = messageEditText.getText().toString();
        if (TextUtils.isEmpty(message)) {
            isMessageFieldOk = false;
            messageEditText.setError(getString(R.string.error_empty_field));
        }

        return isMessageFieldOk;
    }


}
