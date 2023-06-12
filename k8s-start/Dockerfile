# 指定基础镜像 这里springboot项目运行只需要java jdk环境即可
FROM laosijiddd/java8:1.0
# 维护者信息
MAINTAINER mk
# 重命名
ADD target/k8s-start.jar app.jar
# 对外暴漏的端口号
EXPOSE 8000
# 运行
#ENTRYPOINT ["java", "-jar", "app.jar"]
ENV JAVA_OPTS=""
ENTRYPOINT exec java $JAVA_OPTS -jar app.jar

docker run  -d --name es -p 9200:9200 -p 9300:9300 \
-v /home/mk/docker_volume/elasticsearch/config:/usr/share/elasticsearch/config \
-v /home/mk/docker_volume/elasticsearch/data:/usr/share/elasticsearch/data \
-e "discovery.type=single-node" elasticsearch:8.7.0

docker run  -d --name kib \
-v /home/mk/docker_volume/kibana/config:/usr/share/kibana/config \
-p 5601:5601 docker.elastic.co/kibana/kibana:8.7.0


docker run -d \
--name logstash \
-p 5044:5044 \
-p 9600:9600 \
-v /home/mk/docker_volume/logstash/pipeline:/usr/share/logstash/pipeline \
-v /home/mk/docker_volume/logstash/config:/usr/share/logstash/config \
--add-host=host.docker.internal:host-gateway \
logstash:8.7.0



docker run -d \
  --name=filebeat \
  --user=root \
  --volume="$(pwd)/filebeat.docker.yml:/usr/share/filebeat/filebeat.yml:ro" \
  --volume="/var/lib/docker/containers:/var/lib/docker/containers:ro" \
  --volume="/var/run/docker.sock:/var/run/docker.sock:ro" \
  docker.elastic.co/beats/filebeat:8.7.0 filebeat -e --strict.perms=false \
  -E output.elasticsearch.hosts=["192.168.91.8:9200"]



