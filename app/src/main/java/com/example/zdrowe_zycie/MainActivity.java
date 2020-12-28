package com.example.zdrowe_zycie;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.zdrowe_zycie.fragments.FragmentNotificationSettings;
import com.example.zdrowe_zycie.fragments.FragmentUserSettings;
import com.example.zdrowe_zycie.helpers.AlarmHelper;
import com.example.zdrowe_zycie.helpers.SqliteHelper;
import com.example.zdrowe_zycie.utils.AppUtils;
import com.github.mikephil.charting.data.Entry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Random;

import co.ceryle.radiorealbutton.RadioRealButton;
import co.ceryle.radiorealbutton.RadioRealButtonGroup;
import params.com.stepprogressview.StepProgressView;

public class MainActivity extends AppCompatActivity {

    TextView tvIntook, tvTotalIntake, tvCustom;
    ConstraintLayout main_activity_parent;
    ImageView viewPen, viewIcon1, viewIcon2, viewIcon3, viewIcon4, viewIcon5;
    TextView textIcon1, textIcon2, textIcon3, textIcon4, textIcon5, facts_view;
    LinearLayout icon1, icon2, icon3, icon4, icon5, iconCustom;
    Resources res;
    Cursor cursorFactsWater, cursorFactsEat;
    FloatingActionButton fabAdd;
    SharedPreferences sharedPref;
    int totalIntake, inTook;
    int selectedOption = 0;
    String dateNow;
    boolean flagEat = false;
    boolean notificStatus;
    private SqliteHelper sqliteHelper;
    private boolean doubleBackToExitPressedOnce = false;
    private TypedValue outValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvIntook = findViewById(R.id.tvIntook);
        tvTotalIntake = findViewById(R.id.tvTotalIntake);
        tvCustom = findViewById(R.id.tvCustom);
        main_activity_parent = findViewById(R.id.main_activity_parent);
        viewPen = findViewById(R.id.viewPen);
        viewIcon1 = findViewById(R.id.viewIcon1);
        viewIcon2 = findViewById(R.id.viewIcon2);
        viewIcon3 = findViewById(R.id.viewIcon3);
        viewIcon4 = findViewById(R.id.viewIcon4);
        viewIcon5 = findViewById(R.id.viewIcon5);
        textIcon1 = findViewById(R.id.textIcon1);
        textIcon2 = findViewById(R.id.textIcon2);
        textIcon3 = findViewById(R.id.textIcon3);
        textIcon4 = findViewById(R.id.textIcon4);
        textIcon5 = findViewById(R.id.textIcon5);
        icon1 = findViewById(R.id.Icon1);
        icon2 = findViewById(R.id.Icon2);
        icon3 = findViewById(R.id.Icon3);
        icon4 = findViewById(R.id.Icon4);
        icon5 = findViewById(R.id.Icon5);
        iconCustom = findViewById(R.id.Custom);
        fabAdd = findViewById(R.id.fabAdd);
        res = getResources();
        dateNow = AppUtils.getCurrentDate();
        facts_view = findViewById(R.id.Facts_view);

        sharedPref = getSharedPreferences(AppUtils.getUSERS_SHARED_PREF(), AppUtils.getPRIVATE_MODE());
        sqliteHelper = new SqliteHelper(this);
        cursorFactsEat = sqliteHelper.getFacts(true);
        cursorFactsWater = sqliteHelper.getFacts(false);
        setRandomFact(cursorFactsEat);
        setRandomFact(cursorFactsWater);
        if (flagEat) {
            totalIntake = sharedPref.getInt(AppUtils.getTOTAL_INTAKE_KEY_EAT(), 1);
        } else {
            totalIntake = sharedPref.getInt(AppUtils.getTOTAL_INTAKE_KEY_WATER(), 1);
        }

