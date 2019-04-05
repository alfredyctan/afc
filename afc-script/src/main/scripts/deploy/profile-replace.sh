#!/bin/bash

if [ $# -lt 3 ]; then
	echo "usage : $0 <path pattern> <from value> <to value>"
	exit 1;
fi

PAT=$1
V1=$2
V2=$3

find $DIR -type f -path $PAT -print -exec sed -i "s/$V1/$V2/g" {} \;
