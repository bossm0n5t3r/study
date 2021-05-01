import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val querydslVersion: String by project
val hikariVersion: String by project
val commonsCodecVersion: String by project
val guavaVersion: String by project
val zxcvbnVersion: String by project

plugins {
    val kotlinVersion = "1.4.31"
    id("org.springframework.boot") version "2.4.4"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    war
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    kotlin("plugin.jpa") version kotlinVersion
    kotlin("plugin.allopen") version kotlinVersion
    kotlin("plugin.noarg") version kotlinVersion
    kotlin("kapt") version kotlinVersion
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_15

buildscript {
    repositories {
        maven("https://plugins.gradle.org/m2/")
    }
    dependencies {
        classpath("org.jlleitschuh.gradle:ktlint-gradle:10.0.0")
    }
}

apply(plugin = "org.jlleitschuh.gradle.ktlint")

repositories {
    mavenCentral()
}

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
}

noArg {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    // jpa
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.data:spring-data-envers")
    // Querydsl
    implementation("com.querydsl:querydsl-apt:$querydslVersion")
    implementation("com.querydsl:querydsl-jpa:$querydslVersion")
    kapt("com.querydsl:querydsl-apt:$querydslVersion:jpa")
    // mariadb
    implementation("org.mariadb.jdbc:mariadb-java-client")
    // HikariCP
    implementation("com.zaxxer:HikariCP:$hikariVersion")
    // zxcvbn
    implementation("com.nulab-inc:zxcvbn:$zxcvbnVersion")

    implementation("commons-codec:commons-codec:$commonsCodecVersion")
    implementation("com.google.guava:guava:$guavaVersion")

    // thymeleaf
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

    providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.h2database:h2")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "15"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
