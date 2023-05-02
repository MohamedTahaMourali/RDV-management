package com.example.serveur2.view;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//import com.example.consutationmanagement.model.Patient;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "patient.db";
    private static final int DATABASE_VERSION = 4;
    private static final  String TABLE_NAME = "Patient"  ;
    private static final String COLUMN_EMAIL = "email";
    private final static String COLUMN_PASSWORD = "password";
    private final static String COLUMN_FIRST_NAME = "first_name";
    private final static String COLUMN_LAST_NAME = "last_name";
    private final static String COLUMN_AGE="Age";
    private  static final String COLUMN_ID ="id" ;
    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_FIRST_NAME + " TEXT," +
                    COLUMN_LAST_NAME + " TEXT, " +
                    COLUMN_EMAIL + " TEXT, " +
                    COLUMN_PASSWORD + " TEXT, " +
                    COLUMN_AGE + " TEXT)";


    public DatabaseHelper(Context context) {
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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public boolean addUser(String firstName, String lastName, String email, String password, String age) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FIRST_NAME, firstName);
        values.put(COLUMN_LAST_NAME, lastName);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_AGE, age);
        long result = db.insert(TABLE_NAME, null, values);
        return result != -1;
    }

    public boolean checkUser(String email, String password) {
        String[] columns = {COLUMN_ID};
        SQLiteDatabase db = getReadableDatabase();
        String selection = COLUMN_EMAIL + " = ?" + " AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {email, password};
        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }
}

