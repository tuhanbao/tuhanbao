#!/bin/sh

kill -9 $(netstat -tlnp|grep 9131|awk '{print $7}'|awk -F '/' '{print $1}')
kill -9 $(netstat -tlnp|grep 9132|awk '{print $7}'|awk -F '/' '{print $1}')
sleep 2

../jre/bin/java -server -Xms256m -Xmx256m -Xmn128M -Xdebug -Xrunjdwp:transport=dt_socket,address=8887,server=y,suspend=n -cp lib/log4j-1.2.16.jar:lib/bsh-2.0b4.jar:lib/js.jar:lib/mina-core-2.0.4.jar:lib/slf4j-api-1.6.4.jar:lib/slf4j-log4j12-1.6.4.jar:lib/mysql-5.1.6.jar:lib/jedis-2.1.0.jar:lib/bcprov-jdk16-145-1.jar:lib/javapns-jdk16-163.jar:lib/commons-io-2.0.1.jar:lib/commons-lang-2.5.jar:lib/dom4j-1.6.1.jar:lib/gamecenter.jar GCTest