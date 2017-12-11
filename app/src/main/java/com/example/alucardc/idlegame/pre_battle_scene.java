package com.example.alucardc.idlegame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class pre_battle_scene extends AppCompatActivity {

    String TAG = "pre_battle_scene";
    ImageView modView[] = new ImageView[6];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  //隱藏狀態列
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_battle_scene);
        findsViews();
        setVisible();
    }

    void findsViews() {
        modView[0] = (ImageView)findViewById(R.id.mobView1);
        modView[1] = (ImageView)findViewById(R.id.mobView2);
        modView[2] = (ImageView)findViewById(R.id.mobView3);
        modView[3] = (ImageView)findViewById(R.id.mobView4);
        modView[4] = (ImageView)findViewById(R.id.mobView5);
        modView[5] = (ImageView)findViewById(R.id.mobView6);
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
}
