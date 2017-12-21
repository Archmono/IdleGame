package com.example.alucardc.idlegame;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by auser on 2017/12/18.
 */

public class Inventory extends DialogFragment {

    final int column = 4; //列數
    View v,cv;
    Context context;
    LinearLayout inventory_bottomBag;
    LinearLayout[] inventory_bottomBag_showLine;
    FrameLayout inventory;
    FrameLayout[] itemFrame;
    ImageView imgItem,cItem1,cItem2,cItem3,cItem4;
    TextView tvNoItem,tvName,tvCount,tvPrice,tvHeal, tvPoison,tvCure,tvDesc,tvPlayerMoney;
    ImageView[] items;
    TextView[] itemFrameCounts;
    ArrayList index = new ArrayList();
    Button btnSell,btnAll,btnMaterial,btnConsumables,btnComposite;
    private SQLiteDatabase db;
    int img;

    @RequiresApi(api = Build.VERSION_CODES.M)

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        context = activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.inventory, container, false);
        cv = inflater.inflate(R.layout.composite, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        findViews();
        setBottomView();
//        Log.d("Inventory", index.size()+"");

        return v;
    }

    void findViews() {
        inventory = (FrameLayout) v.findViewById(R.id.inventory);
        inventory_bottomBag = v.findViewById(R.id.inventory_bottomBag);
        imgItem = (ImageView) v.findViewById(R.id.inventory_selectedItem_image);
        tvName = (TextView) v.findViewById(R.id.inventory_selectedItem_name);
        tvCount = (TextView) v.findViewById(R.id.inventory_selectedItem_counts);
        tvPrice = (TextView) v.findViewById(R.id.inventory_selectedItem_price);
        tvHeal = (TextView) v.findViewById(R.id.inventory_selectedItem_heal);
        tvPoison = (TextView) v.findViewById(R.id.inventory_selectedItem_poison);
        tvCure = (TextView) v.findViewById(R.id.inventory_selectedItem_cure);
        tvDesc = (TextView) v.findViewById(R.id.inventory_itemDesc);
        btnAll = (Button) v.findViewById(R.id.inventory_btnAll);
        btnMaterial = (Button) v.findViewById(R.id.inventory_btnMaterial);
        btnConsumables = (Button) v.findViewById(R.id.inventory_btnConsumables);
        btnSell = (Button) v.findViewById(R.id.inventory_btnSell);
        btnComposite = (Button) v.findViewById(R.id.btnComposite);
        btnAll.setOnClickListener(btnTag);
        btnMaterial.setOnClickListener(btnTag);
        btnConsumables.setOnClickListener(btnTag);
        btnSell.setOnClickListener(onSell);
        btnComposite.setOnClickListener(onComposite);
    }

    void setBottomView() {

        index = new ArrayList();
        inventory_bottomBag.removeAllViews();

        for (int i = 0; i < Loading.i_countList.size(); i++) {
            if ( (int)Loading.i_countList.get(i) > 0)
                index.add(i);
        }
        inventory_bottomBag_showLine = new LinearLayout[(index.size()/column)+1]; //動態產生新行
        for(int i=0; i < (index.size()/column)+1; i++) {
            inventory_bottomBag_showLine[i] = new LinearLayout(context);
            inventory_bottomBag.addView(inventory_bottomBag_showLine[i]);
        }
        items = new ImageView[index.size()];
        itemFrameCounts = new TextView[index.size()];
        itemFrame = new FrameLayout[index.size()];
        for (int i = 0; i < index.size(); i++) { //動態產生新物品layout
            itemFrame[i] = new FrameLayout(context);
            itemFrame[i].setBackgroundResource(R.drawable.button_light);
            itemFrameCounts[i] = new TextView(context);
            itemFrameCounts[i].setText(String.valueOf(Loading.i_countList.get((int)index.get(i))));
            itemFrameCounts[i].setGravity(Gravity.END|Gravity.BOTTOM);
//            itemFrameCounts[i].setTextColor(Color.WHITE);
            items[i] = new ImageView(context);
            items[i].setTag(i);
            items[i].setOnClickListener(btnItem);
            img = getResources().getIdentifier(String.valueOf(Loading.i_image_RList.get((int)index.get(i))), "drawable", Loading.APP_NAME);
            items[i].setImageResource(img);
            itemFrame[i].addView(items[i]);
            itemFrame[i].addView(itemFrameCounts[i]);
            inventory_bottomBag_showLine[(i/column)].addView(itemFrame[i]);
        }

        if( index.size()>0) { //初始設置
            itemFrame[0].setBackgroundResource(R.drawable.button_dark);
            imgItem.setImageResource(getResources().getIdentifier(String.valueOf(Loading.i_image_RList.get((int)index.get(0))), "drawable", Loading.APP_NAME));
            tvName.setText(String.valueOf(Loading.i_nametList.get((int)index.get(0))));
            tvCount.setText("");
            tvPrice.setText("售價：" + String.valueOf(Loading.i_priceList.get((int)index.get(0))));
            tvHeal.setText("回復量：" + String.valueOf(Loading.i_healList.get((int)index.get(0))));
            tvPoison.setText("毒性：" + String.valueOf(Loading.i_poisontList.get((int)index.get(0))));
            tvCure.setText("中和：" + String.valueOf(Loading.i_cureList.get((int)index.get(0))));
            tvDesc.setText(String.valueOf(Loading.i_descList.get((int)index.get(0))));
        } else { //無物品顯示
            tvNoItem = new TextView(context);
            imgItem.setImageResource(R.drawable.bottom_light);
            tvNoItem.setText("NO ITEM");
            inventory_bottomBag.addView(tvNoItem);
            tvName.setText("");
            tvCount.setText("");
            tvPrice.setText("");
            tvHeal.setText("");
            tvPoison.setText("");
            tvCure.setText("");
            tvDesc.setText("");
        }
    }

    int tag;
    ImageView.OnClickListener btnItem = new View.OnClickListener() { //切換物品顯示
        @Override
        public void onClick(View view) {
            tag = (int)view.getTag();

            for (int i = 0; i < index.size(); i++) {
                itemFrame[i].setBackgroundResource(R.drawable.button_light);
            }
            imgItem.setImageResource(getResources().getIdentifier(String.valueOf(Loading.i_image_RList.get((int)index.get(tag))), "drawable", Loading.APP_NAME));
            itemFrame[tag].setBackgroundResource(R.drawable.button_dark);
            tvName.setText(String.valueOf(Loading.i_nametList.get((int)index.get(tag))));
//            tvCount.setText("數量：" + String.valueOf(Loading.i_countList.get((int)list.get(tag))));
            tvCount.setText("");
            tvPrice.setText("售價：" + String.valueOf(Loading.i_priceList.get((int)index.get(tag))));
            tvHeal.setText("回復量：" + String.valueOf(Loading.i_healList.get((int)index.get(tag))));
            tvPoison.setText("毒性：" + String.valueOf(Loading.i_poisontList.get((int)index.get(tag))));
            tvCure.setText("中和：" + String.valueOf(Loading.i_cureList.get((int)index.get(tag))));
            tvDesc.setText(String.valueOf(Loading.i_descList.get((int)index.get(tag))));
        }
    };

    Boolean opComposite = false;
    Button.OnClickListener onComposite = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(!opComposite) {
                inventory.addView(cv, (int)convertDpToPixel(300,context), (int)convertDpToPixel(200,context));
                opComposite = true;
            } else {
                inventory.removeView(cv);
                opComposite = false;
            }
        }
    };

    Button.OnClickListener onSell = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (index.size()>0) {
                updateItem(Integer.parseInt(Loading.id_lootList.get((int) index.get(tag)) + ""), Integer.parseInt(Loading.i_countList.get((int) index.get(tag)) + "") - 1);
                updatePlayerMoney(Integer.parseInt(Loading.i_priceList.get((int) index.get(tag)) + ""));
                itemFrameCounts[tag].setText((Loading.i_countList.get((int) index.get(tag)) + "") + "");
                if( itemFrameCounts[tag].getText().toString().equals("0") ) {
                    setBottomView(); //執行重排
                }
            }
        }
    };

    Button.OnClickListener btnTag = new View.OnClickListener() { //標籤按鈕
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.inventory_btnAll:
                    btnAll.setBackgroundResource(R.drawable.btn_tag_on);
                    btnMaterial.setBackgroundResource(R.drawable.btn_tag_off);
                    btnConsumables.setBackgroundResource(R.drawable.btn_tag_off);
                    break;
                case R.id.inventory_btnMaterial:
                    btnMaterial.setBackgroundResource(R.drawable.btn_tag_on);
                    btnAll.setBackgroundResource(R.drawable.btn_tag_off);
                    btnConsumables.setBackgroundResource(R.drawable.btn_tag_off);
                    break;
                case R.id.inventory_btnConsumables:
                    btnConsumables.setBackgroundResource(R.drawable.btn_tag_on);
                    btnMaterial.setBackgroundResource(R.drawable.btn_tag_off);
                    btnAll.setBackgroundResource(R.drawable.btn_tag_off);
                    break;
            }
        }
    };

    public void updatePlayerMoney(int addMoney){
        try {
            InputStream is = context.openFileInput("playerdata.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            String json = new String(buffer, "UTF-8");
            Gson gson = new Gson();
            Player playInfo = gson.fromJson(json, Player.class);

            playInfo.playerStatus.playerMoney += addMoney;
            String json_2 = gson.toJson(playInfo);
            Log.d("JSON", json_2);

            OutputStream os = new FileOutputStream(DBInfo.JSON_FILE);
            os.write(json_2.getBytes());

            MainActivity.tvPlayerMoney.setText("持有金錢 :" + playInfo.playerStatus.playerMoney);

            os.close();
            is.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void updateItem(int ItemId, int ItemCount){
        GameDBHelper itemHelper = GameDBHelper.getInstance(context);
        db = itemHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("i_count",ItemCount);
        db.update("mobsloot",values,"_id_loot="+ItemId,null);
        MainActivity.getItemCounts(context);
    }

    public static float convertDpToPixel(float dp, Context context){
        float px = dp * getDensity(context);
        return px;
    }

    public static float getDensity(Context context){
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.density;
    }

}
