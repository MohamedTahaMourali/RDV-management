package com.example.serveur2;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;


public class RdvDataBase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "rdv.db";
    private static final int DATABASE_VERSION = 2;
    private static final  String TABLE_NAME = "RendezVous"  ;
    private static final String COLUMN_DOCTOR_ID = "doctor_id";
    private static String COLUMN_PATIENT_ID = "patient_id";
    private static String COLUMN_DATE = "date";
    private static String COLUMN_TEMPS = "temps";

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_DOCTOR_ID + " INTEGER  ," +
                    COLUMN_PATIENT_ID + "INTEGER," +
                    COLUMN_DATE + "DATE, " +
                    COLUMN_TEMPS + "DATE," +
                    "PRIMARY KEY (" + COLUMN_DOCTOR_ID + ", " + COLUMN_PATIENT_ID + ","+COLUMN_DATE+ ","+ COLUMN_TEMPS + "))";




    public RdvDataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Code SQL pour créer les tables de la base de données
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    @RequiresApi(api = Build.VERSION_CODES.O)

    public LocalDateTime getLastDate() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT MAX(date) FROM RendezVous";
        Cursor cursor = db.rawQuery(query, null);
        LocalDateTime lastDate = null;
        if (cursor.moveToFirst()) {
            String lastDateString = cursor.getString(0);
            lastDate = LocalDateTime.parse(lastDateString);
        }
        cursor.close();
        return lastDate;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public LocalDateTime getLastTime() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT MAX(time) FROM RendezVous";
        Cursor cursor = db.rawQuery(query, null);
        LocalDateTime lastTime = null;
        if (cursor.moveToFirst()) {
            String lastDateString = cursor.getString(0);
            lastTime = LocalDateTime.parse(lastDateString);
        }
        cursor.close();
        return lastTime;
    }

    public boolean addRdv(String nomPatient, String dateRdv, String heureRdv) {
    return false ;
    }
}

