package com.example.administrator.birthdayreminder;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Administrator on 12/3/2015.
 */
public class myDialog extends DialogFragment {

    AutoCompleteTextView message;
    ImageView fetch_contact;
    ImageView fetch_date;
    ImageView fetch_time;
    Button save;
    EditText name;
    EditText contact;
    EditText date, time;
    ReminderDatabase reminderDatabase;
    ListView listAll;
    All_Activity all_activity;
    AlarmManager alarmManager, alarmManager2;
    Calendar myAlarmDate;

    int Year, Month, Day, Minute, Hour;
     SimpleCursorAdapter simpleCursorAdapter;


    public void reloadListAll(){

        Toast.makeText(getActivity(), "Reloaded", Toast.LENGTH_LONG).show();
        Cursor cursor = reminderDatabase.selectAll();
        cursor.moveToFirst();

        String src[] = new String[]{ReminderDatabase.NAME, ReminderDatabase.DATE};
        int dest[] = new int[]{R.id.myname, R.id.mydate};

        //Changed Context 5/12/2015
        simpleCursorAdapter  = new SimpleCursorAdapter(getActivity().getBaseContext(),R.layout.my_layout,
                cursor,src,dest,1);
        listAll.setAdapter(simpleCursorAdapter);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.addreminder_activity, null);
        View view1 = inflater.inflate(R.layout.all_activity,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);

        reminderDatabase = new ReminderDatabase(getActivity());
        reminderDatabase.openDB();

        all_activity = new All_Activity();

        final long myID = getArguments().getLong("myID");
        Log.w("***ID",""+myID);

        name = (EditText) view.findViewById(R.id.name);
        contact = (EditText) view.findViewById(R.id.contact);
        date = (EditText) view.findViewById(R.id.date);
        time = (EditText) view.findViewById(R.id.time);
        message = (AutoCompleteTextView) view.findViewById(R.id.message);
        save = (Button) view.findViewById(R.id.savebtn);
        fetch_contact = (ImageView) view.findViewById(R.id.fetch_contact);
        fetch_date = (ImageView) view.findViewById(R.id.fetch_date);
        fetch_time = (ImageView) view.findViewById(R.id.fetch_time);
        listAll = (ListView) view1.findViewById(R.id.all_list);

        save.setVisibility(View.GONE);
        fetch_contact.setVisibility(View.GONE);
        fetch_date.setVisibility(View.GONE);
        fetch_time.setVisibility(View.GONE);

        name.setText(getArguments().getString("myName"));
        contact.setText(getArguments().getString("myContact"));
        message.setText(getArguments().getString("myMessage"));
        date.setText(getArguments().getString("myDate"));
        time.setText(getArguments().getString("myTime"));

        final Integer delIntent = Integer.parseInt(getArguments().getString("intentID"));

        Log.w("myintentID", "" + getArguments().getString("intentID"));

        String str[] = getArguments().getString("myDate").split("-");
        String str1[] = getArguments().getString("myTime").split(":");

        Day = Integer.parseInt(str[0]);
        Month = Integer.parseInt(str[1]);
        Year = Integer.parseInt(str[2]);
        Hour = Integer.parseInt(str1[0]);
        Minute = Integer.parseInt(str1[1]);

        myAlarmDate = Calendar.getInstance();
        myAlarmDate.setTimeInMillis(System.currentTimeMillis());

        /*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd HH:mm");
        Log.w("NEW DATE", "" + sdf.format(new Date()));
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Log.w("NEW TIME", "" + sdf.format(new Date()));*/
        //String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

        myAlarmDate.set(Year, Month, Day, Hour, Minute, 0);
        System.out.println("DD-" + Day + "MM-" + Month + "YY-" + Year + "HH-" + Hour + "Mm-" + Minute);



        final Integer _id = (int) System.currentTimeMillis();

        Log.w("Time to change",""+_id.toString());

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (time.getText().toString().contains(":")) {
                    if (!name.getText().toString().trim().equals("")) {
                        if (!(contact.getText().toString().length() == 0 ||
                                message.getText().toString().length() == 0 || date.getText().toString().length() == 0 ||
                                time.getText().toString().length() == 0)) {

                            alarmManager = (AlarmManager) getActivity().getBaseContext().getSystemService(Context.ALARM_SERVICE);
                            Intent myIntent = new Intent(getActivity().getBaseContext(), AlarmReceiver.class);
                            PendingIntent myPendingIntent = PendingIntent.getBroadcast(getActivity().getBaseContext(), delIntent , myIntent, PendingIntent.FLAG_ONE_SHOT);
                            //alarmManager.cancel(PendingIntent.getService(getActivity(),delIntent,myIntent,0));
                            alarmManager.cancel(myPendingIntent);


                            reminderDatabase.updateAll(myID, name.getText().toString(),
                                    contact.getText().toString(), message.getText().toString(),
                                    date.getText().toString(), time.getText().toString(),_id.toString(), delIntent.toString());

                            String str[] = date.getText().toString().split("-");
                            String str1[] = time.getText().toString().split(":");

                            Day = Integer.parseInt(str[0]);
                            Month = Integer.parseInt(str[1]);
                            Year = Integer.parseInt(str[2]);
                            Hour = Integer.parseInt(str1[0]);
                            Minute = Integer.parseInt(str1[1]);

                            System.out.println("DD-"+Day+"MM-"+Month+"YY-"+Year+"HH-"+Hour+"Mm-"+Minute);
                            myAlarmDate = Calendar.getInstance();
                            myAlarmDate.setTimeInMillis(System.currentTimeMillis());

                            myAlarmDate.set(Year, Month, Day, Hour, Minute, 0);
                            alarmManager = (AlarmManager) getActivity().getBaseContext().getSystemService(Context.ALARM_SERVICE);
                            myIntent = new Intent(getActivity().getBaseContext(), AlarmReceiver.class);
                            myIntent.putExtra("birthdayMessage", message.getText().toString());
                            myIntent.putExtra("birthdayContact", contact.getText().toString());
                            myIntent.putExtra("birthdayName", name.getText().toString());
                            myPendingIntent = PendingIntent.getBroadcast(getActivity().getBaseContext(), _id, myIntent, PendingIntent.FLAG_ONE_SHOT);
                            alarmManager.set(AlarmManager.RTC_WAKEUP, myAlarmDate.getTimeInMillis(), myPendingIntent);

                            Toast.makeText(getActivity(), "New Alarm Set", Toast.LENGTH_LONG).show();
                            Toast.makeText(getActivity(), "Data Updated Successfully", Toast.LENGTH_LONG).show();
                            dialog.cancel();
                            reloadListAll();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Invalid Data", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Invalid Time Format", Toast.LENGTH_LONG).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();

        dialog.setTitle("Edit Details");
        return dialog;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }

}
