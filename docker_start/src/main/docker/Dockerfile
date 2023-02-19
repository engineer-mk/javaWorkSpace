# 指定基础镜像 这里springboot项目运行只需要java jdk环境即可
FROM java:latest
# 维护者信息
MAINTAINER mk
# 重命名
ADD *.jar app.jar
# 对外暴漏的端口号
EXPOSE 8000
# 运行
ENTRYPOINT ["java", "-jar", "app.jar"]
