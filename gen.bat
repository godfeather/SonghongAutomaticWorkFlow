REM 解压依赖包到工作空间
del /Q out
mkdir jar-temp
jar -xvf ../mssql-jdbc-7.4.1.jre8.jar com microsoft mssql
jar -xvf ../mysql-connector-java-8.0.21.jar com
jar -xvf ../selenium-server-standalone-3.141.59.jar com javax net okhttp3 okio org
jar -xvf ../gson-2.8.0.jar com
