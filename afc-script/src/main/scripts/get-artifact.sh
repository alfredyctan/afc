#!/bin/bash

exit_on_error() {
	if [ "$1" -ne "0" ] ; then
		echo "$2"
		exit $1
	fi
}

usage()	{
	echo "missing $1"
	echo "Usage: $0 -r <repo-url> -g <group-id> -a <artifact-id> [-s <snapshot-repo-url>] [-c <classifier>] [-t type] [-u <username> -p <password>] [-o <output-directory>] [-q:quiet mode]"
	exit 1
}

while getopts "r:s:u:p:g:a:c:t:v:o:q" OPT;
do
	case $OPT in
	r)
		REPO_URL=$OPTARG
		;;
	s)
		SNAPSHOT_REPO_URL=$OPTARG
		;;
	u)
		USERNAME="--http-user ${OPTARG}"
		;;
	p)
		PASSWORD="--http-password ${OPTARG}"
		;;
	g)
		GROUP_ID=${OPTARG}
		;;
	a)
		ARTIFACT_ID=${OPTARG}
		;;
	v)
		VERSION=${OPTARG}
		;;
	c)
		CLASSIFIER="-${OPTARG}"
		;;
	t)
		TYPE=${OPTARG}
		;;
	o)
		OUTPUT=${OPTARG}
		OUTPUT_OPT="-P ${OPTARG}/"
		;;
	q)
		QUIET="-q"
		;;
	*)
		usage
		exit 1
		;;
	esac
done
shift $((OPTIND-1))

[ -z $REPO_URL ] && usage "repo-url"
[ -z $GROUP_ID ] && usage "group-id"
[ -z $ARTIFACT_ID ] && usage "artifact-id"
[ -z $VERSION ]	&& usage "version"
OUTPUT=${OUTPUT:-"."}

WGET_OPTS="--no-check-certificate ${QUIET}"

#resolve artifact version
TYPE=${TYPE:-"jar"}
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
if [[ $ARTIFACT_VERSION	=~ "-SNAPSHOT" ]]; then
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


ARTIFACT_URL="$ARTIFACT_REPO_URL/$GROUP_ID_PATH/$ARTIFACT_ID/$ARTIFACT_VERSION/$ARTIFACT_ID-${FILE_VERSION}${CLASSIFIER}.$TYPE"

mkdir -p $OUTPUT
wget $WGET_OPTS	$USERNAME $PASSWORD $OUTPUT_OPT $ARTIFACT_URL

if [ ! -f "${OUTPUT}/$ARTIFACT_ID-${FILE_VERSION}${CLASSIFIER}.$TYPE" ]; then
	echo "failed to download $ARTIFACT_URL" 
	usage "type or classifier?"
fi

if [ "$FILE_VERSION" != "$ARTIFACT_VERSION" ]; then
	mv ${OUTPUT}/$ARTIFACT_ID-${FILE_VERSION}${CLASSIFIER}.$TYPE ${OUTPUT}/$ARTIFACT_ID-${ARTIFACT_VERSION}${CLASSIFIER}.$TYPE 
fi
