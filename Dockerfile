#Stage 1 : Build the jar
FROM gradle:ubi-minimal AS builder
COPY --chown=gradle:gradle . /home/gradle/project
WORKDIR /home/gradle/project
RUN gradle bootJar --no-daemon

#Stage 2: Run App
FROM mcr.microsoft.com/openjdk/jdk:21-azurelinux
WORKDIR /app
COPY --from=builder /home/gradle/project/build/libs/*.jar app.jar
EXPOSE 3000
#HEALTHCHECK --interval=30s --timeout=5s --start-period=5s \
#  CMD curl -f http://localhost:8080/health || exit 1
ENTRYPOINT ["java", "-jar", "app.jar"]


