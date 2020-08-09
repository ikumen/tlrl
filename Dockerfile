# --- Start build/compile 
FROM openjdk:8-jdk-alpine as BUILD-STAGE

ARG SPRING_PROFILES
ARG TARGET_DB
ARG SKIP_TESTS

# Setup
RUN apk add --update nodejs npm

# Copy over source
ADD . /tlrl
WORKDIR /tlrl

RUN \
  # Build frontend and copy over to backend
  npm install --prefix frontend \
    && npm run build --prefix frontend \
  # Build the backend
  && SPRING_PROFILES_ACTIVE=${SPRING_PROFILES},${TARGET_DB} \
    ./mvnw -f backend/pom.xml package -Dmaven.test.skip=${SKIP_TESTS} -Ddb=${TARGET_DB} \
    && mv backend/target/tlrl-*-SNAPSHOT.jar backend/tlrl.jar \
  # Clean up
  && rm -rf frontend/node_modules \
  && rm -rf backend/target
 
# --- Start final image
FROM openjdk:8-jre-alpine
LABEL maintainer="ikumen@gnoht.com"

RUN mkdir app
COPY --from=BUILD-STAGE /tlrl/backend/tlrl.jar app/tlrl.jar
EXPOSE 8080

ENTRYPOINT [ "java", "-Dspring.profiles.active=${SPRING_PROFILES},${TARGET_DB}", "-jar", "app/tlrl.jar" ]


