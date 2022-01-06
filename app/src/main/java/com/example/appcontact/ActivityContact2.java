package com.example.appcontact;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ActivityContact2 extends AppCompatActivity {

    String name;
    String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact2);

        Intent intent = getIntent();

        name = intent.getStringExtra(MainActivity.EXTRA_NAME);
        number = intent.getStringExtra(MainActivity.EXTRA_NUMBER);

        TextView textView = (TextView) findViewById(R.id._Name);
        textView.setText(name);
        textView = (TextView) findViewById(R.id._number);
        textView.setText(number);

    }
}