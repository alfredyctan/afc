#!/bin/sh

export STARTUP_DATE=`date +%Y-%m-%d`
export STARTUP_TIMESTAMP=`date +%Y-%m-%d.%H%M%S`

# strip the arg for no console log
for arg do
  shift
  case $arg in
    (-c) CMD_TOOL="Y" ;;
     (*) set -- "$@" "$arg" ;;
  esac
done

# to source the SYS_* environment locally for standalone service, docker is injected during container creation
if [ -r "./instance.env" ]; then
	. ./instance.env
	export $(cut -d= -f1 ./instance.env)
	export MODE="STANDALONE"
else
	export MODE="DOCKER"
fi
echo "$SYS_ENV $SYS_SERVICE $SYS_CLUSTER $SYS_INSTANCE is starting"

# to obtain the instance from docker swarm mode
if [ -z "$SYS_INSTANCE" ]; then
	eval "export SYS_INSTANCE=${SYS_INSTANCE_PATTERN}"
fi

CMD="current/bin/start-service.sh"
CONSOLE="${SYS_LOG}/${SYS_SERVICE}/${SYS_SERVICE}.${SYS_CLUSTER}.${SYS_INSTANCE}.${STARTUP_TIMESTAMP}.console.log"
mkdir -p ${SYS_LOG}/${SYS_SERVICE}

if [ "$CMD_TOOL" = "Y" ]; then
    $CMD "$@" 2>&1
else
	if [ "$MODE" = "STANDALONE" ]; then
        # for standalone service start in bg
        nohup $CMD "$@" > $CONSOLE 2>&1 &
    else
        # for docker container to hold up the process, container will be stopped if no fg job
        nohup $CMD "$@" > $CONSOLE 2>&1
    fi
fi

echo "$SYS_ENV $SYS_SERVICE $SYS_CLUSTER $SYS_INSTANCE is started"
