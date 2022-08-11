plugins {
    application
    kotlin("jvm") version "1.7.10"
}

version = "1.0.7"

repositories {
    mavenCentral()
}

java {
    withJavadocJar()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("ch.qos.logback:logback-classic:1.2.11")
    implementation("io.ktor:ktor-client-core:2.0.3")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:2.0.3")
    implementation("io.ktor:ktor-server-core-jvm:2.1.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}
