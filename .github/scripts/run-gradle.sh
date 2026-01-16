#!/bin/bash
set -e

RAW_TASKS=$1
PUBLISH=$2
VERSION=$3

CLEAN_TASKS="${RAW_TASKS//:engine:/}"
TASKS=$(printf ':engine:%s ' $CLEAN_TASKS)

[[ "$CLEAN_TASKS" == *"test"* ]] && TASKS=":engine:cleanTest $TASKS"

VERSION_ARG=""
[[ "$PUBLISH" == "true" ]] && VERSION_ARG="-PversionName=${VERSION}"

echo "Executing: ./gradlew $TASKS $VERSION_ARG --build-cache --configuration-cache"
./gradlew $TASKS $VERSION_ARG --build-cache --configuration-cache
