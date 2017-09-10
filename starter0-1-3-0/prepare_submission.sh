#!/bin/bash
cwd=$(pwd)
rm -r /tmp/PacmanStarter
mkdir /tmp/PacmanStarter

cp -r src/ /tmp/PacmanStarter
cp pom.xml /tmp/PacmanStarter

cd /tmp/PacmanStarter
jar -cMf submission.zip *
cp submission.zip $cwd