package com.prafull.product.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
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
public class ImagePickerFragment extends DialogFragment {


    private static String message="";

    public interface DialogCallBack {
        void gallary();

        void takeAPicture();
    }


    private static android.support.v4.app.Fragment mContext;
    static DialogCallBack dialogCallBack;

    public static ImagePickerFragment newInstance(String dialogMessage, android.support.v4.app.Fragment ctx) {
        mContext = ctx;
        dialogCallBack = (DialogCallBack) mContext;
        ImagePickerFragment frag = new ImagePickerFragment();
        message = dialogMessage;
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        View diaogView = inflater.inflate(R.layout.dailog_layout, null);
        TextView messageTextView = (TextView) diaogView.findViewById(R.id.dailog_text);
        messageTextView.setText(message);
        Button confirm = (Button) diaogView.findViewById(R.id.confirm);
        Button cancel = (Button) diaogView.findViewById(R.id.cancel);
        confirm.setOnClickListener(clickListner);
        cancel.setOnClickListener(clickListner);
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
                case R.id.confirm:
                    getDialog().cancel();
                    dialogCallBack.gallary();
                    break;
                case R.id.cancel:
                    getDialog().cancel();
                    dialogCallBack.takeAPicture();
                    break;
            }
        }
    };

}
