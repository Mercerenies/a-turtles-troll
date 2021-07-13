#!/bin/bash

shopt -s globstar

kotlinc -cp ~/D/Downloads/spigot/spigot-1.17.1.jar src/ -include-runtime -d ATurtlesTroll.jar &&
    jar uvf ATurtlesTroll.jar plugin.yml
