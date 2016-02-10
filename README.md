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

    heroku local

## Viewing Messages

http://localhost:5000/messages  (blank if nothing has been sent yet)

## Sending Messages

Using curl:

    echo ‘hello’ | curl -X POST -d @- http://localhost:5000/messages

Using Java API: https://github.com/resurfaceio/resurfaceio-logger-java

Using Ruby API: https://github.com/resurfaceio/resurfaceio-logger-ruby
