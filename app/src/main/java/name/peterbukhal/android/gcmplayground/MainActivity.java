package name.peterbukhal.android.gcmplayground;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
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

    private List<Message> messages = new ArrayList<>();

    private class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(MyGcmListenerService.ACTION_NEW_MESSAGE)) {
                messages.add(intent.<Message>getParcelableExtra("message"));
                messageAdapter.notifyDataSetChanged();
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

    private MessageReceiver messageReceiver = new MessageReceiver();
    private MessageAdapter messageAdapter = new MessageAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ListView messages = (ListView) findViewById(R.id.messages);
        messages.setAdapter(messageAdapter);

        registerGcm();
        registerReceiver(messageReceiver, new IntentFilter(MyGcmListenerService.ACTION_NEW_MESSAGE));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(messageReceiver);
    }

    private void registerGcm() {
        Intent intent = new Intent(getApplicationContext(), RegistrationIntentService.class);
        startService(intent);
    }

}
