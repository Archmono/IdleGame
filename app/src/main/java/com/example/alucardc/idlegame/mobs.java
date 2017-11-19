package com.example.alucardc.idlegame;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by auser on 2017/11/14.
 */

public class mobs {

    int _id;
    String[] name;
    int healthPoint;               //需要點擊次數
    int actionBarTime;           //動作條時間,毫秒
    int guardPoint;                 //破防需要次數
    int elementTypes;               //題目屬性種類
    int elementQuestionRange;       //題目難度,屬性球數量3-6
    int[] weakPoint;            //弱點屬性(火1,水2,木3,光4,暗5,心6)
    int stunDuration;            //暈眩時間,毫秒
    double differenceRange;       //個體差異程度,正負 min(基礎值*(1-差異度)) max(基礎*(1+差異度))
    int weakPointEffect;         //弱點屬性對於暈眩時間增加幅度,毫秒
    double damage;                  //對玩家傷害
    int imgId;                  //圖片資源

    public mobs (int id) {
        this._id = id;
    }

    void setMobs() {
        switch (_id) {
            case 10001 :
                name = new String[]{"Poring", "波利"};
                healthPoint = 10;               //需要點擊次數
                actionBarTime = 5000;           //動作條時間,毫秒
                guardPoint = 1;                 //破防需要次數
                elementTypes = 2;               //題目屬性種類
                elementQuestionRange = 3;       //題目難度,屬性球數量3-6
                weakPoint = new int[]{1, 3};            //弱點屬性(火1,水2,木3,光4,暗5,心6)
                stunDuration = 5000;            //暈眩時間,毫秒
                differenceRange = 0.1;       //個體差異程度,正負 min(基礎值*(1-差異度)) max(基礎*(1+差異度))
                weakPointEffect = 2000;         //弱點屬性對於暈眩時間增加幅度,毫秒
                damage = 1;                  //對玩家傷害
                imgId = R.drawable.mobs1002;
                break;

            case 10002 :
                name = new String[]{"Orc","獸人"};
                healthPoint = 30;               //需要點擊次數
                actionBarTime = 5000;           //動作條時間,毫秒
                guardPoint = 1;                 //破防需要次數
                elementTypes = 2;               //題目屬性種類
                elementQuestionRange = 3;       //題目難度,屬性球數量3-6
                weakPoint = new int[]{1, 2};            //弱點屬性(火1,水2,木3,光4,暗5,心6)
                stunDuration = 3000;            //暈眩時間,毫秒
                differenceRange = 0.2;       //個體差異程度,正負 min(基礎值*(1-差異度)) max(基礎*(1+差異度))
                weakPointEffect = 1000;         //弱點屬性對於暈眩時間增加幅度,毫秒
                damage = 3;                  //對玩家傷害
                imgId = R.drawable.mobs1241;
                break;

            case 10003 :
                name = new String[]{"Peco","大嘴鳥"};
                healthPoint = 15;               //需要點擊次數
                actionBarTime = 5000;           //動作條時間,毫秒
                guardPoint = 1;                 //破防需要次數
                elementTypes = 2;               //題目屬性種類
                elementQuestionRange = 3;       //題目難度,屬性球數量3-6
                weakPoint = new int[]{2, 5};            //弱點屬性(火1,水2,木3,光4,暗5,心6)
                stunDuration = 5000;            //暈眩時間,毫秒
                differenceRange = 0.1;       //個體差異程度,正負 min(基礎值*(1-差異度)) max(基礎*(1+差異度))
                weakPointEffect = 1500;         //弱點屬性對於暈眩時間增加幅度,毫秒
                damage = 2;                  //對玩家傷害
                imgId = R.drawable.mobs1019;
                break;
        }
    }
}
