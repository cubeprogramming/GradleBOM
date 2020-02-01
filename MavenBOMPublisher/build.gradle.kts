plugins {
  `java-platform` //Plugin used to build Maven BOM
  `maven-publish` //Plugin used to publish Maven BOM
}

group = "com.cubeprogramming"
version = "1.0-SNAPSHOT"

//Allows <dependencies> block in Maven BOM
javaPlatform {
  allowDependencies()
}

dependencies {
  //Properties from project gradle.properties to variable mapping
  val jacksonModuleVersion: String by project

  //Maven BOM <dependencyManagement> block
  constraints {
    api("commons-httpclient:commons-httpclient:3.1")
    runtime("org.postgresql:postgresql:42.2.5")
  }

  //Maven BOM <dependencies> block
  api(platform("com.fasterxml.jackson:jackson-bom:2.9.8"))
  api("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonModuleVersion")

}


//Publishing to Maven repository
publishing {
  repositories {
    maven{
      //Properties from ./gradle/gradle.properties to variable mapping
      val localMavenUser: String by extra
      val localMavenPassword: String by extra

      //Publishing Maven repository URL and credentials
      url = uri("http://localhost:8081/repository/maven-snapshots/")
      credentials {
        username = localMavenUser
          password = localMavenPassword
      }
    }
  }

  //Defines Maven BOM as valid publication
  publications {
    create<MavenPublication>("myPlatform") {
      from(components["javaPlatform"])
    }
  }
}



