package com.example.alucardc.idlegame;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

public class pre_battle_scene extends AppCompatActivity {

    Context context = this;
    int timeGap,nextTime; //運算用變數
    long newTime,oldTime; //運算用變數
    String tempOldTime,tempNextTime="0"; //暫存用變數
    Calendar rightNow = Calendar.getInstance(); //提取時間用的方法

    LinearLayout playerStatusBar;
    TextView tvHP,tvPlayerID;
    public static TextView tvPlayerHP,tvPlayerMoney;
    public static ImageView playerHPBar;

    String TAG = "pre_battle_scene";
    ImageView modView[] = new ImageView[6];
    ImageView prepareBG;
    int[] prepareBGR = {R.drawable.bg001, R.drawable.bg002}; //準備畫面背景資源

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  //隱藏狀態列
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_battle_scene);
        findsViews();
        Loading.checkPoint = true;
        getPlayerStatus();
        randomViewPosition();

        Context context = this;
        MainActivity.getItemCounts(context);
    }

    void findsViews() {
        modView[0] = (ImageView)findViewById(R.id.mobView1);
        modView[1] = (ImageView)findViewById(R.id.mobView2);
        modView[2] = (ImageView)findViewById(R.id.mobView3);
        modView[3] = (ImageView)findViewById(R.id.mobView4);
        modView[4] = (ImageView)findViewById(R.id.mobView5);
        modView[5] = (ImageView)findViewById(R.id.mobView6);

        tvPlayerID = (TextView)findViewById(R.id.tvPlayerID);
        playerStatusBar = (LinearLayout)findViewById(R.id.playerStatusBar);
        tvHP = (TextView) playerStatusBar.findViewById(R.id.tvHP);
        tvPlayerHP = (TextView) playerStatusBar.findViewById(R.id.tvPlayerHP);
        tvPlayerMoney = (TextView) playerStatusBar.findViewById(R.id.tvPlayerMoney);
        playerHPBar = (ImageView) playerStatusBar.findViewById(R.id.playerHPBar);

        prepareBG = (ImageView) findViewById(R.id.prepareBG);
        prepareBG.setImageResource(prepareBGR[MainActivity.toScene-1]);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/bank_gothic_medium_bt.ttf");
        tvPlayerID.setTypeface(font);
        tvPlayerHP.setTypeface(font);
        tvPlayerMoney.setTypeface(font);
        tvHP.setTypeface(font);
    }

    /*------按鈕------*/

    public void btnInventory(View v){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        // Create and show the dialog.
        android.app.DialogFragment newFragment = new Inventory();
        newFragment.show(ft, "dialog");
        MainActivity.getItemCounts(this);
    }

    public void btnHandBook(View v){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        // Create and show the dialog.
        android.app.DialogFragment newFragment = new MobsHandBook();
        newFragment.show(ft, "dialog");
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

    /*------按鈕------*/

    public void randomViewPosition(){
        float posX,posY;
        float afterPosX,afterPosY;
        float tempX[] = new float[Loading.mobsSlotFilled_S1.length];
        float tempY[] = new float[Loading.mobsSlotFilled_S1.length];

        for(int i = 0; i<Loading.mobsSlotFilled_S1.length; i++){
            posX = modView[i].getX();   //取得第1-6個modView位置x值
            posY = modView[i].getY();   //取得第1-6個modView位置y值

            tempX[i] = (float) (Math.random()*Loading.metricsX*1.8f) -Loading.metricsX;
            if ( MainActivity.toScene == 1 )
                tempY[i] = (float) (Math.random()*650) -150;
            else if ( MainActivity.toScene == 2 )
                tempY[i] = (float) (Math.random()*900) -150;

            if(tempY[i] < 100){
                modView[i].setScaleX(0.5f);
                modView[i].setScaleY(0.5f);
            }else if(tempY[i] < 250){
                modView[i].setScaleX(0.7f);
                modView[i].setScaleY(0.7f);
            }else if(tempY[i] < 370){
                modView[i].setScaleX(0.8f);
                modView[i].setScaleY(0.8f);
            }

            modView[i].setTranslationX(tempX[i]);
            modView[i].setTranslationY(tempY[i]);

            afterPosX = modView[i].getX();   //取得第1-6個modView位置x值
            afterPosY = modView[i].getY();   //取得第1-6個modView位置y值

            for(int j = 0; j<Loading.mobsSlotFilled_S1.length; j++){
                if(modView[j].getY() > afterPosY){
                    Log.d("Position", "i=" + i + " j=" + j + " 計算後的當前圖片Y:" + afterPosY + "  比對的圖片J的Y值:" + modView[j].getY());
                    modView[j].bringToFront();
                }
            }
        }
    }

    public void getPlayerStatus(){
        try {
            InputStream is = this.openFileInput("playerdata.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            String json = new String(buffer, "UTF-8");
            Gson gson = new Gson();
            Player playInfo = gson.fromJson(json, Player.class);

            playInfo.playerStatus.playerMaxHP = 50;
            String json_2 = gson.toJson(playInfo);
            Log.d("JSON", json_2);

            tvPlayerID.setText("ID : " + playInfo.playerStatus.playerID);
            tvPlayerMoney.setText("" + playInfo.playerStatus.playerMoney);
            tvPlayerHP.setText("" + playInfo.playerStatus.playerCurrentHP + " / " + playInfo.playerStatus.playerMaxHP);

            if (playInfo.playerStatus.playerCurrentHP > 0) {
                playerHPBar.setScaleX((float) playInfo.playerStatus.playerCurrentHP / (float) playInfo.playerStatus.playerMaxHP);
                float playerHPBarLocateX = playerHPBar.getX();
                playerHPBar.setPivotX(playerHPBarLocateX);
            }

        } catch (IOException e) {
            e.printStackTrace();
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
        newTime = Calendar.getInstance().getTimeInMillis();
        restorePrefs();
        DateTest();
        setVisible();
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
                random = (int) (Math.random() * Loading.randMobMaxTime - Loading.randMobMinTime) - Loading.randMobMinTime;
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
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }
}
