package com.example.kevin_sct.beastchat.aty;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.kevin_sct.beastchat.R;
import com.example.kevin_sct.beastchat.Utils.DBUtils;

import java.util.ArrayList;

/**
 * Created by kevin_sct on 5/16/17.
 */

public class RankAty extends Activity {
    ListView randlv;
    ArrayAdapter<String> stringArrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rank_layout);
        randlv = (ListView) findViewById(R.id.randlv);

        DBUtils dbUtils = new DBUtils(RankAty.this);
        ArrayList<String[]> arrayList =  dbUtils.queryRank();
        ArrayList<String> data = new ArrayList<>();
        for (int i = 0;i<arrayList.size();i++) {
            String temp = "第" + (i+1) + "名" +  "玩家名：" + arrayList.get(i)[0] + "    花费时间：" +arrayList.get(i)[1] + "秒";
            data.add(temp);
        }

        stringArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);
        randlv.setAdapter(stringArrayAdapter);

    }
}