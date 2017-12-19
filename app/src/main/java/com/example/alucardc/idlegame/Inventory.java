package com.example.alucardc.idlegame;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by auser on 2017/12/18.
 */

public class Inventory extends DialogFragment {

    final int column = 5;
    View v,item;
    Context context;
    LinearLayout inventory_bottomBag;
    LinearLayout[] inventory_bottomBag_showLine;
    FrameLayout[] itemFrame;
    TextView tvNoItem,tvName,tvCount,tvPrice,tvHeal,tvPison,tvCure,tvDesc;
    ImageView[] items;
    TextView[] itemFrameCounts;
    ArrayList list = new ArrayList();
    Button button;
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
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        inventory_bottomBag = v.findViewById(R.id.inventory_bottomBag);
        findViews();

        for (int i = 0; i < Loading.i_countList.size(); i++) {
            if ( (int)Loading.i_countList.get(i) > 0)
                list.add(i);
        }
        inventory_bottomBag_showLine = new LinearLayout[(list.size()/column)+1];
        for(int i=0; i < (list.size()/column)+1; i++) {
            inventory_bottomBag_showLine[i] = new LinearLayout(context);
            inventory_bottomBag.addView(inventory_bottomBag_showLine[i]);
        }
        items = new ImageView[list.size()];
        itemFrameCounts = new TextView[list.size()];
        itemFrame = new FrameLayout[list.size()];
        for (int i = 0; i < list.size(); i++) {
            itemFrame[i] = new FrameLayout(context);
            itemFrame[i].setBackgroundResource(R.drawable.bottom_light);
//            itemFrame[i].setId(Integer.parseInt(R.id.itemFrame+""+i));
            itemFrameCounts[i] = new TextView(context);
            itemFrameCounts[i].setText(String.valueOf(Loading.i_countList.get((int)list.get(i))));
            itemFrameCounts[i].setGravity(Gravity.END|Gravity.BOTTOM);
            itemFrameCounts[i].setTextColor(Color.WHITE);
            items[i] = new ImageView(context);
            items[i].setTag(i);
            items[i].setOnClickListener(btnItem);
            img = getResources().getIdentifier(String.valueOf(Loading.i_image_RList.get((int)list.get(i))), "drawable", Loading.APP_NAME);
            items[i].setImageResource(img);
            itemFrame[i].addView(items[i]);
            itemFrame[i].addView(itemFrameCounts[i]);
            inventory_bottomBag_showLine[(i/column)].addView(itemFrame[i]);
        }
//        inventory_bottomBag_showLine[inventory_bottomBag_showLine.length-1].addView(itemFrame);
        if( list.size()>0) {
            itemFrame[0].setBackgroundResource(R.drawable.bottom_dark);
            tvName.setText(String.valueOf(Loading.i_nametList.get((int)list.get(0))));
//            tvCount.setText("數量：" + String.valueOf(Loading.i_countList.get((int)list.get(0))));
            tvCount.setText("");
            tvPrice.setText("售價：" + String.valueOf(Loading.i_priceList.get((int)list.get(0))));
            tvHeal.setText("回復量：" + String.valueOf(Loading.i_healList.get((int)list.get(0))));
            tvPison.setText("毒性：" + String.valueOf(Loading.i_poisontList.get((int)list.get(0))));
            tvCure.setText("中和：" + String.valueOf(Loading.i_cureList.get((int)list.get(0))));
            tvDesc.setText(String.valueOf(Loading.i_descList.get((int)list.get(0))));
        } else {
            tvNoItem = new TextView(context);
            tvNoItem.setText("NO ITEM");
            inventory_bottomBag.addView(tvNoItem);
        }

        Log.d("Inventory", list.size()+"");

        return v;
    }



    ImageView.OnClickListener btnItem = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int tag = (int)view.getTag();

            for (int i = 0; i < list.size(); i++)
                itemFrame[i].setBackgroundResource(R.drawable.bottom_light);

            itemFrame[tag].setBackgroundResource(R.drawable.bottom_dark);
            tvName.setText(String.valueOf(Loading.i_nametList.get((int)list.get(tag))));
//            tvCount.setText("數量：" + String.valueOf(Loading.i_countList.get((int)list.get(tag))));
            tvCount.setText("");
            tvPrice.setText("售價：" + String.valueOf(Loading.i_priceList.get((int)list.get(tag))));
            tvHeal.setText("回復量：" + String.valueOf(Loading.i_healList.get((int)list.get(tag))));
            tvPison.setText("毒性：" + String.valueOf(Loading.i_poisontList.get((int)list.get(tag))));
            tvCure.setText("中和：" + String.valueOf(Loading.i_cureList.get((int)list.get(tag))));
            tvDesc.setText(String.valueOf(Loading.i_descList.get((int)list.get(tag))));
        }
    };

    void findViews() {
        tvName = (TextView) v.findViewById(R.id.inventory_selectedItem_name);
        tvCount = (TextView) v.findViewById(R.id.inventory_selectedItem_counts);
        tvPrice = (TextView) v.findViewById(R.id.inventory_selectedItem_price);
        tvHeal = (TextView) v.findViewById(R.id.inventory_selectedItem_heal);
        tvPison = (TextView) v.findViewById(R.id.inventory_selectedItem_poison);
        tvCure = (TextView) v.findViewById(R.id.inventory_selectedItem_cure);
        tvDesc = (TextView) v.findViewById(R.id.inventory_itemDesc);
    }
}
