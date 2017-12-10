package com.ismek.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ProjectPreferences {

    public static ProgressDialog showDialog(Context cntx, String title, String message){
        ProgressDialog dialog = new ProgressDialog(cntx);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.show();
        return dialog;
    }

    public static SweetAlertDialog showSweetDialog(Context cntx,String message){
        SweetAlertDialog pDialog = new SweetAlertDialog(cntx, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText(message);
        pDialog.setCancelable(false);
        pDialog.show();
        return pDialog;
    }

}