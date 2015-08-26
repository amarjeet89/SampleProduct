package com.prafull.product.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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
    private TextView totalValue;
    private TextView couponValue;
    private double totalAmount = 0;
    private Button addMoreItem;
    private Button checkOut;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        Intent intent = getIntent();
        plates = intent.getParcelableArrayListExtra(CommonUtil.CONFIRM_ORDER);
        confirmListView = (ListView) findViewById(R.id.confirm_list);
        totalValue = (TextView) findViewById(R.id.total_value);
        couponValue = (TextView) findViewById(R.id.coupon_value);
        addMoreItem = (Button) findViewById(R.id.add_more_item);
        addMoreItem.setOnClickListener(this);
        checkOut = (Button) findViewById(R.id.check_out);
        checkOut.setOnClickListener(this);
        confirmAdapter = new ConfirmAdapter(this, plates);
        confirmListView.setAdapter(confirmAdapter);

        updateTotalValue();

    }

    private void updateTotalValue() {
        totalAmount = 0;
        if (plates != null) {
            if (plates.size() > 0) {
                for (int i = 0; i < plates.size(); i++) {
                    Plate plate = plates.get(i);
                    totalAmount = totalAmount + plate.getPrice() * plate.getQty();
                }

                totalValue.setText(String.valueOf(totalAmount + 32) + " Rs.");
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_more_item) {
            finish();
        }else if(v.getId() == R.id.check_out){
            startActivity(new Intent(this,SampleActivity.class));
        }
    }

    public void updateSelectedOrder(int position) {
        if (plates.size() > 0) {
            plates.remove(position);
            if (plates.size() == 0)
                finish();
            confirmAdapter.refresh(plates);
            updateTotalValue();
        } else if (plates.size() == 0) {
            finish();
        }
    }
}
