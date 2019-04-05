#!/bin/bash

SYS_DEPLOY=`dirname ${BASH_SOURCE[0]}`
. $SYS_DEPLOY/../profile/system.env

usage() {
	echo "missing $1"
	echo "Usage: $0 -e <environment> -v <version> [-s <service>] [-r <region>] [-c <cluster>] [-i <instance>] -b -d"
	exit 1
}

deploy_services() {
	for SERVICE_PATH in `find $SYS_DEPLOY/profile -mindepth 1 -maxdepth 1 -type d`
	do
		if [ -r "$SERVICE_PATH/service.cfg" ]; then
			. $SERVICE_PATH/service.cfg
		fi
		if [ -z "$GROUP_ID" ]; then
			continue;
		fi

		if [ -z "$SERVICE" -o "$SERVICE" = "$(basename $SERVICE_PATH)" ]; then
			_SERVICE=$(basename $SERVICE_PATH)
			deploy_regions $_SERVICE
		fi
		GROUP_ID=""
	done
}

deploy_regions() {
	_SERVICE=$1
	for REGION_PATH in `find $SYS_DEPLOY/profile/$_SERVICE/$ENV -mindepth 1 -maxdepth 1 -type d`
	do
		if [ -z "$REGION" -o "$REGION" = "$(basename $REGION_PATH)" ]; then
			_REGION=$(basename $REGION_PATH)
			deploy_clusters $_SERVICE $_REGION
		fi
	done
}

deploy_clusters() {
	_SERVICE=$1
	_REGION=$2
	for CLUSTER_PATH in `find $SYS_DEPLOY/profile/$_SERVICE/$ENV/$_REGION -mindepth 1 -maxdepth 1 -type d`
	do
		if [ -z "$CLUSTER" -o "$CLUSTER" = "$(basename $CLUSTER_PATH)" ]; then
			_CLUSTER=$(basename $CLUSTER_PATH)
			deploy_instances $_SERVICE $_REGION $_CLUSTER
		fi
	done
}

deploy_instances() {
	_SERVICE=$1
	_REGION=$2
	_CLUSTER=$3
	for INSTANCE_PATH in `find $SYS_DEPLOY/profile/$_SERVICE/$ENV/$_REGION/$_CLUSTER -mindepth 1 -maxdepth 1 -type f`
	do
		_INSTANCE=$(basename $INSTANCE_PATH)
		_INSTANCE=${_INSTANCE%.env}
		if [ -z "$INSTANCE" -o "$INSTANCE" = "$_INSTANCE" ]; then
			. $INSTANCE_PATH
			echo "ssh $SERVICE_USER@$SERVICE_HOST \"cd $SYS_SCRIPT/deploy; ./deploy-instance.sh -s $_SERVICE -e $ENV -r $_REGION -c $_CLUSTER -i $_INSTANCE -v $VERSION $BOOT $DAEMON\""
			ssh $SERVICE_USER@$SERVICE_HOST "cd $SYS_SCRIPT/deploy; ./deploy-instance.sh -s $_SERVICE -e $ENV -r $_REGION -c $_CLUSTER -i $_INSTANCE -v $VERSION $BOOT $DAEMON"
			SERVICE_USER=""
			SERVICE_HOST=""
		fi
	done
}

while getopts "s:e:r:c:i:v:bd" OPT;
do
	case $OPT in
	s)
		SERVICE=${OPTARG}
		;;
	e)
		ENV=${OPTARG}
		;;
	r)
		REGION=${OPTARG}
		;;
	c)
		CLUSTER=${OPTARG}
		;;
	i)
		INSTANCE=${OPTARG}
		;;
	v)
		VERSION=${OPTARG}
		;;
	b)
		BOOT="-b"
		;;
	d)
		DAEMON="-d"
		;;
	*)
		usage
		exit 1
		;;
	esac
done
shift $((OPTIND-1))

[ -z $ENV ] && usage "environment"
[ -z $VERSION ] && usage "version"
{ [ "$VERSION" != "latest" ] && [ "$VERSION" != "release" ]; } && [ -z "$SERVICE" ] && usage "service, it is required for specific version"

deploy_services