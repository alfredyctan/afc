#!/bin/bash

BASE=`dirname $0`
. "$BASE/../service/include.sh"

ELASTICSEARCH=`find /opt/sys/app -maxdepth 1 -type d -name elasticsearch`
if [ -z "$ELASTICSEARCH" ]; then
	log "elastic search is not installed in current host, skip elastic indices housekeep"
	exit 0;
fi

CURATOR=`which curator`
if [ -z "CURATOR" ]; then
	log "curator is not installed in current host, skip elastic indices housekeep"
	exit 0;
fi

/usr/bin/curator --config /opt/sys/script/housekeep/curator.yml /opt/sys/script/housekeep/housekeep-elastic.yml
