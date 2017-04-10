package com.ttu.appathon.request.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ttu.appathon.request.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * Created by Pareshan on 10/17/2015.
 */
public class EmergencyDBOpenHelper extends SQLiteOpenHelper {

    // Initializing DB name and version constants
    private static final String LOGTAG = "emergency";
    private static final String DATABASE_NAME = "emergency.db";
    private static final int DATABASE_VERSION = 8;
    private Context context;

    private InputStream inputStream;



// Initializing table column name constants

    static final String TABLE_NAME_EMERGENCY = "emergency";
    static final String COUNTRY_ID = "country_id";
    static final String COUNTRY_NAME = "country_name";
    static final String POLICE = "police_no";
    static final String AMBULANCE = "ambulance_no";
    static final String FIRE = "fireControl_no";
    static final String NOTES = "notes";

    private static final String TABLE_CREATE =
            " CREATE TABLE " + TABLE_NAME_EMERGENCY + "("
                    + COUNTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COUNTRY_NAME + " TEXT, "
                    + POLICE + " TEXT, "
                    + AMBULANCE + " TEXT, "
                    + FIRE + " TEXT, "
                    + NOTES + " TEXT "
                    + ")";

    public EmergencyDBOpenHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        inputStream = context.getResources().openRawResource(R.raw.ememrgencydata);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.beginTransaction();
        db.execSQL(TABLE_CREATE);
        Log.i(LOGTAG, "Table has been created " + TABLE_NAME_EMERGENCY);




        BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";

        String columns = EmergencyDBOpenHelper.COUNTRY_NAME + ","
                + EmergencyDBOpenHelper.POLICE + ","
                + EmergencyDBOpenHelper.AMBULANCE + ","
                + EmergencyDBOpenHelper.FIRE + ","
                + EmergencyDBOpenHelper.NOTES;

        String str1 = "INSERT INTO " + EmergencyDBOpenHelper.TABLE_NAME_EMERGENCY + " (" + columns + ") values(";
        String str2 = ");";


        try {
            while ((line = buffer.readLine()) != null) {
                StringBuilder sb = new StringBuilder(str1);
                StringBuilder temp = new StringBuilder("");
                String[] str = line.split(",");
                sb.append("'" + str[0] + "',");
                sb.append("'" + str[1] + "',");
                sb.append("'" + str[2] + "',");
                sb.append("'" + str[3] + "',");
                for(int i = 4; i<str.length; i++)
                {
                    temp.append(str[i]);
                }
                if (temp.equals(" "))
                {

                    sb.append("null");
                }
                else{
                    sb.append("'" + temp + " " + "'");
                }

                Log.i(LOGTAG, String.valueOf(temp));

                sb.append(str2);
                Log.i(LOGTAG,sb.toString());
                db.execSQL(sb.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
            db.endTransaction();
    }

        db.setTransactionSuccessful();
        db.endTransaction();




    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME_EMERGENCY);
        Log.i(LOGTAG, "Table Dropped : " + TABLE_NAME_EMERGENCY);
        db.execSQL("DELETE FROM sqlite_sequence WHERE name = 'emergency' ");
        onCreate(db);

    }





}// end of class
