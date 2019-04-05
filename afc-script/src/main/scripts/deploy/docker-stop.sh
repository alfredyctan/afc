#!/bin/sh
. ./include.sh
. ../profile/system.env

usage()	{
	echo "missing $1"
	echo "Usage: $0 -s \"<service>\" -e \"<env>\" [-c \"<cluster>\"] [-i \"<instance>\"]"
	exit 1
}

while getopts "v:s:e:c:i:" OPT;
do
	case $OPT in
	s)
		SERVICE=${OPTARG}
		;;
	e)
		ENV=${OPTARG}
		;;
	c)
		CLUSTER=${OPTARG}
		;;
	i)
		INSTANCE=${OPTARG}
		;;
	*)
		usage
		exit 1
		;;
	esac
done
shift $((OPTIND-1))

[ -z $SERVICE ] && usage "service"
[ -z $ENV ] && usage "env"
CLUSTER=${CLUSTER:-"default"}

echo "stopping $SERVICE $ENV $CLUSTER $INSTANCE" 

SYS_ENV=$ENV
SYS_SERVICE=$SERVICE
SYS_CLUSTER=$CLUSTER

RUN_BASEDIR=.
SERVICE_BASEDIR=profile/$SERVICE
ENV_DIR=$SERVICE_BASEDIR/$ENV
CLUSTER_DIR=$ENV_DIR/$CLUSTER

INSTANCES=${INSTANCE:-`find $RUN_BASEDIR/$CLUSTER_DIR -name *.env | sed -E "s/^.*\/(.*)\..*$/\1/g"`}
for INSTANCE in $INSTANCES
do
	SYS_INSTANCE=$INSTANCE
	CONTAINER_NAME=${SYS_ENV}-${SYS_SERVICE}-${SYS_CLUSTER}-${SYS_INSTANCE}

	DOCKER_STOP="$SUDO docker stop \$($SUDO docker ps -a -q --filter=\"name=${CONTAINER_NAME}\")"
	DOCKER_RM="$SUDO docker container rm \$($SUDO docker ps -a -q --filter=\"name=${CONTAINER_NAME}\")"
	echo "ssh $DOCKER_USER@$DOCKER_HOST \"$DOCKER_STOP; $DOCKER_RM;\""
	#ssh $DOCKER_USER@$DOCKER_HOST "$DOCKER_STOP; $DOCKER_RM;"
done
