@echo off
dir /s /B *.java > sources.txt
mkdir classes
javac -encoding UTF-8 -d classes @sources.txt
delete sources.txt
jar cvfe asd.jar hu.bme.bitsplease.App -C classes .