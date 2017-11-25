package com.example.alucardc.idlegame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class pre_battle_scene extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_battle_scene);
    }

    public void start_fight(View v){
        Intent it = new Intent();
        it.setClass(this,battle_scene.class);
        startActivity(it);
    }
}
