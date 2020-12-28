package com.example.zdrowe_zycie;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.zdrowe_zycie.utils.AppUtils;
import com.google.android.material.textfield.TextInputLayout;

import co.ceryle.radiorealbutton.RadioRealButton;
import co.ceryle.radiorealbutton.RadioRealButtonGroup;

public class InitUserInfoActivity extends AppCompatActivity {

    private TextInputLayout weight, growth, age;
    private Spinner workTime;
    private EditText et_weight, et_growth, et_age;
    private String sex = "Mężczyzna";
    private float physicalActivity;
    private InputMethodManager imm;
    private ConstraintLayout parent;
    private SharedPreferences sharedPref;
    private RadioRealButtonGroup gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init_user_info);

        sharedPref = getSharedPreferences(AppUtils.getUSERS_SHARED_PREF(), AppUtils.getPRIVATE_MODE());
        weight = (TextInputLayout) findViewById(R.id.etWeight);
        growth = findViewById(R.id.etGrowth);
        age = findViewById(R.id.etAge);
        workTime = findViewById(R.id.physicalActivity_init);
        gender = findViewById(R.id.radioGroup_gender);
        Button btnContinue = (Button) findViewById(R.id.btnContinue);


        gender.setPosition(0);
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
                //Toast.makeText(InitUserInfoActivity.this, "Płeć: " + sex, Toast.LENGTH_SHORT).show();
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                parent = (ConstraintLayout) findViewById(R.id.init_user_info_parent_layout);
                imm.hideSoftInputFromWindow(parent.getWindowToken(), 0);


                et_weight = weight.getEditText();
                et_growth = growth.getEditText();
                et_age = age.getEditText();
                switch ((int) workTime.getSelectedItemId()) {
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
                        physicalActivity = 1;
                        break;
                }

                if (et_weight.getText().toString().isEmpty() || et_growth.getText().toString().isEmpty() || et_age.getText().toString().isEmpty() || sex.isEmpty()) {
                    Toast.makeText(InitUserInfoActivity.this, "Wszystkie pola muszą być wypęłnione i zaznaczona płeć!", Toast.LENGTH_SHORT).show();
                } else {

                    if (Integer.parseInt(et_weight.getText().toString()) > 9 && Integer.parseInt(et_weight.getText().toString()) < 251) {
                        if (Integer.parseInt(et_age.getText().toString()) > 0 && Integer.parseInt(et_age.getText().toString()) < 131) {
                            if (Integer.parseInt(et_growth.getText().toString()) > 49 && Integer.parseInt(et_growth.getText().toString()) < 291) {
                                SharedPreferences.Editor editor = sharedPref.edit();
                                double water,eat;
                                water = AppUtils.calculateIntake(sex, Integer.parseInt(et_weight.getText().toString()), physicalActivity, Integer.parseInt(et_growth.getText().toString()),Integer.parseInt(et_age.getText().toString()),false);
                                eat = AppUtils.calculateIntake(sex, Integer.parseInt(et_weight.getText().toString()), physicalActivity, Integer.parseInt(et_growth.getText().toString()),Integer.parseInt(et_age.getText().toString()),true);
                                editor.putInt(AppUtils.getTOTAL_INTAKE_KEY_WATER(), (int) water);
                                editor.putInt(AppUtils.getTOTAL_INTAKE_KEY_EAT(), (int) eat);
                                editor.putInt(AppUtils.getWeightKey(), Integer.parseInt(et_weight.getText().toString()));
                                editor.putInt(AppUtils.getGrowthKey(), Integer.parseInt(et_growth.getText().toString()));
                                editor.putInt(AppUtils.getAgeKey(), Integer.parseInt(et_age.getText().toString()));
                                editor.putInt(AppUtils.getWorkTimeKey(), (int) workTime.getSelectedItemId());
                                editor.putString(AppUtils.getSexKey(), sex);
                                editor.putBoolean(AppUtils.getMY_VALUES_KEY(), false);
                                editor.putBoolean(AppUtils.getFirstRunKey(), false);
                                editor.apply();
                                //Snackbar.make(v, "Ja workaju!", Snackbar.LENGTH_SHORT).show();
                                startActivity(new Intent(InitUserInfoActivity.this, MainActivity.class));
                                InitUserInfoActivity.this.finish();
                            } else {
                                Toast.makeText(InitUserInfoActivity.this, "Podaj poprawny wzrost! (50-290)", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(InitUserInfoActivity.this, "Podaj poprawny wiek! (1-130)", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(InitUserInfoActivity.this, "Podaj poprawną wagę! (10-250)", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}