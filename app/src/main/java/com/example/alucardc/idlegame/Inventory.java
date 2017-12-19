package com.example.alucardc.idlegame;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by auser on 2017/12/18.
 */

public class Inventory extends DialogFragment {

    Context context;
    LinearLayout inventory_bottomBag;
    LinearLayout[] inventory_bottomBag_showLine;
    TextView tvName,tvCount,tvPrice,tvHeal,tvPison,tvCure;
    ImageView[] items;
    ArrayList list = new ArrayList();
    Button button;
//    int index = 0;
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
        View v = inflater.inflate(R.layout.inventory, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        inventory_bottomBag = v.findViewById(R.id.inventory_bottomBag);

        for (int i = 0; i < Loading.i_countList.size(); i++) {
            if ( (int)Loading.i_countList.get(i) > 0)
//                index++;
                list.add(i);
            Log.d("Inventory",Loading.i_countList.size()+"");
        }

        items = new ImageView[list.size()];
//        inventory_bottomBag = new LinearLayout(context);
        for (int i = 0; i < list.size(); i++) {
            items[i] = new ImageView(context);
            items[i].setOnClickListener(btnItem);
            img = getResources().getIdentifier(String.valueOf(Loading.i_image_RList.get((int)list.get(i))), "drawable", Loading.APP_NAME);
            items[i].setImageResource(img);
            inventory_bottomBag.addView(items[i]);
        }

        if( list.size()>0) {

        }

        Log.d("Inventory", list.size()+"");

        return v;
    }

    ImageView.OnClickListener btnItem = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };
}
