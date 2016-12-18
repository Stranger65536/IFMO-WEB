#!/bin/bash
mvn install:install-file -Dfile=../lib/ojdbc7.jar -DgroupId=com.oracle -DartifactId=jdbc-connector-java -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true