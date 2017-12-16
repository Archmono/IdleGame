package com.example.alucardc.idlegame;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by auser on 2017/12/7.
 */

public class LootFail {

    int[] cId = new int[6];
    ArrayList lootFail = new ArrayList();
    public static String[] lootSet;
    public static String[] num;

    public LootFail () {
        for (int i =0; i<6; i++)
            this.cId[i] = Loading.idList.indexOf(Loading.mobsSlotFilled_S1[i]);
    }

    Boolean again;
    void count () {
        for (int i = 0; i < battle_scene.mobSum; i++) {
            int[] lootsDP = (int[]) Loading.lootsDropRateList.get(cId[i]);
            int[] loots = (int[]) Loading.lootsList.get(cId[i]);
            do {
                int rand = (int) (Math.random() * 100) + 1;
                for (int j = 0; j < lootsDP.length; j++) { //隨機運算過程
                    rand -= lootsDP[j];
                    if (rand <= 0) { //運算完畢
                        lootFail.add(loots[j]+""); //取得物品位置資料並回存至結果
                        int conRand = (int) (Math.random() * 2);
                        switch (conRand) {
                            case 0:
                                again = false;
                                break;
                            case 1:
                                again = true;
                                break;
                        }
                        break;
                    }
                }
            } while (again);
        }
        lootSet = new String[lootFail.size()];
        for (int i=0; i<lootFail.size(); i++){
            lootSet[i] = (String) lootFail.get(i);
        }
        reCount();
    }

    void reCount() {
        num = new String[lootFail.size()];
        for(int i=0; i < lootSet.length; i++) {
            int count=0;
            for(int j=0; j < lootSet.length; j++) {
                if( lootSet[i].equals( lootSet[j]) ) {
                    count++;  //計算元素出現的次數
                }
            }
            Log.d("reCount",lootSet[i]+" "+count);
            num[i] = "x" + count + ","+lootSet[i];
        }
    }
}
