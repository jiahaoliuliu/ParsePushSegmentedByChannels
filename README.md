# ParsePushSegmentedByChannels

An Android app which sends push notification to devices subscribed to Parse by channels. The channels could be concatenated. i.e. If more than one channel are selected, only the people subscribed to both channels will receive the push notification.

To do so, there are two requirements:

1. From the Parse settings of the project, go to Push and enable client push notification
2. From the Client site, use the follow code:

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
                if (e == null) {
                    Log.v(TAG, "Push send correctly");
                } else {
                    Log.e(TAG, "Error sending push", e);
                }
            }
        });

Where channel1 is the name of the first channel and channel2 is the name of the second channel.

You can add as many channel as you want.
