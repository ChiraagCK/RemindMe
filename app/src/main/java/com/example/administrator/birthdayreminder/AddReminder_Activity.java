package com.example.administrator.birthdayreminder;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.app.FragmentManager;

import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TableLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.ResourceBundle;

public class AddReminder_Activity extends Fragment implements View.OnClickListener {


    AutoCompleteTextView message;
    Button save;
    EditText name;
    EditText contact;
    EditText code;
    EditText date, time;
    ImageView fetch_contact;
    ImageView fetch_date;
    ImageView fetch_time;
    int Day, Month, Year;
    int Minute, Hour;
    int flag = 0;
    public static int myFragFlag = 0;
    public static int myFlag = 0;

    ArrayAdapter<String> arrayAdapter;
    String[] defaultMsgs={"Happy Birthday..!!!:D","Many Many Happy Returns of The Day..:)","Happy Birthday Dear:):*",
            "It’s your birthday and you’re still just as"+"\n"+"beautiful, genuine and kind as the day I met you."+"\n"+"Wishing you a Happy Birthday with gratitude.",
            "Here’s to hoping all of your birthday wishes"+"\n"+"will come true once you blow out those candles."+"\n"+"If anyone deserves all the happiness in the"+"\n"+"world, it’s you. Thanks for always being there for me.Happy Birthday..!!"};


    View view1;

    public  static  final  String DEFAULT = " ";

    public  static  final  int REQ_CODE = 100;
    public  static  final int RESULT_OK = -1;
    public  static  final int RESULT_CANCELLED = 0;
    private ReminderDatabase reminderDatabase;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        reminderDatabase = new ReminderDatabase(view.getContext());
        reminderDatabase.openDB();

