#!/bin/sh

exit_on_error() {
	if [ "$1" -ne "0" ] ; then
		echo "$2"
		exit $1
	fi
}

usage()	{
	echo "missing $1"
	echo "Usage: $0 -v <release|version>"
	exit 1
}

pull() {
	REPO_URL=http://jenkins.system.local:8081/repository/system
	SNAPSHOT_REPO_URL=http://sg-infra-ci1.system.local:8081/repository/system
	GROUP_ID=com/system
	ARTIFACT_ID=system-scripts
	CLASSIFIER=pack
	TYPE=tar.gz
	
	GROUP_ID_PATH=`echo $GROUP_ID |	sed "s/\./\//g"`
	METADATA_URL="$REPO_URL/$GROUP_ID_PATH/$ARTIFACT_ID/maven-metadata.xml"
	METADATA=`mktemp maven-metadata.xml.XXXXXX`
	wget $WGET_OPTS	-O $METADATA $METADATA_URL
	
	case $VERSION in
		release)
			ARTIFACT_VERSION=`grep -e "<release>.*<\/release>" $METADATA | sed "s/^.*<release>//g" | sed "s/<\\/release>.*$//g"`
			;;
		latest)
			if [ -n "$SNAPSHOT_REPO_URL" ]; then
				SNAPSHOT_METADATA_URL="$SNAPSHOT_REPO_URL/$GROUP_ID_PATH/$ARTIFACT_ID/maven-metadata.xml"
				SNAPSHOT_METADATA=`mktemp maven-metadata.xml.XXXXXX`
				wget $WGET_OPTS	-O $SNAPSHOT_METADATA $SNAPSHOT_METADATA_URL
			fi
			ARTIFACT_VERSION=`grep -e "<version>.*<\/version>" $METADATA $SNAPSHOT_METADATA | sed "s/^.*<version>//g" | sed "s/<\\/version>.*$//g" | sort -V | tail -1`
			rm -f $SNAPSHOT_METADATA
			;;
		*)
			ARTIFACT_VERSION=$VERSION
			;;
	esac
	rm $METADATA
	
	#resolve artifact file version
	if [ `echo "$ARTIFACT_VERSION" | grep -e "-SNAPSHOT"` ]; then
	 	if [ -n "$SNAPSHOT_REPO_URL" ]; then
			ARTIFACT_REPO_URL=$SNAPSHOT_REPO_URL
		else
			ARTIFACT_REPO_URL=$REPO_URL
		fi
		SNAPSHOT_METADATA_URL="$ARTIFACT_REPO_URL/$GROUP_ID_PATH/$ARTIFACT_ID/$ARTIFACT_VERSION/maven-metadata.xml"
		SNAPSHOT_METADATA=`mktemp maven-metadata.xml.XXXXXX`
		wget $WGET_OPTS	-O $SNAPSHOT_METADATA $SNAPSHOT_METADATA_URL
		FILE_VERSION=`grep -e "<value>.*</value>" $SNAPSHOT_METADATA | sed "s/^.*<value>//g" | sed "s/<\\/value>//g" | tail -1`
	else
		ARTIFACT_REPO_URL=$REPO_URL
		FILE_VERSION=$ARTIFACT_VERSION
	fi
	rm -f $SNAPSHOT_METADATA
	
	FILE=${ARTIFACT_ID}-${FILE_VERSION}-${CLASSIFIER}.$TYPE
	
	ARTIFACT_URL="$ARTIFACT_REPO_URL/$GROUP_ID_PATH/$ARTIFACT_ID/$ARTIFACT_VERSION/$FILE"
	
	wget $ARTIFACT_URL
	exit_on_error $? "cannot download artifact $FILE"
	
	find * -type f -not -name $FILE -exec rm -f {} \; 
	find * -depth -type d -not -name $FILE -exec rm -d {} \; 
	
	tar -zxvf $FILE 
	
	if [ `basename $0` != "pull.sh" ]; then
		rm -f $0
	fi
	
	rm -f $FILE
}

while getopts "v:e:" OPT;
do
	case $OPT in
	v)
		VERSION=${OPTARG}
		;;
	e)
		ENV=${OPTARG}
		;;
	*)
		usage
		exit 1
		;;
	esac
done
shift $((OPTIND-1))

VERSION=${VERSION:-"latest"}

if [ -z "$ENV" ]; then
	pull
else
	for INSTANCE in `find deploy/profile/pull/$ENV -name *.env`
	do
		echo $INSTANCE
		. $INSTANCE
		echo $SERVICE_USER@$SERVICE_HOST
		ssh $SERVICE_USER@$SERVICE_HOST "mkdir -p /opt/sys/script"
		scp $0 "$SERVICE_USER@$SERVICE_HOST:/opt/sys/script"
		ssh $SERVICE_USER@$SERVICE_HOST "cd /opt/sys/script; ./pull.sh $VERSION"
	done
fi
