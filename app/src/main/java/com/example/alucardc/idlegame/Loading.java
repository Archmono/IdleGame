package com.example.alucardc.idlegame;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

/**
 * Created by acer on 2017/12/2.
 */

public class Loading extends Activity {

    String TAG = "MainDataTest";
    String TAG2 = "MainPlayerTest";

    /*↓SharedPreferences裡儲存的資料名稱↓*/
    public static final String DATE_PREF = "DATE_PREF";
    public static final String PREF_OLD_TIME = "DATE_OldTime";
    public static final String[] PREF_HASMOB = {"HasMob1","HasMob2","HasMob3","HasMob4","HasMob5","HasMob6"};
    public static final String DATE_NEXT_DATE = "DATE_NextTime";
    /*↑SharedPreferences裡儲存的資料名稱↑*/

    /*↓SQLite取出的資料↓*/
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
    /*↑SQLite取出的資料↑*/

    int timeGap,nextTime; //運算用變數
    long newTime,oldTime; //運算用變數
    String tempOldTime,tempNextTime="0"; //暫存用變數
    String[] tempHasMod = new String[6]; //暫存用變數
    Calendar rightNow = Calendar.getInstance(); //提取時間用的方法

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  //隱藏狀態列
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        DBInfo.DB_FILE = getDatabasePath("idlegame.db")+"";    //database的絕對路徑
        copyDBFile();
        newTime = rightNow.getTimeInMillis();
        restorePrefs();
        DateTest();
        Log.d("LOGDATE_Mobs", tempHasMod[0] +" "+ tempHasMod[1] +" "+ tempHasMod[2] +" "+ tempHasMod[3] +" "+ tempHasMod[4] +" "+ tempHasMod[5]+" ");
        onSave();

        getPlayerStatus();

//        Log.d(TAG, idList.toString());
//        Log.d(TAG, nameList.toString());
//        Log.d(TAG, healthPointList.toString());
//        Log.d(TAG, rarityList.toString());
//        Log.d(TAG, speedList.toString());
//        Log.d(TAG, qCountsList.toString());
//        Log.d(TAG, qTypesList.toString());
//        Log.d(TAG, qRangeList.toString());
//        Log.d(TAG, stunTimeList.toString());
//        Log.d(TAG, atkList.toString());
//        Log.d(TAG, imageList.toString());
//        int[] loots = (int[]) lootsList.get(0);
//        Log.d(TAG, loots[0]+", "+loots[1]+", "+loots[2]);
//        int[] lootsDP = (int[]) lootsDropRateList.get(0);
//        Log.d(TAG, lootsDP[0]+", "+lootsDP[1]+", "+lootsDP[2]);
    }

    @Override
    protected void onDestroy() {
        onSave();
        super.onDestroy();
    }

    private void restorePrefs() { //讀取的位置
        tempHasMod = new String[]{"0", "0", "0", "0", "0", "0"}; //暫存重置不知為何new在class外部會有問題所以先寫在這裡
        SharedPreferences settings = getSharedPreferences(DATE_PREF, 0);
        for(int i=0; i<6; i++){
            tempHasMod[i] = settings.getString(PREF_HASMOB[i], "");
        }
        tempNextTime = settings.getString(DATE_NEXT_DATE, "");
        tempOldTime = settings.getString(PREF_OLD_TIME, "");
        if(tempOldTime.equals("")) { //玩家第一次開啟沒有舊時間，就
            tempHasMod = new String[]{"0", "0", "0", "0", "0", "0"}; //初始化怪物生成
            oldTime = rightNow.getTimeInMillis(); //將oldTime設置為now
        } else {
            nextTime = Integer.parseInt(tempNextTime);
            oldTime = Long.parseLong(tempOldTime);
        }
        Log.d("LOGDATE_NewTime", newTime+"");
        Log.d("LOGDATE_OldTime", oldTime+"");
    }

    protected void onSave() { //儲存的位置，目前在onCreate最底下呼叫
        SharedPreferences settings = getSharedPreferences(DATE_PREF, 0);
        settings.edit().putString(PREF_OLD_TIME, String.valueOf(rightNow.getTimeInMillis())).commit();
        for(int i=0; i<6; i++){
            settings.edit().putString(PREF_HASMOB[i], tempHasMod[i]).commit();
        }
        settings.edit().putString(DATE_NEXT_DATE, String.valueOf(nextTime)).commit();
    }

    int random;
    void DateTest () {
        timeGap = (int)(newTime-oldTime)/1000; //計算上次開啟遊戲與本次開啟遊戲時間差
        Log.d("LOGDATE_timeGap", timeGap + "");
        Log.d("LOGDATE_NextTime", nextTime+"");
        for(int i=0;i<6;i++) { //迴圈檢查，如怪物陣列滿了將不會進入
            if (tempHasMod[i].equals("0")) { //如果怪物陣列裡的ID為0
                getData("1"); //取得場景1一隨機怪物ID (變數存在RandomTest.cId)
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
                    tempHasMod[i] = RandomTest.cId; //放入生怪ID至怪物陣列
                }
            }
        }
    }

    public void onClick (View v) { //切換到主畫面
        Intent i = new Intent(Loading.this, MainActivity.class);
        startActivity(i);
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
            int lootsdpInside[] = {loots_1_dp,loots_2_dp,loots_3_dp};
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
            lootsDropRateList.add(lootsdpInside);

            c.moveToNext();
        }
        c.close();
        int[] raritygArray =  new int[rarityList.size()]; //ArrayList轉int[]
        for(int i=0; i< rarityList.size(); i++) {
            raritygArray[i] = Integer.parseInt(rarityList.get(i).toString());
        }
        RandomTest randomTest = new RandomTest(raritygArray); //將資料庫稀有度帶入隨機生怪
        randomTest.randomTest(); //執行運算方法
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

            Log.d(TAG2,"玩家100101號道具存量 : " + String.valueOf(playInfo.playerInventory[0].i100101));
            Log.d(TAG2,"怪物圖鑑1001號生態介紹解鎖狀態 : " + String.valueOf(playInfo.playerMobsCollection[0].m1001.mobsBio));

        } catch (IOException e) {
            e.printStackTrace();
        }
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
