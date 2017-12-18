package com.example.alucardc.idlegame;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

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
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    int timeGap,nextTime; //運算用變數
    long newTime,oldTime; //運算用變數
    String tempOldTime,tempNextTime="0"; //暫存用變數
    Calendar rightNow = Calendar.getInstance(); //提取時間用的方法

    String TAG = "MainDataTest";
    String TAG2 = "MainPlayerTest";
    TextView textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  //隱藏狀態列
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        getData("1");
        getPlayerStatus();
        countMobs();
        Log.d(TAG, Loading.idList.toString());
        Log.d(TAG, Loading.nameList.toString());
        Log.d(TAG, Loading.healthPointList.toString());
        Log.d(TAG, Loading.rarityList.toString());
        Log.d(TAG, Loading.speedList.toString());
        Log.d(TAG, Loading.qCountsList.toString());
        Log.d(TAG, Loading.qTypesList.toString());
        Log.d(TAG, Loading.qRangeList.toString());
        Log.d(TAG, Loading.stunTimeList.toString());
        Log.d(TAG, Loading.atkList.toString());
        Log.d(TAG, Loading.imageList.toString());
//        int[] loots = (int[]) Loading.lootsList.get(0);
//        Log.d(TAG, loots[0]+", "+loots[1]+", "+loots[2]);
//        int[] lootsDP = (int[]) Loading.lootsDropRateList.get(0);
//        Log.d(TAG, lootsDP[0]+", "+lootsDP[1]+", "+lootsDP[2]);

    }

    void countMobs(){
        int sum=0;
        textView2 = (TextView) findViewById(R.id.textView2);
        for(int i=0; i<6; i++){
            System.out.println(Loading.mobsSlotFilled_S1[i]);
            if( !Loading.mobsSlotFilled_S1[i].equals("0") ) {
                sum++;
            }
        }
        textView2.setText(sum+"/ "+nextTime+"s");
    }

    public void enter_scene_1(View view){
        Intent it = new Intent();
        it.setClass(MainActivity.this,pre_battle_scene.class);
        startActivity(it);
    }

    public void btnInventory(View v){
        PlayerInventory PInventory = new PlayerInventory();
        PInventory.getCurrentInventory(this);
    }

    public void getPlayerStatus(){
        try {
            InputStream is = this.getResources().openRawResource(R.raw.playerdata);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            String json = new String(buffer, "UTF-8");
            Gson gson = new Gson();
            Player playInfo = gson.fromJson(json, Player.class);
            Log.d(TAG2,"玩家名稱 : " + String.valueOf(playInfo.playerStatus[0].playerID));
            Log.d(TAG2,"玩家攻擊力 : " + String.valueOf(playInfo.playerStatus[0].playerATK));
            Log.d(TAG2,"玩家目前HP : " + String.valueOf(playInfo.playerStatus[0].playerCurrentHP));
            Log.d(TAG2,"玩家最大HP : " + String.valueOf(playInfo.playerStatus[0].playerMaxHP));

//            Log.d(TAG2,"玩家100101號道具存量 : " + String.valueOf(playInfo.playerInventory[0].i100101));
            Log.d(TAG2,"怪物圖鑑1001號生態介紹解鎖狀態 : " + String.valueOf(playInfo.playerMobsCollection[0].m1001.mobsBio));

            Log.d(TAG2,"關卡二解鎖進度 : " + String.valueOf(playInfo.playerSceneProgress[0].Scene_1));

        } catch (IOException e) {
            e.printStackTrace();
        }
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
//
//    public void getData(String scene){
//        GameDBHelper helper = GameDBHelper.getInstance(this);
//        String[] column = { "_id", "rareWeight","scene_1", "scene_2"};
//        Cursor c = helper.getReadableDatabase().query("mobsdata", null, "scene_" + scene + "=?", new String[]{"1"}, null, null, null);
//
//        c.moveToFirst();
//        for (int i = 0; i < c.getCount(); i++) {
//            String id = c.getString(c.getColumnIndex("_id"));
//            String name = c.getString(c.getColumnIndex("name"));
//            int hp = c.getInt(c.getColumnIndex("healthPoint"));
//            int rarity = c.getInt(c.getColumnIndex("rareWeight"));
//            int speed = c.getInt(c.getColumnIndex("speed"));
//            int qCounts = c.getInt(c.getColumnIndex("qCounts"));
//            int qTypes = c.getInt(c.getColumnIndex("qTypes"));
//            int qRange = c.getInt(c.getColumnIndex("qRange"));
//            int stunTime = c.getInt(c.getColumnIndex("stunTime"));
//            int atk = c.getInt(c.getColumnIndex("atk"));
//            String imageR = c.getString(c.getColumnIndex("image_R"));
//            int loots_1 = c.getInt(c.getColumnIndex("loots_1"));
//            int loots_2 = c.getInt(c.getColumnIndex("loots_2"));
//            int loots_3 = c.getInt(c.getColumnIndex("loots_3"));
//            int loots_1_dp = c.getInt(c.getColumnIndex("loots_1_dp"));
//            int loots_2_dp = c.getInt(c.getColumnIndex("loots_2_dp"));
//            int loots_3_dp = c.getInt(c.getColumnIndex("loots_3_dp"));
//            int lootsInside[] = {loots_1,loots_2,loots_3};
//            int lootsdpInside[] = {loots_1_dp,loots_2_dp,loots_3_dp};
//            idList.add(id);
//            nameList.add(name);
//            healthPointList.add(hp);
//            rarityList.add(rarity);
//            speedList.add(speed);
//            qCountsList.add(qCounts);
//            qTypesList.add(qTypes);
//            qRangeList.add(qRange);
//            stunTimeList.add(stunTime);
//            atkList.add(atk);
//            imageList.add(imageR);
//            lootsList.add(lootsInside);
//            lootsDropRateList.add(lootsdpInside);
//
//            c.moveToNext();
//        }
//        c.close();
//    }

    @Override
    public void onBackPressed() {  //返回鍵事件

    }

    public static boolean settle =false;
    @Override
    protected void onResume() {
        super.onResume();
        if (settle){
            onSave();
            settle = false;
        }
        rightNow = Calendar.getInstance();
        newTime = rightNow.getTimeInMillis();
        restorePrefs();
        countMobs();
        DateTest();
        onSave();
    }

    protected void restorePrefs() { //讀取的位置
        Log.d(TAG,"onResume");
        SharedPreferences settings = getSharedPreferences(Loading.DATE_PREF, 0);
        for(int i=0; i<6; i++){
            Loading.mobsSlotFilled_S1[i] = settings.getString(Loading.PREF_MOBS_SLOT_FILLED_S1[i], "");
        }
        tempNextTime = settings.getString(Loading.DATE_NEXT_DATE, "");
        tempOldTime = settings.getString(Loading.PREF_OLD_TIME, "");
        nextTime = Integer.parseInt(tempNextTime);
        oldTime = Long.parseLong(tempOldTime);
        Log.d("LOGDATE_NewTime", newTime+"");
        Log.d("LOGDATE_OldTime", oldTime+"");
    }

    protected void onSave() { //儲存的位置
        SharedPreferences settings = getSharedPreferences(Loading.DATE_PREF, 0);
        settings.edit().putString(Loading.PREF_OLD_TIME, String.valueOf(rightNow.getTimeInMillis())).commit();
        for(int i=0; i<6; i++){
            settings.edit().putString(Loading.PREF_MOBS_SLOT_FILLED_S1[i], Loading.mobsSlotFilled_S1[i]).commit();
        }
        settings.edit().putString(Loading.DATE_NEXT_DATE, String.valueOf(nextTime)).commit();
    }

    int random;
    void DateTest () {
        timeGap = (int)(newTime-oldTime)/1000; //計算上次開啟遊戲與本次開啟遊戲時間差
        Log.d("LOGDATE_timeGap", timeGap + "");
        Log.d("LOGDATE_NextTime", nextTime+"");
        for(int i=0;i<6;i++) { //迴圈檢查，如怪物陣列滿了將不會進入
            if (Loading.mobsSlotFilled_S1[i].equals("0")) { //如果怪物陣列裡的ID為0
                Loading.getRarity();
                random = (int) (Math.random() * 25) + 5; //隨機 5-30秒
                if(nextTime > 0) { //如果有紀錄上次未生成怪物時間
                    random = nextTime; //覆蓋掉隨機時間
                    nextTime = 0; //重置下次生怪時間
                }
                Log.d("LOGDATE_Random"+i+":", random + "");
                timeGap -= random; //利用時間差運算是否繼續生怪
                if (timeGap < 0) { //無時間繼續生怪
                    nextTime = (-timeGap); //將剩餘時間存入下次生怪時間
                    break; //跳出迴圈
                } else {
                    Loading.mobsSlotFilled_S1[i] = RandomTest.cId; //放入生怪ID至怪物陣列
                }
            }
        }
    }
}



