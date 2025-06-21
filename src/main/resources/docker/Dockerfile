FROM docker.io/openjdk:17.0.2
ADD Open-1.0-SNAPSHOT.jar app.jar

ENV TZ="Asia/Shanghai"
ENV JAVA_TOOL_OPTIONS="-Dfile.encoding=UTF-8 -Dsun.jnu.encoding=UTF-8"
ENV LANG C.UTF-8
ENV LC_ALL C.UTF-8
EXPOSE 8080
ENTRYPOINT ["java","-Dfile.encoding=utf-8","-jar","/app.jar"]
