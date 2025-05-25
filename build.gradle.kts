import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    // 기본 자바, 애플리케이션 플러그인
    java
    application

    // JavaFX 플러그인 (org.openjfx)
    id("org.openjfx.javafxplugin") version "0.0.13"

    // ShadowJar (fat-jar) 플러그인
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.example"
version = "1.0"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

// JavaFX 모듈 설정
javafx {
    version = "17.0.15"
    modules = listOf("javafx.controls")
}

dependencies {
    // JNA Platform: User32, Kernel32 등 Win32 API 바인딩 포함
    implementation("net.java.dev.jna:jna-platform:5.13.0")

    // Gson
    implementation("com.google.code.gson:gson:2.9.1")
}

application {
    // 메인 클래스 (default package 에서 TimerApp 클래스)
    mainClass.set("TimerApp")
}

// 한글 주석/문자 처리를 위해 UTF-8 인코딩 지정
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

// ShadowJar 설정: 모든 의존성 포함한 fat-jar 생성
tasks.named<ShadowJar>("shadowJar") {
    archiveBaseName.set("FocusTimer-all")  // 결과물 이름: FocusTimer-all.jar
    archiveClassifier.set("")              // 분류자 제거
    mergeServiceFiles()                    // JNA 서비스 파일 병합

    // 매니페스트에 Main-Class 지정
    manifest {
        attributes["Main-Class"] = application.mainClass.get()
    }
}
import org.gradle.jvm.tasks.Jar

        tasks.register<Jar>("fatJar") {
            group = "assembly"
            description = "모든 runtime 의존성을 포함한 fat JAR 생성"
            duplicatesStrategy = DuplicatesStrategy.EXCLUDE

            manifest {
                attributes["Main-Class"] = "TimerApp"   // 실제 메인 클래스 이름
            }

            // runtimeClasspath 에 있는 모든 의존성을 풀어서 포함
            from(configurations.runtimeClasspath.get().map {
                if (it.isDirectory) it else zipTree(it)
            })

            // 기본 jar 내용도 함께
            with(tasks.jar.get())
        }
