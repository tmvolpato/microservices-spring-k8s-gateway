#!/bin/sh
# git pre-push hook running build & tests using maven and only pushing if tests succeeded
#
# will do a local check-out of your currentl commit state and see if that one builds.
# this will spare you nasty surprises from missing to add files or having broken partially commited files
#

PWD=$(pwd)
ROOT="file://$(git rev-parse --show-toplevel)"
TMP=$(mktemp -d)

cd "$TMP"
git clone "$ROOT" test --depth 1
cd test

mvn clean test
SUCCESS=$?

cd "$PWD"
rm -rf "$TMP"

exit $SUCCESS