package com.example.alucardc.idlegame;

import android.content.Intent;
import android.database.Cursor;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  //隱藏狀態列
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DBInfo.DB_FILE = getDatabasePath("idlegame")+".db";    //database的絕對路徑
//        DBInfo.JSON_FILE = getFilesDir("playerdata.json");

    }
    public void enter_scene_1(View view){
        Intent it = new Intent();
        it.setClass(MainActivity.this,pre_battle_scene.class);
        startActivity(it);
    }

//    public void getData(){
//        GameDBHelper helper = GameDBHelper.getInstance(this);
//        String[] column = { "_id", "name", "healthPoint"};
//        Cursor c = helper.getReadableDatabase().query("mobsdata", column, "healthPoint>?", new String[]{"10"}, null, null, null);
//
//        c.moveToFirst();
//        for (int i = 0; i < c.getCount(); i++) {
//            String id = c.getString(c.getColumnIndex("_id"));
//            String name = c.getString(c.getColumnIndex("name"));
//            int hp = c.getInt(c.getColumnIndex("healthPoint"));
//            c.moveToNext();
//            idList.add(id);
//            nameList.add(name);
//            healthPointList.add(hp);
//        }
//        c.close();
//    }
//
//    public void getData(String scene1, String scene2){
//        GameDBHelper helper = GameDBHelper.getInstance(this);
//        String[] column = { "_id", "rareWeight","scene_1", "scene_2"};
//        Cursor c = helper.getReadableDatabase().query("mobsdata", column, "scene_1=? AND scene_2=?", new String[]{scene1,scene2}, null, null, null);
//
//        c.moveToFirst();
//        for (int i = 0; i < c.getCount(); i++) {
//            String id = c.getString(c.getColumnIndex("_id"));
//            int rarity = c.getInt(c.getColumnIndex("rareWeight"));
//            idList.add(id);
//            rarityList.add(rarity);
//            c.moveToNext();
//        }
//        c.close();
//    }

    public void copyJSONFile()
    {
        try {
            File f = new File(DBInfo.DB_FILE);
            File dbDir = new File(DBInfo.DB_FILE.substring(0,DBInfo.DB_FILE.length()-12));
            Log.d("GameDBHelper", "copyFiles : "+DBInfo.DB_FILE);
            dbDir.mkdirs();
            if (! f.exists())
            {

                InputStream is = getResources().openRawResource(R.raw.idlegame);
                OutputStream os = new FileOutputStream(DBInfo.DB_FILE);
                int read;
                Log.d("GameDBHelper", "Start Copy");
                while ((read = is.read()) != -1)
                {
                    os.write(read);
                }
                Log.d("GameDBHelper", "FilesCopied");
                os.close();
                is.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
