package com.example.alucardc.idlegame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class pre_battle_scene extends AppCompatActivity {

    ImageView modView1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_battle_scene);
        modView1 = (ImageView)findViewById(R.id.mobView1);
        loadData();
    }

    public void start_fight(View v){
        Intent it = new Intent();
        it.setClass(this,battle_scene.class);
        startActivity(it);
    }

    void loadData()
    {
        int mobsId = RandomTest.cId;

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

            modView1.setImageResource(mobs_1_img_resID);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
