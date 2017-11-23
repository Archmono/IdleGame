package com.example.alucardc.idlegame;

import android.os.Handler;
import android.os.Message;
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

import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.LogRecord;

public class MainActivity extends AppCompatActivity {

    ProgressBar PB;
    int prepareTime = 10;
    boolean introFight = true;
    Timer timer01 = new Timer();

    ImageView mobsImage1,mobsImage2,mobsImage3,mobsImage4,mobsImage5,mobsImage6;
    TextView tvPrepareFight,mobsName1,mobsName2,mobsName3,mobsName4,mobsName5,mobsName6;
    LinearLayout blockView,mobs1,mobs2,mobs3,mobs4,mobs5,mobs6;
    ImageView mobsHPbar1,mobsHPbar2,mobsHPbar3,mobsHPbar4,mobsHPbar5,mobsHPbar6;
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

        GameTest gameTest = new GameTest(30,2,6,R.drawable.mobs1002);
        gameTest.count();
        loadData();

        //IEnumerator & yield 之前C#做時間漸變的關鍵字
    }

    void findViews(){
        PB = (ProgressBar)findViewById(R.id.pbtest);
        blockView = (LinearLayout)findViewById(R.id.fightBlockView);
        tvPrepareFight = (TextView)findViewById(R.id.tvPrepareFight);

        mobs1 = (LinearLayout) findViewById(R.id.mobs1);
        mobs2 = (LinearLayout) findViewById(R.id.mobs2);
        mobs3 = (LinearLayout) findViewById(R.id.mobs3);
        mobs4 = (LinearLayout) findViewById(R.id.mobs4);
        mobs5 = (LinearLayout) findViewById(R.id.mobs5);
        mobs6 = (LinearLayout) findViewById(R.id.mobs6);

        mobsName1 =  mobs1.findViewById(R.id.mobsName);
        mobsName2 =  mobs2.findViewById(R.id.mobsName);
        mobsName3 =  mobs3.findViewById(R.id.mobsName);
        mobsName4 =  mobs4.findViewById(R.id.mobsName);
        mobsName5 =  mobs5.findViewById(R.id.mobsName);
        mobsName6 =  mobs6.findViewById(R.id.mobsName);

        mobsImage1 =  mobs1.findViewById(R.id.mobsImage);
        mobsImage2 =  mobs2.findViewById(R.id.mobsImage);
        mobsImage3 =  mobs3.findViewById(R.id.mobsImage);
        mobsImage4 =  mobs4.findViewById(R.id.mobsImage);
        mobsImage5 =  mobs5.findViewById(R.id.mobsImage);
        mobsImage6 =  mobs6.findViewById(R.id.mobsImage);

        mobsHPbar1 = mobs1.findViewById(R.id.imageViewBarHP);
        mobsHPbar2 = mobs2.findViewById(R.id.imageViewBarHP);
        mobsHPbar3 = mobs3.findViewById(R.id.imageViewBarHP);
        mobsHPbar4 = mobs4.findViewById(R.id.imageViewBarHP);
        mobsHPbar5 = mobs5.findViewById(R.id.imageViewBarHP);
        mobsHPbar6 = mobs6.findViewById(R.id.imageViewBarHP);
    }

    void mobsSetOnClickListener(){
        mobs1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mobsCurrentHP1--;   //-玩家攻擊力
                if(mobsCurrentHP1 > 0) {
                    float HPscale = ((float) mobsCurrentHP1 / (float) mobsMaxHP1);
                    Log.d("Mobs HP Load", mobsMaxHP1 + " " + mobsCurrentHP1);
                    mobsHPbar1.setScaleX(HPscale);
                    float mobs1HPbarLocateX = mobsHPbar1.getX();
                    mobsHPbar1.setPivotX(mobs1HPbarLocateX);
                    Log.d("MOBS1 HP:", "" + HPscale);
                }else{
                    mobs1.setVisibility(View.GONE);
                }
            }
        });
        mobs2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        mobs3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        mobs4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        mobs5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        mobs6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    public class GameTest{
        int healthPoint;               //需要點擊次數;
        int elementTypes;               //題目屬性種類
        int elementQuestionRange;       //題目難度,屬性球數量3-6
        int imgId;

        public GameTest(int healthPoint, int elementTypes, int elementQuestionRange, int imgId)
        {
            this.healthPoint = healthPoint;
            this.elementTypes = elementTypes;
            this.elementQuestionRange = elementQuestionRange;
            this.imgId = imgId;
        }

        void count()
        {
            int[] elementTypesCons = {1,2,3,4,5,6};
            int[] setTypes = new int[elementTypes];
            for (int i=0; i < setTypes.length; i++) {
                int tamp = elementTypesCons[i];
                int randMob = (int) (Math.random() * 6);
                setTypes[i] = elementTypesCons[randMob];
                elementTypesCons[randMob] = tamp;
//                Log.d("count",setTypes[i]+"");
            }
            for (int i=0; i < elementQuestionRange; i++)
            {
                int random = (int) (Math.random() * elementTypes);
                Log.d("count",setTypes[random]+"");
            }
        }
    }

    public View.OnClickListener blockListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            if(introFight == true){
                tvPrepareFight.setText("");
                timer01.schedule(battlePrepare, 500, 100);
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
                Log.d("timer","time" + prepareTime);
            }else{
                Message msg = mHandler.obtainMessage();
                msg.what = 2;
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
            int mobs_1_actionbarDuration = mobs_1.getInt("actionbarDuration");
            int mobs_1_img_resID = getResources().getIdentifier(mobs_1_img1,"drawable", getPackageName());
            //設置變數存取mobs_1中對應標籤""的資料

            mobsName1.setText(mobs_1_name);
            mobsImage1.setImageResource(mobs_1_img_resID);
            mobsMaxHP1 = mobs_1_HP;
            mobsCurrentHP1 = mobs_1_HP;
            Log.d("Mobs HP Load",mobsMaxHP1 + " " +mobsCurrentHP1);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