        message = (AutoCompleteTextView) view.findViewById(R.id.message);
        arrayAdapter = new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_dropdown_item_1line,defaultMsgs);
        message.setAdapter(arrayAdapter);

        fetch_contact = (ImageView) view.findViewById(R.id.fetch_contact);
        fetch_date = (ImageView) view.findViewById(R.id.fetch_date);
        fetch_time = (ImageView) view.findViewById(R.id.fetch_time);
        save = (Button) view1.findViewById(R.id.savebtn);

        name = (EditText) view1.findViewById(R.id.name);
        contact = (EditText) view1.findViewById(R.id.contact);
        date = (EditText) view1.findViewById(R.id.date);
        time = (EditText) view1.findViewById(R.id.time);

        fetch_contact.setOnClickListener(this);
        fetch_date.setOnClickListener(this);
        fetch_time.setOnClickListener(this);
        save.setOnClickListener(this);
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.addreminder_activity, container, false);
        view1 = view;
        reminderDatabase = new ReminderDatabase(view.getContext());
        reminderDatabase.openDB();
        return view;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {

        int id;
        id = v.getId();
        if(id == R.id.fetch_contact){
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent,REQ_CODE);
        }
        else if(id == R.id.fetch_date){
            showDatePicker();
        }
        else if(id == R.id.fetch_time){
            showTimePicker();
        }
        else if(id == R.id.savebtn){

            reminderDatabase = new ReminderDatabase(view1.getContext());
            reminderDatabase.openDB();
/*
            reminderDatabase.db.rawQuery("DELETE FROM _myreminder2", null);

        String query="ALTER TABLE _myreminder2 ADD COLUMN _intentid NUMBER";
          final Cursor cursor = reminderDatabase.db.rawQuery(query,null);


            Log.w("ID",cursor.getColumnName(0));
            Log.w("ID",cursor.getColumnName(1));
            Log.w("ID",cursor.getColumnName(2));
            Log.w("ID",cursor.getColumnName(3));
            Log.w("ID",cursor.getColumnName(4));
            Log.w("ID",cursor.getColumnName(5));
            Log.w("ID",cursor.getColumnName(6));*/

            if(!name.getText().toString().trim().equals("")){
                if(!(contact.getText().toString().length()==0 ||
                        date.getText().toString().length()==0 ||
                        time.getText().toString().length()==0)){

                    Cursor cursor1 = reminderDatabase.selectAll();
                    while(cursor1.moveToNext()) {
                        Log.w("Name1", cursor1.getString(1));
                        Log.w("Name2", "" + name.getText());
                        if (cursor1.getString(1).equals(name.getText().toString()))
                            flag = 1;
                    }
                    final Integer _id = (int) System.currentTimeMillis();
                        if(flag == 0) {
                            reminderDatabase.insertInto(name.getText().toString(), contact.getText().toString(),
                                    message.getText().toString(), date.getText().toString(),
                                    time.getText().toString(), _id.toString());

                            Calendar myAlarmDate = Calendar.getInstance();
                            myAlarmDate.setTimeInMillis(System.currentTimeMillis());

                            myAlarmDate.set( Year, Month, Day, Hour, Minute, 0);

                            AlarmManager alarmManager = (AlarmManager) getActivity().getBaseContext().getSystemService(Context.ALARM_SERVICE);

                            Intent _myIntent = new Intent(getActivity().getBaseContext(), AlarmReceiver.class);
                            _myIntent.putExtra("birthdayMessage", message.getText().toString());
                            _myIntent.putExtra("birthdayContact", contact.getText().toString());
                            _myIntent.putExtra("birthdayName", name.getText().toString());

                            Log.w("********Time", "" + _id);
                            Toast.makeText(getActivity(),"Adding Intent"+_id,Toast.LENGTH_LONG).show();
                            PendingIntent _myPendingIntent = PendingIntent.getBroadcast(getActivity().getBaseContext(), _id, _myIntent, PendingIntent.FLAG_ONE_SHOT);
                            alarmManager.set(AlarmManager.RTC_WAKEUP, myAlarmDate.getTimeInMillis(), _myPendingIntent);

                            Toast.makeText(v.getContext(),"Reminder Added",Toast.LENGTH_LONG).show();

                        }
                    else{
                            Toast.makeText(v.getContext(),"Reminder Already Exist",Toast.LENGTH_LONG).show();
                            flag = 0;
                        }

                    //Cursor cursor = reminderDatabase.selectAll();
                    //cursor.moveToFirst();
                }
                else{
                    Toast.makeText(v.getContext(),"Please Enter Details",Toast.LENGTH_LONG).show();
                }
            }
            else{
                Toast.makeText(v.getContext(),"Please Enter Details",Toast.LENGTH_LONG).show();
            }


        }

    }
    private void showDatePicker() {
        DatePickFragment date = new DatePickFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(onDate);

        date.show(getFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener onDate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            Day = dayOfMonth;
            Month = monthOfYear;
            Year = year;

            date.setText(String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear+1)
                    + "-" + String.valueOf(year));
        }
    };

    private void showTimePicker() {
        TimePickFragment time = new TimePickFragment();
        /**
         * Set Up Current Date Into dialog
         */

        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("hour", calender.get(Calendar.HOUR_OF_DAY));
        args.putInt("minute", calender.get(Calendar.MINUTE));
        time.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        time.setCallBack(onTime);

        time.show(getFragmentManager(),"Time Picker");

        //time.show(getFragmentManager(), "Date Picker");
    }

    TimePickerDialog.OnTimeSetListener onTime = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Minute = minute;
            Hour = hourOfDay;
            time.setText(String.valueOf(hourOfDay) + ":" + String.valueOf(minute));
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri uri;
        String contact_id=null;
        String contacts[];
        if(requestCode == REQ_CODE){
            if(resultCode == RESULT_OK){
                uri = data.getData();
                Log.w("URI",""+uri);
               contact_id = uri.getLastPathSegment();
                contacts = new String[]{contact_id};
                ContentResolver contentResolver = getContext().getContentResolver();


                Cursor cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?", contacts, null);

                String phoneNumber;
                String contactName;

                int contactIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

                if(cursor != null && cursor.getCount()>0) {
                    cursor.moveToFirst();

                    do {
                        phoneNumber = cursor.getString(contactIndex);
                        contactName = cursor.getString(contactIndex);
                    } while (cursor.moveToNext());

                    contact = (EditText)view1.findViewById(R.id.contact);
                    code = (EditText)view1.findViewById(R.id.code);

                    phoneNumber = phoneNumber.replaceAll("\\s+","");
                    //contact.setText(phoneNumber);
                    Log.w("LENGTH", "" + phoneNumber.length());

                    if(phoneNumber.length()==14) {
                        contact.setText(phoneNumber.substring(phoneNumber.length() - 11, phoneNumber.length()));
                        code.setText(phoneNumber.substring(0, 3));
                    }
                    if(phoneNumber.length()==13) {
                        contact.setText(phoneNumber.substring(phoneNumber.length() - 10, phoneNumber.length()));
                        code.setText(phoneNumber.substring(0, 3));
                    }
                    else if(phoneNumber.length()==12){
                        contact.setText(phoneNumber.substring(phoneNumber.length() - 10, phoneNumber.length()));
                        code.setText(phoneNumber.substring(0, 2));
                    }
                    else if(phoneNumber.length()==11){
                        contact.setText(phoneNumber.substring(phoneNumber.length() - 10, phoneNumber.length()));
                        code.setText("+91");
                    }
                    else if(phoneNumber.length()==10){
                        contact.setText(phoneNumber.substring(phoneNumber.length() - 10, phoneNumber.length()));
                        code.setText("+91");
                    }

                    Log.w("Contact NO", "" + phoneNumber);
                }
            }
        }
    }
}

