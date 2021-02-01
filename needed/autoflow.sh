#!/bin/bash
# Linux&Unix 运行脚本
param="${@}"
paramLen="${#}"
remainder=`expr ${paramLen} % 2`
if [ ${remainder} -eq 1 ];then
	param=${param}" added"
fi
java -jar release.jar ${param}