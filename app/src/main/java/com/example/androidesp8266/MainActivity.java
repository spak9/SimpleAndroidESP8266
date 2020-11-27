package com.example.androidesp8266;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button houseInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        houseInterface = (Button) findViewById(R.id.houseInterface);

         // When the button is clicked, start the InterfaceActivity
        houseInterface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seeHouseInterface();
            }
        });
    }

    /*
    The only method on this Activity. It should just create an Intent
    and start a new Activity that will be the interface of both
    1. LEDs on/off
    2. DHT11 Temperature/humitity GET
     */
    public void seeHouseInterface() {
        Intent intent = new Intent(this, InterfaceActivity.class);
        startActivity(intent);
    }
}