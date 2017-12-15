package com.example.alucardc.idlegame;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by acer on 2017/12/14.
 */

public class SettlementDialog extends DialogFragment {

    Context context;
    TextView tvGetItem;
    ListView listView;
    Button lootViewBtn;
    ArrayAdapter listAdapter;

    @RequiresApi(api = Build.VERSION_CODES.M)

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        context=activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.settlement_dialog, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        tvGetItem = (TextView)v.findViewById(R.id.tvGetItem);
        listView = (ListView)v.findViewById(R.id.lootViwe);
        lootViewBtn = (Button)v.findViewById(R.id.lootViewBtn);
        lootViewBtn.setOnClickListener(listener);
        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/witches_magic.ttf");
        tvGetItem.setTypeface(font);
        listAdapter = new ArrayAdapter(context, R.layout.simple_list_item_1, LootFail.lootSet);
        listView.setAdapter(listAdapter);

        return v;
    }

    Button.OnClickListener listener = new Button.OnClickListener(){
        @Override
        public void onClick(View view) {
            getDialog().dismiss();
        }
    };
}
