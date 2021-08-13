package com.example.muneerapps.dialogs;

import android.content.Context;

import com.techiness.progressdialoglibrary.ProgressDialog;

public class Progress_Monitor {

    ProgressDialog progressDialog;
    Context c;

    public Progress_Monitor(Context c)
    {
        this.c = c;
    }

    public void Setup_Progressing(String title,String message)
    {
        progressDialog = new ProgressDialog(c, ProgressDialog.THEME_DARK);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    public void Drop_Progressing()
    {
        progressDialog.dismiss();
    }
}

