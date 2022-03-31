#!/bin/bash

shopt -s globstar

kotlinc -cp ~/D/Downloads/spigot/spigot-api-1.18.2-R0.1-SNAPSHOT.jar src/ -include-runtime -d ATurtlesTroll.jar &&
    jar uvf ATurtlesTroll.jar plugin.yml
