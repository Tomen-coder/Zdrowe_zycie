package com.example.zdrowe_zycie.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public final class AppUtils {

    private static final String USERS_SHARED_PREF = "user_pref";
    private static final int PRIVATE_MODE = 0;

    private static String WEIGHT_KEY = "weight"; // waga użytkownika
    private static String AGE_KEY = "age"; // jego wiek
    private static String WORK_TIME_KEY = "worktime"; // aktywność fizyczna
    private static String SEX_KEY = "sex"; // płeć
    private static String GROWTH_KEY = "growth"; // wzrost
    private static String MY_VALUES_KEY = "myvalues"; // flaga dla własnych wartości wody i jedzenia

    private static String FIRST_RUN_KEY = "firstrun";
    public static String TOTAL_INTAKE_KEY_WATER = "totalintakewater";
    public static String TOTAL_INTAKE_KEY_EAT = "totalintakeeat";
    private static String NOTIFICATION_STATUS_KEY = "notificationstatus";
    private static String NOTIFICATION_FREQUENCY_KEY = "notificationfrequency";
    private static String NOTIFICATION_MSG_KEY = "notificationmsg";
    private static String NOTIFICATION_TONE_URI_KEY = "notificationtone";

    public static final float calculateIntake(String sex, int weight, float workTime, int growth, int wiek, boolean flag) {
        float val;
        if (flag) {// dla jedzenia
            if (sex == "Mężczyzna") {
                val = (float) ((66.47 + (13.7 * weight) + (5 * growth) - (6.76 * wiek)) * workTime);
            } else {
                val = (float) ((655.1 + (9.567 * weight) + (1.85 * growth) - (4.68 * wiek)) * workTime);
            }
        } else {// dla wody
            if (sex == "Mężczyzna") {
                val = weight * 35;
            } else {
                val = weight * 31;
            }
        }
        return val;
    }


    public static final String getCurrentDate() {
        SimpleDateFormat dateform = new SimpleDateFormat("dd-MM-yyyy");
        return dateform.format(Calendar.getInstance().getTime());
    }

    public static final String getMY_VALUES_KEY() {
        return AppUtils.MY_VALUES_KEY;
    }

    public static final String getUSERS_SHARED_PREF() {
        return AppUtils.USERS_SHARED_PREF;
    }

    public static final int getPRIVATE_MODE() {
        return AppUtils.PRIVATE_MODE;
    }

    public static final String getTOTAL_INTAKE_KEY_WATER() {
        return AppUtils.TOTAL_INTAKE_KEY_WATER;
    }

    public static final String getTOTAL_INTAKE_KEY_EAT() {
        return AppUtils.TOTAL_INTAKE_KEY_EAT;
    }

    public static final String getNOTIFICATION_STATUS_KEY() {
        return AppUtils.NOTIFICATION_STATUS_KEY;
    }

    public static final String getNOTIFICATION_FREQUENCY_KEY() {
        return AppUtils.NOTIFICATION_FREQUENCY_KEY;
    }

    public static final String getNOTIFICATION_MSG_KEY() {
        return AppUtils.NOTIFICATION_MSG_KEY;
    }

    public static final String getNOTIFICATION_TONE_URI_KEY() {
        return AppUtils.NOTIFICATION_TONE_URI_KEY;
    }

    public static final String getWeightKey() {
        return AppUtils.WEIGHT_KEY;
    }

    public static final String getWorkTimeKey() {
        return AppUtils.WORK_TIME_KEY;
    }

    public static final String getSexKey() {
        return AppUtils.SEX_KEY;
    }

    public static final String getAgeKey() {
        return AppUtils.AGE_KEY;
    }

    public static  final String getGrowthKey() {
        return AppUtils.GROWTH_KEY;
    }

    public static final  String getFirstRunKey() {
        return AppUtils.FIRST_RUN_KEY;
    }
}


