package com.example.alucardc.idlegame;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
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

    public static String APP_NAME;

    /*↓SharedPreferences裡儲存的資料名稱↓*/
    public static final String DATE_PREF = "DATE_PREF";
    public static final String PREF_OLD_TIME = "DATE_OldTime";
    public static final String[] PREF_MOBS_SLOT_FILLED_S1 = {"S1_P1", "S1_P2", "S1_P3", "S1_P4", "S1_P5", "S1_P6"};
    public static final String[] PREF_MOBS_SLOT_FILLED_S2 = {"S2_P1", "S2_P2", "S2_P3", "S2_P4", "S2_P5", "S2_P6"};
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

    //從mobsloot table取出的物品資料
    public static ArrayList id_lootList = new ArrayList();
    public static ArrayList i_image_RList = new ArrayList();
    public static ArrayList i_nametList = new ArrayList();
    public static ArrayList i_countList = new ArrayList();  //在MainActivity與準備畫面作為暫存資料庫中的物品存量使用
    public static ArrayList i_priceList = new ArrayList();
    public static ArrayList i_healList = new ArrayList();
    public static ArrayList i_poisontList = new ArrayList();
    public static ArrayList i_cureList = new ArrayList();
    public static ArrayList i_descList = new ArrayList();
    //從mobsloot table取出的物品資料

    int timeGap,nextTime; //運算用變數
    long newTime,oldTime; //運算用變數
    String tempOldTime,tempNextTime="0"; //暫存用變數
    public static String[] mobsSlotFilled_S1 = new String[6]; //暫存用變數
    public static String[] mobsSlotFilled_S2 = new String[6];
    Calendar rightNow = Calendar.getInstance(); //提取時間用的方法

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  //隱藏狀態列
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        APP_NAME = getPackageName();
        DBInfo.DB_FILE = getDatabasePath("idlegame.db")+"";    //database的絕對路徑
        copyDBFile();                                           //如果沒有db檔案存在目錄databases下,從RAW複製一份
        DBInfo.JSON_FILE = getFilesDir()+"/playerdata.json";    //json的檔案路徑
        copyJSONFile();                                         //如果沒有json檔案存在目錄files下,從RAW複製一份

        newTime = rightNow.getTimeInMillis();
        restorePrefs();
        DateTest();
        Log.d("LOGDATE_Mobs", mobsSlotFilled_S1[0] +" "+ mobsSlotFilled_S1[1] +" "+ mobsSlotFilled_S1[2] +" "+ mobsSlotFilled_S1[3] +" "+ mobsSlotFilled_S1[4] +" "+ mobsSlotFilled_S1[5]+" ");
        onSave();

        getItem();
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
//        Log.d(TAG, id_lootList.toString());
        Log.d(TAG, i_nametList.toString());
