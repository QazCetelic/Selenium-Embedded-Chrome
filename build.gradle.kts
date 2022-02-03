import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.31"
}

group = "me.qaz"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.6.0")

    // TODO test what is unnecessary and remove it
    implementation("org.seleniumhq.selenium:selenium-java:4.1.1")
    implementation("org.seleniumhq.selenium:selenium-api:4.1.1")
    implementation("org.seleniumhq.selenium:selenium-server:3.141.59")
    implementation("org.seleniumhq.selenium:selenium-chrome-driver:4.1.1")

    implementation("org.slf4j:slf4j-api:1.7.33")
    implementation("org.slf4j:slf4j-simple:1.7.33")

    implementation("net.lingala.zip4j:zip4j:2.9.1")
}

// Hyper JAR
tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "MainKt"
    }
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}