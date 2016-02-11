# resurfaceio-lastn
&copy; 2016 Resurface Labs LLC, All Rights Reserved

## Basic Workflow 

    git clone git@github.com:resurfaceio/resurfaceio-lastn.git ~/resurfaceio-lastn
    cd ~/resurfaceio-lastn
    git pull
    (make changes)
    git status                                (review changes)
    git add -A
    git commit -m "#123 Updated readme"       (123 is the GitHub issue number)
    git pull
    git push origin master

## Running Locally

    cd ~/resurfaceio-lastn
    heroku local
    browse to http://localhost:5000/messages

## Running at Heroku

    cd ~/resurfaceio-lastn
    heroku create {appname}
    git push heroku master
    browse to http://{appname}.herokuapp.com/messages

## Sending Messages

Using curl:

    echo 'hello' | curl -X POST -d @- http://localhost:5000/messages

Using Java API: https://github.com/resurfaceio/resurfaceio-logger-java

Using Ruby API: https://github.com/resurfaceio/resurfaceio-logger-ruby
