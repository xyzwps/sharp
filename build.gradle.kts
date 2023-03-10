plugins {
	java
    jacoco
	id("org.springframework.boot") version "3.0.2"
	id("io.spring.dependency-management") version "1.1.0"
//    id("test-report-aggregation")
}

group = "run.antleg"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_19

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

object Versions  {
    const val testContainers = "1.17.6"
    const val jjwt = "0.11.5"
    const val lombok = "1.18.26"
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
//	implementation("org.springframework.boot:spring-boot-starter-data-redis")
	implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-aop")
	implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-security")
//  Temporary explicit version to fix Thymeleaf bug
    implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6:3.1.1.RELEASE")
    implementation("org.springframework.security:spring-security-test")

//	implementation("org.springframework.session:spring-session-data-redis")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.2")
    implementation("com.github.f4b6a3:ulid-creator:5.1.0")
    implementation("io.vavr:vavr:0.10.4")
    implementation("org.apache.httpcomponents.client5:httpclient5:5.2.1")
    implementation("io.jsonwebtoken:jjwt-api:${Versions.jjwt}")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:${Versions.jjwt}")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:${Versions.jjwt}")

    implementation(project(":sharp-validation"))

    compileOnly("org.projectlombok:lombok:${Versions.lombok}")
    testCompileOnly("org.projectlombok:lombok:${Versions.lombok}")
    annotationProcessor("org.projectlombok:lombok:${Versions.lombok}")
    testAnnotationProcessor("org.projectlombok:lombok:${Versions.lombok}")

	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("com.mysql:mysql-connector-j")

    testImplementation("io.projectreactor:reactor-core")
    testImplementation("org.springframework:spring-webflux")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.testcontainers:junit-jupiter")
	testImplementation("org.testcontainers:mysql")
    testImplementation("io.rest-assured:rest-assured:5.3.0")
}

dependencyManagement {
	imports {
		mavenBom("org.testcontainers:testcontainers-bom:${Versions.testContainers}")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<Test>().configureEach {
    jvmArgs("--enable-preview")
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("--enable-preview")
}

tasks.withType<JavaExec>().configureEach {
    jvmArgs("--enable-preview")
}

