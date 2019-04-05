#!/bin/bash

LOG_FOLDER="/opt/sys/log/housekeep"
export LOG_FILE=$LOG_FOLDER/housekeep.log

BASE=`dirname $0`
. "$BASE/../service/include.sh"

if [ ! -d "$LOG_FOLDER" ]; then
	mkdir -p "$LOG_FOLDER"
fi

log "log housekeep starting"
$BASE/housekeep-log.sh >> $LOG_FILE 2>&1
log "log housekeep completed"

log "elastic housekeep starting"
$BASE/housekeep-elastic.sh >> $LOG_FILE 2>&1
log "elastic housekeep completed"
