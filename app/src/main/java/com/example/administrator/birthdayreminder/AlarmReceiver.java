package com.example.administrator.birthdayreminder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.io.InputStream;

public class AlarmReceiver extends BroadcastReceiver {
    private Uri mySound;
    private String birthdayName;
    private String birthdayMessage;
    private String birthdayContact;

    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
// Sets an ID for the notification, so it can be updated

        birthdayContact = intent.getStringExtra("birthdayContact");
        birthdayMessage = intent.getStringExtra("birthdayMessage");
        birthdayName = intent.getStringExtra("birthdayName");
        //Toast.makeText(context,"MyMessage"+intent.getStringExtra("MyMessage"),Toast.LENGTH_LONG).show();
        NotifyMe(context);
    }

    public void NotifyMe(Context context) {

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, Home_Activity.class), 0);

        //calling
        PendingIntent intentCall = PendingIntent.getActivity(context, 0,
                new Intent(Intent.ACTION_CALL, Uri.parse("tel:+91" + birthdayContact)), 0);

        //send sms with text
        //Uri uri = Uri.parse("smsto:" + birthdayContact);
        Intent myIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+birthdayContact));
        myIntent.putExtra("sms_body", birthdayMessage);
        //myIntent.setType("vnd.android-dir/mms-sms");

        PendingIntent intentSMS = PendingIntent.getActivity(context, 0, myIntent, 0);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
       // shareIntent.setAction(Intent.ACTION_SEND);


        Uri uri = Uri.parse("smsto:" + birthdayContact);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setPackage("com.whatsapp");
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, birthdayMessage);
        //shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        PendingIntent watsapp = PendingIntent.getActivity(context,0,shareIntent,0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.app_icon)
                        .setContentTitle("Birthday Reminder")
                        .setContentText("Hey " + birthdayName + " has a Birthday today..!!!")
                        .setSubText("send Birthday Wishes!!")
                        .addAction(R.mipmap.icon_call, "CALL", intentCall)
                        .addAction(R.mipmap.message_icon, "SMS", watsapp);

        mBuilder.setContentIntent(contentIntent);
        //mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mySound = Uri.parse("android.resource://com.example.administrator.birthdayreminder/" + R.raw.notification_sound1);
        mBuilder.setAutoCancel(true);
        mBuilder.setSound(mySound);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }
}
