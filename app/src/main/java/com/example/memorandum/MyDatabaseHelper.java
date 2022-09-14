package com.example.memorandum;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.memorandum.model.Memorandum;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    private final static String DATABASE_NAME = "Memorandum.db";
    private final static int DATABASE_VERSION = 1;
    private final static String TABLE_NAME = "memorendum";
    private final static String COLUMN_ID = "_id";
    private final static String COLUMN_STATE = "state";
    private final static String COLUMN_TITLE = "title";
    private final static String COLUMN_LOCATION_NAME = "location_name";
    private final static String COLUMN_LOCATION_LATITUDE = "latitude";
    private final static String COLUMN_LOCATION_LONGITUDE = "longitude";
    private final static String COLUMN_DATE = "date";
    private final static String COLUMN_HOUR = "hour";
    private final static String COlUMN_DESCRIPTION = "desciption";


    public MyDatabaseHelper(@Nullable Context context){
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE "+TABLE_NAME+" ("+COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                COLUMN_STATE+" TEXT, "+
                COLUMN_TITLE+" TEXT, "+
                COLUMN_DATE+" TEXT, "+
                COLUMN_HOUR+" TEXT, "+
                COLUMN_LOCATION_NAME+" TEXT, "+
                COLUMN_LOCATION_LATITUDE+" INTEGER, "+
                COLUMN_LOCATION_LONGITUDE+" INTEGER, "+
                COlUMN_DESCRIPTION+" TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    void addMemorandum(Memorandum m){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_STATE,m.getStato());
        cv.put(COLUMN_TITLE,m.getTitolo());
        cv.put(COLUMN_DATE,m.getData().toString());
        cv.put(COLUMN_HOUR,m.getData().getTime());
        cv.put(COLUMN_LOCATION_NAME,m.getLuogo().getName());
        cv.put(COLUMN_LOCATION_LATITUDE,m.getLuogo().getLatitude());
        cv.put(COLUMN_LOCATION_LONGITUDE,m.getLuogo().getLongitude());
        cv.put(COlUMN_DESCRIPTION,m.getDescrizione());

        long result = db.insert(TABLE_NAME, null, cv);
        if (result == -1){
            Toast.makeText(context, "Operation Failed", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Operation Succeed", Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor readAllData(){
        String query = "SELECT * FROM "+TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public void updateData(Memorandum m){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_STATE,m.getStato());
        cv.put(COLUMN_TITLE,m.getTitolo());
        cv.put(COLUMN_DATE,m.getData().toString());
        cv.put(COLUMN_HOUR,m.getData().getTime());
        cv.put(COLUMN_LOCATION_NAME,m.getLuogo().getName());
        cv.put(COLUMN_LOCATION_LATITUDE,m.getLuogo().getLatitude());
        cv.put(COLUMN_LOCATION_LONGITUDE,m.getLuogo().getLongitude());
        cv.put(COlUMN_DESCRIPTION,m.getDescrizione());

        long result = db.update(TABLE_NAME, cv, "_id=?", new String[]{m.getId()});
        if (result == -1){
            Toast.makeText(context, "Operation Failed", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Operation Succeed", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteOneRow(Memorandum m){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "_id=?", new String[]{m.getId()});
    }
}
