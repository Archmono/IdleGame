package com.example.alucardc.idlegame;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.alucardc.idlegame.Loading.DATE_PREF;
import static com.example.alucardc.idlegame.Loading.LAST_REG_HP_TIME;

public class battle_scene extends AppCompatActivity {

    private SQLiteDatabase db;
    MediaPlayer battle_bgm_01;

    ProgressBar PB;
    int prepareTime = 3;
    public static boolean condition = true;
    Timer timerPrepare = new Timer();
    Timer timerMobsProgress = new Timer();
    boolean introFight = true;
    boolean isPaused = false;
    boolean endFight = false;
    int playerCurrentHP;    //玩家目前血量,從JSON讀取值暫存用
    int playerMaxHP;        //玩家最大HP
    int[] currentActionTime = new int[6];
    int[] currentStunTime = new int[6];

    //音效用變數
    SoundPool soundPool;
    private int getHit,hitMobs;
    //音效用變數

    //時間變數
    Calendar rightNow = Calendar.getInstance();
    long tempNowTime;
    //時間變數

    TextView tvPrepareFight,playerHP;
    LinearLayout questionLayout,blockView;
    LinearLayout[] mobs;
    ProgressBar[] actionbar;
    ImageView[] playerCtrBut,questionImg,mobsImage,mobsHPbar;
    TextView[] mobsName;
    int[] mobsQid = {R.drawable.fire,R.drawable.water,R.drawable.wood,R.drawable.light,R.drawable.dark,R.drawable.heart};
    int[] playerCtrB = {R.id.playerCtrB1,R.id.playerCtrB2,R.id.playerCtrB3,R.id.playerCtrB4,R.id.playerCtrB5,R.id.playerCtrB6};
    int[] mobsInclude = {R.id.mobsInclude1,R.id.mobsInclude2,R.id.mobsInclude3,R.id.mobsInclude4,R.id.mobsInclude5,R.id.mobsInclude6};
    GameTest gameTest;

    int[] elementTypes = new int[6];               //題目屬性種類
    int[] elementQRange = new int[6];        //題目難度,屬性球數量3-6
    int[] mobsMaxHP = new int[6];
    int[] mobsCurrentHP = new int[6];
    int[] mobsATK = new int[6];
    int[] mobsSpeed = new int[6];
    int[] mobsStunTime = new int[6];

    //玩家狀態區
    LinearLayout playerStatusBar;
    TextView tvHP,tvPlayerID,tvPlayerHP,tvPlayerMoney;
    ImageView playerHPBar;
    //玩家狀態區

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 1) {
                PB.setVisibility(View.VISIBLE);

            } else if(msg.what == 2) {
                tvPrepareFight.setVisibility(View.GONE);
                PB.setVisibility(View.GONE);
                blockView.setVisibility(View.GONE);
                timerPrepare.cancel();

            } else if(msg.what == 3) {
                tvPrepareFight.setVisibility(View.VISIBLE);
                blockView.setVisibility(View.VISIBLE);
                tvPrepareFight.setText("暫停遊戲");

            }  else if(msg.what == 9) {
                tvPlayerHP.setText("HP : " + playerCurrentHP + " / " + playerMaxHP);
                if (playerCurrentHP > 0) { //更新玩家血條
                    playerHPBar.setScaleX((float) playerCurrentHP / (float) playerMaxHP);
                    float playerHPBarLocateX = playerHPBar.getX();
                    playerHPBar.setPivotX(playerHPBarLocateX);
                }
                soundPool.play(getHit, 1.0F, 1.0F, 0, 0, 1.0F);
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  //隱藏狀態列
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle_scene);
        findViews();
        setMobs();
        mobsSetOnClickListener();
        getPlayerStatus();

        blockView.setOnClickListener(blockListener);
