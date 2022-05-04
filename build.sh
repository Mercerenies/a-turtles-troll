#!/bin/bash

shopt -s globstar

merge_json() {
    mkdir tmp
    (
        cd tmp
        unzip -quo "../$JSON_PATH"
        jar uf ../ATurtlesTroll.jar **/*.class
    )
    rm -r tmp
}

merge_data() {
    jar uf ./ATurtlesTroll.jar data/*.txt
}

if [ -n "$1" ]; then
    SPIGOT_PATH=$1
else
    SPIGOT_PATH=~/D/Downloads/spigot/spigot-api-1.18.2-R0.1-SNAPSHOT.jar
fi

JSON_PATH="lib/json-20220320.jar"

kotlinc -cp "$SPIGOT_PATH:$JSON_PATH" src/ -include-runtime -d ATurtlesTroll.jar &&
    jar uf ATurtlesTroll.jar plugin.yml README.md LICENSE.txt &&
    jar ufm ATurtlesTroll.jar Manifest.txt
    merge_json &&
    merge_data &&
    echo "Done."
