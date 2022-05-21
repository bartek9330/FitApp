package com.fitwebappclient.interfaces;

import android.app.Activity;
import android.app.ProgressDialog;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import com.fitwebappclient.R;

public class DialogsHandling {  // TODO implement it in all fragments
    private View view;
    private ProgressDialog progressDialog;

    public DialogsHandling(View view) {
        this.view = view;
        progressDialog = new ProgressDialog(view.getContext());
    }

    public void disableLoadingBar() {
        if(progressDialog != null){
            progressDialog.dismiss();
        }
    }

    public void enableLoadingBar() {
        progressDialog.setTitle(view.getResources().getString(R.string.progress_dialog_title));
        progressDialog.setMessage(view.getResources().getString(R.string.progress_dialog_describe));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void handleErrorWhileLoadingData() {
        Toast.makeText(view.getContext(), view.getResources().getText(R.string.server_connection_error), Toast.LENGTH_SHORT).show();
        Activity activity = (Activity) view.getContext();
        activity.finish();
    }

}
