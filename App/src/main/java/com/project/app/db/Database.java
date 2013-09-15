package com.project.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.project.app.AppConfig;
import com.project.app.model.Party;

public class Database extends SQLiteOpenHelper {

    static final String DATABASE_NAME = AppConfig.DATABASE_NAME;
    static final int DATABASE_VERSION = AppConfig.DATABASE_VERSION;

    public Database(Context paramContext){
        super(paramContext, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase database){
        databaseSetup(database);
    }

    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion){
        switch(oldVersion){
            case 1:
                upgradeFrom1to2(database);
            case 2:
                upgradeFrom2to3(database);
            case 3:
                upgradeFrom3to4(database);
            case 4:
                upgradeFrom4to5(database);
        }
    }

    public void databaseSetup(SQLiteDatabase database){
        Party party = new Party(null);
        database.execSQL("DROP TABLE IF EXISTS " + Party.TABLE_NAME);
        database.execSQL(party.getCreateSql());
    }

    public void upgradeFrom1to2(SQLiteDatabase database){
    }

    public void upgradeFrom2to3(SQLiteDatabase database){
    }

    public void upgradeFrom3to4(SQLiteDatabase database){
    }

    public void upgradeFrom4to5(SQLiteDatabase database){
    }

}