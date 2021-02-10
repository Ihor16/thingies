package com.example.thingies;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;

public class MealPlanCalculations extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private TextView date_text;
    
     
    /*
    Dates the user moves out
     */
    private static int USER_YEAR = LocalDate.now().getYear();
    private static int USER_MONTH = Month.APRIL.getValue();
    private static int USER_DAY = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan_calculations);
        TextView textView = findViewById(R.id.textView_mealPlan_link);
        textView.setMovementMethod(LinkMovementMethod.getInstance());

        /*
        Set the listener to the ChangeDate button
         */
        date_text = findViewById(R.id.move_out_date_textView);
        findViewById(R.id.change_date_button).setOnClickListener(v -> showDatePickerDialog());
    }

    /*
    Creates a calendar view with default date that we set
    ASSUME that the user is moving out on April 30
     */
    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                USER_YEAR,
                USER_MONTH - 1,
                USER_DAY
        );
        datePickerDialog.show();
    }

    /*
    Formats the date that the user chose and puts the value in the textView
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        // cause months start from 0 in that Calendar class
        month = month + 1;

        setUserMoveOutDate(year, month, dayOfMonth);
        resetSpendingCalculation();

        // Adds the formatted date to the move out textView
        TextView mover_out_date_textView = findViewById(R.id.move_out_date_textView);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");

        LocalDate ld = LocalDate.of(year, month, dayOfMonth);
        mover_out_date_textView.setText(ld.format(dateTimeFormatter));
    }
    
    // Removes the value from the spending text view
    private void resetSpendingCalculation() {
        TextView daily_spending_textView = findViewById(R.id.textView_MealPlan_result);
        daily_spending_textView.setText("");
    }

    // Updates the constants so that we can recalculate the daily spending    
    private void setUserMoveOutDate(int year, int month, int dayOfMonth) {
        USER_YEAR = year;
        USER_MONTH = month;
        USER_DAY = dayOfMonth;
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
        TextView daily_spending_textView = findViewById(R.id.textView_MealPlan_result);
        DecimalFormat df = new DecimalFormat("#.##");
        String res = (sum == 0) ? ("Come on, enter some value...") :
                ("You're good to go with " + df.format(canSpendDaily) + " CAD per day");
        daily_spending_textView.setText(res);
        daily_spending_textView.setVisibility(View.VISIBLE);
    }

    /*
     Calculates how many days left till the user moves out
     ASSUME: the user moves out on April 30, 2021
     */
    private long calculateDays() {
        LocalDate ld1 = LocalDate.now();
        LocalDate ld2 = LocalDate.of(USER_YEAR, USER_MONTH, USER_DAY);
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