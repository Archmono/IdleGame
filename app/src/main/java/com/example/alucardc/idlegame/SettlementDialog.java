package com.example.alucardc.idlegame;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by acer on 2017/12/14.
 */

public class SettlementDialog extends DialogFragment {

    Context context;
    View v;
    TextView tvGetItem,tvVictory;
    ListView listView;
    Button lootViewBtn;
    Object[] lootSet, num;
    int[] img;
    int itemId,count=0;
    private SQLiteDatabase db;
    ArrayList<HashMap<String,Object>> data = new ArrayList();

    @RequiresApi(api = Build.VERSION_CODES.M)

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        context = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.settlement_dialog, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        MainActivity.getItemCounts(context);
        findViews();

//        listAdapter = new ArrayAdapter(context, R.layout.simple_list_item_1, LootFail.lootSet);

        Set<String> itemSet = new HashSet<String>();
        for (String element : LootFail.lootSet) {
            itemSet.add(element);
        }
        lootSet = new String[itemSet.size()];
        lootSet = itemSet.toArray();
        Set<String> numSet = new HashSet<String>();
        for (String element : LootFail.num) {
            numSet.add(element);
        }
        num = new String[numSet.size()];
        num = numSet.toArray();
        img = new int[numSet.size()];
        for(int i=0; i < lootSet.length; i++){
            img[i] = getResources().getIdentifier("i"+lootSet[i], "drawable", Loading.APP_NAME);
            HashMap<String,Object> item = new HashMap();
            item.put ("img", img[i]);
            itemId = Integer.parseInt(lootSet[i].toString());
            item.put ("item", Loading.i_nametList.get(Loading.id_lootList.indexOf(lootSet[i].toString())));
            count = (Integer.parseInt(num[i].toString().substring(1,num[i].toString().indexOf(","))));
            item.put ("num", num[i].toString().substring(0,num[i].toString().indexOf(",")));
            Log.d("oldCount",Loading.i_countList.size()+"");
            updateItem(itemId,(int)Loading.i_countList.get(Loading.id_lootList.indexOf(String.valueOf(itemId)))+count);
            data.add(item);  //把HashMap添加到ArrayList中
        }
        Log.d("reCount",lootSet.toString());

        SimpleAdapter adapter = new SimpleAdapter(context, data, R.layout.simple_list_item_1,
                new String[]{"img","item","num"},
                new int[]{R.id.img, R.id.text1, R.id.text2});
        listView.setAdapter(adapter);
        Loading.mobsSlotFilled_S1 = new String[] {"0","0","0","0","0","0"};

        return v;
    }

    Button.OnClickListener listener = new Button.OnClickListener(){
        @Override
        public void onClick(View view) {
            getDialog().dismiss();
            MainActivity.settle = true;
            Intent i = new Intent(context, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
    };

    public void updateItem(int ItemId, int ItemCount){
        GameDBHelper itemHelper = GameDBHelper.getInstance(context);
        db = itemHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("i_count",ItemCount);
        db.update("mobsloot",values,"_id_loot="+ItemId,null);
    }

    void findViews() {
        tvGetItem = (TextView)v.findViewById(R.id.tvGetItem);
        tvVictory = (TextView)v.findViewById(R.id.tvVictory);
        listView = (ListView)v.findViewById(R.id.lootViwe);
        lootViewBtn = (Button)v.findViewById(R.id.lootViewBtn);
        lootViewBtn.setOnClickListener(listener);
        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/witches_magic.ttf");
        tvGetItem.setTypeface(font);
        tvVictory.setTypeface(font);
    }
}
