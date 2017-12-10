package com.example.alucardc.idlegame;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;

/**
 * Created by auser on 2017/11/14.
 */

public class mobs {

    JSONArray mobsDataArray = new JSONArray();
    JSONObject mobsData = new JSONObject();


    class Mobs001{
        String[] name = {"Poring","波利"};
        int healthPoint = 10;               //需要點擊次數
        int actionBarTime = 5000;           //動作條時間,毫秒
        int guardPoint = 1;                 //破防需要次數
        int elementTypes = 2;               //題目屬性種類
        int elementQuestionRange = 3;       //題目難度,屬性球數量3-6
        int[] weakPoint = {1,3};            //弱點屬性(火1,水2,木3,光4,暗5,心6)
        int stunDuration = 5000;            //暈眩時間,毫秒
        double differenceRange = 0.1;       //個體差異程度,正負 min(基礎值*(1-差異度)) max(基礎*(1+差異度))
        int weakPointEffect = 2000;         //弱點屬性對於暈眩時間增加幅度,毫秒
        double damage = 1;                  //對玩家傷害
        int image = R.drawable.mobs1002;
    }

    class Mobs002{
        String[] name = {"Orc","獸人"};
        int healthPoint = 30;               //需要點擊次數
        int actionBarTime = 5000;           //動作條時間,毫秒
        int guardPoint = 1;                 //破防需要次數
        int elementTypes = 2;               //題目屬性種類
        int elementQuestionRange = 3;       //題目難度,屬性球數量3-6
        int[] weakPoint = {1,2};            //弱點屬性(火1,水2,木3,光4,暗5,心6)
        int stunDuration = 3000;            //暈眩時間,毫秒
        double differenceRange = 0.2;       //個體差異程度,正負 min(基礎值*(1-差異度)) max(基礎*(1+差異度))
        int weakPointEffect = 1000;         //弱點屬性對於暈眩時間增加幅度,毫秒
        double damage = 3;                  //對玩家傷害
        int image = R.drawable.mobs1241;
    }

    class Mobs003{
        String[] name = {"Peco","大嘴鳥"};
        int healthPoint = 15;               //需要點擊次數
        int actionBarTime = 5000;           //動作條時間,毫秒
        int guardPoint = 1;                 //破防需要次數
        int elementTypes = 2;               //題目屬性種類
        int elementQuestionRange = 3;       //題目難度,屬性球數量3-6
        int[] weakPoint = {2,5};            //弱點屬性(火1,水2,木3,光4,暗5,心6)
        int stunDuration = 5000;            //暈眩時間,毫秒
        double differenceRange = 0.1;       //個體差異程度,正負 min(基礎值*(1-差異度)) max(基礎*(1+差異度))
        int weakPointEffect = 1500;         //弱點屬性對於暈眩時間增加幅度,毫秒
        double damage = 2;                  //對玩家傷害
        int image = R.drawable.mobs1019;
    }
}