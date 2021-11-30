package com.example.projetg29;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;

public class DBServices extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "serviceDB.db";
    public static final String TABLE_SERVICES = "services";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_FORMULAIRE = "formulaire";
    public static final String COLUMN_DOCUMENT = "document";

    public DBServices(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SERVICES_TABLE = "CREATE TABLE "+ TABLE_SERVICES + "("+ COLUMN_NAME + " TEXT,"
                + COLUMN_FORMULAIRE + " TEXT," + COLUMN_DOCUMENT + " TEXT"+ ")";
        db.execSQL(CREATE_SERVICES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVICES);
        onCreate(db);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addService(Service service){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, service.getName());
        values.put(COLUMN_FORMULAIRE, service.getFormulaireMap());
        values.put(COLUMN_DOCUMENT, service.getDocumentMap());
        db.insert(TABLE_SERVICES, null, values);
        db.close();
    }

    public Service findService(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * FROM " + TABLE_SERVICES + " WHERE "+
                COLUMN_NAME + " = \"" + name + "\"";
        Cursor cursor = db.rawQuery(query, null);
        Service service;

        if(cursor.moveToFirst()){
            String serviceName = cursor.getString(0);
            HashMap<String, String> formulaire = new HashMap<>();
            String rawFormulaire = cursor.getString(1);

            try{
                JSONObject jsonObject = new JSONObject(rawFormulaire);
                JSONArray names = jsonObject.names();
                if(names != null) {
                    for (int i = 0; i < names.length(); i++) {
                        String key = names.getString(i);
                        formulaire.put(key, jsonObject.optString(key));
                    }
                }
            } catch (JSONException exception){
                exception.printStackTrace();
            }

            HashMap<String, Intent> document = new HashMap<>();
            String rawDocument = cursor.getString(2);

            try{
                JSONObject jsonObject = new JSONObject(rawDocument);
                JSONArray names = jsonObject.names();
                if(names != null) {
                    for (int i = 0; i < names.length(); i++) {
                        String key = names.getString(i);
                        String value = jsonObject.optString(key);
                        Uri uri = Uri.parse(value);
                        Intent intent = new Intent();
                        intent.putExtra("imageUri", uri);
                        document.put(key, intent);
                    }
                }
            } catch (JSONException exception){
                exception.printStackTrace();
            }
            service = new Service(serviceName, formulaire, document);
            cursor.close();
        } else{
            service = null;
        }
        db.close();
        return  service;
    }

    public boolean deleteService(String name){
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "Select * FROM " + TABLE_SERVICES + " WHERE " +
                COLUMN_NAME + " = \"" + name + "\"";
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            String nameStr = cursor.getString(0);
            db.delete(TABLE_SERVICES, COLUMN_NAME + " = '"+ nameStr +"'", null);
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }

    public LinkedList<Service> getAllServices(){
        LinkedList<Service> list = new LinkedList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * FROM " + TABLE_SERVICES;
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            while(!cursor.isAfterLast()){
                String servicename = cursor.getString(0);

                list.add(findService(servicename));
                cursor.moveToNext();

            }
        }
        return list;
    }
}
