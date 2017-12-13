package com.example.alucardc.idlegame;

import android.util.Log;

/**
 * Created by auser on 2017/11/23.
 */

public class RandomTest { //在Loading中，隨機時間生怪時創建一此類別

    String TAG = "RandomTest";
    public static String cId; //方法運算結果，套用在Loading中產生的怪物字串陣列裡
    int rand,sum = 0; //運算用變數
    int[] mobsRarity; //運算用變數

    public RandomTest (int[] mobsRarity) { //取得怪物稀有度
        this.mobsRarity = new int[mobsRarity.length];
        this.mobsRarity = mobsRarity;
    }

    void randomTest() { //計算方法

        for (int i = 0; i < mobsRarity.length; i++) {
            sum += mobsRarity[i];
        }
        Log.d(TAG, "SUM : " + sum);

        rand = (int) (Math.random() * sum) + 1; //隨機 1 - 怪物總稀有度之值

        Log.d(TAG, "RAND : " + rand);

        for (int i = 0; i < mobsRarity.length; i++) { //隨機運算過程

            rand -= mobsRarity[i];
            Log.d(TAG, "-= mobsRarity[" + i + "] :" + rand);
            if (rand <= 0) { //運算完畢
                cId = Loading.idList.get(i).toString(); //取得怪物id資料並回存至結果
                break;
            }
        }
        Log.d(TAG, "cId : " + cId);

    }

}
