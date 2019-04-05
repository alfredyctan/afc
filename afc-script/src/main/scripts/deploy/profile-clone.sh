#!/bin/bash

if [ $# -lt 2 ]; then
	echo "usage : $0 <from env> <to env> [profile]"
	exit 1;
fi

ENV1=$1
ENV2=$2
PROF=$3

for ENV in `find profile/$PROF -type d -name $ENV1`
do
	TAR="`dirname $ENV`/$ENV2"
	cp -R $ENV $TAR
	find $TAR -type f -exec sed -i "s/$ENV1/$ENV2/g" {} \;
	echo "cloned $ENV to $TAR"
done
