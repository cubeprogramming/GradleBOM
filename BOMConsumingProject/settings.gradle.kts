//import java.io.InputStreamReader
//import java.io.BufferedReader
//import java.util.Properties
//import java.net.URL

//rootProject.name = "BOMConsumingProject"
pluginManagement {
    //Defining access to remote gradle.properties file that resides on GIT server
    val prop = java.util.Properties()
    val propertyResource = java.net.URL("https://raw.githubusercontent.com/cubeprogramming/GradleBOM/master/MavenBOMPublisher/gradle.properties")
    prop.load(java.io.InputStreamReader(propertyResource.openStream()))

    //Defining access to plugins.properties file that resides on GIT server
    val pluginsProp = java.util.Properties()
    val pluginsPropResource = java.net.URL("https://raw.githubusercontent.com/cubeprogramming/GradleBOM/master/MavenBOMPublisher/plugins.properties")
    pluginsProp.load(java.io.InputStreamReader(pluginsPropResource.openStream()))

    plugins {
        java
        kotlin("jvm") version prop.getProperty("kotlinPluginVersion")
//        id("org.jetbrains.kotlin.jvm") version prop.getProperty("kotlinPluginVersion")
        pluginsProp.forEach{
            (k, v) -> id(k.toString()) version v.toString()
        }
    }
}


gradle.allprojects{
    apply(plugin = "java")

    repositories {
        //Defining access to repostoryList file that resides on GIT server
        val repositoryListRes = java.net.URL("https://raw.githubusercontent.com/cubeprogramming/GradleBOM/master/MavenBOMPublisher/repositoryList")
        val repositoryLines = java.io.BufferedReader(java.io.InputStreamReader(repositoryListRes.openStream()))

        mavenCentral()
        repositoryLines.forEachLine {
            maven(url = uri(it))
        }
    }

    configure<JavaPluginConvention> {
        sourceCompatibility = JavaVersion.VERSION_1_8
    }
}




