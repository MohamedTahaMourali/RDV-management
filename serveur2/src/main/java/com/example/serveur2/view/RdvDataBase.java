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
        System.out.println(newRowId);
        System.out.println(newRowId != -1);
        return newRowId != -1;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)

    public boolean verifyRdv(int patientId, String tel, String desc, String date, String temps) {
        SQLiteDatabase rdvDB = getReadableDatabase();
        String[] projection = {
                COLUMN_DATE,
                COLUMN_TEMPS
        };
        String sortOrder =
                COLUMN_DATE + " DESC, " + COLUMN_TEMPS + " DESC";

        // Requête pour récupérer la date minimale dans la table
        Cursor minDateCursor = rdvDB.query(
                TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder,
                "1"
        );
        LocalDateTime minRdvDateTime = null;
        if (minDateCursor.moveToNext()) {
            String minDate = minDateCursor.getString(minDateCursor.getColumnIndexOrThrow(COLUMN_DATE));
            String minTime = minDateCursor.getString(minDateCursor.getColumnIndexOrThrow(COLUMN_TEMPS));
            minRdvDateTime = LocalDateTime.parse(minDate + "T" + minTime);
            System.out.println("La date minimale dans la table : "+minRdvDateTime);
        }
        minDateCursor.close();

        Cursor cursor = rdvDB.query(
                TABLE_NAME,
                projection,
                COLUMN_DATE + " = ?",
                new String[]{date},
                null,
                null,
                sortOrder
        );

        LocalDateTime lastRdvDateTime = null;
        if (cursor.moveToLast()) {
            String lastDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE));
            String lastTime = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEMPS));
            lastRdvDateTime = LocalDateTime.parse(lastDate + "T" + lastTime);
            System.out.println("La dernière date pour cette journée : "+lastRdvDateTime);
        }
        cursor.close();

        LocalDateTime newRdvDateTime = LocalDateTime.parse(date + "T" + temps);

        boolean isValid = false;
        if (minRdvDateTime != null && newRdvDateTime.isBefore(minRdvDateTime)) {
            System.out.println("La date est inférieure à la date minimale : "+isValid);
        } else if (lastRdvDateTime == null && newRdvDateTime.isAfter(LocalDateTime.parse(date + "T08:00:00"))) {
            isValid = true;
        } else if (lastRdvDateTime != null && newRdvDateTime.isAfter(lastRdvDateTime.plusMinutes(15))) {
            isValid = true;
        } else if (lastRdvDateTime == null) {
            isValid = true;
        }

        if (isValid) {
            System.out.println("La date est valide : "+isValid);
            System.out.println("Ajout...");
            return addRdv(patientId, tel, desc, date, temps);
        } else {
            System.out.println("La date n'est pas valide");
            return false;
        }
    }

    public void clearRdvTable() {
        SQLiteDatabase rdvDB = getWritableDatabase();
        rdvDB.delete(TABLE_NAME, null, null);
    }

}



