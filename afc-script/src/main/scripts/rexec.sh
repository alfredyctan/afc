#!/bin/sh

if [ $# -lt 2 ]; then
	echo "usage : $0 <\"host1 host2 host3 ...\"> <escaped remote cmd>"
	exit 0;
fi

HOSTS=$1
shift 1

for HOST in $HOSTS
do
    ssh `whoami`@$HOST "$@"
done
