package com.example.adrin.proyecto_centro_estetico;

/**
 * Created by Adrián on 05/06/2017.
 */

import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

public class SendEmailTask extends AsyncTask {

    private Activity sendMailActivity;

    public SendEmailTask(Activity activity) {
        sendMailActivity = activity;
    }

    public SendEmailTask() {
    }

    protected void onPreExecute() {
        //statusDialog = new ProgressDialog(sendMailActivity);
        //statusDialog.setMessage("Getting ready...");
        //statusDialog.setIndeterminate(false);
        //statusDialog.setCancelable(false);
        //statusDialog.show();
    }

    @Override
    protected Object doInBackground(Object... args) {
        try {
            //Log.i("SendMailTask", "About to instantiate GMail...");
            //publishProgress("Processing input....");
            GMail androidEmail = new GMail(args[0].toString(),
                    args[1].toString(), (List) args[2], args[3].toString(),
                    args[4].toString());
            //publishProgress("Preparing mail message....");
            androidEmail.createEmailMessage();
            //publishProgress("Sending email....");
            androidEmail.sendEmail();
            //publishProgress("Email Sent.");
            //Log.i("SendMailTask", "Mail Sent.");
        } catch (Exception e) {
            //publishProgress(e.getMessage());
            Log.e("SendMailTask", e.getMessage(), e);
        }
        return null;
    }

    @Override
    public void onProgressUpdate(Object... values) {
        //statusDialog.setMessage(values[0].toString());
    }

    @Override
    public void onPostExecute(Object result) {
        //statusDialog.dismiss();
    }

}