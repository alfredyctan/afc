#!/bin/sh

config() {
    DIR=$1
    NAME=$2
    CFG_ENV=$DIR/env/${SYS_ENV}

    if [ -f "$CFG_ENV/${SYS_CLUSTER}/${SYS_INSTANCE}/$NAME" ]; then
        echo "$CFG_ENV/${SYS_CLUSTER}/${SYS_INSTANCE}/$NAME"
        exit;
    fi

    if [ -f "$CFG_ENV/${SYS_INSTANCE}/$NAME" ]; then
        echo "$CFG_ENV/${SYS_INSTANCE}/$NAME"
        exit;
    fi

    if [ -f "$CFG_ENV/${SYS_CLUSTER}/$NAME" ]; then
        echo "$CFG_ENV/${SYS_CLUSTER}/$NAME"
        exit;
    fi

    if [ -f "$CFG_ENV/$NAME" ]; then
        echo "$CFG_ENV/$NAME"
        exit;
    fi

    if [ -f "$DIR/$NAME" ]; then
        echo "$DIR/$NAME"
        exit;
    fi
}

SPRING_PROFILES="${SYS_ENV},${SYS_REGION},${SYS_CLUSTER},${SYS_INSTANCE}"

INCLUDE_PROFILES="${INCLUDE_PROFILES}"
if [ -n "$INCLUDE_PROFILES" ]; then
	SPRING_PROFILES="${INCLUDE_PROFILES},${SPRING_PROFILES}"
fi

if [ -n "$ACTIVE_PROFILES" ]; then
	SPRING_PROFILES="${ACTIVE_PROFILES},${SPRING_PROFILES}"
fi

LIB=current/lib
CFG=current/config
CFG_ENV=$CFG/env/${SYS_ENV}
CFG_CLUSTER_INSTANCE=$CFG_ENV/${SYS_CLUSTER}/${SYS_INSTANCE}
CFG_INSTANCE=$CFG_ENV/${SYS_INSTANCE}
CFG_CLUSTER=$CFG_ENV/${SYS_CLUSTER}

VM_OPTIONS=`echo $(cat $(config $CFG vm.options))`
VM_OPTIONS=`eval echo $VM_OPTIONS`
CLASSPATH_CFG="${CFG_CLUSTER_INSTANCE}:${CFG_INSTANCE}:${CFG_CLUSTER}:${CFG_ENV}:${CFG}"
CLASSPATH_LIB=`echo $(find $LIB -type f -name *.jar) | sed "s/ /:/g"`

APP_LAUNCHER_MAIN="${APP_LAUNCHER_MAIN}"
if [ -z "$APP_LAUNCHER_MAIN" ]; then
    APP_LAUNCHER_MAIN="org.springframework.boot.loader.JarLauncher"
fi

echo "starting service $SYS_ENV $SYS_REGION $SYS_SERVICE $SYS_CLUSTER $SYS_INSTANCE"
echo $JAVA_HOME/bin/java -cp $CLASSPATH_CFG:$CLASSPATH_LIB ${VM_ARGS} ${VM_OPTIONS} -Dsys.data=$SYS_DATA -Dsys.log=$SYS_LOG -Dsys.env=$SYS_ENV  -Dsys.region=$SYS_REGION -Dsys.service=$SYS_SERVICE -Dsys.cluster=$SYS_CLUSTER -Dsys.instance=$SYS_INSTANCE $APP_LAUNCHER_MAIN "$@" --spring.profiles.active=$SPRING_PROFILES ${APP_ARGS}
$JAVA_HOME/bin/java -cp $CLASSPATH_CFG:$CLASSPATH_LIB ${VM_ARGS} ${VM_OPTIONS} -Dsys.data=$SYS_DATA -Dsys.log=$SYS_LOG -Dsys.env=$SYS_ENV -Dsys.region=$SYS_REGION -Dsys.service=$SYS_SERVICE -Dsys.cluster=$SYS_CLUSTER -Dsys.instance=$SYS_INSTANCE $APP_LAUNCHER_MAIN "$@" --spring.profiles.active=$SPRING_PROFILES ${APP_ARGS}
