package com.example.projetg29;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.projetg29.Compte;

import java.util.LinkedList;
import java.util.List;

public class MyDBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "compteDB.db";
    public static final String TABLE_COMPTES = "comptes";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_USERTYPE = "usertype";

    public MyDBHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_COMPTES_TABLE = "CREATE TABLE " + TABLE_COMPTES + "("+ COLUMN_USERNAME + " TEXT,"
                + COLUMN_PASSWORD + " TEXT," + COLUMN_USERTYPE + " TEXT" + ")";
        db.execSQL(CREATE_COMPTES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPTES);
        onCreate(db);
    }

    public void addCompte(Compte compte){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, compte.getUsername());
        values.put(COLUMN_PASSWORD, compte.getPassword());
        values.put(COLUMN_USERTYPE, compte.getType());
        db.insert(TABLE_COMPTES, null, values);
        db.close();
    }

    public Compte findCompte(String username){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * FROM " + TABLE_COMPTES + " WHERE " +
                COLUMN_USERNAME + " = \"" + username + "\"";
        Cursor cursor = db.rawQuery(query, null);
        Compte compte;
        if(cursor.moveToFirst()){
            if(cursor.getString(2).equals("Administrateur")) {
                compte = new Administrateur(cursor.getString(0), cursor.getString(1));
            }
            else if(cursor.getString(2).equals("Employé")) {
                compte = new Employe(cursor.getString(0), cursor.getString(1));
            }
            else{
                compte = new Client(cursor.getString(0), cursor.getString(1));
            }
            cursor.close();
        } else{
            compte = null;
        }
        db.close();
        return compte;
    }

    public boolean deleteCompte(String username, String type){
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "Select * FROM " + TABLE_COMPTES + " WHERE " + COLUMN_USERNAME + " = \"" + username + "\" AND " + COLUMN_USERTYPE + " = \"" + type + "\"";
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            String userStr = cursor.getString(0);
            db.delete(TABLE_COMPTES, COLUMN_USERNAME + " = '" + userStr+"' AND " + COLUMN_USERTYPE + " = \"" + type + "\"", null);
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }

    public boolean deleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_COMPTES, null, null);
        return true;
    }

    // Ceci retourne une Linkedlist contenant le nom de chaque compte suivi de son type
    public LinkedList<String> getAllComptes(){
        LinkedList<String> list = new LinkedList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * FROM " + TABLE_COMPTES;
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            while(!cursor.isAfterLast()){
                String name = cursor.getString(0);
                String type = cursor.getString(2);

                list.add(name);
                list.add(type);
                cursor.moveToNext();

            }
        }
        return list;
    }
}
