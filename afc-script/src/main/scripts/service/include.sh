log() {
	LOG_TIMESTAMP=`date "+%Y-%m-%d %H:%M:%S"`
	if [ -n "$LOG_FILE" ]; then
		echo "$LOG_TIMESTAMP [INFO] : $1" >> $LOG_FILE 2>&1
	else
		echo "$LOG_TIMESTAMP [INFO] : $1" 2>&1
	fi
}

config() {
    DIR=$1
    NAME=$2
    CFG_ENV=$DIR/env/${SYS_ENV}

    if [ -f "$CFG_ENV/${SYS_CLUSTER}/${SYS_INSTANCE}/$NAME" ]; then
	echo "$CFG_ENV/${SYS_CLUSTER}/${SYS_INSTANCE}/$NAME" 
    	exit;
    fi

    if [ -f "$CFG_ENV/${SYS_INSTANCE}/$NAME" ]; then
	echo "$CFG_ENV/${SYS_INSTANCE}/$NAME" 
    	exit;
    fi

    if [ -f "$CFG_ENV/${SYS_CLUSTER}/$NAME" ]; then
	echo "$CFG_ENV/${SYS_CLUSTER}/$NAME" 
    	exit;
    fi

    if [ -f "$CFG_ENV/$NAME" ]; then
	echo "$CFG_ENV/$NAME"
    	exit;
    fi

    if [ -f "$DIR/$NAME" ]; then
	echo "$DIR/$NAME" 
    	exit;
    fi
}

resolve() {
	SRC=$1; shift 1
	VAR=$*

	for FILE in `find $SRC -type f ! -name "*.bak"`
	do
		TAR=`mktemp -u $FILE.XXXXXX`; cp $FILE $TAR
		TAR2=`mktemp -u $TAR.XXXXXX`
		for VAR_NAME in $VAR
		do
			VAR_VALUE="echo $`echo $VAR_NAME`"
			VAR_VALUE=`eval $VAR_VALUE`
			VAR_VALUE=`echo $VAR_VALUE | sed "s/\\\//\\\\\\\\\//g"`

			sed "s/\\\${$VAR_NAME}/$VAR_VALUE/g" $TAR > $TAR2
			mv $TAR2 $TAR
		done
		if [ -n "`diff $FILE $TAR`" ];  then
			mv $FILE $FILE.bak
			mv $TAR $FILE
			chmod 640 $FILE
		else
			rm -f $TAR
		fi
	done
}
