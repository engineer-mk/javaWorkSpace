#!/usr/bin/env bash
# shellcheck disable=SC2046
# shellcheck disable=SC2034
IMAGE_NAME="k8s-start"
IMAGE_VERSION="1.1"
REMOTE_ADDRESS="registry-vpc.cn-hangzhou.aliyuncs.com/xmg-docker"

docker rmi $(docker images -aq -f "dangling=true")
mvn clean package
docker build -t ${IMAGE_NAME}:${IMAGE_VERSION} -f Dockerfile .
docker tag ${IMAGE_NAME}:${IMAGE_VERSION} ${REMOTE_ADDRESS}/${IMAGE_NAME}:${IMAGE_VERSION}
docker push ${REMOTE_ADDRESS}/${IMAGE_NAME}:${IMAGE_VERSION}

#scp deployment.yml ubuntu:/home/mk/k8s-yml/k8s-start