//        Log.d(TAG, i_image_RList.toString());
//        Log.d(TAG, i_descList.toString());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent().setClass(Loading.this, MainActivity.class));
            }
        }, 2000);
    }

    public void restorePrefs() { //讀取的位置
//        tempHasMod = new String[]{"0", "0", "0", "0", "0", "0"}; //暫存重置不知為何new在class外部會有問題所以先寫在這裡
        SharedPreferences settings = getSharedPreferences(DATE_PREF, 0);
        for(int i=0; i<6; i++){
            mobsSlotFilled_S1[i] = settings.getString(PREF_MOBS_SLOT_FILLED_S1[i], "");
            mobsSlotFilled_S2[i] = settings.getString(PREF_MOBS_SLOT_FILLED_S2[i], "");
        }
        tempNextTime = settings.getString(DATE_NEXT_DATE, "");
        tempOldTime = settings.getString(PREF_OLD_TIME, "");
        if(tempOldTime.equals("")) { //玩家第一次開啟沒有舊時間，就
            mobsSlotFilled_S1 = new String[]{"0", "0", "0", "0", "0", "0"}; //初始化怪物生成
            mobsSlotFilled_S2 = new String[]{"0", "0", "0", "0", "0", "0"};
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
            settings.edit().putString(PREF_MOBS_SLOT_FILLED_S1[i], mobsSlotFilled_S1[i]).commit();
        }
        settings.edit().putString(DATE_NEXT_DATE, String.valueOf(nextTime)).commit();
    }


    int random;
    void DateTest () {
        timeGap = (int)(newTime-oldTime)/1000; //計算上次開啟遊戲與本次開啟遊戲時間差
        Log.d("LOGDATE_timeGap", timeGap + "");
        Log.d("LOGDATE_NextTime", nextTime+"");
        getData("1"); //取得場景1一隨機怪物ID (變數存在RandomTest.cId)
        for(int i=0;i<6;i++) { //迴圈檢查，如怪物陣列滿了將不會進入
            if (mobsSlotFilled_S1[i].equals("0")) { //如果怪物陣列裡的ID為0
                getRarity();
                random = (int) (Math.random() * 25) + 5; //隨機 5-30秒
                if(nextTime > 0) { //如果有紀錄上次未生成怪物時間
                    random = nextTime; //覆蓋掉隨機時間
                    nextTime = 0; //重置下次生怪時間
                }
                Log.d("LOGDATE_Random", random + "");
                timeGap -= random; //利用時間差運算是否繼續生怪
                if (timeGap < 0) { //無時間繼續生怪
                    nextTime = (-timeGap); //將剩餘時間存入下次生怪時間
                    break; //跳出迴圈
                } else {
                    mobsSlotFilled_S1[i] = RandomTest.cId; //放入生怪ID至怪物陣列
                }
            }
        }
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
    }

    public void getItem(){
        GameDBHelper itemHelper = GameDBHelper.getInstance(this);
        Cursor citem = itemHelper.getReadableDatabase().query("mobsloot", null, null, null, null, null, null);

        citem.moveToFirst();
        for (int i = 0; i < citem.getCount(); i++) {
            String id = citem.getString(citem.getColumnIndex("_id_loot"));
            String imageR = citem.getString(citem.getColumnIndex("i_image_R"));
            String name = citem.getString(citem.getColumnIndex("i_name"));
            int price = citem.getInt(citem.getColumnIndex("i_price"));
            int heal = citem.getInt(citem.getColumnIndex("i_heal"));
            int poison = citem.getInt(citem.getColumnIndex("i_poison"));
            int cure = citem.getInt(citem.getColumnIndex("i_cure"));
            String desc = citem.getString(citem.getColumnIndex("i_desc"));
            id_lootList.add(id);
            i_image_RList.add(imageR);
            i_nametList.add(name);
            i_priceList.add(price);
            i_healList.add(heal);
            i_poisontList.add(poison);
            i_cureList.add(cure);
            i_descList.add(desc);

            citem.moveToNext();
        }
    }

    static void getRarity() {

        int[] raritygArray =  new int[rarityList.size()]; //ArrayList轉int[]
        for(int i=0; i< rarityList.size(); i++) {
            raritygArray[i] = Integer.parseInt(rarityList.get(i).toString());
        }
        RandomTest randomTest = new RandomTest(raritygArray); //將資料庫稀有度帶入隨機生怪
        randomTest.randomTest(); //執行運算方法
    }

    public void getPlayerStatus(){
        try {
            InputStream is = this.openFileInput("playerdata.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            String json = new String(buffer, "UTF-8");
            Gson gson = new Gson();
            Player playInfo = gson.fromJson(json, Player.class);
            Log.d(TAG2,"玩家名稱 : " + String.valueOf(playInfo.playerStatus.playerID));
            Log.d(TAG2,"玩家攻擊力 : " + String.valueOf(playInfo.playerStatus.playerATK));
            Log.d(TAG2,"玩家目前HP : " + String.valueOf(playInfo.playerStatus.playerCurrentHP));
            Log.d(TAG2,"玩家最大HP : " + String.valueOf(playInfo.playerStatus.playerMaxHP));


            Log.d(TAG2,"怪物圖鑑1001號生態介紹解鎖狀態 : " + String.valueOf(playInfo.playerMobsCollection.m1001.mobsBio));

            Log.d(TAG2,"關卡解鎖進度 : " + String.valueOf(playInfo.playerSceneProgress.Scene_1));

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
    public void copyJSONFile()
    {
        try {
            File f = new File(DBInfo.JSON_FILE);
            Log.d("JSON files", "copyFiles : "+DBInfo.JSON_FILE);
            if (! f.exists())
            {
                InputStream is = getResources().openRawResource(R.raw.playerdata);
                OutputStream os = new FileOutputStream(DBInfo.JSON_FILE);
                int read;
                Log.d("JSON files", "Start Copy");
                while ((read = is.read()) != -1)
                {
                    os.write(read);
                }
                Log.d("JSON files", "FilesCopied");
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