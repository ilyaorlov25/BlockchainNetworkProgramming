import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.20"
    application
}

group = "me.ilyao"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation ("com.google.code.gson:gson:2.10.1")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}

tasks.create("BlockchainJar", Jar::class) {
    group = "build"
    description = "Creates JAR of the application"
    manifest.attributes["Main-Class"] = "MainKt"
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    val dependencies = configurations
        .runtimeClasspath
        .get()
        .map(::zipTree)
    exclude("META-INF/*.RSA", "META-INF/*.SF", "META-INF/*.DSA", "META-INF/INDEX.LIST")
    from(dependencies)
    with(tasks.jar.get())
}