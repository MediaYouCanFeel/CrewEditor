package com.mycf.edittracker;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by John on 8/25/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "EditTracker.db";

    /* PROJECTS TABLE */
    public static final String TABLE_1_NAME = "projects";
    public static final String T1_COL_1 = "ID";
    public static final String T1_COL_2 = "Title";

    /* SCENES TABLE */
    public static final String TABLE_2_NAME = "scenes";
    public static final String T2_COL_1 = "ID";
    public static final String T2_COL_2 = "Project_Id";
    public static final String T2_COL_3 = "Number";
    public static final String T2_COL_4 = "Location";
    public static final String T2_COL_5 = "Time";
    public static final String T2_COL_6 = "Status";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create Projects table
        db.execSQL("CREATE TABLE " + TABLE_1_NAME + " (" + T1_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + T1_COL_2 + " TEXT)");

        //Create Scenes table
        db.execSQL("CREATE TABLE " + TABLE_2_NAME + " (" + T2_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + T2_COL_2 + " TEXT,"
                + T2_COL_3 + " TEXT,"
                + T2_COL_4 + " TEXT,"
                + T2_COL_5 + " TEXT,"
                + T2_COL_6 + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_1_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_2_NAME);
        onCreate(db);
    }

    //////////////////
    //   PROJECTS   //
    //////////////////
    public boolean insertNewProject(String title) {
        title = title.trim();
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO " + TABLE_1_NAME + " (" + T1_COL_2 + ") VALUES ('" + title + "');");
        return true;
    }

    public Cursor getAllProjects() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_1_NAME, null);
        return res;
    }

    public String getProjectTitle(String projId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT " + T1_COL_2 + " FROM " + TABLE_1_NAME + " WHERE " + T1_COL_1 + "=" + projId, null);
        res.moveToFirst();
        return res.getString(0);
    }

    public void updateProjectTitle(String newTitle, String projId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_1_NAME + " SET " + T1_COL_2 + "='" + newTitle + "' WHERE " + T1_COL_1 + "=" + projId);
    }

    public void deleteProject(String projId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_1_NAME + " WHERE " + T1_COL_1 + "=" + projId);
    }

    ////////////////
    //   SCENES   //
    ////////////////
    public boolean insertNewScene(String projId, String number, String location, String time) {
        number = number.trim();
        location = location.trim();
        time = time.trim();
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO " + TABLE_2_NAME + " ("
                + T2_COL_2 + "," + T2_COL_3 + "," + T2_COL_4 + "," + T2_COL_5 + ") VALUES ("
                + projId + ",'" + number + "','" + location + "','" + time
                + "');");
        return true;
    }

    public boolean updateScene(String sceneId, String number, String location, String time) {
        number = number.trim();
        location = location.trim();
        time = time.trim();
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_2_NAME + " SET "
                + T2_COL_3 + "='" + number + "', "
                + T2_COL_4 + "='" + location + "',"
                + T2_COL_5 + "='" + time + "' WHERE " + T2_COL_1 + "=" + sceneId);
        return true;
    }

    public Cursor getScenesForProject(String projId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_2_NAME + " WHERE " + T2_COL_2 + "='" + projId + "'", null);
        return res;
    }

    public String getSceneNumber(String sceneId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT " + T2_COL_3 + " FROM " + TABLE_2_NAME + " WHERE " + T2_COL_1 + "=" + sceneId, null);
        res.moveToFirst();
        return res.getString(0);
    }

    public String getSceneLocation(String sceneId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT " + T2_COL_4 + " FROM " + TABLE_2_NAME + " WHERE " + T2_COL_1 + "=" + sceneId, null);
        res.moveToFirst();
        return res.getString(0);
    }

    public String getSceneTime(String sceneId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT " + T2_COL_5 + " FROM " + TABLE_2_NAME + " WHERE " + T2_COL_1 + "=" + sceneId, null);
        res.moveToFirst();
        return res.getString(0);
    }
}
