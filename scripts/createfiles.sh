#!/bin/bash

NUMPEERS=$1
NUMFILES=$2

for (( i=0; i<$NUMPEERS; i++ )); do
    mkdir peer$i
    for ((j=0; j<$NUMFILES; j++)); do
        base64 /dev/urandom | head -c $((RANDOM%20000+1000)) > peer$i/file-p$i-0$j
    done
done
