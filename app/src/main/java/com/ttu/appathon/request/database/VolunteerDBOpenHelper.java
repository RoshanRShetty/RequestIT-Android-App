package com.ttu.appathon.request.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.InputStream;

/**
 * Created by Pareshan on 11/16/2015.
 */
public class VolunteerDBOpenHelper extends SQLiteOpenHelper {

    // Initializing DB name and version constants
    private static final String LOGTAG = "Volunteer";
    private static final String DATABASE_NAME = "volunteer.db";
    private static final int DATABASE_VERSION = 2;
    private Context context;

    private InputStream inputStream;



// Initializing table column name constants

    static final String TABLE_NAME_VOLUNTEER = "volunteer";
    static final String VOLUNTEER_ID = "volunteer_id";
    static final String VOLUNTEER_NAME = "volunteer_name";
    static final String VOLUNTEER_EMAIL = "volunteer_email";
    static final String  VOLUNTEER_TELENO = "volunteer_teleno";
    static final String  VOLUNTEER_CITY = "volunteer_city";
    static final String  DATE_MODIFIED = "date_modified";
    static final String NOTES = "notes";
    static final String  VOLUNTEER_COUNTRY = "volunteer_country";

    private static final String TABLE_CREATE =
            " CREATE TABLE " + TABLE_NAME_VOLUNTEER + "("
                    + VOLUNTEER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + VOLUNTEER_NAME + " TEXT, "
                    + VOLUNTEER_EMAIL + " TEXT, "
                    + VOLUNTEER_TELENO + " TEXT, "
                    + VOLUNTEER_CITY + " TEXT, "
                    + VOLUNTEER_COUNTRY + " TEXT, "
                    + DATE_MODIFIED + " TEXT, "
                    + NOTES + " TEXT "
                    + ")";


    public VolunteerDBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.beginTransaction();
        try {
            db.execSQL(TABLE_CREATE);
            Log.i(LOGTAG, "Table created successfully" + TABLE_NAME_VOLUNTEER);

        } catch(SQLiteException sqlExcep) {

            sqlExcep.printStackTrace();
            Log.i(LOGTAG,sqlExcep.toString());
            db.endTransaction();

        }
        db.setTransactionSuccessful();
        db.endTransaction();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.beginTransaction();
        try {

            db.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME_VOLUNTEER);
            Log.i(LOGTAG, "Table Dropped : " + TABLE_NAME_VOLUNTEER);
            db.execSQL("DELETE FROM sqlite_sequence WHERE name = 'volunteer' ");
        } catch(SQLException sqlExcep){
            sqlExcep.printStackTrace();
            Log.i(LOGTAG,sqlExcep.toString());
            db.endTransaction();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        onCreate(db);

    }
} // end of class
