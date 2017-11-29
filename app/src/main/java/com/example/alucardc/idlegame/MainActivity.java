package com.example.alucardc.idlegame;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    int showHP[] = new int[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  //隱藏狀態列
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadData();

        getData();
        for(int i =0 ;i<10; i++){
            Log.d("showHP", "" + showHP[i]);
        }

    }
    public void enter_scene_1(View view){
        Intent it = new Intent();
        it.setClass(MainActivity.this,pre_battle_scene.class);
        startActivity(it);
    }


    void loadData()
    {
        String[] mobsId;
        int[] mobsRareWeight;
        try {
            InputStream is = this.getResources().openRawResource(R.raw.mobsdata);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            String json = new String(buffer, "UTF-8");
            JSONObject jsonObject = new JSONObject(json);
            JSONArray array = jsonObject.getJSONArray("mobsdata");
            JSONObject mobs[] = new JSONObject[array.length()];
            mobsId = new String[array.length()];
            mobsRareWeight = new int[array.length()];
            for(int i=0; i < array.length(); i++){
                mobs[i] = array.getJSONObject(i);
                mobsId[i] = mobs[i].getString("_id");
                mobsRareWeight[i] = mobs[i].getInt("rareWeight");
            }
            RandomTest randomTest = new RandomTest(mobsRareWeight);
            randomTest.randomTest();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void getData(){
        GameDBHelper helper = GameDBHelper.getInstance(this);
        String[] column = { "_id", "name", "healthPoint"};
        Cursor c = helper.getReadableDatabase().query("mobsdata", column, "healthPoint>?", new String[]{"10"}, null, null, null);

        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i++) {
            String id = c.getString(c.getColumnIndex("_id"));
            String name = c.getString(c.getColumnIndex("name"));
            int hp = c.getInt(c.getColumnIndex("healthPoint"));
            c.moveToNext();
            Log.d("datatest", id + ", " + name + ", " + hp);
            showHP[i] = hp;
        }

        c.close();
    }
}
