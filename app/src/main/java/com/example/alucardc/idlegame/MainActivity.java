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

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.LogRecord;

public class MainActivity extends AppCompatActivity {

    ProgressBar PB;
    ImageView mobsImage,mobsImage2,mobsImage3;
    TextView tvPrepareFight,mobsName,mobsName2,mobsName3;
    LinearLayout mobs1,mobs2,mobs3,blockView;
    int prepareTime = 100;
    boolean introFight = true;
    Timer timer01 = new Timer();

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

        blockView.setOnClickListener(blockListener);

        GameTest gameTest = new GameTest(30,2,6,R.drawable.mobs1002);
        gameTest.count();
        test();

        //IEnumerator & yield 之前C#做時間漸變的關鍵字
    }

    void findViews(){
        PB = (ProgressBar)findViewById(R.id.pbtest);
        mobs1 = (LinearLayout) findViewById(R.id.mobs1);
        mobs2 = (LinearLayout) findViewById(R.id.mobs2);
        mobs3 = (LinearLayout) findViewById(R.id.mobs3);
        mobsName =  mobs1.findViewById(R.id.mobsName);
        mobsImage =  mobs1.findViewById(R.id.mobsImage);
        mobsName2 =  mobs2.findViewById(R.id.mobsName);
        mobsImage2 =  mobs2.findViewById(R.id.mobsImage);
        mobsName3 =  mobs3.findViewById(R.id.mobsName);
        mobsImage3 =  mobs3.findViewById(R.id.mobsImage);
        blockView = (LinearLayout)findViewById(R.id.fightBlockView);
        tvPrepareFight = (TextView)findViewById(R.id.tvPrepareFight);
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

    void test()
    {
            mobsImage.setImageResource(R.drawable.mobs1002);
            mobsName.setText("波利");

            mobsImage2.setImageResource(R.drawable.mobs1241);
            mobsName2.setText("獸人");

            mobsImage3.setImageResource(R.drawable.mobs1019);
            mobsName3.setText("大嘴鳥");
    }

    public View.OnClickListener blockListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            if(introFight == true){
                tvPrepareFight.setText("");
                timer01.schedule(battlePrepare, 500, 100);
                introFight = false;
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
}
