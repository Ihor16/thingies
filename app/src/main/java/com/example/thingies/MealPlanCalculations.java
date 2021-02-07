package com.example.thingies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;

public class MealPlanCalculations extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan_calculations);
        TextView textView = findViewById(R.id.textView_mealPlan_link);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void onClick_CalcMealPlan(View view) {

        Intent intent = getIntent();

        EditText editText_res = findViewById(R.id.editTextNumber_resDol);
        EditText editText_flex = findViewById(R.id.editTextNumber_flexDol);

        /*
         * Calculates how much money the user has
         */
        float resDol = Float.parseFloat(editText_res.getText().toString());
        float flexDol = Float.parseFloat(editText_flex.getText().toString());
        float sum = resDol + flexDol;

        /*
         * Calculates how many days the user has
         * ASSUME: the user moves out on April 30, 2021
         */
        LocalDate ld1 = LocalDate.now();
        LocalDate ld2 = LocalDate.of(2021, Month.APRIL, 30);
        long diff = ChronoUnit.DAYS.between(ld1, ld2);

        float canSpendDaily = (sum / diff);
        DecimalFormat df = new DecimalFormat("#.##");

        TextView textView = findViewById(R.id.textView_MealPlan_result);
        String res = "You're good to go with " + df.format(canSpendDaily) + " CAD per day";
        textView.setText(res);
        textView.setVisibility(View.VISIBLE);
    }
}