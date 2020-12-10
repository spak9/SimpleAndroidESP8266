# Simple Android Application Using ESP8266


## General Interface

The general interface will be split into 2: 
- The LED interface 
- The DHT11 (temperature & humiditity) interface 

## LED Interface

The following will detail some decisions for how the
LED interface will work
-   For the state of the lightbulb, whether it be through
    finish() or whether the application actually closes, 
    we need to know what state the lightbulb will be in
    when the user starts the app. (e.g user turns on the
    lightbulb, then closes the app. Lightbulb is physically
    on, therefore LED interface state must reflect that)
    Therefore, one of the ways to do that is through 
    doing a small request on "StartUp" of the interface,
    that is, click on the `houseInterface` button. We synchronize 
    this by sending a small HTTP request right when `MainActivity`
    goes through `onResume()` getting the status of the LEDs.
    
## Temperature Interface

-   This interface was quite simple. Similar to LEDs, the Android 
    device will send a small HTTP GET request to the ESP8266, respond
    with the temperature in fahrenheit as a string, do what we will on the
    Android side with it.
- 12/7: Sometimes, there seems to be some integer overflow 
