#!/bin/sh
# Maven Wrapper Script
MAVEN_WRAPPER_JAR="./.mvn/wrapper/maven-wrapper.jar"
MAVEN_WRAPPER_DOWNLOAD_URL="https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar"

if [ ! -f "$MAVEN_WRAPPER_JAR" ]; then
    echo "Maven wrapper not found, downloading..."
    mkdir -p .mvn/wrapper
    curl -o "$MAVEN_WRAPPER_JAR" "$MAVEN_WRAPPER_DOWNLOAD_URL"
fi

exec java -classpath "$MAVEN_WRAPPER_JAR" "-Dmaven.multiModuleProjectDirectory=$PWD" org.apache.maven.wrapper.MavenWrapperMain "$@"
