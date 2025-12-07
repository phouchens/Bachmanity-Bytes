plugins {
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.serialization") version "1.9.22"
    application
    id("org.graalvm.buildtools.native") version "0.10.0"
}

group = "com.bachmanity"
version = "1.0.0"

val ktorVersion = "2.3.7"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
    implementation("com.github.ajalt.clikt:clikt:4.2.1")

    // Ktor dependencies
    implementation("io.ktor:ktor-server-core:2.3.7")
    implementation("io.ktor:ktor-server-netty:2.3.7")
    implementation("io.ktor:ktor-server-html-builder:2.3.7")
    implementation("io.ktor:ktor-server-content-negotiation:2.3.7")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.7")

    testImplementation(kotlin("test"))
}

application {
    mainClass.set("com.bachmanity.bytes.MainKt")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "com.bachmanity.bytes.MainKt"
    }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}

graalvmNative {
    toolchainDetection.set(false)
    binaries {
        named("main") {
            imageName.set("bachmanity-bytes")
            mainClass.set("com.bachmanity.bytes.MainKt")
            buildArgs.add("--no-fallback")
            buildArgs.add("-O2")
            buildArgs.add("--gc=serial")

            // Enable resource inclusion
            buildArgs.add("-H:IncludeResources=quotes.json")
        }
    }
}
