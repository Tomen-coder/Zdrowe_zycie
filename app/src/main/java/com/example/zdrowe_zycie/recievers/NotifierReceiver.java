package com.example.zdrowe_zycie.recievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat.Builder;

import com.example.zdrowe_zycie.R;
import com.example.zdrowe_zycie.helpers.NotificationHelper;
import com.example.zdrowe_zycie.helpers.SqliteHelper;
import com.example.zdrowe_zycie.utils.AppUtils;

public final class NotifierReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        SharedPreferences prefs = context.getSharedPreferences(AppUtils.getUSERS_SHARED_PREF(), AppUtils.getPRIVATE_MODE());
        String RingtoneURI = prefs.getString(
                AppUtils.getNOTIFICATION_TONE_URI_KEY(),
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION).toString()
        );

        String title = prefs.getString(
                AppUtils.getNOTIFICATION_MSG_KEY(),
                context.getResources().getString(R.string.pref_notification_message_value)
        );
        SqliteHelper sqliteHelper = new SqliteHelper(context);
        int Intook = sqliteHelper.getIntook(AppUtils.getCurrentDate(),false);
        int Left = sqliteHelper.getIntakeWater(AppUtils.getCurrentDate()) - Intook;
        String messageToShow = "Wypiłeś dzisiaj " + Intook + " ml, do celu "+ Left +" ml";
        NotificationHelper nHelper = new NotificationHelper(context);
        Builder nBuilder = nHelper.getNotification(title, messageToShow, RingtoneURI);
        nHelper.notify(1, nBuilder);
    }
}

