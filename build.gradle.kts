plugins {
    java
    kotlin("jvm") version "1.9.10"
    `java-library`
    `maven-publish`
}

group = "com.IceCreamQAQ.YuQ"
version = "1.0.0-DEV1"

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://maven.icecreamqaq.com/repository/maven-public/")
}

dependencies {
    api("com.IceCreamQAQ:YuQ:0.1.0.0-DEV33")
}
