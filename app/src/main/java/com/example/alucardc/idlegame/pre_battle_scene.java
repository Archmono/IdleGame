package com.example.alucardc.idlegame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

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
        Intent it = new Intent();
        it.setClass(this,battle_scene.class);
        startActivity(it);
    }

//    public void onClear (View v) {
//        for (int i=0;i<6;i++){
//            mob[i].setVisibility(View.INVISIBLE);
//            tempHasMod[i] = "0";
//        }
//        onSave();
//    }

    void setVisible () {
        Log.d("LOGDATE",Loading.tempHasMod[0]+Loading.tempHasMod[1]+Loading.tempHasMod[2]+Loading.tempHasMod[3]+Loading.tempHasMod[4]+Loading.tempHasMod[5]);
        for(int i=0; i<Loading.tempHasMod.length; i++) {
            if(Loading.tempHasMod[i].equals("0"))
                modView[i].setVisibility(View.INVISIBLE);
            else {
                int inx = Loading.idList.indexOf(Loading.tempHasMod[i]);
                int mobs_img_resID = getResources().getIdentifier((String)Loading.imageList.get(inx), "drawable", getPackageName());
                modView[i].setImageResource(mobs_img_resID);
            }
        }
    }
}
