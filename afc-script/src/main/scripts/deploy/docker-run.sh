#!/bin/bash
SYS_DEPLOY=`dirname ${BASH_SOURCE[0]}`

. $SYS_DEPLOY/include.sh
. $SYS_DEPLOY/../profile/system.env

usage()	{
	echo "missing $1"
	echo "Usage: $0 -s \"<service>\" -e \"<environment>\" -r \"<region>\" -v \"<version>\" [-c \"<cluster>\"] [-i \"<instance>\"] [-n] [docker args...]"
	exit 1
}

while getopts "v:s:e:c:i:r:u" OPT;
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
	i)
		INSTANCE=${OPTARG}
		;;
	n)
		IGNORE_SYSTEM_VOLUME="Y"
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
if [ "$IGNORE_SYSTEM_VOLUME" = "Y" ]; then
	SYSTEM_VOLUME=""
else
	SYSTEM_VOLUME="volume/system.vol"
fi


echo "deploying $SERVICE $ENVIRONMENT $REGION $VERSION $CLUSTER $INSTANCE" 

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

build_param $RUN_BASEDIR --add-host ADD_HOST "$ENV_DIR/add-host $CLUSTER_DIR/host" "$CLUSTER_DIR/host.exclude"
build_param $RUN_BASEDIR --network NETWORK "$ENV_DIR/network $CLUSTER_DIR/network" "$CLUSTER_DIR/network.exclude"

INSTANCES=${INSTANCE:-`find $RUN_BASEDIR/$CLUSTER_DIR -name *.env | sed -E "s/^.*\/(.*)\..*$/\1/g"`}
for INSTANCE in $INSTANCES
do
	CONTAINER_NAME=${SERVICE}-${CLUSTER}-${INSTANCE}

	build_param $RUN_BASEDIR -v VOLUME "$SYSTEM_VOLUME $CLUSTER_DIR/$INSTANCE.vol" "$CLUSTER_DIR/$INSTANCE.vol.exclude"
	build_param $RUN_BASEDIR -p PORT "$CLUSTER_DIR/$INSTANCE.port"
	build_param $RUN_BASEDIR -e ENV "env/system.env $CLUSTER_DIR/$INSTANCE.env" "$CLUSTER_DIR/$INSTANCE.env.exclude"
	build_option $RUN_BASEDIR OPTIONS "$SERVICE_BASEDIR/service.options $CLUSTER_DIR/$INSTANCE.options" "$CLUSTER_DIR/$INSTANCE.options.exclude"
	build_option $RUN_BASEDIR ARGS "$CLUSTER_DIR/$INSTANCE.args"
	source_param $RUN_BASEDIR "$CLUSTER_DIR/$INSTANCE.env"
	ENV="$ENV -e SYS_ENV=${ENVIRONMENT} -e SYS_SERVICE=${SERVICE} -e SYS_REGION=${REGION} -e SYS_CLUSTER=${CLUSTER} -e SYS_INSTANCE=${INSTANCE}"

	DOCKER_CONTAINER_LS="$SUDO docker container ls -a -f name=${CONTAINER_NAME} -q;"
	echo "ssh $DOCKER_USER@$DOCKER_HOST $DOCKER_CONTAINER_LS"
	CONTAINER_LS=`ssh $DOCKER_USER@$DOCKER_HOST $DOCKER_CONTAINER_LS`
	if [ -n "$CONTAINER_LS" ]; then
		CONTAINER_LS=`echo $CONTAINER_LS`
		DOCKER_CONTAINER_STOP="$SUDO docker container stop ${CONTAINER_LS};"
		DOCKER_CONTAINER_RM="$SUDO docker container rm ${CONTAINER_LS};"
		echo "ssh $DOCKER_USER@$DOCKER_HOST \"$DOCKER_CONTAINER_STOP $DOCKER_CONTAINER_RM\""
		ssh $DOCKER_USER@$DOCKER_HOST "$DOCKER_CONTAINER_STOP $DOCKER_CONTAINER_RM"
	fi


	DOCKER_RUN="$SUDO docker run -d $ADD_HOST $NETWORK --name ${CONTAINER_NAME} $OPTIONS $VOLUME $PORT $ENV $* $DOCKER_IMAGE $ARGS;"
	echo "ssh $DOCKER_USER@$DOCKER_HOST \"$DOCKER_LOGIN $DOCKER_PULL $DOCKER_RUN\""
	ssh $DOCKER_USER@$DOCKER_HOST "$DOCKER_LOGIN $DOCKER_PULL $DOCKER_RUN"
	if [ $? -ne 0 ]; then
		EXIT_CODE=$?
		echo "exit code $EXIT_CODE"
		exit $EXIT_CODE
	fi
# clean the dangling image through jenkins instead of every deployment to avoid error
#	DOCKER_DANGLING="$SUDO docker images -f dangling=true -q"
#	echo "ssh $DOCKER_USER@$DOCKER_HOST $DOCKER_DANGLING"
#	DANGLING=`ssh $DOCKER_USER@$DOCKER_HOST $DOCKER_DANGLING`
#	if [ -n "$DANGLING" ]; then
#		DANGLING=`echo $DANGLING`
#		DOCKER_CLEAN="$SUDO docker rmi $DANGLING"
#		echo "ssh $DOCKER_USER@$DOCKER_HOST \"$DOCKER_CLEAN\""
#		ssh $DOCKER_USER@$DOCKER_HOST "$DOCKER_CLEAN"
#	fi
done
EXIT_CODE=$?
echo "exit code $EXIT_CODE"
exit $EXIT_CODE
