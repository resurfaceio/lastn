# resurfaceio-lastn
&copy; 2016-2017 Resurface Labs LLC

## Build System 

Standard Maven for building & testing code, nothing special.

## Running Locally

    heroku local
    browse to http://localhost:5000/messages

## Running at Heroku

    heroku create {appname}
    git push heroku master
    browse to http://{appname}.herokuapp.com/messages

## Sending Messages

    echo '"hello"' | curl -X POST -d @- http://localhost:5000/messages

Using Java API: https://github.com/resurfaceio/logger-java

Using Ruby API: https://github.com/resurfaceio/logger-ruby
