package com.mycf.edittracker;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

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
    public static final String T2_COL_7 = "Workflow_Id";

    /* WORKFLOWS TABLE*/
    public static final String TABLE_3_NAME = "workflows";
    public static final String T3_COL_1 = "ID";
    public static final String T3_COL_2 = "Name";
    public static final String T3_COL_3 = "Steps";

    /* WORKFLOW STEPS */
    public static final String TABLE_4_NAME = "workflow_steps";
    public static final String T4_COL_1 = "ID";
    public static final String T4_COL_2 = "Name";
    public static final String T4_COL_3 = "Abbreviation";

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
                + T2_COL_6 + " TEXT,"
                + T2_COL_7 + " TEXT)");

        //Create Workflow table
        db.execSQL("CREATE TABLE " + TABLE_3_NAME + " (" + T3_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + T3_COL_2 + " TEXT,"
                + T3_COL_3 + " TEXT)");

        //Create Workflow Steps table
        db.execSQL("CREATE TABLE " + TABLE_4_NAME + " (" + T4_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + T4_COL_2 + " TEXT,"
                + T4_COL_3 + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_1_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_2_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_3_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_4_NAME);
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
        newTitle = newTitle.trim();
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

    public boolean addWorkflowToScene(String workflowId, String sceneId) {
        sceneId = sceneId.trim();
        workflowId = workflowId.trim();
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_2_NAME + " SET "
                + T2_COL_7 + "='" + workflowId + "' WHERE " + T2_COL_1 + "=" + sceneId);
        String[] steps = getWorkflowSteps(workflowId);
        int numOfSteps = steps.length;
        String[] initStatus = new String[numOfSteps];
        for (int i = 0; i < numOfSteps; i++) {
            initStatus[i] = "FALSE";
        }
        String initStatusStr = convertArrayToString(initStatus);
        updateSceneStatus(sceneId, initStatusStr);
        return true;
    }

    public boolean updateSceneStatus(String sceneId, String status) {
        status = status.trim();
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_2_NAME + " SET "
                + T2_COL_6 + "='" + status + "' WHERE " + T2_COL_1 + "=" + sceneId);
        return true;
    }

    public boolean updateSceneStatus(String sceneId, HashMap<String, String> workflowStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT " + T2_COL_7 + " FROM " + TABLE_2_NAME + " WHERE " + T2_COL_1 + "=" + sceneId, null);
        res.moveToFirst();
        String workflowId = res.getString(0);

        res = db.rawQuery("SELECT " + T3_COL_3 + " FROM " + TABLE_3_NAME + " WHERE " + T3_COL_1 + "=" + workflowId, null);
        res.moveToFirst();
        String workflowStepsStr = res.getString(0);
        String[] workflowSteps = convertStringToArray(workflowStepsStr);

        String[] statusArr = new String[workflowSteps.length];
        for (int i = 0; i < workflowSteps.length; i++) {
            statusArr[i] = workflowStatus.get(workflowSteps[i]);
        }

        String newStatusStr = convertArrayToString(statusArr);

        db.execSQL("UPDATE " + TABLE_2_NAME + " SET "
                + T2_COL_6 + "='" + newStatusStr + "' WHERE " + T2_COL_1 + "=" + sceneId);

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

    public HashMap<String, String> getWorkflow(String sceneId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT " + T2_COL_7 + " FROM " + TABLE_2_NAME + " WHERE " + T2_COL_1 + "=" + sceneId, null);
        res.moveToFirst();
        String workflowId = res.getString(0);
        res = db.rawQuery("SELECT " + T3_COL_3 + " FROM " + TABLE_3_NAME + " WHERE " + T3_COL_1 + "=" + workflowId, null);
        res.moveToFirst();
        String[] workflowSteps = convertStringToArray(res.getString(0));

        res = db.rawQuery("SELECT " + T2_COL_6 + " FROM " + TABLE_2_NAME + " WHERE " + T2_COL_1 + "=" + sceneId, null);
        res.moveToFirst();
        String[] statusArr = convertStringToArray(res.getString(0));

        HashMap<String, String> workflowStatus = new HashMap<>();
        for (int i = 0; i < workflowSteps.length; i++) {
            workflowStatus.put(workflowSteps[i], statusArr[i]);
        }

        return workflowStatus;
    }

    public String getWorkflowId(String sceneId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT " + T2_COL_7 + " FROM " + TABLE_2_NAME + " WHERE " + T2_COL_1 + "=" + sceneId, null);
        res.moveToFirst();
        return res.getString(0);
    }

    public String[] getStatus(String sceneId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT " + T2_COL_6 + " FROM " + TABLE_2_NAME + " WHERE " + T2_COL_1 + "=" + sceneId, null);
        res.moveToFirst();
        String statusStr = res.getString(0);
        return convertStringToArray(statusStr);
    }

    /**
     * Method to calculate and return the percentage complete of a scene.
     *
     * @param sceneId The ID of the scene
     * @return percentage complete of the scene (as an int)
     */
    public int getPercentageCompleteScene(String sceneId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT " + T2_COL_6 + " FROM " + TABLE_2_NAME + " WHERE " + T2_COL_1 + "=" + sceneId, null);
        res.moveToFirst();
        String statusStr = res.getString(0);
        String[] statusArr = convertStringToArray(statusStr);
        int completeSteps = 0;
        int totalSteps = 0;
        for (int i = 0; i < statusArr.length; i++) {
            totalSteps++;
            if (statusArr[i].equals("TRUE")) {
                completeSteps++;
            }
        }

        return ((completeSteps * 100)/totalSteps);
    }

    ///////////////////
    //   WORKFLOWS   //
    ///////////////////
    public static String strSeparator = "__,__";

    public boolean insertNewWorkflow(String name, String[] steps) {
        name = name.trim();
        String stepsArr = convertArrayToString(steps);
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO " + TABLE_3_NAME + " ("
                + T3_COL_2 + "," + T3_COL_3 + ") VALUES ('"
                + name + "','" + stepsArr + "');");
        return true;
    }

    public String[] getWorkflowSteps(String workflowId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT Steps FROM " + TABLE_3_NAME + " WHERE " + T3_COL_1 + "=" + workflowId + "", null);
        res.moveToFirst();
        String stepsStr = res.getString(0);
        return convertStringToArray(stepsStr);
    }

    public static String convertArrayToString(String[] array) {
        String str = "";
        for (int i = 0; i < array.length; i++) {
            str = str + array[i];
            if( i < (array.length - 1)) {
                str = str + strSeparator;
            }
        }
        return str;
    }

    public static String[] convertStringToArray(String str){
        String[] arr = str.split(strSeparator);
        return arr;
    }

    ////////////////////////
    //   WORKFLOW STEPS   //
    ////////////////////////
    public boolean insertNewWorkflowStep(String name, String abbreviation) {
        name = name.trim();
        abbreviation = abbreviation.trim();
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO " + TABLE_4_NAME + " ("
                + T4_COL_2 + "," + T4_COL_3 + ") VALUES ('"
                + name + "','" + abbreviation + "');");
        return true;
    }

    public String getWorkflowStepName(String workflowStepId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT " + T4_COL_2 + " FROM " + TABLE_4_NAME + " WHERE " + T4_COL_1 + "=" + workflowStepId, null);
        res.moveToFirst();
        return res.getString(0);
    }

    public String getWorkflowStepAbbr(String workflowStepId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT " + T4_COL_3 + " FROM " + TABLE_4_NAME + " WHERE " + T4_COL_1 + "=" + workflowStepId, null);
        res.moveToFirst();
        return res.getString(0);
    }

}
