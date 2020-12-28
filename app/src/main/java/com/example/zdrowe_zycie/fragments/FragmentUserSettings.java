package com.example.zdrowe_zycie.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.zdrowe_zycie.MainActivity;
import com.example.zdrowe_zycie.R;
import com.example.zdrowe_zycie.helpers.SqliteHelper;
import com.example.zdrowe_zycie.utils.AppUtils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;

import co.ceryle.radiorealbutton.RadioRealButton;
import co.ceryle.radiorealbutton.RadioRealButtonGroup;


public final class FragmentUserSettings extends BottomSheetDialogFragment {

    private SharedPreferences sharedPref;
    private final Context mCtx;
    private Float physicalActivity;
    private HashMap _$_findViewCache;
    private ConstraintLayout mainUserLayout;
    private RadioRealButtonGroup gender;
    private String sex, dateNow;
    ;
    private Button buttonOk;
    private Spinner spiner;
    private SqliteHelper sqliteHelper;
    private boolean myValues, flagEat;
    private int item_id, currentWater, currentEat;
    private Switch custSwitch;
    private EditText et_weight, et_age, et_growth, et_cust_water, et_cust_aet;
    private TextInputLayout weight, age, growth, cust_water, cust_aet;
    private StringBuilder sBilder1, sBilder2, sBilder3, sBilder4, sBilder5;

