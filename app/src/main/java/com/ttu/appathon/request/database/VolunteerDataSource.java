package com.ttu.appathon.request.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Pareshan on 11/16/2015.
 */
public class VolunteerDataSource {

    private static final String[] allColumns = {

            "rowid _id",
            VolunteerDBOpenHelper.VOLUNTEER_ID,
            VolunteerDBOpenHelper.VOLUNTEER_NAME,
            VolunteerDBOpenHelper.VOLUNTEER_EMAIL,
            VolunteerDBOpenHelper.VOLUNTEER_TELENO,
            VolunteerDBOpenHelper.VOLUNTEER_CITY,
            VolunteerDBOpenHelper.DATE_MODIFIED,
            VolunteerDBOpenHelper.NOTES
    };

    private static final String LOGTAG = "VolunteerDataSource";
    private static final String clause = VolunteerDBOpenHelper.VOLUNTEER_CITY + " = ?" + " and " +
            VolunteerDBOpenHelper.VOLUNTEER_COUNTRY + " = ?";
    SQLiteOpenHelper dbhelper;
    SQLiteDatabase database;

    public VolunteerDataSource(Context context) {
        dbhelper = new VolunteerDBOpenHelper(context);
    }


    public void open() {
        Log.i(LOGTAG, " Database Opened:");
        database = dbhelper.getWritableDatabase();
    }

    public void close() {
        Log.i(LOGTAG, "Database Closed");
        dbhelper.close();
    }

    public Cursor getData(String city, String country) {

        open();
        Log.i(LOGTAG, city);
        Log.i(LOGTAG, country);


        Cursor cursor = database.query(VolunteerDBOpenHelper.TABLE_NAME_VOLUNTEER,  //table name
                allColumns, //columns
                clause, //where condition
                new String[]{city, country}, //where coditions[]
                null, //group by
                null, // having
                null); // order by


        Log.i(LOGTAG, "Cursor Executed, Rows fetched " + cursor.getCount());

        return cursor;

    }


}// end of class
