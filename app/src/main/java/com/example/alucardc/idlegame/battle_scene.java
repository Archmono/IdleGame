package com.example.alucardc.idlegame;

import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class battle_scene extends AppCompatActivity {

    ProgressBar PB;
    int prepareTime = 3;
    boolean introFight = true;
    Timer timer01 = new Timer();
    Timer mobsTimer = new Timer();
    int playerCurrentHP = 100;  //暫時設定的玩家HP值,待完成
    int[] currentActionTime = new int[6];

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

    int mobsMaxHP1,mobsMaxHP2,mobsMaxHP3,mobsMaxHP4,mobsMaxHP5,mobsMaxHP6;
    int mobsCurrentHP1,mobsCurrentHP2,mobsCurrentHP3,mobsCurrentHP4,mobsCurrentHP5,mobsCurrentHP6;
    int mobsATK1,mobsATK2,mobsATK3,mobsATK4,mobsATK5,mobsATK6;
    int mobsSpeed1,mobsSpeed2,mobsSpeed3,mobsSpeed4,mobsSpeed5,mobsSpeed6;


    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 1){
                PB.setVisibility(View.VISIBLE);
                blockView.setVisibility(View.VISIBLE);
            }else if(msg.what == 2){
                tvPrepareFight.setVisibility(View.GONE);
                PB.setVisibility(View.GONE);
                blockView.setVisibility(View.GONE);
            }else if(msg.what == 3){
                blockView.setVisibility(View.VISIBLE);
                blockView.setOnClickListener(blockListener);
                tvPrepareFight.setVisibility(View.VISIBLE);
                tvPrepareFight.setText("暫停中");
            }else if(msg.what == 4){

            }
            else if(msg.what == 9){
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
        setContentView(R.layout.activity_battle_scene);
        findViews();
        setMobs();
        mobsSetOnClickListener();

        blockView.setOnClickListener(blockListener);
//        loadData();

        Typeface font = Typeface.createFromAsset(this.getAssets(), "fonts/witches_magic.ttf");
        playerHP.setTypeface(font);


    }

    @Override
    protected void onPause() {
        mobsTimer.cancel();
        Message msg = mHandler.obtainMessage();
        msg.what = 3;
        msg.sendToTarget();
        super.onPause();
    }


    void setMobs() {
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

                currentActionTime[i] = mobsSpeed[i];
            }

        }
    }

//    public void getData(String searchMobsID){
//        helper = new GameDBHelper(this, "idlegame.db", null, 1);
//        String[] column = { "_id", "name", "healthPoint"};
//        Cursor c = helper.getReadableDatabase().query("mobsdata", column, "_id=?", new String[]{searchMobsID}, null, null, null);
//
//        c.moveToFirst();
//        for (int i = 0; i < c.getCount(); i++) {
//            String id = c.getString(c.getColumnIndex("_id"));
//            String name = c.getString(c.getColumnIndex("name"));
//            int hp = c.getInt(c.getColumnIndex("healthPoint"));
//            c.moveToNext();
//            Log.d("datatest", id + ", " + name + ", " + hp);
//            String[] ad = {id , name};
//            showHP = hp;
//        }
//        c.close();
//    }

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

    public int mobIndex;
    void mobsSetOnClickListener(){
        mobs[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modClickEvent(0);
            }
        });
        mobs[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modClickEvent(1);
            }
        });
        mobs[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modClickEvent(2);
            }
        });
        mobs[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modClickEvent(3);
            }
        });
        mobs[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modClickEvent(4);
            }
        });
        mobs[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modClickEvent(5);
            }
        });
    }


    public boolean[] attackable = {false,false,false,false,false,false};
    void modClickEvent(int mobIndex) {
        this.mobIndex = mobIndex;
        if (attackable[mobIndex]) { //可被攻擊
            mobsCurrentHP[mobIndex]--;   //-玩家攻擊力
            if (mobsCurrentHP[mobIndex] > 0) {
                mobsHPbar[mobIndex].setScaleX((float) mobsCurrentHP[mobIndex] / (float) mobsMaxHP[mobIndex]);
                float mobs1HPbarLocateX = mobsHPbar[mobIndex].getX();
                mobsHPbar[mobIndex].setPivotX(mobs1HPbarLocateX);
            } else {
                mobs[mobIndex].setVisibility(View.GONE);
//                mob1ActionTimer.cancel();
            }
        } else if (now == 0) {

            gameTest = new GameTest(elementTypes[mobIndex],elementQRange[mobIndex]);
            gameTest.count();
        }
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


    public View.OnClickListener blockListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            if(introFight){
                tvPrepareFight.setText("");
                timer01.schedule(battlePrepare, 500, 1000);
                introFight = false;
                Log.d("Click test", "view block is clicked");
            }else{
                tvPrepareFight.setText("");
                blockView.setVisibility(View.GONE);
                Message msg = mHandler.obtainMessage();
                msg.what = 4;
                msg.sendToTarget();

                Log.d("battlePrepare", mobsTimer+"");
                mobsTimer = new Timer();
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
                Log.d("battlePrepare", "else submit");
                Message msg = mHandler.obtainMessage();
                msg.what = 2;
                msg.sendToTarget();
                timer01.cancel();
                mobsTimer = new Timer();
                mobsTimer.schedule(mob1ActionTimer,500,100);
            }
        }
    };

    private TimerTask mob1ActionTimer = new TimerTask() {
        @Override
        public void run() {
            for(int i = 0; i<6 ;i++){
                actionbar[i].setProgress(currentActionTime[i]*100/mobsSpeed[i]);
//            Log.d("Speed",currentActionTime[0] +"  "+ mobsSpeed[0]);
                if(mobsCurrentHP[i] > 0){
                    currentActionTime[i] -= 100;
                }

                if(currentActionTime[i] <= 0){
                    playerCurrentHP -= mobsATK[0];
                    currentActionTime[i] = mobsSpeed[i];
                    Message msg = mHandler.obtainMessage();
                    msg.what = 9;
                    msg.sendToTarget();
                }
            }
        }
    };

    //    void loadData()