//        loadData();

        Typeface font = Typeface.createFromAsset(this.getAssets(), "fonts/witches_magic.ttf");
        playerHP.setTypeface(font);

        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 5);
        getHit = soundPool.load(this, R.raw.get_hitted, 1);
        hitMobs = soundPool.load(this, R.raw.player_attack, 1);
    }

    @Override
    protected void onPause() {
        battle_bgm_01.pause();
        if(!endFight){
            Message msg = mHandler.obtainMessage();
            msg.what = 3;                           //msg3訊息內容為遮擋view的顯示 & 中央提示暫停文字顯示
            msg.sendToTarget();
            condition = false;             //onPause時把怪物計時條timer取消
            isPaused = true;
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        battle_bgm_01 = MediaPlayer.create(battle_scene.this, R.raw.battle_bgm_01);
        battle_bgm_01.setLooping(true);
        battle_bgm_01.start();
        super.onResume();
    }

    void setMobs() {
        countMobs();
        for(int i=0; i<Loading.mobsSlotFilled_S1.length; i++) {
            if(Loading.mobsSlotFilled_S1[i].equals("0"))
                mobs[i].setVisibility(View.GONE);
            else {
                int inx = Loading.idList.indexOf(Loading.mobsSlotFilled_S1[i]);
                int mobs_img_resID = getResources().getIdentifier((String)Loading.imageList.get(inx), "drawable", getPackageName());
                mobsImage[i].setImageResource(mobs_img_resID);
                elementTypes[i] = (int)Loading.qTypesList.get(inx);
                elementQRange[i] = (int)Loading.qRangeList.get(inx);
                mobsName[i].setText((String)Loading.nameList.get(inx));
                mobsMaxHP[i] = (int)Loading.healthPointList.get(inx);
                mobsCurrentHP[i] = (int)Loading.healthPointList.get(inx);
                mobsATK[i] = (int)Loading.atkList.get(inx);
                mobsSpeed[i] = (int)Loading.speedList.get(inx);
                mobsStunTime[i] = (int)Loading.stunTimeList.get(inx);
                currentStunTime[i] = (int)Loading.stunTimeList.get(inx);

                currentActionTime[i] = mobsSpeed[i];
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

            String json_2 = gson.toJson(playInfo);
//            Log.d("JSON", json_2);

            tvPlayerID.setText("ID : " + playInfo.playerStatus.playerID);
            tvPlayerMoney.setText("" + playInfo.playerStatus.playerMoney);
            tvPlayerHP.setText("HP : " + playInfo.playerStatus.playerCurrentHP + " / " + playInfo.playerStatus.playerMaxHP);
            playerCurrentHP = playInfo.playerStatus.playerCurrentHP;
            playerMaxHP = playInfo.playerStatus.playerMaxHP;

            if (playerCurrentHP > 0) { //更新玩家血條
                playerHPBar.setScaleX((float) playerCurrentHP / (float) playerMaxHP);
                float playerHPBarLocateX = playerHPBar.getX();
                playerHPBar.setPivotX(playerHPBarLocateX);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updatePlayerHP(){
        try {
            InputStream is = this.openFileInput("playerdata.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            String json = new String(buffer, "UTF-8");
            Gson gson = new Gson();
            Player playInfo = gson.fromJson(json, Player.class);

            playInfo.playerStatus.playerCurrentHP = playerCurrentHP;
            String json_2 = gson.toJson(playInfo);
//            Log.d("JSON", json_2);

            OutputStream os = new FileOutputStream(DBInfo.JSON_FILE);
            os.write(json_2.getBytes());
            os.close();
            is.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void unlockStatus(int mobsID, String unlockKey, int unlockValue){
        Context context = this;
        GameDBHelper itemHelper = GameDBHelper.getInstance(context);
        db = itemHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(unlockKey,unlockValue);
        db.update("mobshandbook",values,"_id="+mobsID,null);
        MainActivity.getItemCounts(context);
    }

    void findViews(){
        PB = (ProgressBar)findViewById(R.id.pbtest);
        blockView = (LinearLayout)findViewById(R.id.fightBlockView);
        tvPrepareFight = (TextView)findViewById(R.id.tvPrepareFight);
        questionLayout = (LinearLayout)findViewById(R.id.questionLayout);
        playerHP = (TextView)findViewById(R.id.healthPointCounts);

        mobs = new LinearLayout[6];
        mobsName = new TextView[6];
        mobsImage = new ImageView[6];
        actionbar = new ProgressBar[6];
        mobsHPbar = new ImageView[6];
        questionImg = new ImageView[6];
        playerCtrBut = new ImageView[6];

        tvPlayerID = (TextView)findViewById(R.id.tvPlayerID);
        playerStatusBar = (LinearLayout)findViewById(R.id.playerStatusBar);
        tvHP = (TextView) playerStatusBar.findViewById(R.id.tvHP);
        tvPlayerHP = (TextView) playerStatusBar.findViewById(R.id.tvPlayerHP);
        tvPlayerMoney = (TextView) playerStatusBar.findViewById(R.id.tvPlayerMoney);
        playerHPBar  = (ImageView) playerStatusBar.findViewById(R.id.playerHPBar);


        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/bank_gothic_medium_bt.ttf");
        tvPlayerID.setTypeface(font);
        tvPlayerHP.setTypeface(font);
        tvPlayerMoney.setTypeface(font);
        tvHP.setTypeface(font);

        for (int i = 0; i < 6; i++)
        {
            mobs[i] = (LinearLayout)findViewById(mobsInclude[i]);
            Log.d("mobs", ""+mobsInclude[i]);
            questionImg[i] = new ImageView(this);
            playerCtrBut[i] = (ImageView)findViewById(playerCtrB[i]);
            playerCtrBut[i].setOnClickListener(playerCtrListener);
            mobsName[i] = mobs[i].findViewById(R.id.mobsName);
            mobsImage[i] = mobs[i].findViewById(R.id.mobsImage);
            actionbar[i] = mobs[i].findViewById(R.id.progressBarAction);
            mobsHPbar[i] = mobs[i].findViewById(R.id.imageViewBarHP);

        }

    }

    public int mobIndex;
    void mobsSetOnClickListener(){
        mobsImage[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modClickEvent(0);
            }
        });
        mobsImage[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modClickEvent(1);
            }
        });
        mobsImage[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modClickEvent(2);
            }
        });
        mobsImage[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modClickEvent(3);
            }
        });
        mobsImage[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modClickEvent(4);
            }
        });
        mobsImage[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modClickEvent(5);
            }
        });
    }


    boolean[] attackable = {false,false,false,false,false,false};
    boolean[] isChecked = {false,false,false,false,false,false};

    @TargetApi(Build.VERSION_CODES.M)
    void modClickEvent(int mobIndex) {
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        this.mobIndex = mobIndex;
        Log.d("isChecked", isChecked[mobIndex]+"");
        if (attackable[mobIndex]) { //可被攻擊
            mobsCurrentHP[mobIndex]--;   //-玩家攻擊力
            if (mobsCurrentHP[mobIndex] > 0) {
                mobsHPbar[mobIndex].setScaleX((float) mobsCurrentHP[mobIndex] / (float) mobsMaxHP[mobIndex]);
                float mobs1HPbarLocateX = mobsHPbar[mobIndex].getX();
                mobsHPbar[mobIndex].setPivotX(mobs1HPbarLocateX);
                mobs[mobIndex].startAnimation(shake);
                soundPool.play(hitMobs, 1.0F, 1.0F, 0, 0, 1.0F);    //播放打擊音效
            } else {
                mobs[mobIndex].setVisibility(View.GONE);
//                mob1ActionTimer.cancel();
            }
        } else if ( !isChecked[mobIndex] ) { //非當前目標對象
            Log.d("isChecked", isChecked[mobIndex]+"");
            gameTest = new GameTest(elementTypes[mobIndex],elementQRange[mobIndex]);
            gameTest.count();
            questionLayout.setX(mobs[mobIndex].getX());
            questionLayout.setY(mobs[mobIndex].getY());
            isChecked = new boolean[]{false,false,false,false,false,false};
        }
        isChecked[mobIndex] = true; //設為當前目標對象
    }

    public class GameTest{

        int elementTypes;               //題目屬性種類
        int elementQRange;       //題目難度,屬性球數量3-6

        public GameTest(int elementTypes, int elementQRange)
        {
            this.elementTypes = elementTypes;
            this.elementQRange = elementQRange;
        }

        @TargetApi(Build.VERSION_CODES.M)
        @RequiresApi(api = Build.VERSION_CODES.M)
        void count()
        {
            int[] elementTypesCons = {0,1,2,3,4,5};
            int[] setTypes = new int[elementTypes];
            for (int i=0; i < setTypes.length; i++) {
                int temp = elementTypesCons[i];
                int randMob = (int) (Math.random() * 6);
                setTypes[i] = elementTypesCons[randMob];
                elementTypesCons[randMob] = temp;
            }
            if ( !isChecked[mobIndex] ) {
                qte = new ArrayList<>();
                questionLayout.removeAllViews();
                for (int i = 0; i < elementQRange; i++) {
                    int random = (int) (Math.random() * elementTypes);
                    qte.add(i, (setTypes[random]));
                    questionImg[i].setImageResource(mobsQid[qte.get(i)]);
                    questionImg[i].setLayoutParams(new LinearLayout.LayoutParams(75 , 75));
                    questionImg[i].setScaleType(ImageView.ScaleType.CENTER_CROP);
                    questionLayout.addView(questionImg[i]);
                    Log.d("qte", qte.toString());
                }
                questionImg[0].setLayoutParams(new LinearLayout.LayoutParams(100 , 100));
//                questionImg[0].setForegroundGravity(Gravity.TOP);
            }
        }
    }

    public boolean right ;
    public ArrayList<Integer> qte = new ArrayList<>();
    int now = 0; //玩家qte進度
    int ans; //判斷玩家按的按鈕

    View.OnClickListener playerCtrListener = new View.OnClickListener () {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.playerCtrB1 : ans = 0; break;
                case R.id.playerCtrB2 : ans = 1; break;
                case R.id.playerCtrB3 : ans = 2; break;
                case R.id.playerCtrB4 : ans = 3; break;
                case R.id.playerCtrB5 : ans = 4; break;
                case R.id.playerCtrB6 : ans = 5; break;
            }

            if(now < gameTest.elementQRange) { //判斷是否正在qte
                if (ans == qte.get(now)) {
                    right = true;
                    now++;
                } else {
                    right = false;
                    now = 0;
                }

                if (right && questionLayout.getChildCount() > 1) {
                    questionLayout.removeViewAt(0);
                    questionImg[now].setLayoutParams(new LinearLayout.LayoutParams(100 , 100));
                } else if (right && questionLayout.getChildCount() == 1) { //清除最後一顆屬性時
                    //接入怪物可被攻擊狀態
                    questionLayout.removeViewAt(0);
                    attackable[mobIndex] = true;
                    now = 0;

                } else if(!attackable[mobIndex]){ //答錯
                    now = 0;
                    questionLayout.removeAllViews();
                    for (int i = 0; i < gameTest.elementQRange; i++) { //重置題目
                        questionImg[i].setImageResource(mobsQid[qte.get(i)]);
                        questionLayout.addView(questionImg[i]);
                    }
                }
            }
        }
    };


    public View.OnClickListener blockListener = new View.OnClickListener(){ //為畫面遮罩設置點擊監聽器
        @Override
        public void onClick(View view) {
        if(introFight){
            tvPrepareFight.setText("");                                         //點擊時將中央文字欄位清空
            BattlePrepareTimerTask bpTimerTask = new BattlePrepareTimerTask();  //new新的準備畫面timerTask
            timerPrepare.schedule(bpTimerTask, 500, 1000);                      //為timer timerPrepare設置timerTask
            introFight = false;                                                 //點擊後進入場景,將檢查值設置為false
            Log.d("Click test", "view block is clicked");
            Log.d("TAG","condition:" + condition+", !intro fight" + introFight + ", isPaused" + isPaused);
        } else {
            if(isPaused) {
                Message msg = mHandler.obtainMessage();
                msg.what = 2;                               //發送msg2,將暫停用遮擋畫面設定為GONE的可見度
                msg.sendToTarget();
                condition = true;
                Log.d("TAG","condition:" + condition+", !intro fight" + introFight + ", isPaused" + isPaused + ", msg=2");
            }
        }
        }
    };

    public class BattlePrepareTimerTask extends TimerTask{
        public void run() {
            if(prepareTime > 0) {
                Message msg = mHandler.obtainMessage();
                msg.what = 1;
                msg.sendToTarget();
                PB.setProgress(prepareTime);
                prepareTime--;
                Log.d("戰前準備時間", prepareTime+"");
            }else{
                timerPrepare.cancel();                      //準備倒數小於0,戰鬥開始後把準備時間用timer取消 (msg2中也有取消,測試效果中暫時保留)
                Log.d("isPaused", "倒數時間為0,isPaused即將設定為false : "+isPaused);
                condition = true;
                MobsProgressTimerTask mobsProgressTimerTask = new MobsProgressTimerTask();
                timerMobsProgress.schedule(mobsProgressTimerTask,500,200);

                Message msg = mHandler.obtainMessage();
                msg.what = 2;                               //發送msg2,將暫停用遮擋畫面設定為GONE的可見度
                msg.sendToTarget();

            }
        }
    }

    public class MobsProgressTimerTask extends TimerTask {
        public void run() {
            if (condition) {
                for (int i = 0; i < mobSum; i++) {
                    if (!attackable[i]) {
                        actionbar[i].setProgress(currentActionTime[i] * 100 / mobsSpeed[i]);
//                       Log.d("Speed",currentActionTime[0] +"  "+ mobsSpeed[0]);
                        if (mobsCurrentHP[i] > 0) {
                            currentActionTime[i] -= 200;
                        }

                    } else {
                        currentStunTime[i] -= 200;
                    }
                    if (currentActionTime[i] <= 0) {
                        playerCurrentHP -= mobsATK[i];
                        currentActionTime[i] = mobsSpeed[i];
                        Message msg = mHandler.obtainMessage();
                        msg.what = 9;
                        msg.sendToTarget();
                    }
                    if (currentStunTime[i] <= 0) {
                        attackable[i] = false;
                        isChecked[i] = false;
                        currentActionTime[i] = mobsSpeed[i];
                        currentStunTime[i] = mobsStunTime[i];
                    }
                }
            }
            if (mobsCurrentHP[0] <= 0 &&
                    mobsCurrentHP[1] <= 0 &&
                    mobsCurrentHP[2] <= 0 &&
                    mobsCurrentHP[3] <= 0 &&
                    mobsCurrentHP[4] <= 0 &&
                    mobsCurrentHP[5] <= 0) {
                LootFail lootFail = new LootFail();
                lootFail.count();
                Log.d("TAG", LootFail.lootSet+"");
                updatePlayerHP();   //更新目前血量到JSON
                for(int i = 0; i<mobSum; i++){
                    String str = Loading.mobsSlotFilled_S1[i];
                    unlockStatus(Integer.parseInt(str),"image",1);
                    unlockStatus(Integer.parseInt(str),"name",1);
                }
                showSettlement();
                resetLastRegTime();
                timerMobsProgress.cancel();
                endFight = true;
            }
        }
    }

    public void btnBattlePause(View v){
        condition = false;
        isPaused = true;
        Message msg = mHandler.obtainMessage();
        msg.what = 3;                           //msg3訊息內容為遮擋view的顯示 & 中央提示暫停文字顯示
        msg.sendToTarget();
    }

    public void btnDoom(View v){
        for(int i = 0; i<mobSum; i++){
            mobsCurrentHP[i] = 0;
        }
    }

    private void showSettlement() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        // Create and show the dialog.
        android.app.DialogFragment newFragment = new SettlementDialog();
        newFragment.show(ft, "dialog");
    }

    public static int mobSum=0;
    void countMobs() {
        mobSum = 0;
        for (int i = 0; i < 6; i++) {
            System.out.println(Loading.mobsSlotFilled_S1[i]);
            if (!Loading.mobsSlotFilled_S1[i].equals("0")) {
                mobSum++;
            }
        }
    }

    void resetLastRegTime(){
        rightNow = Calendar.getInstance();
        tempNowTime = rightNow.getTimeInMillis();
        SharedPreferences settings = getSharedPreferences(DATE_PREF, 0);
        settings.edit().putString(LAST_REG_HP_TIME,String.valueOf(tempNowTime)).commit();
    }

    @Override
    public void onBackPressed() {  //返回鍵事件

    }
}