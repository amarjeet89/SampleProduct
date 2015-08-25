package com.prafull.product.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.prafull.product.R;
import com.prafull.product.adapter.ConfirmAdapter;
import com.prafull.product.pojo.Plate;
import com.prafull.product.util.CommonUtil;

import java.util.ArrayList;

/**
 * Created by SHUBHANSU on 8/25/2015.
 */
public class ConfirmOrderActivity extends AppCompatActivity {
    private ListView confirmListView;
    private ArrayList<Plate> plates;
    private ConfirmAdapter confirmAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        Intent intent = getIntent();
        plates =  intent.getParcelableArrayListExtra(CommonUtil.CONFIRM_ORDER);
        confirmListView = (ListView)findViewById(R.id.confirm_list);
        confirmAdapter = new ConfirmAdapter(this, plates);
        confirmListView.setAdapter(confirmAdapter);

    }
}
