#!/bin/bash

if [ "$#" == "1" ]
then
   git add -A
   git commit -m "${1}"
   git push
elif [ "$#" == "2" ]
then
   git add -A
   git commit -m "${1}" -m "${2}"
   git push
fi
