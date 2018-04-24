# resurfaceio-lastn
&copy; 2016-2018 Resurface Labs LLC

Heroku test app that tracks the last N messages received.

## Build System 

Standard Maven for building & testing code, nothing special.

## Running Locally

    mvn install
    heroku local
    browse to http://localhost:5000/messages

## Running at Heroku

    heroku create {appname}
    git push heroku master
    browse to http://{appname}.herokuapp.com/messages

## Sending Messages

    echo '"hello"' | curl -X POST -d @- http://localhost:5000/messages
