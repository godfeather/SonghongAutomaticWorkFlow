#!/bin/bash
# Unix/Linux解压依赖包到classpath
echo 正在重新解压依赖jar包到classpath
rm -rf out/*
rm -rf jar-temp/*
mkdir jar-temp
cd jar-temp
jar -xf ../mssql-jdbc-7.4.1.jre8.jar com microsoft mssql
jar -xf ../mysql-connector-java-8.0.21.jar com
jar -xf ../selenium-server-standalone-3.141.59.jar com javax net okhttp3 okio org
jar -xf ../gson-2.8.0.jar com
cd ..
