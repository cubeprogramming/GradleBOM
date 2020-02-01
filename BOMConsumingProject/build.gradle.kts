plugins {
    java
    kotlin("jvm") version "1.3.61"
}

group = "com.cubeprogramming"
version = "1.0-SNAPSHOT"

repositories {
//    mavenLocal()
    mavenCentral()
    maven(url = uri("http://localhost:8081/repository/maven-snapshots/"))
}

dependencies {
    //api(platform(project("JavaPlatformProject")))
    implementation(platform("com.cubeprogramming:JavaPlatformProject:1.0-SNAPSHOT"))
    implementation(kotlin("stdlib-jdk8"))
    testImplementation("junit", "junit", "4.12")
}



configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}