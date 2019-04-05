export SYS_ENV=$1

PROFILE=`dirname ${BASH_SOURCE[0]}`

. $PROFILE/system.env
export $(cut -d= -f1 $PROFILE/system.env)

. $PROFILE/alias.system
. $PROFILE/alias.docker

CQLSHS=`find $SYS_APP -type f -name cqlsh`
if [ -n "$CQLSHS" ]; then
	for CQLSH in $CQLSHS
	do 
		CASSANDRA_BIN=$(readlink -f $(dirname $CQLSH))
		export PATH=$PATH:$CASSANDRA_BIN
	done
fi
