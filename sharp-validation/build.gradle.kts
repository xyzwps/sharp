plugins {
    java
}

group = "run.antleg"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_19

repositories {
    mavenCentral()
}

dependencies {
    implementation("jakarta.validation:jakarta.validation-api:3.0.2")

    testImplementation(platform("org.junit:junit-bom:5.9.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.withType<Test> {
    useJUnitPlatform()
}