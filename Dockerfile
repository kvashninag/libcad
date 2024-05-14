FROM openjdk:17-slim
RUN mkdir -p /opt/portal/logs \
    && mkdir -p /opt/portal/conf \
    && apt update -y && apt upgrade -y && apt install -y curl && apt autoremove -y && apt clean
ENV JAR_FILE=demo4-0.0.1.jar
WORKDIR /opt/portal
ADD libs libs
COPY build/libs/${JAR_FILE} ${JAR_FILE}
ENTRYPOINT java -jar ${JAR_FILE}
