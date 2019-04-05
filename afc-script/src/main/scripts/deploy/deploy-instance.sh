#!/bin/bash
. ../profile/system.env

usage() {
	echo "missing $1"
	echo "Usage: $0 -s <service> -e <environment> -r <region> -c <cluster> -i <instance> -v <version> -b"
	exit 1
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

[ -z $SERVICE ] && usage "service"
[ -z $ENV ] && usage "environment"
[ -z $REGION ] && usage "region"
[ -z $INSTANCE ] && usage "instance"
[ -z $VERSION ] && usage "version"
CLUSTER=${CLUSTER:-"default"}

if [ -d "$SYS_APP/$SERVICE/${REGION}_${CLUSTER}_${INSTANCE}" ]; then
	rm -rf $SYS_APP/$SERVICE/${REGION}_${CLUSTER}_${INSTANCE}
	echo "removed the current installation at $SYS_APP/$SERVICE/${REGION}_${CLUSTER}_${INSTANCE}"
fi
mkdir -p $SYS_APP/$SERVICE/${REGION}_${CLUSTER}_${INSTANCE}

INSTANCE_ENV=$SYS_APP/$SERVICE/${REGION}_${CLUSTER}_${INSTANCE}/instance.env
cp profile/$SERVICE/$ENV/$REGION/$CLUSTER/$INSTANCE.env $SYS_APP/$SERVICE/${REGION}_${CLUSTER}_${INSTANCE}/instance.env
echo "SYS_SERVICE=$SERVICE" >> $INSTANCE_ENV
echo "SYS_REGION=$REGION" >> $INSTANCE_ENV
echo "SYS_CLUSTER=$CLUSTER" >> $INSTANCE_ENV
echo "SYS_INSTANCE=$INSTANCE" >> $INSTANCE_ENV

echo "rebuild the new installation at $SYS_APP/$SERVICE/${REGION}_${CLUSTER}_${INSTANCE}"

. profile/$SERVICE/service.cfg
./get-artifact.sh -r $REPO_URL -g $GROUP_ID -a $ARTIFACT_ID -c server -t tar.gz -v $VERSION -o $SYS_APP/$SERVICE/${REGION}_${CLUSTER}_${INSTANCE}

cd $SYS_APP/$SERVICE/${REGION}_${CLUSTER}_${INSTANCE}
ln -s ../../../script/service/start.sh start.sh
ln -s ../../../script/service/stop.sh stop.sh

mkdir -p $SYS_LOG/$SERVICE $SYS_DATA/$SERVICE/${REGION}/${CLUSTER}/${INSTANCE}
ln -s $SYS_LOG/$SERVICE log
ln -s $SYS_DATA/$SERVICE data

SUFFIX=server.tar.gz
TAR_BALL=`find * -name ${ARTIFACT_ID}-*-${SUFFIX}`
VERSION=${TAR_BALL#${ARTIFACT_ID}-}
VERSION=${VERSION%-${SUFFIX}}

mkdir $VERSION
tar -zxvf $TAR_BALL -C $VERSION
ln -s $VERSION current
rm -f $TAR_BALL

if [ "$BOOT" = "-b" ]; then
	echo "restarting service..."
	 . ../../../script/profile/system.profile $ENV
	./stop.sh
	./start.sh
fi

if [ "$DAEMON" = "-d" ]; then
	echo "installing systemd service..."
	../../../script/deploy/install-systemd.sh $SERVICE $ENV $REGION $CLUSTER $INSTANCE
fi
echo "installation completed"