        if(sharedPref.getBoolean(AppUtils.getFirstRunKey(),true))
        {
            startActivity(new Intent(this, InitUserInfoActivity.class));
            this.finish();
        }

    }

    public void updateValues(boolean flagEat) {
        if (flagEat) {
            totalIntake = sharedPref.getInt(AppUtils.getTOTAL_INTAKE_KEY_EAT(), 1);
        } else {
            totalIntake = sharedPref.getInt(AppUtils.getTOTAL_INTAKE_KEY_WATER(), 1);
        }
        inTook = sqliteHelper.getIntook(dateNow, flagEat);
        setWaterLevel(inTook, totalIntake);
    }


    @Override
    protected void onStart() {
        super.onStart();

        ImageButton ButtonUserSettings = findViewById(R.id.ButtonUserSettings);
        ButtonUserSettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentUserSettings Fragment = new FragmentUserSettings((Context)MainActivity.this);
                Bundle args = new Bundle();
                args.putBoolean("flagEat", flagEat);
                Fragment.setArguments(args);
                Fragment.show(MainActivity.this.getSupportFragmentManager(), Fragment.getTag());
            }
        });

        ImageButton ButtonNotificationSettings = findViewById(R.id.ButtonNotificationSettings);
        ButtonNotificationSettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentNotificationSettings Fragment = new FragmentNotificationSettings(MainActivity.this);
                Bundle args = new Bundle();
                args.putBoolean("flagEat", flagEat);
                Fragment.setArguments(args);
                Fragment.show(MainActivity.this.getSupportFragmentManager(), Fragment.getTag());
            }
        });

        ImageButton ButtonStatictic = findViewById(R.id.buttonStatistic);
        ButtonStatictic.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, StatsActivity.class);
                myIntent.putExtra("key", flagEat);
                MainActivity.this.startActivity(myIntent);
            }
        });

        RadioRealButtonGroup ChoseActivity = findViewById(R.id.radioChose);
        ChoseActivity.setPosition(0);
        ChoseActivity.setOnClickedButtonListener(new RadioRealButtonGroup.OnClickedButtonListener() {
            @Override
            public void onClickedButton(RadioRealButton button, int position) {
                switch (position) {
                    case 0:
                        flagEat = false;
                        SetWaterView();
                        updateValues(flagEat);
                        break;
                    case 1:
                        flagEat = true;
                        SetEatView();
                        updateValues(flagEat);
                        break;
                    default:
                        break;

                }
            }
        });

        outValue = new TypedValue();
        this.getApplicationContext().getTheme().resolveAttribute(
                android.R.attr.selectableItemBackground,
                outValue,
                true
        );

        notificStatus = sharedPref.getBoolean(AppUtils.getNOTIFICATION_STATUS_KEY(), true);
        AlarmHelper alarm = new AlarmHelper();
        if (!alarm.checkAlarm(this) && notificStatus) {
            alarm.setAlarm(
                    this,
                    Long.valueOf(sharedPref.getInt(AppUtils.getNOTIFICATION_FREQUENCY_KEY(), 30))
            );
        }

        sqliteHelper.addAll(dateNow, 0, this.totalIntake, true);
        sqliteHelper.addAll(dateNow, 0, this.totalIntake, false);
        sqliteHelper.updateTotalIntake(dateNow, sharedPref.getInt(AppUtils.getTOTAL_INTAKE_KEY_EAT(), 1), true);
        sqliteHelper.updateTotalIntake(dateNow, sharedPref.getInt(AppUtils.getTOTAL_INTAKE_KEY_WATER(), 1), false);

        updateValues(flagEat);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View it) {
                if (selectedOption != 0) {
                    if (inTook * 100 / totalIntake <= 140) {
                        if (sqliteHelper.addIntook(dateNow, selectedOption, flagEat) > 0) {
                            inTook += selectedOption;
                            setWaterLevel(inTook, totalIntake);
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Co za duzo to niezdrowo!", Toast.LENGTH_LONG).show();

                    }

                    selectedOption = 0;
                    tvCustom.setText("Custom");
                    icon1.setBackground(getDrawable(outValue.resourceId));
                    icon2.setBackground(getDrawable(outValue.resourceId));
                    icon3.setBackground(getDrawable(outValue.resourceId));
                    icon4.setBackground(getDrawable(outValue.resourceId));
                    icon5.setBackground(getDrawable(outValue.resourceId));
                    iconCustom.setBackground(getDrawable(outValue.resourceId));

                } else {
                    YoYo.with(Techniques.Shake)
                            .duration(700)
                            .playOn(findViewById(R.id.cardView));
                    Toast.makeText(MainActivity.this, "Wybierz ilość", Toast.LENGTH_LONG).show();
                }
            }
        });

        icon1.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View it) {


                if (flagEat) {
                    selectedOption = 50;
                } else {
                    selectedOption = 100;
                }
                icon1.setBackground(getDrawable(R.drawable.splash_bg_gradiant));
                icon2.setBackground(getDrawable(outValue.resourceId));
                icon3.setBackground(getDrawable(outValue.resourceId));
                icon4.setBackground(getDrawable(outValue.resourceId));
                icon5.setBackground(getDrawable(outValue.resourceId));
                iconCustom.setBackground(getDrawable(outValue.resourceId));

            }
        });

        icon2.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View it) {

                selectedOption = 150;
                icon1.setBackground(getDrawable(outValue.resourceId));
                icon2.setBackground(getDrawable(R.drawable.splash_bg_gradiant));
                icon3.setBackground(getDrawable(outValue.resourceId));
                icon4.setBackground(getDrawable(outValue.resourceId));
                icon5.setBackground(getDrawable(outValue.resourceId));
                iconCustom.setBackground(getDrawable(outValue.resourceId));

            }
        });

        icon3.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View it) {

                if (flagEat) {
                    selectedOption = 200;
                } else {
                    selectedOption = 300;
                }
                icon1.setBackground(getDrawable(outValue.resourceId));
                icon2.setBackground(getDrawable(outValue.resourceId));
                icon3.setBackground(getDrawable(R.drawable.splash_bg_gradiant));
                icon4.setBackground(getDrawable(outValue.resourceId));
                icon5.setBackground(getDrawable(outValue.resourceId));
                iconCustom.setBackground(getDrawable(outValue.resourceId));

            }
        });
        icon4.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View it) {

                if (flagEat) {
                    selectedOption = 300;
                } else {
                    selectedOption = 500;
                }
                icon1.setBackground(getDrawable(outValue.resourceId));
                icon2.setBackground(getDrawable(outValue.resourceId));
                icon3.setBackground(getDrawable(outValue.resourceId));
                icon4.setBackground(getDrawable(R.drawable.splash_bg_gradiant));
                icon5.setBackground(getDrawable(outValue.resourceId));
                iconCustom.setBackground(getDrawable(outValue.resourceId));

            }
        });
        icon5.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View it) {

                if (flagEat) {
                    selectedOption = 500;
                } else {
                    selectedOption = 1000;
                }
                icon1.setBackground(getDrawable(outValue.resourceId));
                icon2.setBackground(getDrawable(outValue.resourceId));
                icon3.setBackground(getDrawable(outValue.resourceId));
                icon4.setBackground(getDrawable(outValue.resourceId));
                icon5.setBackground(getDrawable(R.drawable.splash_bg_gradiant));
                iconCustom.setBackground(getDrawable(outValue.resourceId));

            }
        });
        iconCustom.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View it) {

                LayoutInflater li = LayoutInflater.from((Context) MainActivity.this);
                View promptsView = li.inflate(R.layout.custom_input_dialog, (ViewGroup) null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder((Context) MainActivity.this);
                alertDialogBuilder.setView(promptsView);
                final TextInputEditText userInput = promptsView.findViewById(R.id.CustomInputLabel);
                alertDialogBuilder.setPositiveButton("OK", (new DialogInterface.OnClickListener() {
                    public final void onClick(DialogInterface dialog, int id) {
                        String inputText = userInput.getText().toString();
                        if (!TextUtils.isEmpty(inputText)) {
                            tvCustom.setText(inputText + " ml");
                            selectedOption = Integer.parseInt(inputText);
                        }
                    }
                }));
                alertDialogBuilder.setNegativeButton("Cancel", (new DialogInterface.OnClickListener() {
                    public final void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }));
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                icon1.setBackground(getDrawable(outValue.resourceId));
                icon2.setBackground(getDrawable(outValue.resourceId));
                icon3.setBackground(getDrawable(outValue.resourceId));
                icon4.setBackground(getDrawable(outValue.resourceId));
                icon5.setBackground(getDrawable(outValue.resourceId));
                iconCustom.setBackground(getDrawable(R.drawable.splash_bg_gradiant));
            }
        });
    }

    private void SetEatView() {
        main_activity_parent.setBackgroundResource(R.drawable.ic_eat_bg);
        viewPen.setImageDrawable(ResourcesCompat.getDrawable(res, R.drawable.ic_pen_orange, null));
        Drawable dish = ResourcesCompat.getDrawable(res, R.drawable.ic_dish, null);
        viewIcon1.setImageDrawable(dish);
        viewIcon2.setImageDrawable(dish);
        viewIcon3.setImageDrawable(dish);
        viewIcon4.setImageDrawable(dish);
        viewIcon5.setImageDrawable(dish);
        textIcon1.setText("50kkal");
        textIcon2.setText("150kkal");
        textIcon3.setText("200kkal");
        textIcon4.setText("300kkal");
        textIcon5.setText("500kkal");
        icon1.setBackground(getDrawable(outValue.resourceId));
        icon2.setBackground(getDrawable(outValue.resourceId));
        icon3.setBackground(getDrawable(outValue.resourceId));
        icon4.setBackground(getDrawable(outValue.resourceId));
        icon5.setBackground(getDrawable(outValue.resourceId));
        iconCustom.setBackground(getDrawable(outValue.resourceId));
        selectedOption = 0;
        ((RadioRealButtonGroup) findViewById(R.id.radioChose)).setPosition(1);
        setRandomFact(cursorFactsEat);
    }

    private void SetWaterView() {
        main_activity_parent.setBackgroundResource(R.drawable.ic_water_bg);
        viewPen.setImageDrawable(ResourcesCompat.getDrawable(res, R.drawable.ic_pen_blue, null));
        Drawable glass = ResourcesCompat.getDrawable(res, R.drawable.ic_glass, null);
        viewIcon1.setImageDrawable(glass);
        viewIcon2.setImageDrawable(glass);
        viewIcon3.setImageDrawable(glass);
        viewIcon4.setImageDrawable(glass);
        viewIcon5.setImageDrawable(glass);
        textIcon1.setText("100ml");
        textIcon2.setText("150ml");
        textIcon3.setText("300ml");
        textIcon4.setText("500ml");
        textIcon5.setText("1000ml");
        icon1.setBackground(getDrawable(outValue.resourceId));
        icon2.setBackground(getDrawable(outValue.resourceId));
        icon3.setBackground(getDrawable(outValue.resourceId));
        icon4.setBackground(getDrawable(outValue.resourceId));
        icon5.setBackground(getDrawable(outValue.resourceId));
        iconCustom.setBackground(getDrawable(outValue.resourceId));
        selectedOption = 0;
        ((RadioRealButtonGroup) findViewById(R.id.radioChose)).setPosition(0);
        setRandomFact(cursorFactsWater);
    }

    private final void setWaterLevel(int inTook, int totalIntake) {
        TextView tvIntook = findViewById(R.id.tvIntook);
        TextView tvTotalIntake = findViewById(R.id.tvTotalIntake);
        YoYo.with(Techniques.SlideInDown).duration(500L).playOn(tvIntook);
        tvIntook.setText(String.valueOf(inTook));
        if (flagEat) {
            tvTotalIntake.setText("/" + totalIntake + " kkal");
        } else {
            tvTotalIntake.setText("/" + totalIntake + " ml");
        }
        int progress = (int) ((float) inTook / (float) totalIntake * 100);
        StepProgressView intakeProgress = findViewById(R.id.intakeProgress);
        YoYo.with(Techniques.Pulse)
                .duration(500L)
                .playOn(intakeProgress);
        intakeProgress.setCurrentProgress(progress);
        if (inTook * 100 / totalIntake > 140) {
            Toast.makeText(MainActivity.this, "Wystarczy na dzisiaj", Toast.LENGTH_LONG).show();
        }

    }

    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(MainActivity.this, "Please click BACK again to exit", Toast.LENGTH_LONG).show();
        new Handler().postDelayed(new Runnable() {
            public final void run() {
                MainActivity.this.doubleBackToExitPressedOnce = false;
            }
        }, 1000L);
    }

    public void setRandomFact(Cursor cursor) {
        int count;
        if (cursor.moveToFirst()) {
            count = cursor.getCount();
            Random random = new Random();
            int randomNumber = random.nextInt(count);
            for (int i = 0; i < randomNumber; ++i) {
                cursor.moveToNext();
            }
            String fact = cursor.getString(1);
            facts_view.setText((CharSequence) fact);
        } else {
            Toast.makeText(this, (CharSequence) "Empty table", Toast.LENGTH_LONG).show();
        }
    }
}