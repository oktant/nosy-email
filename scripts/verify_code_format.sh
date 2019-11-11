#!/bin/bash

set -ev

JAR_NAME=google-java-format-1.7-all-deps.jar

if [ ! -f "$JAR_NAME" ]; then
    wget -q https://github.com/google/google-java-format/releases/download/google-java-format-1.7/${JAR_NAME}
    chmod 755 ${JAR_NAME}
fi

files=$(git diff --name-only HEAD $(git merge-base HEAD $TRAVIS_BRANCH) | grep -Ei "\.java$")
if [ ! -z "${files}" ]; then
    comma_files=$(echo "$files" | paste -s -d "," -)
    java -jar ${JAR_NAME} --set-exit-if-changed ${comma_files}
fi


