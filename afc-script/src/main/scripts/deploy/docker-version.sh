#!/bin/bash

exit_on_error() {
	if [ "$1" -ne "0" ] ; then
		echo "$2"
		exit $1
	fi
}

usage()	{
	echo "missing $1"
	echo "Usage: $0 -r <registry-url> -n <namespace> -a <artifact-id> -v <version> [-s <snapshot-registry-url>] [-u <username> -p <password>]"
	exit 1
}

list_version() {
	JSON=$1
	if [[ $JSON =~ .*tags.* ]]; then
		LIST=`echo $JSON | sed "s/^.*tags.*\[//g" | sed "s/\].*$//g"`
	else
		LIST=""
	fi
	echo $LIST
}

while getopts "r:s:u:p:n:a:v:" OPT;
do
	case $OPT in
	r)
		REPO_URL="http://$OPTARG"
		;;
	s)
		SNAPSHOT_REPO_URL="http://$OPTARG"
		;;
	u)
		USERNAME="--http-user ${OPTARG}"
		;;
	p)
		PASSWORD="--http-password ${OPTARG}"
		;;
	n)
		NAMESPACE=${OPTARG}
		;;
	a)
		ARTIFACT_ID=${OPTARG}
		;;
	v)
		VERSION=${OPTARG}
		;;
	*)
		usage
		exit 1
		;;
	esac
done
shift $((OPTIND-1))

[ -z $REPO_URL ] && usage "repo-url"
[ -z $NAMESPACE ] && usage "group-id"
[ -z $ARTIFACT_ID ] && usage "artifact-id"
[ -z $VERSION ]	&& usage "version"
OUTPUT=${OUTPUT:-"."}

TAG_LIST=`curl -s -X GET ${REPO_URL}/v2/${NAMESPACE}/${ARTIFACT_ID}/tags/list`
case $VERSION in
	release)
		ARTIFACT_VERSION=`list_version $TAG_LIST| sed "s/[\",]/\\n/g" | grep -v SNAPSHOT | sort -u -V -r | head -1`
		;;
	latest)
		if [ -n "$SNAPSHOT_REPO_URL" ]; then
			SNAPSHOT_TAG_LIST=`curl -s -X GET ${SNAPSHOT_REPO_URL}/v2/${NAMESPACE}/${ARTIFACT_ID}/tags/list`
			SNAPSHOT_ARTIFACT_VERSION=`list_version $SNAPSHOT_TAG_LIST`
		fi
		ARTIFACT_VERSION=`list_version $TAG_LIST`,`echo $SNAPSHOT_ARTIFACT_VERSION`
		ARTIFACT_VERSION=(`echo $ARTIFACT_VERSION | sed "s/[\",]/\\n/g" | sort -u -V -r | head -2`)
		if [ -n "${ARTIFACT_VERSION[1]}" ] && [[ ${ARTIFACT_VERSION[1]} = "${ARTIFACT_VERSION[0]}-SNAPSHOT" ]]; then
			ARTIFACT_VERSION=${ARTIFACT_VERSION[1]}
		else 
			ARTIFACT_VERSION=${ARTIFACT_VERSION[0]}
		fi
		;;
	*)
		ARTIFACT_VERSION=$VERSION
		;;
esac
if [ -z "$ARTIFACT_VERSION" ]; then
	ARTIFACT_VERSION=$VERSION
fi
echo $ARTIFACT_VERSION
