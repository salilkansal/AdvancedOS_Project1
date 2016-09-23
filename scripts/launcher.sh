#!/bin/bash


# Change this to your netid
netid=sxk150430

#
# This assumes your config file is named "config.txt"
# and is located in your project directory
#
CONFIG=configuration/configuration.txt

#
# Directory your java classes are in
#
BINDIR=AOSProject/bin

#
# Your main project class
#
PROG=Project


cat ${CONFIG} | sed -e "s/#.*//" | sed -e "/^\s*$/d" |
(
    read i
    END=${i}
	for ((i=0;i<END;i++));
	do
    	read line
    	host=$( echo $line | awk '{ print $2 }' )
        port=$( echo $line | awk '{ print $3 }' )
        ssh $netid@$host java $BINDIR/$PROG $i $port &
	done
   
)


