import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.5.0"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.5.10"
	kotlin("plugin.spring") version "1.5.10"

	java
	application
	jacoco
	distribution
}

group = "it.unibo"
version = "1.0"
java.sourceCompatibility = JavaVersion.VERSION_11

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	kotlinOptions {
		jvmTarget = "11"
	}
}
repositories {
	mavenCentral()
	jcenter() 	//required by andrea pivetta
	flatDir{ dirs("../../unibolibs")   }   //Our libraries
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf:2.5.6")
	implementation("org.springframework.boot:spring-boot-starter-web:2.5.6")
	implementation("org.springframework.boot:spring-boot-starter-websocket:2.5.6")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.0")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	developmentOnly("org.springframework.boot:spring-boot-devtools:2.5.6")
	implementation("org.springframework.boot:spring-boot-starter-webflux:2.5.6")
	//See https://mkyong.com/spring-boot/intellij-idea-spring-boot-template-reload-is-not-working/
	/* INTELLIJ
	File –> Setting –> Build, Execution, Deployment –> Compiler –> check this Build project automatically
	SHIFT+CTRL+A registry | compiler.automake.allow.when.app.running
	If the static files are not reloaded, press CTRL+F9 to force a reload.
	 */
	testImplementation("org.springframework.boot:spring-boot-starter-test:2.5.6")

	//Webjars See https://www.baeldung.com/maven-webjars
//WebJars have nothing to do with Spring
	implementation("org.webjars:webjars-locator-core:0.48")
	implementation("org.webjars:sockjs-client:1.5.1")
	implementation("org.webjars:stomp-websocket:2.3.4")
	implementation("org.webjars:bootstrap:5.1.1")
	implementation("org.webjars:jquery:3.6.0")

//KOTLIN

	// Align versions of all Kotlin components
	implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

	// Use the Kotlin JDK 8 standard library.
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	// This dependency is used by the application.
	implementation("com.google.guava:guava:31.0.1-jre")

	//COROUTINE
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:1.3.8")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2-native-mt")


//JSON
	// https://mvnrepository.com/artifact/org.json/json
	implementation("org.json:json:20210307")

	//OkHttp library for websockets with Kotlin
	implementation("com.squareup.okhttp3:okhttp:4.9.2")

//OkHttp library for websockets with Kotlin
	implementation("com.squareup.okhttp3:okhttp:4.9.2")

//ADDED FOR THE HTTP CLIENT
	// https://mvnrepository.com/artifact/org.apache.httpcomponents/httpclient
	implementation ("org.apache.httpcomponents:httpclient:4.5.13")
	// https://mvnrepository.com/artifact/commons-io/commons-io
	implementation ("commons-io:commons-io:2.6")

//COAP
	// https://mvnrepository.com/artifact/org.eclipse.californium/californium-core
	//FOR A MORE RECENT VERSION, WE MUST INTRODUCE SOME Exception handling in the code
	implementation("org.eclipse.californium:californium-core:3.0.0")
	// https://mvnrepository.com/artifact/org.eclipse.californium/californium-proxy
	implementation("org.eclipse.californium:californium-proxy:2.0.0-M12")

//PLANNER aimacode
// https://mvnrepository.com/artifact/com.googlecode.aima-java/aima-core
	implementation("com.googlecode.aima-java:aima-core:3.0.0")

//STRING COLORS
	// https://mvnrepository.com/artifact/com.andreapivetta.kolor/kolor
	implementation( "com.andreapivetta.kolor:kolor:1.0.0" )

//UNIBO
	implementation("IssActorKotlinRobotSupport:IssActorKotlinRobotSupport:2.0")
	implementation("uniboIssSupport:IssWsHttpJavaSupport:1.0")
	implementation("uniboInterfaces:uniboInterfaces")
	implementation("uniboProtocolSupport:unibonoawtsupports")
	implementation("uniboplanner20:it.unibo.planner20:1.0")
	implementation("qak:it.unibo.qakactor:2.4")



}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
application {
	// Define the main class for the application.
	mainClass.set("it.unibo.clientGui.ApplicationKt")
}

version = "1.0.1"

tasks.jar {
	manifest {
		attributes["Main-Class"] = "it.unibo.clientGui.ApplicationKt"
		attributes(mapOf("Implementation-Title" to project.name,
			"Implementation-Version" to project.version))
	}
}