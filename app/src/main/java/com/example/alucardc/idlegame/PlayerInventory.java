package com.example.alucardc.idlegame;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by auser on 2017/12/18.
 */

public class PlayerInventory {

    final String TAG = "道具欄 :";
    Context context;

    public void getCurrentInventory(Context context) {
        try {
            InputStream is = context.openFileInput("playerdata.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            String json = new String(buffer, "UTF-8");
            Gson gson = new Gson();
            Player playInfo = gson.fromJson(json, Player.class);
            

            String.valueOf(playInfo.playerInventory[0].i100101);

            Log.d(TAG, "玩家100101號道具存量 : " + playInfo.playerInventory);
            Log.d(TAG, "玩家100101號道具存量 : " + String.valueOf(playInfo.playerInventory[0].i100102));
//            Log.d(TAG, "怪物圖鑑1001號生態介紹解鎖狀態 : " + String.valueOf(playInfo.playerMobsCollection[0].m1001.mobsBio));
//
//            Log.d(TAG, "關卡解鎖進度 : " + String.valueOf(playInfo.playerSceneProgress[0].Scene_1));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
