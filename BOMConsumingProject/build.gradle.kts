plugins {
    // java - no need to specify `java` plugin as it is specified in `settings.gradle.kts` file

    //Specifying plugins without version as it is coming from `settings.gradle.kts` file
    kotlin("jvm")
    id("io.spring.dependency-management")
}

group = "com.cubeprogramming"
version = "1.0-SNAPSHOT"


dependencies {
    //Maven BOM importing statement
    implementation(platform("com.cubeprogramming:MavenBOMPublisher:1.0-SNAPSHOT"))

    //Dependency referencing without specifying dependency version
    implementation(kotlin("stdlib-jdk8"))

    //Referencing new dependency that does not exist in Maven BOM or overriding version of existing dependency
    testImplementation("junit", "junit", "4.12")
}


//This configuration is also specified in `settings.gradle.kts` file
/*configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}*/

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}
