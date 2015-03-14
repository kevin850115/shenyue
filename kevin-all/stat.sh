#!/bin/bash

dir=(`find .   -maxdepth 1 -type d | grep 'kevin-'`)
echo "------------------------ Total Summary----------------------------- "
echo "FileType: Java  Xml  Vm"
echo -n "Total:" 
printf "\e[0;31m"
echo -n `find $i -name "*.java" | grep -v 'target' |grep -v 'bin' | grep -v 'static' | xargs cat | wc -l`" "
printf "\e[0m"
echo `find $i -name "*.xml" | grep -v 'target' | grep -v 'bin' | grep -v 'static' | xargs cat | wc -l` `find $i -name "*.vm" | grep -v 'target' | grep -v 'bin' | grep -v 'static' |  xargs cat | wc -l`
for i in ${dir[@]};do
echo $i `find $i -name "*.java" | grep -v 'target' | grep -v 'bin' | grep -v 'static' | xargs cat | wc -l` `find $i -name "*.xml" | grep -v 'target' | grep -v 'bin' | grep -v 'static' | xargs cat | wc -l` `find $i -name "*.vm" |grep -v 'target'| grep -v 'bin' | grep -v 'static' | xargs cat | wc -l`
done
