package com.example.alucardc.idlegame;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

import static com.example.alucardc.idlegame.Loading.DATE_PREF;
import static com.example.alucardc.idlegame.Loading.LAST_REG_HP_TIME;

public class MainActivity extends AppCompatActivity {

    int timeGap,nextTime; //運算用變數
    long newTime,oldTime; //運算用變數

    MediaPlayer bgm01;

    public static int hpRegKey = 10;   //回復HP的時間秒數

    int maxScenes = 2; //最大場景數
    public static int toScene = 1; //前往場景編號，初始為1
    int[] enterIconR = {R.drawable.scene_1_icon, R.drawable.scene_2_icon};
    int[] bgBottomR = {R.drawable.bg1_bottom, R.drawable.bg2_bottom};
    int[] tvScenesNumR = {R.drawable.tv_scenes1, R.drawable.tv_scenes2};
    int[] tvPlaceR = {R.drawable.tv_forest, R.drawable.tv_graveyard};
    /*切換場景資源*/

    String lastRegTime;
    long tempNowTime;
    String tempOldTime,tempNextTime="0"; //暫存用變數
    Calendar rightNow = Calendar.getInstance(); //提取時間用的方法

    int playerCurrentHP,playerMaxHP;

    String TAG = "MainDataTest";
    String TAG2 = "MainPlayerTest";
    TextView tvHP,tvEnemy,tvPollution;
    ImageView pollutionBar,enterIcon,bgBottom,tvScenesNum,tvPlace;
    LinearLayout playerStatusBar;
    public static TextView tvPlayerID,tvPlayerHP,tvPlayerMoney;
    public static ImageView playerHPBar;

    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  //隱藏狀態列
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        getData("1");
        findViews();
        getPlayerStatus();
        pollutionBar.setScaleX(0.5f);
        pollutionBar.setPivotX(pollutionBar.getX());
        countMobs();

//        Log.d(TAG, Loading.idList.toString());
//        Log.d(TAG, Loading.nameList.toString());
//        Log.d(TAG, Loading.healthPointList.toString());
//        Log.d(TAG, Loading.rarityList.toString());
//        Log.d(TAG, Loading.speedList.toString());
//        Log.d(TAG, Loading.qCountsList.toString());
//        Log.d(TAG, Loading.qTypesList.toString());
//        Log.d(TAG, Loading.qRangeList.toString());
//        Log.d(TAG, Loading.stunTimeList.toString());
//        Log.d(TAG, Loading.atkList.toString());
//        Log.d(TAG, Loading.imageList.toString());
//        int[] loots = (int[]) Loading.lootsList.get(0);
//        Log.d(TAG, loots[0]+", "+loots[1]+", "+loots[2]);
//        int[] lootsDP = (int[]) Loading.lootsDropRateList.get(0);
//        Log.d(TAG, lootsDP[0]+", "+lootsDP[1]+", "+lootsDP[2]);

    }

    void countMobs(){
        int sum=0;
        tvEnemy = (TextView) findViewById(R.id.tvEnemy);
        for(int i=0; i<6; i++){
            System.out.println(Loading.mobsSlotFilled_S1[i]);
            if( !Loading.mobsSlotFilled_S1[i].equals("0") ) {
                sum++;
            }
        }
        tvEnemy.setText("Enemy " + sum+"/ "+nextTime+"s");
    }

    public void findViews(){
        tvPlayerID = (TextView)findViewById(R.id.tvPlayerID);
        playerStatusBar = (LinearLayout)findViewById(R.id.playerStatusBar);
        tvPlayerHP = (TextView) playerStatusBar.findViewById(R.id.tvPlayerHP);
        tvPlayerMoney = (TextView) playerStatusBar.findViewById(R.id.tvPlayerMoney);
        tvHP = (TextView) playerStatusBar.findViewById(R.id.tvHP);
        tvEnemy = (TextView) findViewById(R.id.tvEnemy);
        tvPollution = (TextView) findViewById(R.id.tvPollution);
        playerHPBar = (ImageView) playerStatusBar.findViewById(R.id.playerHPBar);
        pollutionBar = (ImageView) findViewById(R.id.pollutionBar);
        enterIcon = (ImageView) findViewById(R.id.enterIcon);
        bgBottom = (ImageView) findViewById(R.id.bgBottom);
        tvScenesNum = (ImageView) findViewById(R.id.tvScenesNum);
        tvPlace = (ImageView) findViewById(R.id.tvPlace);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/bank_gothic_medium_bt.ttf");
        tvPlayerID.setTypeface(font);
        tvPlayerHP.setTypeface(font);
        tvPlayerMoney.setTypeface(font);
        tvHP.setTypeface(font);
        tvEnemy.setTypeface(font);
        tvPollution.setTypeface(font);
    }

    //=======================按鈕==========================
    public void enter_scene_1(View view){
        toScene = nowScene;
        Intent it = new Intent();
        it.setClass(MainActivity.this,pre_battle_scene.class);
        it.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(it);
    }
    //=======================按鈕==========================
    public void btnInventory(View v){
        showInventory();
        getItemCounts(this);
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

    int nowScene = 1;
    public void onChangeScenes (View v) {

        switch (v.getId()) {
            case R.id.btnRight:
                nowScene++;
                break;
            case R.id.btnLeft:
                nowScene--;
                break;
        }
        if (nowScene<1) nowScene =1;
        if (nowScene>=maxScenes) nowScene = maxScenes;
        enterIcon.setImageResource(enterIconR[nowScene-1]);
        bgBottom.setImageResource(bgBottomR[nowScene-1]);
        tvScenesNum.setImageResource(tvScenesNumR[nowScene-1]);
        tvPlace.setImageResource(tvPlaceR[nowScene-1]);
    }

    //=======================按鈕==========================

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
            Log.d(TAG2,"關卡二解鎖進度 : " + String.valueOf(playInfo.playerSceneProgress.Scene_1));

//            playInfo.playerStatus.playerMaxHP = 50;
            String json_2 = gson.toJson(playInfo);
            Log.d("JSON", json_2);
            playerCurrentHP = playInfo.playerStatus.playerCurrentHP;
            playerMaxHP = playInfo.playerStatus.playerMaxHP;
            regPlayerHPbyTime();

            tvPlayerID.setText("ID : " + playInfo.playerStatus.playerID);
            tvPlayerMoney.setText("" + playInfo.playerStatus.playerMoney);
            tvPlayerHP.setText(playInfo.playerStatus.playerCurrentHP + " / " + playInfo.playerStatus.playerMaxHP);

            if (playInfo.playerStatus.playerCurrentHP > 0) {
                playerHPBar.setScaleX((float) playInfo.playerStatus.playerCurrentHP / (float) playInfo.playerStatus.playerMaxHP);
                float playerHPBarLocateX = playerHPBar.getX();
                playerHPBar.setPivotX(playerHPBarLocateX);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateItem(int ItemId, int ItemCount){
        GameDBHelper itemHelper = GameDBHelper.getInstance(this);
        db = itemHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("i_count",ItemCount);
        db.update("mobsloot",values,"_id_loot="+ItemId,null);
    }

    public static void getItemCounts(Context context){
        Loading.i_countList.clear();
        GameDBHelper helper = GameDBHelper.getInstance(context);
        String[] column = {"i_count"};
        Cursor c = helper.getReadableDatabase().query("mobsloot", column, null, null, null, null, null);
//        Cursor c = helper.getReadableDatabase().query("mobsdata", column, "scene_" + scene + "=?", new String[]{"1"}, null, null, null);

        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i++) {
            int counts = c.getInt(c.getColumnIndex("i_count"));
            Loading.i_countList.add(counts);

            c.moveToNext();
        }
        c.close();
    }

    private void showInventory() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        // Create and show the dialog.
        android.app.DialogFragment newFragment = new Inventory();
        newFragment.show(ft, "dialog");
    }

    public static boolean settle =false;

    @Override
    protected void onPause() {
        super.onPause();
        bgm01.stop();
    }

    @Override
    protected void onResume() {

        bgm01 = MediaPlayer.create(MainActivity.this, R.raw.main_bgm_01);
        bgm01.setLooping(true);
        bgm01.start();

        super.onResume();
        if (settle){
            onSave();
            settle = false;
        }
        getPlayerStatus();
        rightNow = Calendar.getInstance();
        newTime = rightNow.getTimeInMillis();
        restorePrefs();
        countMobs();
        DateTest();
        onSave();
        Log.d("tvPlayerMoney",tvPlayerMoney.toString());
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
        SharedPreferences settings = getSharedPreferences(DATE_PREF, 0);
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

    public void regPlayerHPbyTime(){
        boolean isHealed = false;
        SharedPreferences settings = getSharedPreferences(DATE_PREF, 0);
        rightNow = Calendar.getInstance();
        tempNowTime = rightNow.getTimeInMillis();
        lastRegTime = settings.getString(LAST_REG_HP_TIME,"");
        if(lastRegTime.equals("")){
            settings.edit().putString(LAST_REG_HP_TIME,String.valueOf(tempNowTime)).commit();
            Log.d("HP_REG","沒有上次恢復時間,設定為現在時刻");
            lastRegTime = settings.getString(LAST_REG_HP_TIME,"");
        }
        int regTimeGap = (int)(tempNowTime - Long.parseLong(lastRegTime))/1000;
        int regCounts = regTimeGap / hpRegKey;
        Log.d("HP_REG","時間間隔 : "+regTimeGap + "  回復次數 : "+regCounts);
        for(int i = 0; i<regCounts ; i++){
            if(playerCurrentHP < playerMaxHP) {
                playerCurrentHP += 1;
                isHealed = true;
                Log.d("HP_REG","HP+1 這次有被治療");
            }
        }
        if(isHealed) {
            settings.edit().putString(LAST_REG_HP_TIME, String.valueOf(tempNowTime)).commit();
            Log.d("HP_REG","確認有被治療,上次治療時間寫入SharePref中");
            try {
                InputStream is = this.openFileInput("playerdata.json");
                byte[] buffer = new byte[is.available()];
                is.read(buffer);
                String json = new String(buffer, "UTF-8");
                Gson gson = new Gson();
                Player playInfo = gson.fromJson(json, Player.class);

                playInfo.playerStatus.playerCurrentHP = playerCurrentHP;
                Log.d("HP_REG","寫JSON部分,將JSON中目前HP設置為剛剛回復過的暫存血量");
                String json_2 = gson.toJson(playInfo);
//                Log.d("JSON", json_2);

                OutputStream os = new FileOutputStream(DBInfo.JSON_FILE);
                os.write(json_2.getBytes());
                os.close();
                is.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

//    public static void PlayBGM(final Context context, final SoundType type)
//    {
//        new Thread(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                MediaPlayer mediaPlayer = new MediaPlayer();
//                int resId = -1;
//                switch (type)
//                {
//                    case INCOMING_NOTIFICATION:
//                        resId=R.raw.noti_sound;
//                        break;
//                    case SEND_BETTING_SLIP:
//                        resId=R.raw.slip_sent;
//                        break;
//                    case TRIVIA_RIGHT_ANSWER:
//                        resId=R.raw.game_bonus;
//                        break;
//                    case TRIVIA_WRONG_ANSWER:
//                        resId=R.raw.whistle_referee_trivia_bad_answer;
//                        break;
//                }
//                if (resId != -1)
//                {
//                    mediaPlayer = MediaPlayer.create(context, resId);
//                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//                    mediaPlayer.setLooping(false);
//                    mediaPlayer.start();
//
//                    while (mediaPlayer.isPlaying() == true)
//                    {
//                    }
//                }
//            }
//        }).start();
//
//    }

    @Override
    public void onBackPressed() {  //返回鍵事件

    }
}


