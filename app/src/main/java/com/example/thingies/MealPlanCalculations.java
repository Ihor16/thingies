package com.example.thingies;

import androidx.appcompat.app.AppCompatActivity;

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

        EditText editText_res = findViewById(R.id.editTextNumber_resDol);
        EditText editText_flex = findViewById(R.id.editTextNumber_flexDol);

        float sum = calculateMoney(editText_res, editText_flex);
        long diff = calculateDays();
        float canSpendDaily = (sum / diff);

        /*
        Prints the message for the user; if they didn't enter value in any field, print another message
        ASSUME: if the user didn't enter a value, the sum variable is 0
         */
        DecimalFormat df = new DecimalFormat("#.##");
        TextView textView = findViewById(R.id.textView_MealPlan_result);
        String res = (sum == 0) ? ("Come on, enter some value...") :
                ("You're good to go with " + df.format(canSpendDaily) + " CAD per day");
        textView.setText(res);
        textView.setVisibility(View.VISIBLE);
    }

    /*
     Calculates how many days left till the user moves out
     ASSUME: the user moves out on April 30, 2021
     */
    private long calculateDays() {
        LocalDate ld1 = LocalDate.now();
        LocalDate ld2 = LocalDate.of(2021, Month.APRIL, 30);
        return ChronoUnit.DAYS.between(ld1, ld2);
    }

    private static boolean isEditTextEmpty(EditText editText) {
        return editText.getText().toString().isEmpty();
    }

    /*
    If the editText is empty, assign 0 to its value
    and move the cursor to the left
     */
    private static void handleIfEmpty(EditText editText) {
        editText.setText("0");
        editText.setSelection(1);
    }

    /*
    Calculate how much money the user has
     */
    private static float calculateMoney(EditText editText_res, EditText editText_flex) {

        float resDol;
        float flexDol;
        float sum;

        /*
        +-------+------------+------------------+
        | Res→  | empty      | #                |
        | Flex↓ |            |                  |
        +-------+------------+------------------+
        | empty | Sum : 0    | Flex : 0         |
        |       |            | Sum : Res        |
        +-------+------------+------------------+
        | #     | Res : 0    | Sum : Res + Flex |
        |       | Sum : Flex |                  |
        +-------+------------+------------------+
         */

        if (isEditTextEmpty(editText_res) && isEditTextEmpty(editText_flex)) {
            sum = 0;
        } else if (isEditTextEmpty(editText_res)) {
            handleIfEmpty(editText_res);
            sum = Float.parseFloat(editText_flex.getText().toString());
        } else if (isEditTextEmpty(editText_flex)) {
            handleIfEmpty(editText_flex);
            sum = Float.parseFloat(editText_res.getText().toString());
        } else {
            resDol = Float.parseFloat(editText_res.getText().toString());
            flexDol = Float.parseFloat(editText_flex.getText().toString());
            sum = resDol + flexDol;
        }
        return sum;
    }
}