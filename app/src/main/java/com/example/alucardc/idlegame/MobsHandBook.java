package com.example.alucardc.idlegame;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MobsHandBook extends DialogFragment {

    Context context;
    final int column = 4;
    View v;
    ImageView mobsImg;
    ImageView[] mobsIcon;
    TextView mobsName,mobsHP,mobsAtk,mobsSpd,mobsDesc,mobsLoots1,mobsLoots2,mobsLoots3;
    Button btnAll,btnForest,btnGraveYard;
    LinearLayout bottomView;
    LinearLayout[] bottomViewLine;
    private ArrayList idList = new ArrayList();
    private ArrayList nameList = new ArrayList();
    private ArrayList healthPointList = new ArrayList();
    private ArrayList speedList = new ArrayList();
    private ArrayList stunTimeList = new ArrayList();
    private ArrayList atkList = new ArrayList();
    private ArrayList imageList = new ArrayList();
    private ArrayList lootsList = new ArrayList();
    private ArrayList descList = new ArrayList();

    private ArrayList<Integer> unlockID = new ArrayList();
    private ArrayList<Integer> unlockImage = new ArrayList();
    private ArrayList<Integer>  unlockName = new ArrayList();
    private ArrayList<Integer>  unlockHP = new ArrayList();
    private ArrayList<Integer>  unlockATK = new ArrayList();
    private ArrayList<Integer>  unlockSpeed = new ArrayList();
    private ArrayList<Integer>  unlockDesc = new ArrayList();
    private ArrayList<Integer>  unlockLoots1 = new ArrayList();
    private ArrayList<Integer>  unlockLoots2 = new ArrayList();
    private ArrayList<Integer>  unlockLoots3 = new ArrayList();

    private ArrayList<Integer> index = new ArrayList();
    int img;


    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        context = activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.mobs_hand_book, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        findViews();

        getData("0");

        getUnlockStatus();

        setBottomView();


        return v;
    }

    void findViews(){
        mobsImg = (ImageView) v.findViewById(R.id.handbook_selectedMob_image);
        mobsName = (TextView) v.findViewById(R.id.handbook_mobsname_tv);
        mobsHP = (TextView) v.findViewById(R.id.handbook_mobsHP_tv);
        mobsAtk = (TextView) v.findViewById(R.id.handbook_mobsAtk_tv);
        mobsSpd = (TextView) v.findViewById(R.id.handbook_mobsSpd_tv);
        mobsDesc = (TextView) v.findViewById(R.id.handbook_mobsDesc);
        mobsLoots1 = (TextView) v.findViewById(R.id.handbook_loots1_tv);
        mobsLoots2 = (TextView) v.findViewById(R.id.handbook_loots2_tv);
        mobsLoots3 = (TextView) v.findViewById(R.id.handbook_loots3_tv);
        btnAll = (Button) v.findViewById(R.id.handbook_btnAll);
        btnForest = (Button) v.findViewById(R.id.handbook_btnForest);
        btnGraveYard = (Button) v.findViewById(R.id.handbook_btnGraveYard);
        bottomView = (LinearLayout) v.findViewById(R.id.handbook_bottom_view);
        btnAll.setOnClickListener(btnTag);
        btnForest.setOnClickListener(btnTag);
        btnGraveYard.setOnClickListener(btnTag);
    }

    void setBottomView(){
        index = new ArrayList();
        bottomView.removeAllViews();

        for (int i = 0; i < nameList.size(); i++) {
            index.add(i);
        }
        bottomViewLine = new LinearLayout[(index.size()/column)+1]; //動態產生新行
        for(int i=0; i < (index.size()/column)+1; i++) {
            bottomViewLine[i] = new LinearLayout(context);
            bottomView.addView(bottomViewLine[i]);
        }
        mobsIcon = new ImageView[index.size()];
        for (int i = 0; i < index.size(); i++) { //動態產生新物品layout
//            itemFrameCounts[i].setTextColor(Color.WHITE);
            mobsIcon[i] = new ImageView(context);
            mobsIcon[i].setTag(i);
            mobsIcon[i].setBackgroundResource(R.drawable.bottom_dark_65);
            mobsIcon[i].setLayoutParams(new LinearLayout.LayoutParams(200 , 200));
            mobsIcon[i].setOnClickListener(btnIcon);
            img = getResources().getIdentifier(String.valueOf(imageList.get((int)index.get(i))), "drawable", Loading.APP_NAME);

            Log.d("IDtest",unlockImage.get(unlockID.indexOf(idList.get((int)index.get(i))))+"");

            if(unlockID.get(unlockID.indexOf(idList.get((int)index.get(i))))!= -1 && unlockImage.get(unlockID.indexOf(idList.get((int)index.get(i))))==1){
                mobsIcon[i].setImageResource(img);
            } else {
                mobsIcon[i].setImageResource(R.drawable.q_mark);
            }

            bottomViewLine[(i/column)].addView(mobsIcon[i]);
        }
    }

    public void getUnlockStatus(){
        GameDBHelper helper = GameDBHelper.getInstance(context);

        Cursor c = helper.getReadableDatabase().query("mobshandbook", null, null, null, null, null, null);

        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i++) {
            int id = c.getInt(c.getColumnIndex("_id"));
            int image = c.getInt(c.getColumnIndex("image"));
            int name = c.getInt(c.getColumnIndex("name"));
            int hp = c.getInt(c.getColumnIndex("healthpoint"));
            int atk = c.getInt(c.getColumnIndex("attack"));
            int speed = c.getInt(c.getColumnIndex("speed"));
//            int stunTime = c.getInt(c.getColumnIndex("stunTime"));  //預留數值,目前無顯示
            int desc = c.getInt(c.getColumnIndex("desc"));
            int loots_1 = c.getInt(c.getColumnIndex("loots_1"));
            int loots_2 = c.getInt(c.getColumnIndex("loots_2"));
            int loots_3 = c.getInt(c.getColumnIndex("loots_3"));

            unlockID.add(id);
            unlockImage.add(image);
            unlockName.add(name);
            unlockHP.add(hp);
            unlockATK.add(atk);
            unlockSpeed.add(speed);
            unlockDesc.add(desc);
            unlockLoots1.add(loots_1);
            unlockLoots2.add(loots_2);
            unlockLoots3.add(loots_3);

            c.moveToNext();
        }
    }

    public void getData(String scene){

        GameDBHelper helper = GameDBHelper.getInstance(context);
        idList.clear();
        nameList.clear();
        healthPointList.clear();
        speedList.clear();
        stunTimeList.clear();
        atkList.clear();
        imageList.clear();
        lootsList.clear();

        if(scene.equals("0")){
            Cursor c = helper.getReadableDatabase().query("mobsdata", null, null, null, null, null, null);

            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++) {
                int id = c.getInt(c.getColumnIndex("_id"));
                String name = c.getString(c.getColumnIndex("name"));
                int hp = c.getInt(c.getColumnIndex("healthPoint"));
                int speed = c.getInt(c.getColumnIndex("speed"));
                int stunTime = c.getInt(c.getColumnIndex("stunTime"));
                int atk = c.getInt(c.getColumnIndex("atk"));
                String imageR = c.getString(c.getColumnIndex("image_R"));
                String desc = c.getString(c.getColumnIndex("mobs_desc"));
                int loots_1 = c.getInt(c.getColumnIndex("loots_1"));
                int loots_2 = c.getInt(c.getColumnIndex("loots_2"));
                int loots_3 = c.getInt(c.getColumnIndex("loots_3"));
                int lootsInside[] = {loots_1,loots_2,loots_3};

                if(i!=0) {
                    idList.add(id);
                    nameList.add(name);
                    healthPointList.add(hp);
                    speedList.add(speed);
                    stunTimeList.add(stunTime);
                    atkList.add(atk);
                    imageList.add(imageR);
                    lootsList.add(lootsInside);
                    descList.add(desc);
                }

                c.moveToNext();
            }
            c.close();
        } else {
//        String[] column = { "_id", "rareWeight","scene_1", "scene_2"};
            Cursor c = helper.getReadableDatabase().query("mobsdata", null, "scene_" + scene + "=?", new String[]{"1"}, null, null, null);

            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++) {
                int id = c.getInt(c.getColumnIndex("_id"));
                String name = c.getString(c.getColumnIndex("name"));
                int hp = c.getInt(c.getColumnIndex("healthPoint"));
                int speed = c.getInt(c.getColumnIndex("speed"));
                int stunTime = c.getInt(c.getColumnIndex("stunTime"));
                int atk = c.getInt(c.getColumnIndex("atk"));
                String imageR = c.getString(c.getColumnIndex("image_R"));
                String desc = c.getString(c.getColumnIndex("mobs_desc"));
                int loots_1 = c.getInt(c.getColumnIndex("loots_1"));
                int loots_2 = c.getInt(c.getColumnIndex("loots_2"));
                int loots_3 = c.getInt(c.getColumnIndex("loots_3"));
                int lootsInside[] = {loots_1,loots_2,loots_3};

                idList.add(id);
                nameList.add(name);
                healthPointList.add(hp);
                speedList.add(speed);
                stunTimeList.add(stunTime);
                atkList.add(atk);
                imageList.add(imageR);
                lootsList.add(lootsInside);
                descList.add(desc);

                c.moveToNext();
            }
            c.close();
        }
    }

    ImageView.OnClickListener btnIcon = new View.OnClickListener() { //切換物品顯示
        @Override
        public void onClick(View view) {
            int tag = (int)view.getTag();

            for (int i = 0; i < index.size(); i++) {
                mobsIcon[i].setBackgroundResource(R.drawable.bottom_dark_65);
            }

            if(unlockImage.get(unlockID.indexOf(idList.get((int)index.get(tag)))) == 1){
                mobsImg.setImageResource(getResources().getIdentifier(String.valueOf(imageList.get((int)index.get(tag))), "drawable", Loading.APP_NAME));
            }else{
                mobsImg.setImageResource(R.drawable.q_mark);
            }

            if(unlockName.get(unlockID.indexOf(idList.get((int)index.get(tag)))) == 1){
                mobsName.setText(String.valueOf(nameList.get((int)index.get(tag))));
            }else{
                mobsName.setText("???");
            }

            if(unlockHP.get(unlockID.indexOf(idList.get((int)index.get(tag)))) == 1){
                mobsHP.setText("血量：" + String.valueOf(healthPointList.get((int)index.get(tag))));
            }else{
                mobsHP.setText("血量 : ???");
            }

            if(unlockHP.get(unlockID.indexOf(idList.get((int)index.get(tag)))) == 1){
                mobsAtk.setText("攻擊：" + String.valueOf(atkList.get((int)index.get(tag))));
            }else{
                mobsAtk.setText("攻擊：???");
            }

            if(unlockHP.get(unlockID.indexOf(idList.get((int)index.get(tag)))) == 1){
                mobsSpd.setText("速度：" + (Float.parseFloat(speedList.get((int)index.get(tag))+"")/1000f)+"sec");
            }else{
                mobsSpd.setText("速度：???");
            }

            if(unlockDesc.get(unlockID.indexOf(idList.get((int)index.get(tag)))) == 1){
                mobsDesc.setText(String.valueOf(descList.get((int)index.get(tag))));
            }else{
                mobsDesc.setText("怪物資訊 : ???");
            }

            mobsIcon[tag].setBackgroundResource(R.drawable.button_dark_65);

            int[] loots = (int[]) lootsList.get(tag);
            if(unlockLoots1.get(unlockID.indexOf(idList.get((int)index.get(tag)))) == 1){
                mobsLoots1.setText(Loading.i_nametList.get(Loading.id_lootList.indexOf(String.valueOf(loots[0])))+"");
            }else{
                mobsLoots2.setText("???");
            }
            if(unlockLoots2.get(unlockID.indexOf(idList.get((int)index.get(tag)))) == 1){
                mobsLoots2.setText(Loading.i_nametList.get(Loading.id_lootList.indexOf(String.valueOf(loots[1])))+"");
            }else{
                mobsLoots2.setText("???");
            }
            if(unlockLoots3.get(unlockID.indexOf(idList.get((int)index.get(tag)))) == 1) {
                mobsLoots3.setText(Loading.i_nametList.get(Loading.id_lootList.indexOf(String.valueOf(loots[2]))) + "");
            } else if(String.valueOf(loots[2]).equals("0")){
                mobsLoots3.setText("");
            } else {
                mobsLoots3.setText("???");
            }

        }
    };

    Button.OnClickListener btnTag = new View.OnClickListener() { //標籤按鈕
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.handbook_btnAll:
                    getData("0");
                    btnAll.setBackgroundResource(R.drawable.btn_tag_darkon);
                    btnForest.setBackgroundResource(R.drawable.btn_tag_darkoff);
                    btnGraveYard.setBackgroundResource(R.drawable.btn_tag_darkoff);
                    break;
                case R.id.handbook_btnForest:
                    getData("1");
                    btnForest.setBackgroundResource(R.drawable.btn_tag_darkon);
                    btnAll.setBackgroundResource(R.drawable.btn_tag_darkoff);
                    btnGraveYard.setBackgroundResource(R.drawable.btn_tag_darkoff);
                    break;
                case R.id.handbook_btnGraveYard:
                    getData("2");
                    btnGraveYard.setBackgroundResource(R.drawable.btn_tag_darkon);
                    btnForest.setBackgroundResource(R.drawable.btn_tag_darkoff);
                    btnAll.setBackgroundResource(R.drawable.btn_tag_darkoff);
                    break;
            }

            setBottomView();
            Log.d("switch", "hello hello");
        }
    };
}
