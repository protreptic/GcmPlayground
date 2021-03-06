package name.peterbukhal.android.gcmplayground;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static class Message implements Parcelable {

        private String time;
        private String message;

        protected Message(Parcel in) {
            time = in.readString();
            message = in.readString();
        }

        public static final Creator<Message> CREATOR = new Creator<Message>() {

            @Override
            public Message createFromParcel(Parcel in) {
                return new Message(in);
            }

            @Override
            public Message[] newArray(int size) {
                return new Message[size];
            }
        };

        public Message(String time, String message) {
            this.time = time;
            this.message = message;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(time);
            dest.writeString(message);
        }

        public String getTime() {
            return time;
        }

        public String getMessage() {
            return message;
        }
    }

    private class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(MyGcmListenerService.ACTION_NEW_MESSAGE)) {
                Message message = intent.getParcelableExtra("message");

                ContentValues contentValues = new ContentValues();
                contentValues.put("time", message.getTime());
                contentValues.put("message", message.getMessage());

                SQLiteDatabase database = messagesDatabase.getWritableDatabase();
                database.insert("messages", null, contentValues);
                database.close();

                updateMessages();
            }
        }
    }

    private void updateMessages() {
        List<Message> messageList = new ArrayList<>();

        SQLiteDatabase database = messagesDatabase.getWritableDatabase();
        Cursor cursor = database.query("messages", null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            messageList.add(
                    new Message(
                            cursor.getString(cursor.getColumnIndex("time")),
                            cursor.getString(cursor.getColumnIndex("message"))));
        }

        cursor.close();
        database.close();

        messages.clear();
        messages.addAll(messageList);
        messageAdapter.notifyDataSetChanged();
    }

    private void clearMessages() {
        SQLiteDatabase database = messagesDatabase.getWritableDatabase();
        database.execSQL("delete from messages;");
        database.close();

        updateMessages();
    }

    private class RegistrationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(RegistrationIntentService.ACTION_REGISTRATION)) {
                getSharedPreferences(getPackageName(), MODE_PRIVATE)
                        .edit()
                        .putString(TAG_TOKEN, intent.getStringExtra(TAG_TOKEN))
                        .commit();
            }
        }
    }

    private class MessageAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return messages.size();
        }

        @Override
        public Message getItem(int position) {
            return messages.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MessageHolder messageHolder;

            if (convertView == null) {
                convertView = getLayoutInflater().inflate(android.R.layout.simple_list_item_2, parent, false);

                messageHolder = new MessageHolder();
                messageHolder.text1 = (TextView) convertView.findViewById(android.R.id.text1);
                messageHolder.text2 = (TextView) convertView.findViewById(android.R.id.text2);

                convertView.setTag(messageHolder);
            } else {
                messageHolder = (MessageHolder) convertView.getTag();
            }

            Message message = getItem(position);

            messageHolder.text1.setText(message.getMessage());
            messageHolder.text2.setText(message.getTime());

            return convertView;
        }

        private class MessageHolder {

            TextView text1;
            TextView text2;

        }

    }

    private ArrayList<Message> messages = new ArrayList<>();
    private MessagesDatabaseHelper messagesDatabase;
    private LocalBroadcastManager broadcastManager;
    private MessageReceiver messageReceiver = new MessageReceiver();
    private RegistrationReceiver registrationReceiver = new RegistrationReceiver();
    private MessageAdapter messageAdapter = new MessageAdapter();

    private String token;

    public static final String TAG_TOKEN = "tag_token";
    public static final String TAG_MESSAGES = "tag_messages";

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(TAG_TOKEN, token);
        outState.putParcelableArrayList(TAG_MESSAGES, messages);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        messagesDatabase = new MessagesDatabaseHelper(getApplicationContext());

        broadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
        broadcastManager.registerReceiver(registrationReceiver, new IntentFilter(RegistrationIntentService.ACTION_REGISTRATION));
        broadcastManager.registerReceiver(messageReceiver, new IntentFilter(MyGcmListenerService.ACTION_NEW_MESSAGE));

        if (savedInstanceState != null
                && savedInstanceState.containsKey(TAG_TOKEN)
                && savedInstanceState.containsKey(TAG_MESSAGES)) {
            token = savedInstanceState.getString(TAG_TOKEN);
            messages.addAll(savedInstanceState.<Message>getParcelableArrayList(TAG_MESSAGES));
        } else if (preferences.contains(TAG_TOKEN)) {
            token = preferences.getString(TAG_TOKEN, null);
            updateMessages();
        } else {
            Intent intent = new Intent(getApplicationContext(), RegistrationIntentService.class);
            startService(intent);
        }

        setContentView(R.layout.activity_main);

        ListView messages = (ListView) findViewById(R.id.messages);
        messages.setAdapter(messageAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        broadcastManager.unregisterReceiver(registrationReceiver);
        broadcastManager.unregisterReceiver(messageReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clearMessages: {
                clearMessages();

                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
