#!/bin/bash

BASE=`dirname $0`
. "$BASE/../service/include.sh"

usage() {
	echo "missing $1"
	echo "Usage: $0 -f <folder> -p <pattern> -t <type> -v <version> -d <day> -c <copy>"
	exit 1
}

purge_parent() { 
	FILE=$1
	PARENT=$(find `dirname $FILE` -prune -empty 2>/dev/null)
	if [ -n "$PARENT" ]; then
		rm -rf $PARENT
		log "$PARENT removed" 
	fi
}


while getopts "f:p:t:v:c:d:" OPT;
do
	case $OPT in
	f)
		FOLDER=${OPTARG}
		;;
	p)
		PATTERN=${OPTARG}
		;;
	t)
		TYPE=${OPTARG}
		;;
	v)
		VERSION=${OPTARG}
		;;
	c)
		COPY=${OPTARG}
		;;
	d)
		DAY=${OPTARG}
		;;
	a)
		ACTION="-b"
		;;
	*)
		usage
		exit 1
		;;
	esac
done
shift $((OPTIND-1))

[ -z "$FOLDER" ] && usage "folder"
[ -z "$PATTERN" ] && usage "pattern"
TYPE=${TYPE:-"f"}
{ [ "$TYPE" != "t" ] && [ "$TYPE" != "f" ]; } && usage "correct type, (d)irectory) / (f)older"

for FOLD in `find $FOLDER -type d | sort -n`
do
	if [ -n "$DAY" ]; then
		for FILE in `find $FOLD -type $TYPE -name "$PATTERN" -mtime +$DAY | sort -n`
		do
			rm -rf $FILE
			log "$FILE removed"
			purge_parent $FILE 
		done
	fi
	
	if [ -n "$COPY" ]; then
		for FILE in `find $FOLD -type $TYPE -name "$PATTERN" | sort -n | head -n -$COPY`
		do
			rm -rf $FILE
			log "$FILE removed"
			purge_parent $FILE 
		done
	fi
	
	if [ -n "$VERSION" ]; then
		for FILE in `find $FOLD -type $TYPE -name "$PATTERN" | sort -V | head -n -$VERSION`
		do
			rm -rf $FILE
			log "$FILE removed"
			purge_parent $FILE 
		done
	fi
done
