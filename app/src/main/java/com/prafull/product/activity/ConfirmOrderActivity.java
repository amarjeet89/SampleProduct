package com.prafull.product.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.prafull.product.R;
import com.prafull.product.adapter.ConfirmAdapter;
import com.prafull.product.pojo.Plate;
import com.prafull.product.util.CommonUtil;

import java.util.ArrayList;

/**
 * Created by SHUBHANSU on 8/25/2015.
 */
public class ConfirmOrderActivity extends AppCompatActivity implements View.OnClickListener {
    private ListView confirmListView;
    private ArrayList<Plate> plates;
    private ConfirmAdapter confirmAdapter;
    Button confirm_order,add_more_items;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        Intent intent = getIntent();
        plates =  intent.getParcelableArrayListExtra(CommonUtil.CONFIRM_ORDER);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar_color)));
        confirm_order=(Button)findViewById(R.id.check_out);
        confirm_order.setOnClickListener(this);
        add_more_items=(Button)findViewById(R.id.add_items);
        add_more_items.setOnClickListener(this);
        confirmListView = (ListView)findViewById(R.id.confirm_list);
        confirmAdapter = new ConfirmAdapter(this, plates);
        confirmListView.setAdapter(confirmAdapter);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case  R.id.check_out:
                startActivity(new Intent(ConfirmOrderActivity.this,SampleActivity.class));
                break;
            case  R.id.add_items:
               finish();
                break;
        }
    }
}
