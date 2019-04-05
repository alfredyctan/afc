#!/bin/bash
SYS_DEPLOY=`dirname ${BASH_SOURCE[0]}`

. $SYS_DEPLOY/include.sh
. $SYS_DEPLOY/../profile/system.env

usage()	{
	echo "missing $1"
	echo "Usage: $0 -s \"<service>\" -e \"<environment>\" -r \"<region>\" -v \"<version>\" [-c \"<cluster>\"] [-n] [docker args...]"
	exit 1
}

while getopts "v:s:e:c:i:r:n" OPT;
do
	case $OPT in
	s)
		SERVICE=${OPTARG}
		;;
	e)
		ENVIRONMENT=${OPTARG}
		;;
	r)
		REGION=${OPTARG}
		;;
	v)
		VERSION=${OPTARG}
		;;
	c)
		CLUSTER=${OPTARG}
		;;
	n)
		IGNORE_SYSTEM_MOUNT="Y"
		;;
	*)
		usage
		exit 1
		;;
	esac
done
shift $((OPTIND-1))

[ -z $SERVICE ] && usage "service"
[ -z $ENVIRONMENT ] && usage "environment"
[ -z $REGION ] && usage "region"
[ -z $VERSION ] && usage "version"
CLUSTER=${CLUSTER:-"default"}
if [ "$IGNORE_SYSTEM_MOUNT" = "Y" ]; then
	SYSTEM_MOUNT=""
else
	SYSTEM_MOUNT="volume/system.mount"
fi
echo "deploying $SERVICE $ENVIRONMENT $REGION $VERSION $CLUSTER" 

RUN_BASEDIR=$SYS_DEPLOY
SERVICE_BASEDIR=profile/$SERVICE
source_param $RUN_BASEDIR "$SERVICE_BASEDIR/service.cfg"


if [ -n "$DOCKER_REGISTRY" ]; then
	VERSION=`$SYS_DEPLOY/docker-version.sh -r $DOCKER_REGISTRY -n $NAMESPACE -a $ARTIFACT -v $VERSION`
	echo "resolved version : $VERSION" 
	DOCKER_IMAGE=$DOCKER_REGISTRY/$NAMESPACE/$ARTIFACT:$VERSION
	DOCKER_LOGIN="$SUDO docker login $DOCKER_REGISTRY --username buildagent --password buildagent;"
else 
	if [ -n "$NAMESPACE" ]; then
		DOCKER_IMAGE=$NAMESPACE/$ARTIFACT:$VERSION
	else
		DOCKER_IMAGE=$ARTIFACT:$VERSION
	fi
fi
DOCKER_PULL="$SUDO docker pull $DOCKER_IMAGE;"

ENV_DIR=$SERVICE_BASEDIR/$ENVIRONMENT
CLUSTER_DIR=$ENV_DIR/$REGION/$CLUSTER

SERVICE_NAME=${SERVICE}-${CLUSTER}

build_param $RUN_BASEDIR --host HOST "$ENV_DIR/host $CLUSTER_DIR/host" "$CLUSTER_DIR/host.exclude"
build_param $RUN_BASEDIR --network NETWORK "$ENV_DIR/network $CLUSTER_DIR/network" "$CLUSTER_DIR/network.exclude"
build_param $RUN_BASEDIR --mount MOUNT "$SYSTEM_MOUNT $CLUSTER_DIR/$CLUSTER.mount" "$CLUSTER_DIR/$CLUSTER.mount.exclude"
build_param $RUN_BASEDIR -p PORT "$CLUSTER_DIR/$CLUSTER.port"
build_param $RUN_BASEDIR -e ENV "env/system.env $CLUSTER_DIR/$CLUSTER.env" "$CLUSTER_DIR/$CLUSTER.env.exclude"
build_option $RUN_BASEDIR OPTIONS "$SERVICE_BASEDIR/service.options $ENV_DIR/$ENVIRONMENT.options $CLUSTER_DIR/$CLUSTER.options" "$CLUSTER_DIR/$CLUSTER.options.exclude"
build_option $RUN_BASEDIR ARGS "$CLUSTER_DIR/$CLUSTER.args"
source_param $RUN_BASEDIR "$CLUSTER_DIR/$CLUSTER.env"
ENV="$ENV -e SYS_ENV=${ENVIRONMENT} -e SYS_SERVICE=${SERVICE} -e SYS_REGION=${REGION} -e SYS_CLUSTER=${CLUSTER} -e TASK_SLOT={{.Task.Slot}}"


DOCKER_SERVICE_LS="$SUDO docker service ls -f name=${SERVICE_NAME} -q;"
echo "ssh $DOCKER_USER@$DOCKER_HOST $DOCKER_SERVICE_LS"
SERVICE_LS=`ssh $DOCKER_USER@$DOCKER_HOST $DOCKER_SERVICE_LS`
if [ -n "$SERVICE_LS" ]; then
	DOCKER_SERVICE_RM="$SUDO docker service rm ${SERVICE_NAME}"
	echo "ssh $DOCKER_USER@$DOCKER_HOST \"$DOCKER_SERVICE_RM\""
	ssh $DOCKER_USER@$DOCKER_HOST "$DOCKER_SERVICE_RM"
fi
 
DOCKER_SERVICE="$SUDO docker service create -d --with-registry-auth --name ${SERVICE_NAME} $NETWORK $HOST $OPTIONS $MOUNT $PORT $ENV $* $DOCKER_IMAGE $ARGS;"
echo "ssh $DOCKER_USER@$DOCKER_HOST \"$DOCKER_LOGIN $DOCKER_PULL $DOCKER_SERVICE\""
ssh $DOCKER_USER@$DOCKER_HOST "$DOCKER_LOGIN $DOCKER_PULL $DOCKER_SERVICE"
EXIT_CODE=$?
echo "exit code $EXIT_CODE"
exit $EXIT_CODE

# clean the dangling image through jenkins instead of every deployment to avoid error
#DOCKER_DANGLING="$SUDO docker images -f dangling=true -q"
#echo "ssh $DOCKER_USER@$DOCKER_HOST $DOCKER_DANGLING"
#DANGLING=`ssh $DOCKER_USER@$DOCKER_HOST $DOCKER_DANGLING`
#if [ -n "$DANGLING" ]; then
#	DANGLING=`echo $DANGLING`
#	DOCKER_CLEAN="$SUDO docker rmi $DANGLING"
#	echo "ssh $DOCKER_USER@$DOCKER_HOST \"$DOCKER_CLEAN\""
#	ssh $DOCKER_USER@$DOCKER_HOST "$DOCKER_CLEAN"
#fi
