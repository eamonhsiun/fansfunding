package com.fansfunding.fan.project.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;

import dmax.dialog.SpotsDialog;

/**
 * Created by Eamon on 2016/7/24.
 */
public class MyDialog extends SpotsDialog{


    public MyDialog(Context context) {
        super(context);
        initDialog();
    }

    public MyDialog(Context context, CharSequence message) {
        super(context, message);
        initDialog();
    }

    public MyDialog(Context context, int theme) {
        super(context, theme);
        initDialog();
    }

    public MyDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initDialog();
    }

    private void initDialog(){
        this.setCancelable(false);
        this.setOnKeyListener(myKeyListener);
    }

    private DialogInterface.OnKeyListener myKeyListener = new OnKeyListener() {
        @Override
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if(keyCode==KeyEvent.KEYCODE_SEARCH){
                return true;
            }
            return false;
        }
    };














}