//    {
//        int mobsId = Integer.parseInt(RandomTest.cId);
//        try {
//            InputStream is = this.getResources().openRawResource(R.raw.mobsdata);
//            byte[] buffer = new byte[is.available()];
//            is.read(buffer);
//            //讀取json到buffer中
//            String json = new String(buffer, "UTF-8");
//            //轉換編碼
//            JSONObject jsonObject = new JSONObject(json);
//            //把json丟到jsonObject中
//            JSONArray array = jsonObject.getJSONArray("mobsdata");
//            //取出"mobsdata"中的資料,放入JSONArray array
//
//            JSONObject mobs_1 = array.getJSONObject(mobsId);
//            //設置mobs_1變數放入JSON中對應位置的資料;
//            String mobs_1_id = mobs_1.getString("_id");
//            String mobs_1_name = mobs_1.getString("name");
//
//            JSONObject mobs_pics = mobs_1.getJSONObject("pics");
//            String mobs_1_img1 = mobs_pics.getString("pic1");
//
//            int mobs_1_HP = mobs_1.getInt("HP");
//            int mobs_1_elementTypes = mobs_1.getInt("elementTypes");
//            int mobs_1_elementQRange = mobs_1.getInt("elementQRange");
//            int mobs_1_actionbarDuration = mobs_1.getInt("actionbarDuration");
//            int mobs_1_img_resID = getResources().getIdentifier(mobs_1_img1,"drawable", getPackageName());
//            //設置變數存取mobs_1中對應標籤""的資料
//
//            mobsName[0].setText(mobs_1_name);
//            mobsImage[0].setImageResource(mobs_1_img_resID);
//            mobsMaxHP1 = mobs_1_HP;
//            mobsCurrentHP1 = mobs_1_HP;
//            mobsSpeed1 = mobs_1_actionbarDuration;
//            currentActionTime = mobsSpeed1;
//
//            Log.d("Mobs HP Load",mobsMaxHP1 + " " +mobsCurrentHP1);
//
//            gameTest = new GameTest(mobs_1_elementTypes,mobs_1_elementQRange);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//    }
    @Override
    public void onBackPressed() {  //返回鍵事件
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("確認視窗");
//        builder.setMessage("確定要結束應用程式嗎?");
//        builder.setIcon(R.mipmap.ic_launcher);
//        builder.setPositiveButton("確定",
//                new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        finish();
//                    }
//                });
//        builder.setNegativeButton("取消",
//                new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
//        builder.show();
    }
}