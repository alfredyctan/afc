#!/bin/bash
if [ -r "./instance.env" ]; then
	. ./instance.env
	export $(cut -d= -f1 ./instance.env)
fi

current/bin/stop-service.sh
