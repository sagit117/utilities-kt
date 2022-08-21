plugins {
    application
    kotlin("jvm") version "1.7.10"
}

version = "1.1.17"

repositories {
    mavenCentral()
}

java {
    withJavadocJar()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("ch.qos.logback:logback-classic:1.2.11")
    implementation("io.ktor:ktor-client-core:2.1.0")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:2.1.0")
    implementation("io.ktor:ktor-server-core-jvm:2.1.0")
    implementation("io.ktor:ktor-server-html-builder-jvm:2.1.0")
    implementation("io.ktor:ktor-server-auth-jwt:2.1.0")
    implementation("org.jetbrains.exposed:exposed-core:0.39.1")
    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.8.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}
