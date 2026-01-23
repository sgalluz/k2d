#!/bin/bash
set -e

RAW_TASKS=$1
VERSION=$2

CLEAN_TASKS="${RAW_TASKS//:engine:/}"
TASKS=$(printf ':engine:%s ' $CLEAN_TASKS)

[[ "$CLEAN_TASKS" == *"test"* ]] && TASKS=":engine:cleanTest $TASKS"

if [[ -n "$VERSION" ]]; then
  export VERSION
  echo "Using VERSION=$VERSION"
fi

echo "Executing: ./gradlew $TASKS --build-cache --configuration-cache"
./gradlew $TASKS --build-cache --configuration-cache
