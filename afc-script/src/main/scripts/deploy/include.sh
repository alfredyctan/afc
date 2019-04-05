#!/bin/bash

exit_on_error() {
	if [ "$1" -ne "0" ] ; then
		echo "$2"
		exit $1
	fi
}

build_param() {
	BASEDIR=$1; OPTION_NAME=$2; PARAM_NAME=$3; FILES=$4; EXCLUDE=$5 PARAM=;
	for FILE in $FILES
	do
		if [ ! -r "$BASEDIR/$FILE" ]; then
			continue;
		fi
		
		dos2unix -q $BASEDIR/$FILE
		for CFG in `cat $BASEDIR/$FILE`
		do
			echo "CFG [$CFG]"
			if [ -r "$EXCLUDE" ]; then
				if [ -n "`grep $CFG $EXCLUDE`" ]; then
					echo "CFG [$CFG] is excluded from $EXCLUDE"
					continue;
				fi
			fi
			PARAM="$PARAM $OPTION_NAME $CFG"
		done
	done
	eval "$PARAM_NAME='$PARAM'"
}

source_param() {
	BASEDIR=$1; FILES=$2;
	for FILE in $FILES
	do
		if [ ! -r "$BASEDIR/$FILE" ]; then
			continue;
		fi
		dos2unix -q $BASEDIR/$FILE
		. "$BASEDIR/$FILE"
	done
}

build_option() {
	BASEDIR=$1; PARAM_NAME=$2; FILES=$3; EXCLUDE=$4 PARAM=;
	for FILE in $FILES
	do
		if [ ! -r "$BASEDIR/$FILE" ]; then
			continue;
		fi
		dos2unix -q $BASEDIR/$FILE

		[[ $(tail -c1 $BASEDIR/$FILE) && -f $BASEDIR/$FILE ]] && echo '' >> $BASEDIR/$FILE
		while read -r OPTION; do
			echo "OPTION [$OPTION]"
			if [ -r "$EXCLUDE" ]; then
				EXCLUDED=`grep -e "$OPTION" $EXCLUDE`
				if [ -n "$EXCLUDED" ]; then
					echo "OPTION [$OPTION] is excluded from $EXCLUDE"
					continue;
				fi
			fi
			PARAM="$PARAM $OPTION"
		done < $BASEDIR/$FILE

	done
	eval "$PARAM_NAME=\"$PARAM\""
}
