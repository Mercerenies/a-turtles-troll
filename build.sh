#!/bin/bash

shopt -s globstar

if [ -n "$1" ]; then
    SPIGOT_PATH=$1
else
    SPIGOT_PATH=~/D/Downloads/spigot/spigot-api-1.18.2-R0.1-SNAPSHOT.jar
fi

JSON_PATH="lib/json-20220320.jar"

kotlinc -cp "$SPIGOT_PATH:$JSON_PATH" src/ -include-runtime -d ATurtlesTroll.jar &&
    jar uvf ATurtlesTroll.jar plugin.yml
