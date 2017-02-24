package com.example.administrator.birthdayreminder;

import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class Upcoming_Activity extends Fragment {

    public static AddReminder_Activity addReminder_activity;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<HashMap<String,String>> arrayList2= new ArrayList<>();
    SimpleAdapter simpleAdapter;
    ListView listUpcoming;
    View view1;
    private ReminderDatabase reminderDatabase;
    private SimpleCursorAdapter simpleCursorAdapter;
    ListView listAll;



    public Upcoming_Activity(){
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Toast.makeText(getContext(),"Hello",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

          view1 = view;

        listUpcoming = (ListView) view1.findViewById(R.id.upcoming_list);
        /*arrayList.add("One");
        arrayList.add("Two");
        arrayList.add("Three");

        arrayAdapter = new ArrayAdapter<String>(view1.getContext(),android.R.layout.simple_list_item_1,
                arrayList);
        listUpcoming.setAdapter(arrayAdapter);
        */

        Toast.makeText(getActivity(),"Coming Here",Toast.LENGTH_LONG).show();
        reminderDatabase = new ReminderDatabase(view1.getContext());
        reminderDatabase.openDB();
        reloadListAll();
    }

    public void reloadListAll(){

        Calendar c = Calendar.getInstance();
        Toast.makeText(getActivity(),"Reload List",Toast.LENGTH_LONG).show();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-M-yyyy");
        String formattedDate = df.format(c.getTime());

        Cursor cursor = reminderDatabase.selectAll();
        //cursor.moveToFirst();

        arrayList2.clear();

        String Diff;
        try {
            Date current = df.parse(formattedDate);

            while(cursor.moveToNext()){

                Diff = ReminderDatabase.getDateDiff(current, df.parse(cursor.getString(4)));

                if(Integer.parseInt(Diff)<30 && Integer.parseInt(Diff)>=0){
                    HashMap<String,String> field= new HashMap<>();
                    if(Integer.parseInt(Diff)==0||Integer.parseInt(Diff)==1){
                        String Diff2;
                       if(Integer.parseInt(Diff)==0)
                                Diff2 = "Today";
                        else
                                Diff2 = "Tomorrow";

                        field.put(ReminderDatabase.NAME,cursor.getString(1));
                        field.put(ReminderDatabase.DAYS, Diff2);
                    }
                    else {
                        field.put(ReminderDatabase.NAME, cursor.getString(1));
                        field.put(ReminderDatabase.DAYS, Diff);
                    }
                    arrayList2.add(field);
                }
                if(Integer.parseInt(Diff)<=-1){
                    Toast.makeText(getActivity(),"Expired",Toast.LENGTH_LONG).show();

                    String newDate[] = cursor.getString(4).split("-");
                    newDate[2] = String.valueOf((Integer.parseInt(newDate[2]) + 1));
                    String passDate = new String(newDate[0]+"-"+newDate[1]+"-"+newDate[2]);
                    reminderDatabase.update(Long.parseLong(cursor.getString(0)), passDate);
                }

            }

        } catch (ParseException e) {
            e.printStackTrace();
        }


        for (int i=0;i<arrayList2.size();i++){
            System.out.println("Item"+arrayList2.get(i));
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(view1.getContext(),arrayList2,R.layout.my_layout2,
                new String[]{ReminderDatabase.NAME, ReminderDatabase.DAYS},
                new int[]{R.id.myname,R.id.mydate});

        listUpcoming.setAdapter(simpleAdapter);
        /*
        String src[] = new String[]{ReminderDatabase.NAME, ReminderDatabase.DATE};
        int dest[] = new int[]{R.id.myname, R.id.mydate};

        simpleCursorAdapter  = new SimpleCursorAdapter(view1.getContext(),R.layout.my_layout,
                cursor,src,dest,1);
        listAll.setAdapter(simpleCursorAdapter);*/

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
              View view = inflater.inflate(R.layout.upcoming_activity, container, false);

        ReminderDatabase reminderDatabase =new ReminderDatabase(getActivity());
        reminderDatabase.openDB();
        view1 = view;


        return view;
    }

}
