package com.example.alucardc.idlegame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity{

    ProgressBar PB;
    int i =5;
    ImageView mobsImage;
    TextView mobsName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  //隱藏狀態列
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PB = (ProgressBar)findViewById(R.id.pbtest);

        GameTest gameTest = new GameTest(30,3,12,R.drawable.mobs1002);
        gameTest.count();

        //IEnumerator & yield 之前C#做時間漸變的關鍵字
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
                int randMob = (int) (Math.random() * 6 + 1);
                setTypes[i] = elementTypesCons[randMob-1];
                elementTypesCons[randMob-1] = tamp;
                //Log.d("count",setTypes[i]+"");
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
        int randMob = (int)(Math.random()*3+1);

        switch (randMob) {
            case 1:
                mobs.Mobs001 mobs001 = new mobs.Mobs001();
                mobsImage.setImageResource(mobs001.image);
                mobsName.setText(mobs001.name[1]);
                break;
            case 2:
                mobs.Mobs002 mobs002 = new mobs.Mobs002();
                mobsImage.setImageResource(mobs002.image);
                mobsName.setText(mobs002.name[1]);
                break;
            case 3:
                mobs.Mobs003 mobs003 = new mobs.Mobs003();
                mobsImage.setImageResource(mobs003.image);
                mobsName.setText(mobs003.name[1]);
                break;
        }
    }
}
