@echo off
chcp 65001
dir /s /B *.java > sources.txt
mkdir classes
javac -encoding UTF-8 -d classes @sources.txt
del sources.txt
java -cp classes hu.bme.bitsplease.App
pause