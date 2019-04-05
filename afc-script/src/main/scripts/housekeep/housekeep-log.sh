#!/bin/bash

BASE=`dirname $0`

$BASE/purge.sh -f "/opt/sys/log/*" -p "*.log.*"       -c 20 -d 10
$BASE/purge.sh -f "/opt/sys/log/*" -p "*.access.log"  -c 20 -d 10
$BASE/purge.sh -f "/opt/sys/log/*" -p "*.console.log" -c 10 -d 10
$BASE/purge.sh -f "/opt/sys/log/*" -p "*.gc.log"      -c 10 -d 10
