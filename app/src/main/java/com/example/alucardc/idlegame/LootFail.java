package com.example.alucardc.idlegame;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by auser on 2017/12/7.
 */

public class LootFail {

    String TAG ="LootFailTAG";
    String cId;
    ArrayList<String> lootFail = new ArrayList();
    public static String[] lootSet;

    public LootFail (String cId) {
        //this.cId = MainActivity.idList.indexOf("");
    }

    Boolean again;
    void count (){

        int[] lootsDP = (int[]) Loading.lootsDropRateList.get(Integer.parseInt(cId));
        int rand = (int) (Math.random() * 100) + 1;

        do {
            for (int i = 0; i < lootsDP.length; i++) { //隨機運算過程
                rand -= lootsDP[i];
                Log.d(TAG, rand+"");
                if (rand <= 0) { //運算完畢
                    lootFail.add(String.valueOf(i)); //取得物品位置資料並回存至結果
                    int conRand = (int) (Math.random() * 2);
                    switch (conRand) {
                        case 0: again = false; break;
                        case 1: again = true; break;
                    }
                    break;
                }
            }
        } while (again);
    }

}
