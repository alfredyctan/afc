#!/bin/bash

function pid() {
    WHOAMI=`whoami`
    ps -ef | grep $WHOAMI | grep java | grep -v grep | grep $SYS_ENV | grep $SYS_SERVICE | grep $SYS_REGION | grep $SYS_CLUSTER | grep $SYS_INSTANCE | cut -b 10-14
}

PID=`pid`
TRIAL=0
echo "stopping service of `whoami`'s $SYS_ENV.$SYS_SERVICE.$SYS_REGION.$SYS_CLUSTER.$SYS_INSTANCE"
if [ -n "$PID" ]; then
    kill $PID
	echo -n .

    while [ -n "`pid`" ] && [ "$TRIAL" -lt 10 ]; do
        sleep 1;
        TRIAL=$((TRIAL + 1))
        echo -n .
    done
    echo

    if [ -z "`pid`" ]; then
        echo "service [$PID] of `whoami`'s $SYS_ENV.$SYS_SERVICE.$SYS_REGION.$SYS_CLUSTER.$SYS_INSTANCE is stopped"
    else
        echo "service [$PID] of `whoami`'s $SYS_ENV.$SYS_SERVICE.$SYS_REGION.$SYS_CLUSTER.$SYS_INSTANCE is not stopped, will do kill -9"
        kill -9 $PID
    fi
else
    echo "service of [$PID] `whoami`'s $SYS_ENV.$SYS_SERVICE.$SYS_REGION.$SYS_CLUSTER.$SYS_INSTANCE is stopped"
fi
