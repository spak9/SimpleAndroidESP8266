package com.example.androidesp8266;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/*
    This Interface class will be split into two main interfaces:
    - 1. LEDs
    - 2. Temperature
    They will both send HTTP requests to the ESP8266 server and
    wait for with temperature data response (JSON) or just OK response
 */

public class InterfaceActivity extends AppCompatActivity {

    // the lightbulb image
    ImageView lightbulb;

    // http://192.168.86.22/

    // Our RequestQueue object for sending HTTP request
    RequestQueue queue = Volley.newRequestQueue(this);

    boolean on = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interface);

        lightbulb = (ImageView)findViewById(R.id.lightbulb);

        // Only 2 states for the lightbulb: on (1) or off (0)
        lightbulb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // if ON, then turn OFF
                // setColorFilter() api is quite complicated online, but will ultimately change color
                if (on) {
                    on = false;
                    int color = ContextCompat.getColor(InterfaceActivity.this, R.color.colorPrimaryDark);
                    //ImageViewCompat.setImageTintList(lightbulb, ColorStateList.valueOf(color));
                    lightbulb.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                    Toast.makeText(InterfaceActivity.this, "on to off", Toast.LENGTH_SHORT).show();
                }
                // if OFF, then turn ON
                else {
                    on = true;
                    int color = ContextCompat.getColor(InterfaceActivity.this, R.color.yellow);
                    lightbulb.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                    Toast.makeText(InterfaceActivity.this, "off to on", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    /*
        We need a method for handling LEDs HTTP POST/responses.
        For our HTTP request/responses, we'll be using Android's Volley library
     */
    public static int sendRequest(boolean ON) {

        return 1;
    }
}