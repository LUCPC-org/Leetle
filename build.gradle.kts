import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {

    id("com.github.johnrengelman.shadow").version("7.1.2")
    kotlin("jvm") version "1.7.10"
    application
}

group = "org.lucpc"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("net.dv8tion:JDA:5.0.0-alpha.22")
    implementation("com.google.code.gson:gson:2.8.9")
}
tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
tasks.jar{
    manifest.attributes["Main-Class"] = "MainKt"
}


application {
    mainClass.set("MainKt")

}