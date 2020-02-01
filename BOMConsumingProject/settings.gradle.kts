import java.io.InputStreamReader
import java.io.BufferedReader
import java.util.Properties
import java.net.URL

//rootProject.name = "BOMConsumingProject"
pluginManagement {
    //Defining access to remote properties file
    val prop = Properties()
    val propertyResource = URL("https://raw.githubusercontent.com/cubeprogramming/GradleBOM/master/MavenBOMPublisher/gradle.properties")
    prop.load(InputStreamReader(propertyResource.openStream()))
    println("Value is =" + prop.getProperty("kotlinPluginVersion"))

    plugins {
        kotlin("jvm") version prop.getProperty("kotlinPluginVersion")
        id("io.spring.dependency-management") version "1.0.9.RELEASE"
    }
}


gradle.allprojects{
    repositories {
        //Define repository list loader
        val repositoryListRes = URL("https://raw.githubusercontent.com/cubeprogramming/GradleBOM/master/MavenBOMPublisher/repositoryList")
        val repositoryLines = BufferedReader(InputStreamReader(repositoryListRes.openStream()))

        mavenCentral()
        repositoryLines.forEachLine {
            maven(url = uri(it))
        }
    }
}




