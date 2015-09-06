package com.prafull.product.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.prafull.product.R;


/**
 * Created by john.francis on 20-05-2015.
 */
public class OrderConfirmDailogFragment extends DialogFragment {


    private static String message="";

    public interface OrderConfirmCallback{
        void call();
        void share();
        void chat();
        void track();
    }


    private static Context mContext;
    static OrderConfirmCallback dialogCallBack;

    public static OrderConfirmDailogFragment newInstance(String dialogMessage, Context ctx) {
        mContext = ctx;
        dialogCallBack = (OrderConfirmCallback) mContext;
        OrderConfirmDailogFragment frag = new OrderConfirmDailogFragment();
        message = dialogMessage;
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        View diaogView = inflater.inflate(R.layout.confirm_dailog_layout, null);
        TextView messageTextView = (TextView) diaogView.findViewById(R.id.approx_time);
        messageTextView.setText(message);
        Button confirm = (Button) diaogView.findViewById(R.id.call);
        Button cancel = (Button) diaogView.findViewById(R.id.chat);
        Button track = (Button) diaogView.findViewById(R.id.track);
        Button share = (Button) diaogView.findViewById(R.id.share);
        confirm.setOnClickListener(clickListner);
        cancel.setOnClickListener(clickListner);
        track.setOnClickListener(clickListner);
        share.setOnClickListener(clickListner);
        return diaogView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    View.OnClickListener clickListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.call:
                    getDialog().cancel();
                    dialogCallBack.call();
                    break;
                case R.id.chat:
                    getDialog().cancel();
                    dialogCallBack.chat();
                    break;
                case R.id.track:
                    getDialog().cancel();
                    dialogCallBack.track();
                    break;
                case R.id.share:
                    getDialog().cancel();
                    dialogCallBack.share();
                    break;
            }
        }
    };

}
