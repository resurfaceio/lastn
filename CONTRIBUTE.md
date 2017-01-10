# resurfaceio-lastn
&copy; 2016-2017 Resurface Labs LLC

## Coding Conventions

Our code style is whatever IntelliJ IDEA does by default, with the exception of allowing lines up to 130 characters.
If you don't use IDEA, that's ok, but your code may get reformatted.

## Git Workflow

    git clone git@github.com:resurfaceio/resurfaceio-lastn.git ~/resurfaceio-lastn
    cd ~/resurfaceio-lastn
    git pull
    (make changes)
    git status                                (review changes)
    git add -A
    git commit -m "#123 Updated readme"       (123 is the GitHub issue number)
    git pull
    git push origin master
