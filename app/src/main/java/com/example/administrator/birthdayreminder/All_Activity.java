package com.example.administrator.birthdayreminder;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class All_Activity extends Fragment{

    public static  FragmentTransaction ft;
    ListView listAll;
    ArrayList<String> arrayList = new ArrayList<>();
    ReminderDatabase reminderDatabase;
    ArrayAdapter<String> arrayAdapter;
    SimpleCursorAdapter simpleCursorAdapter;
    View view1;
    View layout;
    AutoCompleteTextView message;
    ImageView fetch_contact;
    ImageView fetch_date;
    ImageView fetch_time;
    Button save;
    EditText name;
    EditText contact;
    EditText date, time;
    int Year, Month, Day, Minute, Hour;
    AlarmManager alarmManager;
    Calendar myAlarmDate;
    long myID;
    public static AddReminder_Activity addReminder_activity;

    public All_Activity(){
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        reminderDatabase = new ReminderDatabase(view1.getContext());
        reminderDatabase.openDB();

        listAll = (ListView) view.findViewById(R.id.all_list);
        reloadListAll();

        listAll.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(view.getContext(),"Hello",Toast.LENGTH_LONG).show();

                Cursor cursor = reminderDatabase.selectId(id);
                cursor.moveToFirst();
                myDialog mydialog = new myDialog();

                Bundle extra = new Bundle();
                extra.putLong("myID", id);

                Log.w("myID", "" + id);
                Log.w("myID from DB", "" + cursor.getString(0));
                extra.putString("myName", cursor.getString(1));
                extra.putString("myContact", cursor.getString(2));
                extra.putString("myMessage", cursor.getString(3));
                extra.putString("myDate", cursor.getString(4));
                extra.putString("myTime", cursor.getString(5));
                extra.putString("intentID", cursor.getString(6));
                Log.w("myintentID", "" + cursor.getString(6));

                mydialog.setArguments(extra);
                mydialog.show(getActivity().getFragmentManager(), "Edit");


        }
        });


        listAll.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                final long Index = id;
                //Toast.makeText(view1.getContext(),"Id:"+id,Toast.LENGTH_LONG).show();
                builder.setTitle("Delete");
                builder.setMessage("Are you sure, you want to delete?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Cursor cursor = reminderDatabase.selectId(Index);
                        cursor.moveToFirst();
                        Integer delIntent = Integer.parseInt(cursor.getString(6));

                        Toast.makeText(getActivity(),"Deleting Intent"+delIntent,Toast.LENGTH_LONG).show();

                        String str[] = cursor.getString(4).split("-");
                        String str1[] = cursor.getString(5).split(":");

                        Day = Integer.parseInt(str[0]);
                        Month = Integer.parseInt(str[1]);
                        Year = Integer.parseInt(str[2]);
                        Hour = Integer.parseInt(str1[0]);
                        Minute = Integer.parseInt(str1[1]);

                        myAlarmDate = Calendar.getInstance();
                        myAlarmDate.setTimeInMillis(System.currentTimeMillis());
                        myAlarmDate.set(Year, Month, Day, Hour, Minute, 0);

                        alarmManager = (AlarmManager) getActivity().getBaseContext().getSystemService(Context.ALARM_SERVICE);
                        Intent myIntent = new Intent(getActivity().getBaseContext(), AlarmReceiver.class);
                        PendingIntent myPendingIntent = PendingIntent.getBroadcast(getActivity().getBaseContext(), delIntent , myIntent, PendingIntent.FLAG_ONE_SHOT);
                       // alarmManager.set(AlarmManager.RTC_WAKEUP, myAlarmDate.getTimeInMillis(), myPendingIntent);

                        alarmManager.cancel(myPendingIntent);
                        reminderDatabase.delete(Index);

                        reloadListAll();
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });


                builder.show();

                return false;
            }
        });
    }

    public void reloadListAll(){
        Cursor cursor = reminderDatabase.selectAll();
        cursor.moveToFirst();

        String src[] = new String[]{ReminderDatabase.NAME, ReminderDatabase.DATE};
        int dest[] = new int[]{R.id.myname, R.id.mydate};

        simpleCursorAdapter  = new SimpleCursorAdapter(view1.getContext(),R.layout.my_layout,
                cursor,src,dest,1);
        listAll.setAdapter(simpleCursorAdapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ReminderDatabase reminderDatabase = new ReminderDatabase(getActivity());
        reminderDatabase.openDB();
        View view = inflater.inflate(R.layout.all_activity, container, false);
        layout = inflater.inflate(R.layout.addreminder_activity, container,false);
        name = (EditText) layout.findViewById(R.id.name);
        contact = (EditText) layout.findViewById(R.id.contact);
        date = (EditText) layout.findViewById(R.id.date);
        time = (EditText) layout.findViewById(R.id.time);
        message = (AutoCompleteTextView) layout.findViewById(R.id.message);
        save = (Button) layout.findViewById(R.id.savebtn);
        fetch_contact = (ImageView) layout.findViewById(R.id.fetch_contact);
        fetch_date = (ImageView) layout.findViewById(R.id.fetch_date);
        fetch_time = (ImageView) layout.findViewById(R.id.fetch_time);
        save.setVisibility(View.GONE);
        fetch_contact.setVisibility(View.GONE);
        fetch_date.setVisibility(View.GONE);
        fetch_time.setVisibility(View.GONE);


        view1 = view;
        return view;
    }


}
