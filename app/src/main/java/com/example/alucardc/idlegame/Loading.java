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
    /*↓SharedPreferences裡儲存的資料名稱↓*/
    public static final String DATE_PREF = "DATE_PREF";
    public static final String PREF_OLD_TIME = "DATE_OldTime";
    public static final String[] PREF_HASMOB = {"HasMob1","HasMob2","HasMob3","HasMob4","HasMob5","HasMob6"};
    public static final String DATE_NEXT_DATE = "DATE_NextTime";
    /*↑SharedPreferences裡儲存的資料名稱↑*/

    /*↓SQLite取出的資料↓*/
    public static ArrayList idList = new ArrayList();
    public static ArrayList rarityList = new ArrayList();
    /*↑SQLite取出的資料↑*/

    int timeGap,nextTime; //運算用變數
    long newTime,oldTime; //運算用變數
    String tempOldTime,tempNextTime="0"; //暫存用變數
    String[] tempHasMod = new String[6]; //暫存用變數
    Calendar rightNow = Calendar.getInstance(); //提取時間用的方法

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
    }

    @Override
    protected void onDestroy() {
        onSave();
        super.onDestroy();
    }

    int random;
    void DateTest () {
        timeGap = (int)(newTime-oldTime)/1000; //計算上次開啟遊戲與本次開啟遊戲時間差
        Log.d("LOGDATE_timeGap", timeGap + "");
        Log.d("LOGDATE_NextTime", nextTime+"");
        for(int i=0;i<6;i++) { //迴圈檢查，如怪物陣列滿了將不會進入
            if (tempHasMod[i].equals("0")) { //如果怪物陣列裡的ID為0
                getRarityData("1"); //取得場景1一隨機怪物ID (變數存在RandomTest.cId)
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

    public void getRarityData(String scene){
        GameDBHelper helper = GameDBHelper.getInstance(this);
        String[] column = { "_id", "rareWeight","scene_1", "scene_2"};
        Cursor c = helper.getReadableDatabase().query("mobsdata", column, "scene_" + scene + "=?", new String[]{"1"}, null, null, null);

        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i++) {
            String id = c.getString(c.getColumnIndex("_id"));
            int rarity = c.getInt(c.getColumnIndex("rareWeight"));
            idList.add(id);
            rarityList.add(rarity);
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

    public void copyDBFile() {
        try {
            File f = new File(DBInfo.DB_FILE);
            Log.d("GameDBHelper", "" + DBInfo.DB_FILE);
            if (!f.exists()) {
                InputStream is = getResources().openRawResource(R.raw.idlegame);
                OutputStream os = new FileOutputStream(DBInfo.DB_FILE);
                int read;
                while ((read = is.read()) != -1) {
                    os.write(read);
                }
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
