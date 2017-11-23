package com.example.alucardc.idlegame;

import android.util.Log;

/**
 * Created by auser on 2017/11/23.
 */

public class RandomTest {

    String TAG = "RandomTest";
    int rand,sum = 0;
    int[] mobsRarity;
    int cId;

    public RandomTest (int[] mobsRarity) {
        this.mobsRarity = new int[mobsRarity.length];
        this.mobsRarity = mobsRarity;
    }
    void randomTest() {

        for (int i = 0; i < mobsRarity.length; i++) {
            sum += mobsRarity[i];
        }
        Log.d(TAG, "SUM : " + sum);

        rand = (int) (Math.random() * sum) + 1;

        Log.d(TAG, "RAND : " + rand);

        for (int i = 0; i < mobsRarity.length; i++) {

            rand -= mobsRarity[i];
            Log.d(TAG, "-= mobsRarity[" + i + "] :" + rand);
            if (rand <= 0) {
                cId = i;
                break;
            }
        }
        Log.d(TAG, "cId : " + cId);

    }

}
