#!/bin/bash
set -e

RAW_TASKS=$1

CLEAN_TASKS="${RAW_TASKS//:engine:/}"
TASKS=$(printf ':engine:%s ' $CLEAN_TASKS)

[[ "$CLEAN_TASKS" == *"test"* ]] && TASKS=":engine:cleanTest $TASKS"

OPTS="--build-cache"
[[ "$CLEAN_TASKS" != *"publish"* ]] && OPTS="$OPTS --configuration-cache"

echo "Executing: ./gradlew $TASKS $OPTS"
./gradlew $TASKS $OPTS
