/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Kotlin library project to get you started.
 * For more details take a look at the 'Building Java & JVM projects' chapter in the Gradle
 * User Manual available at https://docs.gradle.org/7.4.1/userguide/building_java_projects.html
 */

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

sourceSets {
  main {
    java {
      setSrcDirs(listOf("src"))
    }
    resources {
      setSrcDirs(listOf("."))
      include("plugin.yml")
      include("README.md")
      include("LICENSE.txt")
      include("data/*.txt")
    }
  }
}

tasks {
  withType<KotlinCompile> {
    kotlinOptions {
      apiVersion = "1.5"
      languageVersion = "1.5"
      jvmTarget = "17"
    }
  }
  withType<JavaCompile> {
    sourceCompatibility = "17"
    targetCompatibility = "17"
  }
  jar {
    val dependencies = configurations
        .runtimeClasspath
        .get()
        .filter { !it.name.contains("spigot") }
        .map(::zipTree)
    from(dependencies)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    manifest {
      attributes(
        "Name" to "com/mercerenies/turtletroll/",
        "Specification-Title" to "A Turtle's Troll",
        "Implementation-Title" to "com.mercerenies.turtletroll",
      )
    }
  }
}

plugins {
  // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
  id("org.jetbrains.kotlin.jvm") version "1.8.22"

  // Apply the java-library plugin for API and implementation separation.
  `java-library`
}

repositories {
  // Use Maven Central for resolving dependencies.
  mavenCentral()
  maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
  // Align versions of all Kotlin components
  implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

  // Use the Kotlin JDK 8 standard library.
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

  compileOnly("io.papermc.paper:paper-api:1.19.2-R0.1-SNAPSHOT")

  implementation(
    fileTree("lib/") {
      include("*.jar")
    }
  )

  // This dependency is used internally, and not exposed to consumers on their own compile classpath.
  //implementation("com.google.guava:guava:30.1.1-jre")

  // Use the Kotlin test library.
  //testImplementation("org.jetbrains.kotlin:kotlin-test")

  // Use the Kotlin JUnit integration.
  //testImplementation("org.jetbrains.kotlin:kotlin-test-junit")

  // This dependency is exported to consumers, that is to say found on their compile classpath.
  //api("org.apache.commons:commons-math3:3.6.1")
}
