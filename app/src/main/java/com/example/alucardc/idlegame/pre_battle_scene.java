package com.example.alucardc.idlegame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class pre_battle_scene extends AppCompatActivity {

    ImageView modView[] = new ImageView[6];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  //隱藏狀態列
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_battle_scene);
        findsViews();
//        loadData();
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

//    void setVisible () {
//        Log.d("LOGDATE",tempHasMod[0]+tempHasMod[1]+tempHasMod[2]+tempHasMod[3]+tempHasMod[4]+tempHasMod[5]);
//        for(int i=0; i<tempHasMod.length; i++) {
//            if(tempHasMod[i].equals("0"))
//                mob[i].setVisibility(View.INVISIBLE);
//            else
//                mob[i].setVisibility(View.VISIBLE);
//        }
//    }
    void loadData()
    {
        int mobsId = Integer.parseInt(RandomTest.cId);

        try {
            InputStream is = this.getResources().openRawResource(R.raw.mobsdata);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            String json = new String(buffer, "UTF-8");
            JSONObject jsonObject = new JSONObject(json);
            JSONArray array = jsonObject.getJSONArray("mobsdata");
            JSONObject mobs_1 = array.getJSONObject(mobsId);

            JSONObject mobs_pics = mobs_1.getJSONObject("pics");
            String mobs_1_img1 = mobs_pics.getString("pic1");
            int mobs_1_img_resID = getResources().getIdentifier(mobs_1_img1,"drawable", getPackageName());

//            modView1.setImageResource(mobs_1_img_resID);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
