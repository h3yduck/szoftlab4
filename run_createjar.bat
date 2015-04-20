chcp 65001
dir /s /B *.java > sources.txt
rmdir classes /s /q
mkdir classes
javac -encoding UTF-8 -d classes @sources.txt
del sources.txt
jar cvfe szoftlab4.jar hu.bme.bitsplease.App -C classes .
pause