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
    public static final String CREATE_ImagePath11 = "create table ImagePath11 ("
            + "id integer primary key autoincrement,"
            + "path text)";
    public static final String CREATE_ImagePath12 = "create table ImagePath12 ("
            + "id integer primary key autoincrement,"
            + "path text)";
    public static final String CREATE_ImagePath13 = "create table ImagePath13 ("
            + "id integer primary key autoincrement,"
            + "path text)";
    public static final String CREATE_ImagePath14 = "create table ImagePath14 ("
            + "id integer primary key autoincrement,"
            + "path text)";
    public static final String CREATE_ImagePath15 = "create table ImagePath15 ("
            + "id integer primary key autoincrement,"
            + "path text)";
    public static final String CREATE_ImagePath16 = "create table ImagePath16 ("
            + "id integer primary key autoincrement,"
            + "path text)";
    public static final String CREATE_ImagePath21 = "create table ImagePath21 ("
            + "id integer primary key autoincrement,"
            + "path text)";
    public static final String CREATE_ImagePath22 = "create table ImagePath22 ("
            + "id integer primary key autoincrement,"
            + "path text)";
    public static final String CREATE_ImagePath23 = "create table ImagePath23 ("
            + "id integer primary key autoincrement,"
            + "path text)";
    public static final String CREATE_ImagePath24 = "create table ImagePath24 ("
            + "id integer primary key autoincrement,"
            + "path text)";
    public static final String CREATE_ImagePath25 = "create table ImagePath25 ("
            + "id integer primary key autoincrement,"
            + "path text)";
    public static final String CREATE_ImagePath26 = "create table ImagePath26 ("
            + "id integer primary key autoincrement,"
            + "path text)";
    public static final String CREATE_ImagePath27 = "create table ImagePath27 ("
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
        db.execSQL(CREATE_ImagePath11);
        db.execSQL(CREATE_ImagePath12);
        db.execSQL(CREATE_ImagePath13);
        db.execSQL(CREATE_ImagePath14);
        db.execSQL(CREATE_ImagePath15);
        db.execSQL(CREATE_ImagePath16);
        db.execSQL(CREATE_ImagePath21);
        db.execSQL(CREATE_ImagePath22);
        db.execSQL(CREATE_ImagePath23);
        db.execSQL(CREATE_ImagePath24);
        db.execSQL(CREATE_ImagePath25);
        db.execSQL(CREATE_ImagePath26);
        db.execSQL(CREATE_ImagePath27);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("drop table if exists ImagePath");
    }
}
