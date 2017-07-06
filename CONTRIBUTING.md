# Contributing to resurfaceio-lastn
&copy; 2016-2017 Resurface Labs LLC

## Coding Conventions

Our code style is whatever IntelliJ IDEA does by default, with the exception of allowing lines up to 130 characters.
If you don't use IDEA, that's ok, but your code may get reformatted.

## Git Workflow

Initial setup:

```
git clone git@github.com:resurfaceio/lastn.git resurfaceio-lastn
cd resurfaceio-lastn
```

Running unit tests:

```
mvn test
```

Committing changes:

```
git add -A
git commit -m "#123 Updated readme"       (123 is the GitHub issue number)
git pull --rebase                         (avoid merge bubbles)
git push origin master
```
