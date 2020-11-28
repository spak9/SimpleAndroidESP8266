# Simple Android Application Using ESP8266

## LED Interface

The following will detail some decisions for how the
LED interface will work
-   For the state of the lightbulb, whether it be through
    finish() or whether the application actually closes, 
    we need to know what state the lightbulb will be in
    when the user starts the app (e.g user turns on the
    lightbulb, then closes the app. Lightbulb is physically
    on, therefore LED interface state must reflect that
