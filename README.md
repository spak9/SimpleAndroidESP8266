# Simple Android Application Using ESP8266

## Work Flow
The general work flow is just always to checkout a new branch and work on that 
until it's merged with master. 
That is, checkout, work on branch, commit, push, and finally pull request when all done.

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
