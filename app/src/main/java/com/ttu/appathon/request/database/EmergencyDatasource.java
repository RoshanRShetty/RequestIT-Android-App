package com.ttu.appathon.request.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ttu.appathon.request.models.Emergency;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

//import example.registration.Registration;

/**
 * Created by Pareshan on 10/17/2015.
 */
public class EmergencyDatasource {

    // this creates a well formed sql querry for retreving values
    private static final String[] allColums = {
            EmergencyDBOpenHelper.COUNTRY_ID,
            EmergencyDBOpenHelper.COUNTRY_NAME,
            EmergencyDBOpenHelper.POLICE,
            EmergencyDBOpenHelper.AMBULANCE,
            EmergencyDBOpenHelper.FIRE,
            EmergencyDBOpenHelper.NOTES
            };
    private static final String clause = EmergencyDBOpenHelper.COUNTRY_NAME + " = ?";



    private static final String LOGTAG = "EmergencyDataSource";
    SQLiteOpenHelper dbhelper;
    SQLiteDatabase database;

    public EmergencyDatasource(Context context){

        dbhelper = new EmergencyDBOpenHelper(context);


    }


    public void open(){
        Log.i(LOGTAG, " Database Opened:");
        database = dbhelper.getWritableDatabase();
    }

    public void close(){
        Log.i(LOGTAG, "Database Closed");
        dbhelper.close();
    }



    public List<Emergency> getData(String country){

        List<Emergency> emergencyList = new ArrayList<Emergency>();
        open();
        Log.i(LOGTAG,country);

                Cursor cursor = database.query(EmergencyDBOpenHelper.TABLE_NAME_EMERGENCY,  //table name
                    allColums, //columns
                    clause, //where condition
                    new String[]{country}, //where coditions[]
                    null, //group by
                    null, // having
                    null); // order by


        Log.i(LOGTAG, "Cursor Executed, Rows fetched " + cursor.getCount());

        if (cursor.getCount() > 0){

            cursor.moveToFirst();

            while(cursor.isAfterLast() == false){

                Emergency emergencyObject = new Emergency();
                emergencyObject.setCountryID(cursor.getLong(cursor.getColumnIndex(EmergencyDBOpenHelper.COUNTRY_ID)));
                emergencyObject.setCountryName(cursor.getString(cursor.getColumnIndex(EmergencyDBOpenHelper.COUNTRY_NAME)));
                emergencyObject.setPoliceNo(cursor.getString(cursor.getColumnIndex(EmergencyDBOpenHelper.POLICE)));
                emergencyObject.setAmbulanceNo(cursor.getString(cursor.getColumnIndex(EmergencyDBOpenHelper.AMBULANCE)));
                emergencyObject.setFireControlNo(cursor.getString(cursor.getColumnIndex(EmergencyDBOpenHelper.FIRE)));
                emergencyObject.setNotes(cursor.getString(cursor.getColumnIndex(EmergencyDBOpenHelper.NOTES)));


                emergencyList.add(emergencyObject);

                Log.i(LOGTAG, "one row fetched " + emergencyObject.getCountryID() + " " + emergencyObject.getCountryName() + " " + emergencyObject.getPoliceNo() +
                " " + emergencyObject.getAmbulanceNo() + " " + emergencyObject.getFireControlNo() +
                " " + emergencyObject.getNotes());
                cursor.moveToNext();
            }
        }
        //Log.i(LOGTAG,"one row fetched " + emergencyObject.getCountryID() + " " + emergencyObject.getCountryName() );
        return emergencyList;

    }

    public void updateDB(InputStream inputStream)  {

        //database.execSQL("DELETE FROM SCHEDULE");
        //database.execSQL("DELETE FROM sqlite_sequence WHERE name = 'schedule' ");

        BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream));

        String line = "";
/*
        String columns = EmergencyDBOpenHelper.CLASS_TIME + ","
                + EmergencyDBOpenHelper.STUDENT_NAME + ","
                + EmergencyDBOpenHelper.COURSE_NAME + ","
                + EmergencyDBOpenHelper.COURSE_NUMBER + ","
                + EmergencyDBOpenHelper.DAYS + ","
                + EmergencyDBOpenHelper.BUILDING + ","
                + EmergencyDBOpenHelper.ROOM_NO;

        String str1 = "INSERT INTO " + EmergencyDBOpenHelper.TABLE_NAME_EMERGENCY + " (" + columns + ") values(";
        String str2 = ");";

        database.beginTransaction();
        try {
            while ((line = buffer.readLine()) != null) {
                StringBuilder sb = new StringBuilder(str1);
                String[] str = line.split(",");
                String temp = str[1] + "," + str[2];
                temp = temp.substring(1,(temp.length() - 1));

                Log.i(LOGTAG, temp);

                sb.append("'" + str[0] + "',");
                sb.append(" \" "  + temp + "\",");
                sb.append("'" + str[3] + "',");
                sb.append("'" + str[4] + "',");
                sb.append("'" + str[5] + "',");
                sb.append("'" + str[6] + "',");
                sb.append("'" + str[7] + "'");

                sb.append(str2);
                Log.i(LOGTAG,sb.toString());
                database.execSQL(sb.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            database.setTransactionSuccessful();
            database.endTransaction();

        }
*/




    }


}// end of class
