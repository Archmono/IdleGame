package com.example.alucardc.idlegame;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by auser on 2017/11/27.
 */

public class GameDBHelper extends SQLiteOpenHelper{

    private static GameDBHelper instance = null;
    public static GameDBHelper getInstance(Context ctx){
        if(instance == null){
            instance = new GameDBHelper(ctx, "idlegame.db", null, 1);
        }
        return instance;
    }

    private GameDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
