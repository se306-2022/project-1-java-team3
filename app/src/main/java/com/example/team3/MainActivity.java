package com.example.team3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.team3.data.DataProvider;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Uncomment if you want to seed data to db.
//        DataProvider dp = new DataProvider(this);
//        dp.seedData();
    }
}