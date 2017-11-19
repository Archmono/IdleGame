package com.example.alucardc.idlegame;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    ProgressBar PB;
    int mosbsCount = 6;
    ImageView[] questionImg;
    ImageView[] playerCtrBut;
    ImageView[] mobsImage = new ImageView[mosbsCount];
    TextView[] mobsName = new TextView[mosbsCount];
    LinearLayout[] mobs = new LinearLayout[mosbsCount];
    LinearLayout questionLayout;
    int[] mobsId = {R.id.mobs1,R.id.mobs2,R.id.mobs3,R.id.mobs4,R.id.mobs5,R.id.mobs6};
    int[] mobsQid = {R.drawable.fire,R.drawable.water,R.drawable.wood,R.drawable.light,R.drawable.dark,R.drawable.heart};
    int[] playerCtrB = {R.id.playerCtrB1,R.id.playerCtrB2,R.id.playerCtrB3,R.id.playerCtrB4,R.id.playerCtrB5,R.id.playerCtrB6};

    GameTest gameTest = new GameTest(10002, 3, 6);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  //隱藏狀態列
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mobsInclude();
        questionLayout = (LinearLayout)findViewById(R.id.questionLayout);
        questionImg = new ImageView[6];
        playerCtrBut = new ImageView[6];

        for (int i = 0; i < 6; i++)
        {
            questionImg[i] = new ImageView(this);
            playerCtrBut[i] = (ImageView)findViewById(playerCtrB[i]);
            playerCtrBut[i].setOnClickListener(playerCtrListener);
        }

        PB = (ProgressBar)findViewById(R.id.pbtest);
        Timer timer01 = new Timer();
        timer01.schedule(task, 0, 100);
        //IEnumerator & yield 之前C#做時間漸變的關鍵字



        gameTest.setAtInclude();

        mobs[1].setOnClickListener(clickMobs);
    }

    void mobsInclude()
    {
        for (int i=0; i<mosbsCount; i++) {
            mobs[i] = (LinearLayout) findViewById(mobsId[i]);
            mobsName[i] = mobs[i].findViewById(R.id.mobsName);
            mobsImage[i] = mobs[i].findViewById(R.id.mobsImage);
        }
    }

    View.OnClickListener clickMobs = new View.OnClickListener () {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            now = 0;
            gameTest.count();
        }
    };

    public boolean right ;
    public ArrayList<Integer> qte = new ArrayList<>();
    int now = 0;
    int ans;

    View.OnClickListener playerCtrListener = new View.OnClickListener () {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.playerCtrB1 :
                    ans = 0;
                    break;
                case R.id.playerCtrB2 :
                    ans = 1;
                    break;
                case R.id.playerCtrB3 :
                    ans = 2;
                    break;
                case R.id.playerCtrB4 :
                    ans = 3;
                    break;
                case R.id.playerCtrB5 :
                    ans = 4;
                    break;
                case R.id.playerCtrB6 :
                    ans = 5;
                    break;
            }
            Log.d("now的值","now = " + now);
            if(now < gameTest.elementQuestionRange) {
                if (ans == qte.get(now)) {
                    right = true;
                    now++;
                } else {
                    right = false;
                    now = 0;
                }

                if (right && questionLayout.getChildCount() > 0) {
                    questionLayout.removeViewAt(0);
                } else if (right && questionLayout.getChildCount() == 0) {
                    now = 0;
                    //接入怪物可被攻擊狀態
                } else { //答錯
                    now = 0;
                    questionLayout.removeAllViews();
                    for (int i = 0; i < gameTest.elementQuestionRange; i++) {
                        questionImg[i].setImageResource(mobsQid[qte.get(i)]);
                        questionLayout.addView(questionImg[i]);
                        //qte.add(i, qte.get(i));
                    }
                }
            }
        }
    };

    private TimerTask task = new TimerTask(){
        int i = 0;

        @Override
        public void run() {
            if(i <= 100) {
                PB.setProgress(i);
                i++;
                Log.d("timer","i:" + i);
            }
        }
    };

    public class GameTest{

        int id;
        String[] name;
        int healthPoint;               //需要點擊次數
        int actionBarTime;           //動作條時間,毫秒
        int guardPoint;                 //破防需要次數
        int elementTypes;               //題目屬性種類
        int elementQuestionRange;       //題目難度,屬性球數量3-6
        int[] weakPoint;            //弱點屬性(火1,水2,木3,光4,暗5,心6)
        int stunDuration;            //暈眩時間,毫秒
        double differenceRange;       //個體差異程度,正負 min(基礎值*(1-差異度)) max(基礎*(1+差異度))
        int weakPointEffect;         //弱點屬性對於暈眩時間增加幅度,毫秒
        double damage;                  //對玩家傷害
        int imgId;

        public GameTest(int id, int elementTypes, int elementQuestionRange) {
            this.id = id;
            this.elementTypes = elementTypes;
            this.elementQuestionRange = elementQuestionRange;
        }

        void count()
        {
//            mobs mobs = new mobs(id);
//            mobs.setMobs();
            int[] elementTypesCons = {0,1,2,3,4,5};
            int[] setTypes = new int[elementTypes];
            for (int i=0; i < setTypes.length; i++) {
                int tamp = elementTypesCons[i];
                int randMob = (int) (Math.random() * 6);
                setTypes[i] = elementTypesCons[randMob];
                elementTypesCons[randMob] = tamp;
            }
            if (questionLayout.getChildCount() == 0) {
                for (int i = 0; i < elementQuestionRange; i++) {
                    int random = (int) (Math.random() * elementTypes);
                    qte.add(i, (setTypes[random]));
                    questionImg[i].setImageResource(mobsQid[qte.get(i)]);
                    questionLayout.addView(questionImg[i]);
                    Log.d("qte", qte.toString());
                }
            }
        }

        void setAtInclude()
        {
            mobs mobs = new mobs(id);
            mobs.setMobs();
            mobsName[1].setText(mobs.name[0]);
            mobsImage[1].setImageResource(mobs.imgId);
        }
    }
}
