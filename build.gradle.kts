import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.jvm.tasks.Jar
import org.gradle.api.tasks.Sync

plugins {
    application
    id("org.openjfx.javafxplugin") version "0.1.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.focustimer"
version = "1.0"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

javafx {
    version = "17.0.15"
    modules = listOf("javafx.controls")
}

dependencies {
    implementation("net.java.dev.jna:jna-platform:5.13.0")
    implementation("com.google.code.gson:gson:2.9.1")
}

application {
    mainClass.set("TimerApp")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

// shadowJar
tasks.named<ShadowJar>("shadowJar") {
    archiveBaseName.set("FocusTimer-all")
    archiveVersion.set("")
    archiveClassifier.set("")
    mergeServiceFiles()
    manifest {
        attributes["Main-Class"] = application.mainClass.get()
    }
}

// fatJar
tasks.register<Jar>("fatJar") {
    group = "assembly"
    description = "Create a fat JAR including runtime dependencies"
    duplicatesStrategy = org.gradle.api.file.DuplicatesStrategy.EXCLUDE

    archiveBaseName.set("FocusTimer-all")
    archiveVersion.set("")
    archiveClassifier.set("")

    manifest {
        attributes["Main-Class"] = application.mainClass.get()
    }

    from(sourceSets.main.get().output)

    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get()
            .filter { it.name.endsWith(".jar") }
            .map { zipTree(it) }
    })
}

// jpackage input 준비(자바FX jar 포함)
tasks.register<Sync>("prepJpackageInput") {
    group = "distribution"
    description = "Prepare input directory for jpackage"
    dependsOn("fatJar")

    into(layout.buildDirectory.dir("jpackage/input"))
    from(tasks.named("fatJar"))
    from(configurations.runtimeClasspath)
}
