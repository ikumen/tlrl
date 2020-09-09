# --- Start build/compile 
FROM openjdk:8-jdk-alpine as BUILD-STAGE

ARG TLRL_SPRING_PROFILES=${TLRL_SPRING_PROFILES}
ARG TLRL_TARGET_DB=${TLRL_TARGET_DB}
ARG TLRL_SKIP_TESTS=${TLRL_SKIP_TESTS}
ARG POSTGRES_PASSWORD=${POSTGRES_PASSWORD}

# Setup
RUN apk add --update nodejs npm

# Copy over source
ADD . /tlrl
WORKDIR /tlrl

RUN \
  # Build frontend and copy over to backend
  npm install --prefix frontend \
    && npm run build --prefix frontend \
  && if [ "$TLRL_TARGET_DB" == "postgres" ]; then \
        SPRING_PROFILES_ACTIVE=${TLRL_SPRING_PROFILES},${TLRL_TARGET_DB} \
        FLYWAY_USER=postgres FLYWAY_PASSWORD=${POSTGRES_PASSWORD} ./mvnw -f backend/pom.xml \
        flyway:migrate -Ddb=${TLRL_TARGET_DB} \
    fi \ 
  # Build the backend
  && SPRING_PROFILES_ACTIVE=${TLRL_SPRING_PROFILES},${TLRL_TARGET_DB} \
    ./mvnw -f backend/pom.xml package -Dmaven.test.skip=${TLRL_SKIP_TESTS} -Ddb=${TLRL_TARGET_DB} \
    && mv backend/target/tlrl-*-SNAPSHOT.jar backend/tlrl.jar \
  # Clean up
  && rm -rf frontend/node_modules \
  && rm -rf backend/target 
 
# --- Start final image
FROM openjdk:8-jre-alpine
LABEL maintainer="ikumen@gnoht.com"

RUN mkdir app
COPY --from=BUILD-STAGE /tlrl/backend/tlrl.jar /app/tlrl.jar
EXPOSE 8080

ENTRYPOINT [ "java", "-Dspring.profiles.active=${TLRL_SPRING_PROFILES},${TLRL_TARGET_DB}", "-jar", "/app/tlrl.jar" ]


