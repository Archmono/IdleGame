package com.example.alucardc.idlegame;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.LogRecord;

public class MainActivity extends AppCompatActivity {

    ProgressBar PB;
    int prepareTime = 3;
    boolean introFight = true;
    Timer timer01 = new Timer();
    Timer mobsTimer01 = new Timer();
    int playerCurrentHP = 100;  //暫時設定的玩家HP值,待完成
    int currentActionTime;

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

    int mobsMaxHP1,mobsMaxHP2,mobsMaxHP3,mobsMaxHP4,mobsMaxHP5,mobsMaxHP6;
    int mobsCurrentHP1,mobsCurrentHP2,mobsCurrentHP3,mobsCurrentHP4,mobsCurrentHP5,mobsCurrentHP6;
    int mobsATK1,mobsATK2,mobsATK3,mobsATK4,mobsATK5,mobsATK6;
    int mobsSpeed1,mobsSpeed2,mobsSpeed3,mobsSpeed4,mobsSpeed5,mobsSpeed6;

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 1){
                PB.setVisibility(View.VISIBLE);
            }else if(msg.what == 2){
                tvPrepareFight.setVisibility(View.GONE);
                PB.setVisibility(View.GONE);
                blockView.setVisibility(View.GONE);
            }else if(msg.what == 3){
                playerHP.setText("HP : " + playerCurrentHP);
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  //隱藏狀態列
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        mobsSetOnClickListener();

        blockView.setOnClickListener(blockListener);
        loadData();

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

    void mobsSetOnClickListener(){
        mobs[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (attackable) {
                    mobsCurrentHP1--;   //-玩家攻擊力
                    if (mobsCurrentHP1 > 0) {
                        mobsHPbar[0].setScaleX((float) mobsCurrentHP1 / (float) mobsMaxHP1);
                        float mobs1HPbarLocateX = mobsHPbar[0].getX();
                        mobsHPbar[0].setPivotX(mobs1HPbarLocateX);
                    } else {
                        mobs[0].setVisibility(View.GONE);
                        mob1ActionTimer.cancel();
                    }
                } else if (now == 0) {
                        gameTest.count();
                }
            }
        });
        mobs[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        mobs[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        mobs[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        mobs[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        mobs[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    public class GameTest{

        int elementTypes;               //題目屬性種類
        int elementQRange;       //題目難度,屬性球數量3-6

        public GameTest(int elementTypes, int elementQRange)
        {
            this.elementTypes = elementTypes;
            this.elementQRange = elementQRange;
        }

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
            if (questionLayout.getChildCount() == 0) {
                for (int i = 0; i < elementQRange; i++) {
                    int random = (int) (Math.random() * elementTypes);
                    qte.add(i, (setTypes[random]));
                    questionImg[i].setImageResource(mobsQid[qte.get(i)]);
                    questionLayout.addView(questionImg[i]);
                    Log.d("qte", qte.toString());
                }
            }
        }
    }

    public boolean attackable = false;
    public boolean right ;
    public ArrayList<Integer> qte = new ArrayList<>();
    int now = 0;
    int ans;

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
            Log.d("now","now = " + now);
            if(now < gameTest.elementQRange) {
                if (ans == qte.get(now)) {
                    right = true;
                    now++;
                } else {
                    right = false;
                    now = 0;
                }

                if (right && questionLayout.getChildCount() > 1) {
                    questionLayout.removeViewAt(0);
                    Log.d("attackable",questionLayout.getChildCount()+"");
                } else if (right && questionLayout.getChildCount() == 1) {
                    //接入怪物可被攻擊狀態
                    Log.d("attackable", attackable + "");
                    questionLayout.removeViewAt(0);
                    attackable = true;
                    now = 0;

                } else if(!attackable){ //答錯
                    Log.d("attackable",questionLayout.getChildCount()+"");
                    now = 0;
                    questionLayout.removeAllViews();
                    for (int i = 0; i < gameTest.elementQRange; i++) {
                        questionImg[i].setImageResource(mobsQid[qte.get(i)]);
                        questionLayout.addView(questionImg[i]);
                        //qte.add(i, qte.get(i));
                    }
                }
            }
        }
    };


    public View.OnClickListener blockListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            if(introFight == true){
                tvPrepareFight.setText("");
                timer01.schedule(battlePrepare, 500, 1000);
                introFight = false;
                Log.d("Click test", "view block is clicked");
            }
        }
    };
    private TimerTask battlePrepare = new TimerTask(){
        @Override
        public void run() {
            if(prepareTime > 0) {
                Message msg = mHandler.obtainMessage();
                msg.what = 1;
                msg.sendToTarget();
                PB.setProgress(prepareTime);
                prepareTime--;
                Log.d("timer","" + prepareTime);
            }else{
                Message msg = mHandler.obtainMessage();
                msg.what = 2;
                msg.sendToTarget();
                timer01.cancel();
                mobsTimer01.schedule(mob1ActionTimer,500,1000);
            }
        }
    };

    private TimerTask mob1ActionTimer = new TimerTask() {
        @Override
        public void run() {
            if(currentActionTime > 0) {
                actionbar[0].setProgress(currentActionTime*100/mobsSpeed1);
                currentActionTime -= 1000;
                Log.d("MobsTime", ""+ currentActionTime);
            }else{
                actionbar[0].setProgress(currentActionTime*100/mobsSpeed1);
                currentActionTime = mobsSpeed1;
                playerCurrentHP --;
                Message msg = mHandler.obtainMessage();
                msg.what = 3;
                msg.sendToTarget();
            }

        }
    };

    void loadData()
    {

        try {
            InputStream is = this.getResources().openRawResource(R.raw.mobsdata);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            //讀取json到buffer中
            String json = new String(buffer, "UTF-8");
            //轉換編碼
            JSONObject jsonObject = new JSONObject(json);
            //把json丟到jsonObject中
            JSONArray array = jsonObject.getJSONArray("mobsdata");
            //取出"mobsdata"中的資料,放入JSONArray array

            JSONObject mobs_1 = array.getJSONObject(1);
            //設置mobs_1變數放入JSON中對應位置的資料;
            String mobs_1_id = mobs_1.getString("_id");
            String mobs_1_name = mobs_1.getString("name");

            JSONObject mobs_pics = mobs_1.getJSONObject("pics");
            String mobs_1_img1 = mobs_pics.getString("pic1");

            int mobs_1_HP = mobs_1.getInt("HP");
            int mobs_1_elementTypes = mobs_1.getInt("elementTypes");
            int mobs_1_elementQRange = mobs_1.getInt("elementQRange");
            int mobs_1_actionbarDuration = mobs_1.getInt("actionbarDuration");
            int mobs_1_img_resID = getResources().getIdentifier(mobs_1_img1,"drawable", getPackageName());
            //設置變數存取mobs_1中對應標籤""的資料

            mobsName[0].setText(mobs_1_name);
            mobsImage[0].setImageResource(mobs_1_img_resID);
            mobsMaxHP1 = mobs_1_HP;
            mobsCurrentHP1 = mobs_1_HP;
            mobsSpeed1 = mobs_1_actionbarDuration;
            currentActionTime = mobsSpeed1;

            Log.d("Mobs HP Load",mobsMaxHP1 + " " +mobsCurrentHP1);

            gameTest = new GameTest(mobs_1_elementTypes,mobs_1_elementQRange);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onBackPressed() {  //返回鍵事件
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("確認視窗");
        builder.setMessage("確定要結束應用程式嗎?");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setPositiveButton("確定",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                    }
                });
        builder.show();
    }
}
