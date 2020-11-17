#!/bin/bash
# Unix&Linux自动编译打包脚本
rm -rf ./jar-temp/*
cd ./jar-temp
jar -xvf ../mssql-jdbc-7.4.1.jre8.jar com microsoft mssql
jar -xvf ../mysql-connector-java-8.0.21.jar com
jar -xvf ../selenium-server-standalone-3.141.59.jar com javax net okhttp3 okio org
javac -cp ../src:../mssql-jdbc-7.4.1.jre8.jar:../mysql-connector-java-8.0.21.jar:../selenium-server-standalone-3.141.59.jar ../src/RoutineCalculate/Main.java -d ./ -encoding utf8
jar -cvmf ../META-INF/MANIFEST.MF ../out/release.jar ./
cd ..
rm -rf ./jar-temp