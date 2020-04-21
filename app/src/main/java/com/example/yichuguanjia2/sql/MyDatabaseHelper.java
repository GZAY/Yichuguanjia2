package com.example.yichuguanjia2.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    public static final String CREATE_ImagePath1 = "create table ImagePath1 ("
            + "id integer primary key autoincrement,"
            + "path text)";
    public static final String CREATE_ImagePath2 = "create table ImagePath2 ("
            + "id integer primary key autoincrement,"
            + "path text)";
    public static final String CREATE_ImagePath3 = "create table ImagePath3 ("
            + "id integer primary key autoincrement,"
            + "path text)";
    public static final String CREATE_ImagePath4 = "create table ImagePath4 ("
            + "id integer primary key autoincrement,"
            + "path text)";
    public static final String CREATE_ImagePath5 = "create table ImagePath5 ("
            + "id integer primary key autoincrement,"
            + "path text)";
    public static final String CREATE_ImagePath6 = "create table ImagePath6 ("
            + "id integer primary key autoincrement,"
            + "path text)";
    public static final String CREATE_ImagePath7 = "create table ImagePath7 ("
            + "id integer primary key autoincrement,"
            + "path text)";
    public static final String CREATE_ImagePath8 = "create table ImagePath8 ("
            + "id integer primary key autoincrement,"
            + "path text)";
    public static final String CREATE_ImagePath9 = "create table ImagePath9 ("
            + "id integer primary key autoincrement,"
            + "path text)";
    public static final String CREATE_ImagePath10 = "create table ImagePath10 ("
            + "id integer primary key autoincrement,"
            + "path text)";
    private Context mContext;
    public MyDatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ImagePath1);
        db.execSQL(CREATE_ImagePath2);
        db.execSQL(CREATE_ImagePath3);
        db.execSQL(CREATE_ImagePath4);
        db.execSQL(CREATE_ImagePath5);
        db.execSQL(CREATE_ImagePath6);
        db.execSQL(CREATE_ImagePath7);
        db.execSQL(CREATE_ImagePath8);
        db.execSQL(CREATE_ImagePath9);
        db.execSQL(CREATE_ImagePath10);
        Toast.makeText(mContext, "Create succeeded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("drop table if exists ImagePath");
    }
}
