#!/bin/sh

set -e

JAR_NAME=google-java-format-1.7-all-deps.jar

# Download the google-java-formater
if [ ! -f "$JAR_NAME" ]; then
    wget -q https://github.com/google/google-java-format/releases/download/google-java-format-1.7/${JAR_NAME}
    chmod 755 ${JAR_NAME}
fi

REPO_ROOT_DIR="$(git rev-parse --show-toplevel)"

files=$(git diff --cached --name-only --diff-filter=ACMR | grep -Ei "\.java$")
if [ ! -z "${files}" ]; then
    comma_files=$(echo "$files" | paste -s -d "," -)
    java -jar ${JAR_NAME} -i ${comma_files}
    git add $(echo "$files" | paste -s -d " " -)
fi

