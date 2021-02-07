package com.example.thingies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void mealPlanSpendings (View view) {
        Intent intent = new Intent(this, MealPlanCalculations.class);
        startActivity(intent);
    }
}