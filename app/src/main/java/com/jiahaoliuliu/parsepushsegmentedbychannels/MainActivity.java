package com.jiahaoliuliu.parsepushsegmentedbychannels;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SendCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    private static final String DEFAULT_CHANNEL_1 = "test_dbz";
    private static final String DEFAULT_CHANNEL_2 = "country_algeria";
    private static final String DEFUALT_MESSAGE = "Welcome to dubizzle :-)";

    // views
    private EditText channel1EditText;
    private EditText channel2EditText;
    private Button calculateButton;
    private EditText messageEditText;
    private Button sendButton;
    private TextView tvNumberOfUsers;
    private DatePicker datePickerActiveSince;
    private TimePicker timePickerLastActive;
    private CheckBox checkboxChannel1;
    private CheckBox checkboxChannel2;
    private Button btnAddChannel;
    private Switch switchPlatform;

    private ArrayList<View> channelLayoutArrayList;

    private LayoutInflater inflater;

    // default values for year month and day.
    private int mYear = 2015;
    private int mMonth = 6;
    private int mDay = 16;
    private int mMinutes = 0;
    private int mHours = 0;

    static final int DATE_DIALOG_ID = 999;
    static final int TIME_DIALOG_ID = 998;


    private void setupViews(){
        // Link the views
        channel1EditText = (EditText) findViewById(R.id.channel_1_edit_text);
        channel2EditText = (EditText) findViewById(R.id.channel_2_edit_text);

        datePickerActiveSince = (DatePicker) findViewById(R.id.datePickerActiveSince);

        timePickerLastActive = (TimePicker) findViewById(R.id.timePickerLastActive);

        calculateButton = (Button) findViewById(R.id.calculate_button);
        calculateButton.setOnClickListener(onClickListener);

        messageEditText = (EditText) findViewById(R.id.message_edit_text);

        sendButton = (Button) findViewById(R.id.send_button);
        sendButton.setOnClickListener(onClickListener);

        tvNumberOfUsers = (TextView) findViewById(R.id.number_user_text_view);

        checkboxChannel1 = (CheckBox) findViewById(R.id.checkBoxChannel1);
        checkboxChannel2 = (CheckBox) findViewById(R.id.checkBoxChannel2);

        btnAddChannel = (Button) findViewById(R.id.btnAddChannel);
        btnAddChannel.setOnClickListener(onClickListener);

        inflater = LayoutInflater.from(MainActivity.this);

        channelLayoutArrayList = new ArrayList<View>();

        switchPlatform = (Switch) findViewById(R.id.platformSwitch);
        switchPlatform.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setParseBasedOnChecked(isChecked);
            }
        });

        subscribeToTestChannels();

    }

    private void setParseBasedOnChecked(Boolean isAndroid){
        // If we are using testing environment, don't re-initialize parse
        if (MainApplication.IS_TESTING) {
            return;
        }

        if(isAndroid){
            Parse.initialize(this, APIKeys.ANDROID_APPLICATION_ID, APIKeys.ANDROID_CLIENT_ID);
        } else {
            Parse.initialize(this, APIKeys.IOS_APPLICATION_ID, APIKeys.IOS_CLIENT_ID);
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

        subscribeToTestChannels();


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        // Get subscribed channels
//        List<String> subscribedChannels = ParseInstallation.getCurrentInstallation().getList("channels");
//        for (String channel : subscribedChannels) {
//            Log.v(TAG, "Channel: " + channel);
//        }

        setupViews();

        // subscribe to test channels so we can see the effect of our push
        ParsePush.subscribeInBackground(DEFAULT_CHANNEL_1);
        ParsePush.subscribeInBackground(DEFAULT_CHANNEL_2);
        channel1EditText.setText(DEFAULT_CHANNEL_1);
        channel2EditText.setText(DEFAULT_CHANNEL_2);
        messageEditText.setText(DEFUALT_MESSAGE);



    }

    private void subscribeToTestChannels(){
        ParsePush.subscribeInBackground("jaboston_test", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.v(TAG, "jaboston_test channel subscribed correctly");
                } else {
                    Log.e(TAG, "Error subscribing jaboston_test channel", e);
                }
            }
        });
        ParsePush.subscribeInBackground("jaboston_test_2", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.v(TAG, "jaboston_test_2 channel subscribed correctly");
                } else {
                    Log.e(TAG, "Error subscribing jaboston_test_2 channel", e);
                }
            }
        });
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
                case R.id.btnAddChannel:
                    addNewChannelLayout();
                    break;
            }
        }
    };

    /**
     * Calculate number of users by both channels
     */
    private void calculateNumberUsers() {
        if (!areChannelFieldsOk()) {
            Log.e("field error", "field is not ok");
            return;
        }

        String channel1 = channel1EditText.getText().toString();
        String channel2 = channel2EditText.getText().toString();

        createQuery().countInBackground(new CountCallback() {
            @Override
            public void done(int i, ParseException e) {
                int sizeOfList = i;
                Log.e("list size", "The size of the list is: " + sizeOfList);
                tvNumberOfUsers.setText(sizeOfList + "");

                if (e == null) {
                    Log.v(TAG, "Push send correctly");

                } else {
                    Log.e(TAG, "Error sending push");
                }
            }
        });

        // TODO Calculate the number of users
        // The follow code give parse exception because the table
        // _Installation cannot be accessed from client code.
        try {
            tvNumberOfUsers.setText(createQuery().count() + "");
        } catch (ParseException parseException) {
            Log.e(TAG, "Error finding number of users", parseException);
        }
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
        if (TextUtils.isEmpty(channel1) && !checkboxChannel1.isChecked()) {
            areChannelFieldsOk = false;
            channel1EditText.setError(getString(R.string.error_empty_field));
        }

        // Check channel2
        String channel2 = channel2EditText.getText().toString();
        if (TextUtils.isEmpty(channel2) && !checkboxChannel2.isChecked()) {
            areChannelFieldsOk = false;
            channel2EditText.setError(getString(R.string.error_empty_field));
        }

        return areChannelFieldsOk;
    }


    /**
     * Inflate a new channel and add it to the ChannelArrayList
     */
    private void addNewChannelLayout() {
        final LinearLayout canvas = (LinearLayout)this.findViewById(R.id.channel_holder);
        final View cv = inflater.inflate(R.layout.channel_element, canvas, false);

        // Fix the hint on the channel
        EditText editText = (EditText) cv.findViewById(R.id.channel_edit_text);
        editText.setHint("Channel " + ((int)channelLayoutArrayList.size()+3));

        // add the channel layour to the canvas
        canvas.addView(cv);

        // store to channel layout to the array for reference later.
        channelLayoutArrayList.add(cv);
    }

    /**
     * Send message to the users
     */
    private void sendMessage() {
        /**
         * Check fields are filled in correctly
         */

        if (!areChannelFieldsOk()) {
            Toast.makeText(this, "Must fill in channels", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isMessageFieldOk()) {
            Toast.makeText(this, "Must fill in message field", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!areDateFieldsOkay()){
            Toast.makeText(this, "Must fill in date field", Toast.LENGTH_SHORT).show();
            return;
        }

        final Context context = this.getApplicationContext();

        ParseQuery parsePushQuery = createQuery();
        ParsePush parsePush = new ParsePush();
        parsePush.setQuery(parsePushQuery);
        String message = messageEditText.getText().toString();
        parsePush.setMessage(message);
        parsePush.sendInBackground(new SendCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.v(TAG, "Push send correctly");
                    Toast.makeText(context, "Push sent correctly! yeah!", Toast.LENGTH_LONG).show();
                } else {
                    Log.e(TAG, "Error sending push: ", e);
                }
            }
        });


    }

    // when the user selects a date, set the furthest date back to query.
    private void setDateToQuery(){
        mDay = datePickerActiveSince.getDayOfMonth();
        mYear = datePickerActiveSince.getYear();
        mMonth = datePickerActiveSince.getMonth();
    }

    // when the user selects a date, set the furthest date back to query.
    private void setTimeToQuery(){
        mMinutes = timePickerLastActive.getCurrentMinute();
        mHours = timePickerLastActive.getCurrentHour();
    }

    private boolean areDateFieldsOkay() {

        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        int day = c.get(Calendar.DAY_OF_MONTH);

        setDateToQuery();
        setTimeToQuery();

        if (mDay <= day)
            if (mMonth <= month)
                if (mYear <= year)
                    return true;
        return false;
    }

    // create our full query body.
    private ParseQuery createQuery(){
        String channel1 = channel1EditText.getText().toString();
        String channel2 = channel2EditText.getText().toString();

        ParseQuery parsePushQuery = ParseInstallation.getQuery();

        List<String> channelsList = new ArrayList<String>();
        channelsList.add(channel1);
        channelsList.add(channel2);

        for(View v : channelLayoutArrayList){
            EditText editText = (EditText) v.findViewById(R.id.channel_edit_text);
            CheckBox checkBox = (CheckBox) v.findViewById(R.id.channel_checkbox);

            if(!checkBox.isChecked() && !editText.getText().toString().isEmpty())
                channelsList.add(editText.getText().toString());
        }

        Log.e("CHANNELS", "Channels List: " + channelsList);

        parsePushQuery = parsePushQuery.whereContainsAll("channels", channelsList);

        // time now
        long timeNow = Calendar.getInstance().getTimeInMillis();

        // date and time selected
        Calendar cal = Calendar.getInstance();
        cal.set(mYear, mMonth, mDay, mHours, mMinutes, 0);
        Date d = cal.getTime();
        parsePushQuery.whereGreaterThanOrEqualTo("updatedAt", d);

        return parsePushQuery;
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
