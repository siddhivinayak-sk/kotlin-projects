ARG BASE_IMAGE=gcr.io/distroless/java17-debian12:debug-nonroot
ARG BUILDER_IMAGE=eclipse-temurin:17-jre-alpine
ARG ENVOY_IMAGE=envoy-preflight:1.0.0
FROM ${ENVOY_IMAGE} AS envoy-preflight

FROM ${BUILDER_IMAGE} AS builder
WORKDIR application
ARG APPLICATION_PATH
ADD $APPLICATION_PATH application.jar
RUN java -Djarmode=layertools -jar application.jar extract

ENV ALWAYS_KILL_ENVOY=true

FROM ${BASE_IMAGE}
WORKDIR /application
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./
COPY --from=envoy-preflight /usr/bin/envoy-preflight /bin

ARG APPLICATION_PORT=8080
ENV APPLICATION_PORT=$APPLICATION_PORT

EXPOSE $APPLICATION_PORT
ARG APPLICATION_NAME
ENV APPLICATION_NAME=$APPLICATION_NAME

ARG SPRING_PROFILES_ACTIVE
ENV SPRING_PROFILES_ACTIVE="dev, debug"

VOLUME /tmp
ENTRYPOINT ["/bin/envoy-preflight", "java", "org.springframework.boot.loader.JarLauncher"]


# Build Image: docker build --rm --no-cache -t sk-apps/sk-gateway:1.0.0 --build-arg APPLICATION_PATH=build/libs/sk-gateway-0.0.1-SNAPSHOT.jar --build-arg APPLICATION_NAME=sk-gateway -f Dockerfile .
# Run Image: docker run -p 8080:8080 sk-apps/sk-gateway:1.0.0
# Remove Image: docker image rm sk-apps/sk-gateway:1.0.0
# To test: curl http://localhost:8080/httpbin/get
