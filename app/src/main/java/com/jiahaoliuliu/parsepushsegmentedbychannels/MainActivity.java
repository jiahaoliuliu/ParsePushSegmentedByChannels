package com.jiahaoliuliu.parsepushsegmentedbychannels;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseInstallation;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

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
        if (!areAllFieldsOk()) {
            return;
        }

        String channel1 = channel1EditText.getText().toString();
        String channel2 = channel2EditText.getText().toString();

        // TODO Calculate the number of users
    }

    /**
     * Check if all the fields are ok
     * @return
     *      true if all the fields are ok
     *      false otherwise
     */
    private boolean areAllFieldsOk() {
        boolean areAllFieldsOk = true;

        // Check channel1
        String channel1 = channel1EditText.getText().toString();
        if (!TextUtils.isEmpty(channel1)) {
            areAllFieldsOk = false;
            channel1EditText.setError(getString(R.string.error_empty_field));
        }

        String channel2 = channel2EditText.getText().toString();
        if (!TextUtils.isEmpty(channel2)) {
            areAllFieldsOk = false;
            channel2EditText.setError(getString(R.string.error_empty_field));
        }

        return areAllFieldsOk;
    }

    /**
     * Send message to the users
     */
    private void sendMessage() {
        if (!areAllFieldsOk()) {
            return;
        }

        String channel1 = channel1EditText.getText().toString();
        String channel2 = channel2EditText.getText().toString();

        // TODO send message to the users
    }
}
