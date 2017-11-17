package com.example.alucardc.idlegame;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    ProgressBar PB;
    ImageView mobsImage,mobsImage2,mobsImage3;
    TextView mobsName,mobsName2,mobsName3;
    LinearLayout mobs1,mobs2,mobs3;
    int i = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  //隱藏狀態列
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        Timer timer01 = new Timer();

        timer01.schedule(task, 0, 100);


        GameTest gameTest = new GameTest(30,2,6,R.drawable.mobs1002);
        gameTest.count();
        test();

        //IEnumerator & yield 之前C#做時間漸變的關鍵字
    }

    private TimerTask task = new TimerTask(){
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
}
