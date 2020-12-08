package com.example.androidesp8266;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;

import android.animation.ObjectAnimator;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/*
    This Interface class will be split into two main interfaces:
    - 1. LEDs
    - 2. Temperature
    They will both send HTTP requests (through Volley library) to the ESP8266 server and
    wait for temperature data response (JSON) or just OK response
 */

public class InterfaceActivity extends AppCompatActivity {

    // the lightbulb image
    ImageView lightbulb;
    ImageView thermometer;
    TextView leds_text;
    TextView thermometer_text;
    RequestQueue queue;

    // a static URL given to the esp8266
    String BaseUrl = "http://192.168.86.22/";

    boolean on = MainActivity.on;
    String temp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interface);

        // Our RequestQueue object for sending HTTP request
        queue = Volley.newRequestQueue(this);
        lightbulb = (ImageView)findViewById(R.id.lightbulb);
        leds_text = (TextView)findViewById(R.id.leds_interface);
        thermometer_text = (TextView)findViewById(R.id.temp_interface);
        thermometer = (ImageView)findViewById(R.id.thermometer);


        // Only 2 states for the lightbulb: on (1) or off (0)
        lightbulb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // if ON, then turn OFF
                if (on) {
                    int color = ContextCompat.getColor(InterfaceActivity.this, R.color.colorPrimaryDark);
                    //ImageViewCompat.setImageTintList(lightbulb, ColorStateList.valueOf(color));
                    lightbulb.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                    // Toast.makeText(InterfaceActivity.this, "on to off", Toast.LENGTH_SHORT).show();
                    // send our request
                    sendRequest(on);

                    // change leds state
                    on = false;
                    setLEDsText(on);
                }
                // if OFF, then turn ON
                else {

                    int color = ContextCompat.getColor(InterfaceActivity.this, R.color.yellow);
                    lightbulb.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                    //Toast.makeText(InterfaceActivity.this, "off to on", Toast.LENGTH_SHORT).show();
                    // send our request
                    sendRequest(on);

                    // change leds state
                    on = true;
                    setLEDsText(on);
                }
            }
        });

        /*
        The 2nd listener: the thermometer
         */
        thermometer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String url = BaseUrl + "get_data";
                // We'll know the request went through by checking the response (Toast)
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // firstly rotate the image
                        final Animation animShake = AnimationUtils.loadAnimation(InterfaceActivity.this, R.anim.shake);
                        thermometer.startAnimation(animShake);


                        Toast.makeText(InterfaceActivity.this, response.toString(), Toast.LENGTH_LONG).show();
                        temp = response.toString();
                        thermometer_text.setText("Current Temperature: " + response + "\u2109");
                        int color = ContextCompat.getColor(InterfaceActivity.this, R.color.red);
                        thermometer.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error",error.toString());
                    }
                });

                queue.add(stringRequest);
            }
        });
    }

    /*
    onCreate() used for changing the led image color
     */
    @Override
    protected void onResume() {
        // on creating the Activity, automatically put the
        // lightbulb.png image the needed based on the `on` boolean
        super.onResume();
        if (on) {
            int color = ContextCompat.getColor(InterfaceActivity.this, R.color.yellow);
            lightbulb.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
            setLEDsText(on);
        } else {
            int color = ContextCompat.getColor(InterfaceActivity.this, R.color.colorPrimaryDark);
            lightbulb.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
            setLEDsText(on);
        }
    }

    /*
    Overriding onSaveInstanceState() to make sure config changes don't mess
    with the current state of temperature and leds
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // save our current state of leds & temp
        savedInstanceState.putBoolean("leds", on);
        savedInstanceState.putString("temperature", temp);

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        on = savedInstanceState.getBoolean("leds");
        temp = savedInstanceState.getString("temperature");

        // if temp has an actual string value, therefore has been
        // clicked at least once
        if (temp != null) {
            thermometer_text.setText("Current Temperature: " + temp + "\u2109");
            int color = ContextCompat.getColor(InterfaceActivity.this, R.color.red);
            thermometer.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        }
    }
    /*
        This method will take the state of the LEDs and make a HTTP POST
        request, requesting to do `!ON`
     */
    public int sendRequest(boolean ON) {
        // if led is ON, then need to turn it off --> send to ledOFF
        String url;
        if (ON) {
            url = BaseUrl + "turn_leds_off";
        }
        // else, led if OFF, then need to turn it on --> send to ledON
        else {
            url = BaseUrl + "turn_leds_on";
        }

        // We'll know the request went through by checking the response (Toast)
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(InterfaceActivity.this, response.toString(), Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error",error.toString());
            }
        });

        queue.add(stringRequest);
        return 1;
    }

    /*
    Will set the LEDs text either on or off based on it's state
     */
    public void setLEDsText(boolean ON) {
        if (ON) {
            leds_text.setText("LEDs are ON");
        }
        else {
            leds_text.setText("LEDs are OFF");
        }
    }
}