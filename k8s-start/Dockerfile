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
