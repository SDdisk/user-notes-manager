plugins {
    java
    id("org.springframework.boot") version "4.0.1"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.github.sddisk"
version = "0.0.1-SNAPSHOT"
description = "user-notes-backend"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // web
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // security
    implementation("org.springframework.boot:spring-boot-starter-security")
    // jwt
    implementation("io.jsonwebtoken:jjwt-api:0.12.3")
    implementation("io.jsonwebtoken:jjwt-impl:0.12.3")
    implementation("io.jsonwebtoken:jjwt-jackson:0.12.3")

    // jpa
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    // db migration
    implementation("org.springframework.boot:spring-boot-starter-liquibase")

    // kafka
    implementation("org.springframework.boot:spring-boot-starter-kafka")

    // db drivers
    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("com.h2database:h2")
    implementation("org.springframework.boot:spring-boot-h2console")

    // devtools
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-data-jpa-test")
    testImplementation("org.springframework.boot:spring-boot-starter-security-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
    testImplementation("org.springframework.kafka:spring-kafka-test")
    // testcontainers
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:testcontainers-junit-jupiter")
    testImplementation("org.testcontainers:postgresql:1.19.3")
    testImplementation("org.testcontainers:kafka")

    // test runtime
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

}

tasks.withType<Test> {
    useJUnitPlatform()
}
