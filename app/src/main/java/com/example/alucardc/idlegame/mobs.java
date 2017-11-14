package com.example.alucardc.idlegame;

/**
 * Created by auser on 2017/11/14.
 */

public class mobs {
    class mobs001{
        String name = "Poring";
        int healthPoint = 10;       //需要點擊次數
        int actionBarTime = 5000;   //動作條時間,毫秒
        int guardPoint = 1;         //破防需要次數
        int elementTypes = 2;       //題目屬性種類
        int elementQuestionRange = 3;   //題目難度,屬性球數量3-6
        int[] weakPoint = {1,3};        //弱點屬性(火1,水2,木3,光4,暗5,心6)
        int stunDuration = 5000;    //暈眩時間,毫秒
    }

}
