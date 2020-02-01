//rootProject.name = "BOMConsumingProject"
pluginManagement {
    //Defining access to remote properties file
    val prop = java.util.Properties()
    val propertyResource = java.net.URL("https://raw.githubusercontent.com/cubeprogramming/GradleBOM/master/MavenBOMPublisher/gradle.properties")
    prop.load(java.io.InputStreamReader(propertyResource.openStream()))
    println("Value is =" + prop.getProperty("kotlinPluginVersion"))

    val kotlinPluginVersion: String by settings
    plugins {
        kotlin("jvm") version prop.getProperty("kotlinPluginVersion")
        id("io.spring.dependency-management") version "1.0.9.RELEASE"
    }
}


gradle.allprojects{
    repositories {
        //    mavenLocal()
        mavenCentral()
        maven(url = uri("http://localhost:8081/repository/maven-snapshots/"))
    }
}




