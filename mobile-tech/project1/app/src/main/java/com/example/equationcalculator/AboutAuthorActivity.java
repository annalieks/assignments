package com.example.equationcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class AboutAuthorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_author);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}