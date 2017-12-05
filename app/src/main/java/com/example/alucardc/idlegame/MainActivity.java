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

    String TAG = "MainDataTest";
    String TAG2 = "MainPlayerTest";
    public static ArrayList idList = new ArrayList();
    public static ArrayList nameList = new ArrayList();
    public static ArrayList healthPointList = new ArrayList();
    public static ArrayList rarityList = new ArrayList();
    public static ArrayList speedList = new ArrayList();
    public static ArrayList qCountsList = new ArrayList();
    public static ArrayList qTypesList = new ArrayList();
    public static ArrayList qRangeList = new ArrayList();
    public static ArrayList stunTimeList = new ArrayList();
    public static ArrayList atkList = new ArrayList();
    public static ArrayList imageList = new ArrayList();
    public static ArrayList lootsList = new ArrayList();
    public static ArrayList lootsDropRateList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  //隱藏狀態列
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DBInfo.DB_FILE = getDatabasePath("idlegame")+".db";    //database的絕對路徑

        copyDBFile();

//        loadData();
        getData("2");
        getPlayerStatus();

        Log.d(TAG, idList.toString());
        Log.d(TAG, nameList.toString());
        Log.d(TAG, healthPointList.toString());
        Log.d(TAG, rarityList.toString());
        Log.d(TAG, speedList.toString());
        Log.d(TAG, qCountsList.toString());
        Log.d(TAG, qTypesList.toString());
        Log.d(TAG, qRangeList.toString());
        Log.d(TAG, stunTimeList.toString());
        Log.d(TAG, atkList.toString());
        Log.d(TAG, imageList.toString());
        int[] arr = (int[]) lootsList.get(1);
        Log.d(TAG, arr[0]+" ,"+arr[1]+" ,"+arr[2]);
        Log.d(TAG, lootsDropRateList.toString());
        System.out.println();

    }
    public void enter_scene_1(View view){
        Intent it = new Intent();
        it.setClass(MainActivity.this,pre_battle_scene.class);
        startActivity(it);
    }

    public void getPlayerStatus(){
        try {
            InputStream is = this.getResources().openRawResource(R.raw.playerdata);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            String json = new String(buffer, "UTF-8");
            Gson gson = new Gson();
            Player playerStatus = gson.fromJson(json, Player.class);
            Log.d(TAG2,"玩家名稱 : " + String.valueOf(playerStatus.playerStatus[0].playerID));
            Log.d(TAG2,"玩家攻擊力 : " + String.valueOf(playerStatus.playerStatus[0].playerATK));
            Log.d(TAG2,"玩家目前HP : " + String.valueOf(playerStatus.playerStatus[0].playerCurrentHP));
            Log.d(TAG2,"玩家最大HP : " + String.valueOf(playerStatus.playerStatus[0].playerMaxHP));

            Log.d(TAG2,"玩家100101號道具存量 : " + String.valueOf(playerStatus.playerInventory[0].i100101));
            Log.d(TAG2,"怪物圖鑑1001號生態介紹解鎖狀態 : " + String.valueOf(playerStatus.playerMobsCollection[0].m1001.mobsBio));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void loadData()
    {
        String[] mobsId;
        int[] mobsRareWeight;
        try {
            InputStream is = this.getResources().openRawResource(R.raw.mobsdata);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            String json = new String(buffer, "UTF-8");
            JSONObject jsonObject = new JSONObject(json);
            JSONArray array = jsonObject.getJSONArray("mobsdata");
            JSONObject mobs[] = new JSONObject[array.length()];
            mobsId = new String[array.length()];
            mobsRareWeight = new int[array.length()];
            for(int i=0; i < array.length(); i++){
                mobs[i] = array.getJSONObject(i);
                mobsId[i] = mobs[i].getString("_id");
                mobsRareWeight[i] = mobs[i].getInt("rareWeight");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void getData(){
        GameDBHelper helper = GameDBHelper.getInstance(this);
        String[] column = { "_id", "name", "healthPoint"};
        Cursor c = helper.getReadableDatabase().query("mobsdata", column, "healthPoint>?", new String[]{"10"}, null, null, null);

        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i++) {
            String id = c.getString(c.getColumnIndex("_id"));
            String name = c.getString(c.getColumnIndex("name"));
            int hp = c.getInt(c.getColumnIndex("healthPoint"));
            c.moveToNext();
            idList.add(id);
            nameList.add(name);
            healthPointList.add(hp);
        }
        c.close();
    }

    public void getData(String scene1, String scene2){
        GameDBHelper helper = GameDBHelper.getInstance(this);
        String[] column = { "_id", "rareWeight","scene_1", "scene_2"};
        Cursor c = helper.getReadableDatabase().query("mobsdata", column, "scene_1=? AND scene_2=?", new String[]{scene1,scene2}, null, null, null);

        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i++) {
            String id = c.getString(c.getColumnIndex("_id"));
            int rarity = c.getInt(c.getColumnIndex("rareWeight"));
            idList.add(id);
            rarityList.add(rarity);
            c.moveToNext();
        }
        c.close();
    }

    public void getData(String scene){
        GameDBHelper helper = GameDBHelper.getInstance(this);
//        String[] column = { "_id", "rareWeight","scene_1", "scene_2"};
        Cursor c = helper.getReadableDatabase().query("mobsdata", null, "scene_" + scene + "=?", new String[]{"1"}, null, null, null);

        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i++) {
            String id = c.getString(c.getColumnIndex("_id"));
            String name = c.getString(c.getColumnIndex("name"));
            int hp = c.getInt(c.getColumnIndex("healthPoint"));
            int rarity = c.getInt(c.getColumnIndex("rareWeight"));
            int speed = c.getInt(c.getColumnIndex("speed"));
            int qCounts = c.getInt(c.getColumnIndex("qCounts"));
            int qTypes = c.getInt(c.getColumnIndex("qTypes"));
            int qRange = c.getInt(c.getColumnIndex("qRange"));
            int stunTime = c.getInt(c.getColumnIndex("stunTime"));
            int atk = c.getInt(c.getColumnIndex("atk"));
            String imageR = c.getString(c.getColumnIndex("image_R"));
            int loots_1 = c.getInt(c.getColumnIndex("loots_1"));
            int loots_2 = c.getInt(c.getColumnIndex("loots_2"));
            int loots_3 = c.getInt(c.getColumnIndex("loots_3"));
            int loots_1_dp = c.getInt(c.getColumnIndex("loots_1_dp"));
            int loots_2_dp = c.getInt(c.getColumnIndex("loots_2_dp"));
            int loots_3_dp = c.getInt(c.getColumnIndex("loots_3_dp"));
            int lootsInside[] = {loots_1,loots_2,loots_3};
//            ArrayList lootsInside = new ArrayList();
//            lootsInside.add(loots_1);
//            lootsInside.add(loots_2);
//            lootsInside.add(loots_3);
            idList.add(id);
            nameList.add(name);
            healthPointList.add(hp);
            rarityList.add(rarity);
            speedList.add(speed);
            qCountsList.add(qCounts);
            qTypesList.add(qTypes);
            qRangeList.add(qRange);
            stunTimeList.add(stunTime);
            atkList.add(atk);
            imageList.add(imageR);
            lootsList.add(lootsInside);
            lootsDropRateList.add(loots_1_dp + loots_2_dp + loots_3_dp);

            c.moveToNext();
        }
        c.close();
    }

    public void copyDBFile()
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
