#!/usr/bin/env bash
POINT="22"
REMOTE_ADDRESS="ubuntu"
UPLOAD_PATH="/springboot"
FILE_NAME="dubbo-order"
ENV="test1"

mvn clean package
scp -P ${POINT} target/${FILE_NAME}.jar ${REMOTE_ADDRESS}:${UPLOAD_PATH}/
ssh -p ${POINT} ${REMOTE_ADDRESS} "mv ${UPLOAD_PATH}/${FILE_NAME}.jar ${UPLOAD_PATH}/${FILE_NAME}-${ENV}.jar"

ssh -p ${POINT} ${REMOTE_ADDRESS} "nohup java -jar ${UPLOAD_PATH}/${FILE_NAME}-${ENV}.jar --spring.profile.active= ${ENV} &"
