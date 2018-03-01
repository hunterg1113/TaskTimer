package com.example.huntergreer.tasktimer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Hunter on 2/23/2018.
 *
 * Basic database class for the application.
 *
 * The only class that should use this is {@link AppProvider}
 */

class AppDatabase extends SQLiteOpenHelper {
    private static final String TAG = "AppDatabase";

    private static final String DATABASE_NAME = "TaskTimer.db";
    private static final int DATABASE_VERSION = 1;

    private static AppDatabase instance = null;

    private AppDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Get an instance of the app's singleton helper object
     *
     * @param context the content provider's context
     * @return a SQLite database helper object
     */
    static AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = new AppDatabase(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sSQL;
        sSQL = "CREATE TABLE " + TasksContract.TABLE_NAME + " ("
                + TasksContract.Columns._ID + " INTEGER PRIMARY KEY NOT NULL, "
                + TasksContract.Columns.TASKS_NAME + " TEXT NOT NULL, "
                + TasksContract.Columns.TASKS_DESCRIPTION + " TEXT, "
                + TasksContract.Columns.TASKS_SORTORDER + " INTEGER);";
        db.execSQL(sSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                break;
            default:
                throw new IllegalStateException("onUpgrade() with unknown newVersion: " + newVersion);
        }

    }
}
