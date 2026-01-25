#!/bin/bash
set -e

RAW_TASKS=$1

CLEAN_TASKS="${RAW_TASKS//:engine:/}"
TASKS=$(printf ':engine:%s ' $CLEAN_TASKS)

[[ "$CLEAN_TASKS" == *"test"* ]] && TASKS=":engine:cleanTest $TASKS"

echo "Executing: ./gradlew $TASKS --build-cache --configuration-cache"
./gradlew $TASKS --build-cache --configuration-cache
