package com.example.serveur2.view;

import static java.security.AccessController.getContext;

import android.app.DownloadManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;


public class RdvDataBase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "rdv.db";
    private static final int DATABASE_VERSION = 1;
    private static final  String TABLE_NAME = "RendezVous"  ;
    private static final String COLUMN_DESCRIPTION = "description";
    private static String COLUMN_PATIENT_ID = "patient_id";
    private static String COLUMN_DATE = "date";
    private static String COLUMN_TEMPS = "temps";
    private static String COLUMN_TEL="telephone";

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_PATIENT_ID + " INTEGER," +
                    COLUMN_DATE + " DATE, " +
                    COLUMN_TEMPS + " DATE," +
                    COLUMN_DESCRIPTION + " TEXT,"+
                    COLUMN_TEL + " TEXT,"+
                    "PRIMARY KEY ( " + COLUMN_PATIENT_ID + ","+COLUMN_DATE+ ","+ COLUMN_TEMPS + "))";




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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


    public boolean addRdv(int patientId, String tel, String desc, String dateRdv, String heureRdv) {
        // Ajouter le rendez-vous dans la table RendezVous de la base de données rdv.db
        SQLiteDatabase rdvDB = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PATIENT_ID, patientId);
        values.put(COLUMN_DATE, dateRdv);
        values.put(COLUMN_TEMPS, heureRdv);
        values.put(COLUMN_DESCRIPTION, desc);
        values.put(COLUMN_TEL, tel);
        long newRowId = rdvDB.insert(TABLE_NAME, null, values);
        System.out.println(newRowId != -1);
        return newRowId != -1;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean vetifRDV(int patientId, String tel, String desc, String date, String temps) {
        SQLiteDatabase rdvDB = getReadableDatabase();
        String[] projection = {
                COLUMN_DATE,
                COLUMN_TEMPS
        };
        String sortOrder =
                COLUMN_DATE + " DESC, " + COLUMN_TEMPS + " DESC";

        Cursor cursor = rdvDB.query(
                TABLE_NAME, // La table à interroger
                projection, // Les colonnes à retourner
                null, // Les colonnes WHERE clause
                null, // Les valeurs WHERE clause
                null, // GROUP BY clause
                null, // HAVING clause
                sortOrder // ORDER BY clause
        );

        LocalDateTime lastRdvDateTime = null;
        if (cursor.moveToNext()) {
            String lastDate = cursor.getString(
                    cursor.getColumnIndexOrThrow(COLUMN_DATE));
            String lastTime = cursor.getString(
                    cursor.getColumnIndexOrThrow(COLUMN_TEMPS));
            lastRdvDateTime = LocalDateTime.parse(lastDate + "T" + lastTime);
        }
        cursor.close();

        LocalDateTime newRdvDateTime = LocalDateTime.parse(date + "T" + temps);

        boolean isValid = false;
        if (lastRdvDateTime == null) {
            isValid = true;
        } else {
            LocalDateTime minDate = lastRdvDateTime.plusMinutes(15);
            isValid = newRdvDateTime.isAfter(minDate) || newRdvDateTime.isEqual(minDate);
        }

        if (isValid) {
            System.out.println(isValid);
            return addRdv(patientId, tel, desc, date, temps);
        } else {
            return false;
        }
    }


}


