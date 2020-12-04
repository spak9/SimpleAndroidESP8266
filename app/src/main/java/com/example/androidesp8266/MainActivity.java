package com.example.androidesp8266;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {

    // The main button to start the main house interface; the only button
    Button houseInterface;

    // queue for sending messages
    RequestQueue queue;

    // a static URL given to the esp8266, however the uri we need
    // is get_status
    String BaseUrl = "http://192.168.86.22/get_status";

    // assume leds are off, will change with getCurrentState()
    static boolean on = false;

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
    @Override
    protected void onResume() {
        super.onResume();

        // Our RequestQueue object for sending HTTP request
        queue = Volley.newRequestQueue(this);
        // get our current state of leds (either on or off)
        getCurrentState();
    }

    /*
    The only method on this Activity. It should just create an Intent
    and start a new Activity that will be the interface of both
    1. LEDs on/off
    2. DHT11 Temperature/humidity data

     */
    public void seeHouseInterface() {
        Intent intent = new Intent(this, InterfaceActivity.class);
        startActivity(intent);
    }

    /*
    This method will get the initial state of the leds.
    For example, if you leave the leds on and then exit the app, the
    next startup will consider the leds to be off, although they are ON.
    Everytime we create this Activity, it should do a quick request/response for
    the current state of leds and let the `InterfaceActivity` know.
     */
    public void getCurrentState() {
        // make a request to `/get_status`
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BaseUrl, new Response.Listener<String>() {
            // if our response if simply "1", then leds are on, "0" for off
            // update our status boolean
            @Override
            public void onResponse(String response) {
                if (response.toString().equals("1")) {
                    on = true;
                    Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_LONG).show();
                }
                else {
                    on = false;
                    Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error",error.toString());
            }
        });

        queue.add(stringRequest);
    }
}