# 1단계: 빌드
FROM gradle:8-jdk21 AS build
WORKDIR /app
COPY . .
RUN ./gradlew build -x test --no-daemon -Dorg.gradle.jvmargs="-Xmx256m"

# 2단계: 실행
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/build/libs/*SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]


