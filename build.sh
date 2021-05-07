#!/bin/bash

shopt -s globstar

kotlinc -cp ~/Downloads/spigotbuild/spigot-1.16.5.jar src/ -include-runtime -d ATurtlesTroll.jar &&
    jar uvf ATurtlesTroll.jar plugin.yml