    public FragmentUserSettings(Context mCtx) {
        this.mCtx = mCtx;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle args = getArguments();
        flagEat = args.getBoolean("flagEat", false);
        View view = inflater.inflate(R.layout.fragment_user_settings, container, false);
        mainUserLayout = view.findViewById(R.id.mainUserLayout);
        if (flagEat == false) {
            mainUserLayout.setBackgroundResource(R.drawable.blue_bg);
        } else {
            mainUserLayout.setBackgroundResource(R.drawable.orange_bg);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dateNow = AppUtils.getCurrentDate();
        buttonOk = view.findViewById(R.id.buttonSettingsOK);
        sharedPref = mCtx.getSharedPreferences(AppUtils.getUSERS_SHARED_PREF(), AppUtils.getPRIVATE_MODE());
        weight = view.findViewById(R.id.etWeight);
        age = view.findViewById(R.id.etAge);
        growth = view.findViewById(R.id.etGrowth2);
        gender = view.findViewById(R.id.radioGroup_settings);
        spiner = view.findViewById(R.id.physicalActivity_settings);
        custSwitch = view.findViewById(R.id.switchCustom);
        cust_water = view.findViewById(R.id.etCustom_water);
        cust_aet = view.findViewById(R.id.etCustom_eat);

        et_cust_aet = cust_aet.getEditText();
        et_cust_water = cust_water.getEditText();
        et_weight = weight.getEditText();
        et_age = age.getEditText();
        et_growth = growth.getEditText();
        item_id = this.sharedPref.getInt(AppUtils.getWorkTimeKey(), 0);
        sex = this.sharedPref.getString(AppUtils.getSexKey().toString(), "");
        myValues = this.sharedPref.getBoolean(AppUtils.getMY_VALUES_KEY(), false);

        switch (sex) {
            case "Mężczyzna":
                gender.setPosition(0);
                break;
            case "Kobieta":
                gender.setPosition(1);
                break;
            default:
                break;
        }

        if (myValues) {
            custSwitch.setChecked(true);
            cust_water.setEnabled(true);
            cust_aet.setEnabled(true);
        } else {
            custSwitch.setChecked(false);
            cust_water.setEnabled(false);
            cust_aet.setEnabled(false);
        }

        currentWater = this.sharedPref.getInt(AppUtils.getTOTAL_INTAKE_KEY_WATER(), 0);
        currentEat = this.sharedPref.getInt(AppUtils.getTOTAL_INTAKE_KEY_EAT(), 0);
        sBilder1 = (new StringBuilder()).append("");
        et_weight.setText((CharSequence) sBilder1.append(this.sharedPref.getInt(AppUtils.getWeightKey(), 0)).toString());
        sBilder2 = (new StringBuilder()).append("");
        et_age.setText((CharSequence) sBilder2.append(this.sharedPref.getInt(AppUtils.getAgeKey(), 0)).toString());
        sBilder3 = (new StringBuilder()).append("");
        et_growth.setText((CharSequence) sBilder3.append(this.sharedPref.getInt(AppUtils.getGrowthKey(), 0)).toString());
        sBilder4 = (new StringBuilder()).append("");
        et_cust_water.setText((CharSequence) sBilder4.append(this.sharedPref.getInt(AppUtils.getTOTAL_INTAKE_KEY_WATER(), 0)).toString());
        sBilder5 = (new StringBuilder()).append("");
        et_cust_aet.setText((CharSequence) sBilder5.append(this.sharedPref.getInt(AppUtils.getTOTAL_INTAKE_KEY_EAT(), 0)).toString());

        spiner.setSelection(item_id);

        gender.setOnClickedButtonListener(new RadioRealButtonGroup.OnClickedButtonListener() {
            @Override
            public void onClickedButton(RadioRealButton button, int position) {
                switch (position) {
                    case 0:
                        sex = "Mężczyzna";
                        break;
                    case 1:
                        sex = "Kobieta";
                        break;
                    default:
                        break;
                }
                //Toast.makeText(mCtx, "Płeć: " + sex, Toast.LENGTH_SHORT).show();
            }
        });

        custSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cust_water.setEnabled(true);
                    cust_aet.setEnabled(true);
                    myValues = true;
                } else {
                    cust_water.setEnabled(false);
                    cust_aet.setEnabled(false);
                    myValues = false;
                }
                ;
            }
        });

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                et_weight = weight.getEditText();
                et_age = age.getEditText();
                et_growth = growth.getEditText();
                item_id = spiner.getSelectedItemPosition();
                if (et_weight.getText().toString().isEmpty() || et_growth.getText().toString().isEmpty() || et_age.getText().toString().isEmpty() || sex.isEmpty()) {
                    Toast.makeText(mCtx, "Pola muszą być wypęłnione i zaznaczona płeć!", Toast.LENGTH_SHORT).show();
                } else {
                    if (Integer.parseInt(et_weight.getText().toString()) > 9 && Integer.parseInt(et_weight.getText().toString()) < 251) {
                        if (Integer.parseInt(et_age.getText().toString()) > 0 && Integer.parseInt(et_age.getText().toString()) < 131) {
                            if (Integer.parseInt(et_growth.getText().toString()) > 49 && Integer.parseInt(et_growth.getText().toString()) < 291) {
                                SharedPreferences.Editor editor = sharedPref.edit();
                                sqliteHelper = new SqliteHelper(mCtx);

                                switch ((int) spiner.getSelectedItemId()) {
                                    case 0:
                                        physicalActivity = (float) 1.2;
                                        break;
                                    case 1:
                                        physicalActivity = (float) 1.38;
                                        break;
                                    case 2:
                                        physicalActivity = (float) 1.46;
                                        break;
                                    case 3:
                                        physicalActivity = (float) 1.5;
                                        break;
                                    case 4:
                                        physicalActivity = (float) 1.55;
                                        break;
                                    case 5:
                                        physicalActivity = (float) 1.6;
                                        break;
                                    case 6:
                                        physicalActivity = (float) 1.64;
                                        break;
                                    case 7:
                                        physicalActivity = (float) 1.73;
                                        break;
                                    case 8:
                                        physicalActivity = (float) 1.80;
                                        break;
                                    default:
                                        physicalActivity = (float) 1;
                                        break;
                                }
                                editor.putInt(AppUtils.getWeightKey(), Integer.parseInt(et_weight.getText().toString()));
                                editor.putInt(AppUtils.getGrowthKey(), Integer.parseInt(et_growth.getText().toString()));
                                editor.putInt(AppUtils.getAgeKey(), Integer.parseInt(et_age.getText().toString()));
                                editor.putInt(AppUtils.getWorkTimeKey(), (int) spiner.getSelectedItemId());
                                editor.putString(AppUtils.getSexKey(), sex);
                                editor.putBoolean(AppUtils.getMY_VALUES_KEY(), myValues);
                                if (custSwitch.isChecked()) {
                                    et_cust_aet = cust_aet.getEditText();
                                    et_cust_water = cust_water.getEditText();
                                    if (currentWater == Integer.parseInt(et_cust_water.getText().toString())) {

                                    } else {
                                        editor.putInt(AppUtils.getTOTAL_INTAKE_KEY_WATER(), Integer.parseInt(et_cust_water.getText().toString()));
                                        sqliteHelper.updateTotalIntake(dateNow, Integer.parseInt(et_cust_water.getText().toString()), false);
                                    }
                                    if (currentEat == Integer.parseInt(et_cust_aet.getText().toString())) {

                                    } else {
                                        editor.putInt(AppUtils.getTOTAL_INTAKE_KEY_EAT(), Integer.parseInt(et_cust_aet.getText().toString()));
                                        sqliteHelper.updateTotalIntake(dateNow, Integer.parseInt(et_cust_aet.getText().toString()), true);
                                    }
                                } else {
                                    float water = AppUtils.calculateIntake(sex, Integer.parseInt(et_weight.getText().toString()), physicalActivity, Integer.parseInt(et_growth.getText().toString()),Integer.parseInt(et_age.getText().toString()),false);
                                    float eat = AppUtils.calculateIntake(sex, Integer.parseInt(et_weight.getText().toString()), physicalActivity, Integer.parseInt(et_growth.getText().toString()),Integer.parseInt(et_age.getText().toString()),true);
                                    if (currentWater == (int) water) {

                                    } else {
                                        editor.putInt(AppUtils.getTOTAL_INTAKE_KEY_WATER(), (int) water);
                                        sqliteHelper.updateTotalIntake(dateNow, (int) water, false);
                                    }

                                    if (currentEat == (int) eat) {

                                    } else {
                                        editor.putInt(AppUtils.getTOTAL_INTAKE_KEY_EAT(), (int) eat);
                                        sqliteHelper.updateTotalIntake(dateNow, (int) eat, true);
                                    }
                                }
                                editor.apply();
                                FragmentUserSettings.this.dismiss();
                                MainActivity newValues = (MainActivity) FragmentUserSettings.this.getActivity();
                                newValues.updateValues(flagEat);
                            } else {
                                Toast.makeText(mCtx, "Podaj poprawny wzrost! (50-290)", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(mCtx, "Podaj poprawny wiek! (1-130)", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(mCtx, "Podaj poprawną wagę! (10-250)", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

}