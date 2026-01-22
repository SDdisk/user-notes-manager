from gradle:8.14.3-jdk21 as builder

workdir /app

copy gradle gradle
copy gradlew .
copy build.gradle.kts .
copy settings.gradle.kts .

run ./gradlew --no-daemon dependencies

copy src src

run ./gradlew --no-daemon bootJar -x test

from eclipse-temurin:21-jre

workdir /app

copy --from=builder /app/build/libs/*.jar app.jar

entrypoint ["java", "-jar", "app.jar"]