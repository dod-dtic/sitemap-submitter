#!/usr/bin/bash

echo Building sitemap-submitter
mvn clean package $1 -f "./pom.xml"
echo
echo

cp ./target/sitemap-submitter-0.0.1-SNAPSHOT.war ./target/sitemap-submitter.war
