#!/bin/sh
# Maven Wrapper Script
if [ -z "$MAVEN_HOME" ]; then
  MAVEN_HOME="$HOME/.m2/wrapper/dists/apache-maven-3.9.6"
fi
exec "$MAVEN_HOME/bin/mvn" "$@"
