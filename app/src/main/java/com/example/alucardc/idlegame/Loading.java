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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

/**
 * Created by acer on 2017/12/2.
 */

public class Loading extends Activity {

    public static final String DATE_PREF = "DATE_PREF";
    public static final String PREF_OLD_TIME = "DATE_OldTime";
    public static final String[] PREF_HASMOB = {"HasMob1","HasMob2","HasMob3","HasMob4","HasMob5","HasMob6"};
    public static final String DATE_NEXT_DATE = "DATE_NextTime";

    public static ArrayList idList = new ArrayList();
    ArrayList nameList = new ArrayList();
    ArrayList healthPointList = new ArrayList();
    public static ArrayList rarityList = new ArrayList();

    int timeGap,nextTime;
    long newTime,oldTime;
    String tempOldTime,tempNextTime="0";
    String[] tempHasMod = new String[6];
    Calendar rightNow = Calendar.getInstance();

    private void restorePrefs() { //讀取的位置
        tempHasMod = new String[]{"0", "0", "0", "0", "0", "0"}; //暫存重置不知為何new在class外部會有問題所以先寫在這裡
        SharedPreferences settings = getSharedPreferences(DATE_PREF, 0);
        for(int i=0; i<6; i++){
            tempHasMod[i] = settings.getString(PREF_HASMOB[i], "");
        }
        tempNextTime = settings.getString(DATE_NEXT_DATE, "");
        tempOldTime = settings.getString(PREF_OLD_TIME, "");
        if(tempOldTime.equals("")) //玩家第一次開啟沒有舊時間，就將oldTime設置為now
            oldTime = rightNow.getTimeInMillis();
        else {
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
        newTime = rightNow.getTimeInMillis();
        restorePrefs();
        getRarityData("1","0");
        DateTest();
        onSave();
    }

    @Override
    protected void onDestroy() {
        onSave();
        super.onDestroy();
    }
    int random;
    void DateTest () {
        timeGap = (int)(newTime-oldTime)/1000;
        Log.d("LOGDATE_timeGap", timeGap + "");
        Log.d("LOGDATE_NextTime", nextTime+"");
        for(int i=0;i<6;i++) {
            if (tempHasMod[i].equals("0")) {
                random = (int) (Math.random() * 25) + 5; //隨機 5-30
                if(nextTime > 0) {
                    random = nextTime;
                    nextTime = 0;
                }
                Log.d("LOGDATE_Random"+i+":", random + "");
                timeGap -= random;
                if (timeGap < 0) {
                    nextTime = (-timeGap);
                    break;
                } else {
                    tempHasMod[i] = "1";
                }
            }
        }
    }

    public void onClick (View v) {
        Intent i = new Intent(Loading.this, MainActivity.class);
        startActivity(i);
    }


    public void getRarityData(String scene1, String scene2){
        GameDBHelper helper = GameDBHelper.getInstance(this);
        String[] column = { "_id", "rareWeight","scene_1", "scene_2"};
        Cursor c = helper.getReadableDatabase().query("mobsdata", column, "scene_1=? AND scene_2=?", new String[]{scene1,scene2}, null, null, null);

        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i++) {
            String id = c.getString(c.getColumnIndex("_id"));
//            Log.d(TAG, "id"+id);
            int rarity = c.getInt(c.getColumnIndex("rareWeight"));
//            Log.d(TAG, "rarity"+rarity);
            idList.add(id);
            rarityList.add(rarity);
            c.moveToNext();
        }
        c.close();
        Object[] objectList = rarityList.toArray();
        int[] raritygArray =  Arrays.copyOf(objectList,objectList.length,int[].class);
        RandomTest randomTest = new RandomTest(raritygArray);
        randomTest.randomTest();
    }
}
