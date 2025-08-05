plugins {
  id("java")
  id("org.jetbrains.kotlin.jvm") version "1.9.25"
  id("org.jetbrains.intellij") version "1.17.4"
}

group = "com.aicc"
version = "1.1"

repositories {
  mavenCentral()
}

dependencies {
  implementation("io.github.ollama4j:ollama4j:1.0.100")
  implementation("org.slf4j:slf4j-jdk14:2.1.0-alpha1")
  implementation("com.google.guava:guava:31.0.1-jre")
  testImplementation(kotlin("test"))
  testImplementation(kotlin("test-junit"))
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
  version.set("2024.2")
  type.set("IC") // Target IDE Platform

  plugins.set(listOf(/* Plugin Dependencies */))
}

tasks {
  // Set the JVM compatibility versions
  withType<JavaCompile> {
    sourceCompatibility = "17"
    targetCompatibility = "17"
  }
  withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
  }

  patchPluginXml {
    sinceBuild.set("233")
    untilBuild.set("251.*")
  }

  signPlugin {
    certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
    privateKey.set(System.getenv("PRIVATE_KEY"))
    password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
  }

  publishPlugin {
    token.set(System.getenv("PUBLISH_TOKEN"))
  }

  jar {
    manifest {
      attributes["Implementation-Title"] = "AI Code Completion"
      attributes["Implementation-Version"] = version
    }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from({
      configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) }
    })
  }
}
