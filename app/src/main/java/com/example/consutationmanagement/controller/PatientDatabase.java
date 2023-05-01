package com.example.consutationmanagement.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.consutationmanagement.model.Patient;

public class PatientDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "patient.db";
    private static final int DATABASE_VERSION = 2;
    private static final  String TABLE_NAME = "Patient"  ;
    private static final String COLUMN_EMAIL = "email";

    private static String COLUMN_ID = "ID";
    private static String COLUMN_PASSWORD = "Password";
    private static String COLUMN_FIRST_NAME = "First Name";
    private static String COLUMN_LAST_NAME = "Last Name";
    private static String COLUMN_AGE="Age ";
    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_FIRST_NAME + "TEXT," +
                    COLUMN_LAST_NAME + "TEXT," +
                    COLUMN_EMAIL + "TEXT, " +
                    COLUMN_PASSWORD + "TEXT, " +
                    COLUMN_AGE + "TEXT)";


    public PatientDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Code SQL pour créer les tables de la base de données
        db.execSQL(SQL_CREATE_TABLE);
        }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Code pour gérer la mise à jour de la base de données si nécessaire
    }
    public boolean insertPatient(Patient patient) {
        SQLiteDatabase db = this.getWritableDatabase();
        //if (!this.checkUserByEmail(patient.getEmail())) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_FIRST_NAME, patient.getFirstName());
        values.put(COLUMN_LAST_NAME, patient.getLastName());
        values.put(COLUMN_AGE, patient.getAge());
        values.put(COLUMN_EMAIL, patient.getEmail());
        values.put(COLUMN_PASSWORD, patient.getMdp());
        long result = db.insert(TABLE_NAME, null, values);
        db.close();
        return (result==-1);
        //}
        //return false;


    }
    public boolean checkUserByEmail(String email) {
        SQLiteDatabase db = this.getWritableDatabase();

        String[] columns = { COLUMN_ID };
        String selection = COLUMN_EMAIL + " = ?";
        String[] selectionArgs = { email };
        String limit = "1";

        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null, limit);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        db.close();
        return exists;
    }
}

