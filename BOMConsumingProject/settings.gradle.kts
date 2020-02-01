//rootProject.name = "BOMConsumingProject"

pluginManagement {
    val fis = java.io.FileInputStream("custom.gradle.properties")
    val prop = java.util.Properties()
    prop.load(fis)
    println("Value is =" + prop.getProperty("kotlinPluginVersion"))
    val kotlinPluginVersion: String by settings
    plugins {
//        kotlin("jvm") version "$kotlinPluginVersion"
        kotlin("jvm") version prop.getProperty("kotlinPluginVersion")
    }
}



