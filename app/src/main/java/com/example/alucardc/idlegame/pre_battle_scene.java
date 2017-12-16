package com.example.alucardc.idlegame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

public class pre_battle_scene extends AppCompatActivity {

    int timeGap,nextTime; //運算用變數
    long newTime,oldTime; //運算用變數
    String tempOldTime,tempNextTime="0"; //暫存用變數
    Calendar rightNow = Calendar.getInstance(); //提取時間用的方法

    String TAG = "pre_battle_scene";
    ImageView modView[] = new ImageView[6];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  //隱藏狀態列
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_battle_scene);
        findsViews();
        setVisible();
    }

    void findsViews() {
        modView[0] = (ImageView)findViewById(R.id.mobView1);
        modView[1] = (ImageView)findViewById(R.id.mobView2);
        modView[2] = (ImageView)findViewById(R.id.mobView3);
        modView[3] = (ImageView)findViewById(R.id.mobView4);
        modView[4] = (ImageView)findViewById(R.id.mobView5);
        modView[5] = (ImageView)findViewById(R.id.mobView6);
    }

    public void start_fight(View v){
        if(!Loading.mobsSlotFilled_S1[0].equals("0")) {
            Intent it = new Intent();
            it.setClass(this, battle_scene.class);
            startActivity(it);
        } else {
            Toast.makeText(pre_battle_scene.this, "當前場景沒有怪物", Toast.LENGTH_SHORT).show();
        }
    }

//    public void onClear (View v) {
//        for (int i=0;i<6;i++){
//            mob[i].setVisibility(View.INVISIBLE);
//            tempHasMod[i] = "0";
//        }
//        onSave();
//    }

    void setVisible () {
        for(int i=0; i<Loading.mobsSlotFilled_S1.length; i++) {
            if(Loading.mobsSlotFilled_S1[i].equals("0"))
                modView[i].setVisibility(View.INVISIBLE);
            else if (Loading.mobsSlotFilled_S1[i]==null) {
                Toast.makeText(pre_battle_scene.this, "資料未存取", Toast.LENGTH_SHORT).show();
            } else {
                int inx = Loading.idList.indexOf(Loading.mobsSlotFilled_S1[i]);
                int mobs_img_resID = getResources().getIdentifier((String)Loading.imageList.get(inx), "drawable", getPackageName());
                modView[i].setImageResource(mobs_img_resID);
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        rightNow = Calendar.getInstance();
        newTime = rightNow.getTimeInMillis();
        restorePrefs();
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
