REM Windows �Զ��������ű�
@echo off
echo ����������Ŀ��ִ��jar������
javac -cp src;mssql-jdbc-7.4.1.jre8.jar;gson-2.8.0.jar;mysql-connector-java-8.0.21.jar;selenium-server-standalone-3.141.59.jar src/RoutineCalculate/Main.java -d jar-temp -encoding utf8
cd jar-temp
jar -cvmf ../META-INF/MANIFEST.MF ../out/release.jar ./*
cd ..