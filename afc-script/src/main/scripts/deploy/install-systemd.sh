#/bin/bash

if [ "$#" -lt "5" ]; then
    echo "usage: $0 <service> <env> <region> <cluster> <instance>"
    exit;
fi

SERVICE=$1
ENV=$2
REGION=$3
CLUSTER=$4
INSTANCE=$5
echo "installing systemd for $SERVICE $ENV $REGION $CLUSTER $INSTANCE"

UNIT_FILE="${SYS_APP}/${SERVICE}/${REGION}_${CLUSTER}_${INSTANCE}/current/config/env/${ENV}/${CLUSTER}/${INSTANCE}/${SERVICE}.service"
if [ -r "$UNIT_FILE" ]; then
    sudo cp $UNIT_FILE /lib/systemd/system

    sudo systemctl daemon-reload
    sudo systemctl enable ${SERVICE}.service
    sudo systemctl start ${SERVICE}.service
    echo "installed systemd ${SERVICE}.service"
else
    echo "service unit file [$UNIT_FILE] not found, skip installation"
fi