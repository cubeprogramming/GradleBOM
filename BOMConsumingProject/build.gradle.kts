plugins {
//    java
    kotlin("jvm")
    id("io.spring.dependency-management")
}

group = "com.cubeprogramming"
version = "1.0-SNAPSHOT"


dependencies {
    //api(platform(project("JavaPlatformProject")))
    implementation(platform("com.cubeprogramming:MavenBOMPublisher:1.0-SNAPSHOT"))
    implementation(kotlin("stdlib-jdk8"))
    testImplementation("junit", "junit", "4.12")
}



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
