package com.example.administrator.birthdayreminder;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 12/1/2015.
 */
public class ReminderDatabase {

    public static final String ID = "_id";
    public static final String intentID = "_intentid";
    public static final String NAME = "_name";
    public static final String CONTACT = "_contact";
    public static final String MESSAGE = "_message";
    public static final String DATE = "_date";
    public static final String TIME = "_time";
    public static final String DAYS = "_days";
    public static final String TABLE_NAME = "_myreminder";
    public static final String DATABASE_NAME = "_remindme4";
    public static final String ALL[] = new String[]{ID, NAME, CONTACT, MESSAGE, DATE, TIME, intentID};
    public static final int DB_VER = 1;

    ReminderHelper reminderHelper;
    SQLiteDatabase db;

    public String CREATE_TABLE = "CREATE TABLE " +TABLE_NAME+" ( "+ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+NAME +" TEXT, "+CONTACT +" TEXT NOT NULL, "
            +MESSAGE +" TEXT, "+ DATE+" DATE, "+TIME+" DATE, "+intentID+" TEXT)";


    public class ReminderHelper extends SQLiteOpenHelper{

        public ReminderHelper(Context context) {
            super(context, DATABASE_NAME, null, DB_VER);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);
            Log.w("Table", "created");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if( newVersion > oldVersion){
                db.execSQL("DROP table "+TABLE_NAME);
                onCreate(db);
            }
        }
    }




    public  ReminderDatabase(Context context){
        reminderHelper = new ReminderHelper(context);
    }

    public void openDB(){
        db = reminderHelper.getWritableDatabase();
    }

    public void closeDB(){
        db.close();
        db = null;
    }

    public void insertInto(String name, String contact, String msg, String date, String time, String intID){

        ContentValues contentValues = new ContentValues();

        contentValues.put(NAME,name);
        contentValues.put(CONTACT,contact);
        contentValues.put(MESSAGE,msg);
        contentValues.put(DATE, date);
        contentValues.put(TIME, time);
        contentValues.put(intentID, intID);
        db.insert(TABLE_NAME,null,contentValues);
    }


    // Handling Database
    public Cursor selectAll(){

        Cursor cursor = db.query(TABLE_NAME, ALL, null, null, null, null, DATE);
        return cursor;
    }

    public Cursor selectId(long id){
        String [] args = new String[]{Long.toString(id)};
        Cursor cursor = db.query(TABLE_NAME, ALL, ID+"=?", args, null, null, null);
        return cursor;
    }

    public void deleteAll(){
        String str="";
        String query="DROP TABLE "+TABLE_NAME;
        db.rawQuery(query,null);
    }

    public void delete(long id){
        String [] args = new String[]{Long.toString(id)};
        db.delete(TABLE_NAME, ID + "=?", args);
    }


    public void update(long id, String date){

        ContentValues contentValues = new ContentValues();
        Long id2= id;
        contentValues.put(ID,id2);
        contentValues.put(DATE, date);
        String args[] = new String[]{id2.toString()};
        db.update(TABLE_NAME, contentValues, ID + "=?", args);
    }


    public void updateAll(long id,String name, String contact, String msg, String date, String time, String iID, String intID){
        ContentValues contentValues = new ContentValues();

        contentValues.put(NAME,name);
        contentValues.put(CONTACT,contact);
        contentValues.put(MESSAGE,msg);
        contentValues.put(DATE, date);
        contentValues.put(TIME, time);
        contentValues.put(intentID, iID);

        String args[] = new String[]{intID.toString()};
         db.update(TABLE_NAME, contentValues, intentID + "=?", args);
        db.rawQuery("commit",null);
    }


    //Calculate Date Diff

    public static String getDateDiff(Date currentDate, Date nextDate){
        Log.w("Current:", ""+currentDate);
        Log.w("Date:", "" + nextDate);
        long diff  = nextDate.getTime()-currentDate.getTime();
        Long difference = diff / 86400000;
        Log.w("Diff:", "" + diff);
        System.out.println("Current Day => " + difference);

        return difference.toString();
    }

}
