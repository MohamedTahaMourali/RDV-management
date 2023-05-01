package com.example.consutationmanagement.controller;


import android.content.Context;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class DataBaseDoctor extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "doctor.db";
    private static final int DATABASE_VERSION = 2;
    private static final  String TABLE_NAME = "Doctor"  ;
    private static final String COLUMN_EMAIL = "email";

    private static String COLUMN_ID = "ID";
    private static String COLUMN_PASSWORD = "Password";
    private static String COLUMN_FIRST_NAME = "First Name";
    private static String COLUMN_LAST_NAME = "Last Name";
    private static String COLUMN_SPECIALITE="Spécialité ";
    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_FIRST_NAME + "TEXT," +
                    COLUMN_LAST_NAME + "TEXT," +
                    COLUMN_EMAIL + "TEXT, " +
                    COLUMN_PASSWORD + "TEXT, " +
                    COLUMN_SPECIALITE + "TEXT)";


    public DataBaseDoctor(Context context) {
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

}
