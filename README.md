# Gradle Kotlin DSL Maven like BOM creation example

This example explains how to build Maven like "[Bill Of Material](Creating Maven like BOM in Gradle Kotlin DSL with full versioning support for plugins and dependencies)" or BOM for short.

Reason to create Maven like BOM is to have centralised place where we can manage all plugin and dependency versions as well as create some common configuration that all of the project importing this BOM, can share.

Unlike Maven where build script is composed as a set of declarative statements, Gradle build script is code. Gradle syntax is quit strict when it comes to ordering of various elements in the script. One such example is a `plugins {}` section that has to be the first statement in each build script. This is quite logical as Gradle interpreter does not know nothing on its own unless we tell it what to do. So anything Gralde can do is by means of various plugins providing us with various built-in tasks. Some tasks are independent, some are dependent on other tasks. Some tasks can be specialised and some extended. One problem that this creates is that all plugins and all plugin versions have to be specified at the beginning of each `gradle.build` or `gradle.build.kts` file.

## Creating Gradle verison of Maven BOM

Top create Gradle version of Maven BOM we have to create project that is going to hold Gradle build script (`build.gradle.kts`) responsible for generating such a file. In this example project is called [MavenBOMPublisher](https://github.com/cubeprogramming/GradleBOM/tree/master/MavenBOMPublisher) and contains following important files:

- `build.gradle.kts` - build script that generates Maven like BOM file and publishes it to specified Maven compatible repository
- `gradle.properties` - contains version numbers for all dependencies in form of properties
- `plugins.properties` - contains version numbers for all plugins in form of properties
- `repostoryList`- contains the list of common plugin/dependency repositories that all of the projects will use.

### `build.gradle.kts` script explanation

This script is effectively responsible for building Maven like BOM

#### `plugins {}` section:

```kotlin
  plugins {
    `java-platform`
    `maven-publish`
  }
```

- `java-platform` plugin is used to build Maven like BOM
- `maven-publish` plugin is used to publish generated Maven like BOM XML file to desired repository

#### `dependencies{}` section:

```kotlin
  dependencies {
    //Maven BOM <dependencyManagement> block
    constraints {
      api("commons-httpclient:commons-httpclient:3.1")
      runtime("org.postgresql:postgresql:42.2.5")
    }
  }
```

This effectively maps to:

```xml
<dependencyManagement>
    <dependencies>
      <dependency>
        <groupId>commons-httpclient</groupId>
        <artifactId>commons-httpclient</artifactId>
        <version>3.1</version>
      </dependency>
      <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <version>42.2.5</version>
      </dependency>
    </dependencies>
  </dependencyManagement>
```

#### Optional block to include common dependencies that are going to be applied to all projects using this BOM:

```kotlin
javaPlatform {
  allowDependencies()
}
```

##### This allow us to use:

```kotlin
dependencies {
  val jacksonModuleVersion: String by project
  api("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonModuleVersion")
}
```

This effectively maps to:

```xml
<dependencies>
  <dependency>
    <groupId>com.fasterxml.jackson.module</groupId>
    <artifactId>jackson-module-kotlin</artifactId>
    <version>2.9.6</version>
    <scope>compile</scope>
  </dependency>
</dependencies>
```

or a set of depenencies that are going to be applied to all projects. Dependency version can be hardcoded in the file or provided as property in `gradle.property` file, like `jacksonModuleVersion=2.9.6` property that is mapped to string variable trough delegate object.

##### We can also import BOM from another BOM creating hierarchy of BOMs:

```kotlin
dependencies {
  api(platform("com.fasterxml.jackson:jackson-bom:2.9.8"))
}
```

This maps to:

```xml
<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>com.fasterxml.jackson</groupId>
      <artifactId>jackson-bom</artifactId>
      <version>2.9.8</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
  </dependencies>
</dependencyManagement>
```

#### To publish our generated BOM to Maven compatible repository we use `publishing {}` section:

```kotlin
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
```

- `publications {}` section defines that generated BOM will be produced as `pom-default.xml` found in `build/publications/myPlatform` folder.
- `repositories {}` section defines where this generated BOM will be send to. Credential can be supplied from properties file that is usually located in: `./gradle/gradle.properties`

## Importing generated Maven BOM

To import generated Maven BOM we have to reference it in our project `build.gradle.kts` file we have to reference the repository:

```kotlin
repositories {
    mavenCentral()
    maven(url = uri("http://localhost:8081/repository/maven-snapshots/"))
}
```

And then import our BOM as a dependency in order to use it:

```kotlin
dependencies {
    //api(platform(project("JavaPlatformProject")))
    implementation(platform("com.cubeprogramming:MavenBOMPublisher:1.0-SNAPSHOT"))
}
```

Now we are able to specify dependencies without specify version:

```kotlin
dependencies {
    //api(platform(project("JavaPlatformProject")))
    implementation(platform("com.cubeprogramming:MavenBOMPublisher:1.0-SNAPSHOT"))
    implementation("stdlib-jdk8")
    testImplementation("junit", "junit", "4.12")
}
```

We can still overide versions of referred depenencies or introduce new one if we need to to so.

## Centralising management of: plugins, plugin versions and dependency repository references

Unfortunately we can not use Maven BOM generation process in Gradle to generate: `<repositories>`, `<plugins>` or `<pluginManagement>` sections as we can do in Maven BOM. Workaround to this problem is to deploy repository references and list of plugins with corresponding versions to centralised place accessible to all project that are going to reference them. In this example this files are residing in the same place as Maven BOM generation project. This files are:

- [plugins.properties](https://github.com/cubeprogramming/GradleBOM/blob/master/MavenBOMPublisher/plugins.properties) - file containing list of plugins and corresponding versions
- [repositoryList](https://github.com/cubeprogramming/GradleBOM/blob/master/MavenBOMPublisher/repositoryList) file contains the list of common repositories that are going to be used to download plugins and dependencies.

### Creating common `settings.gradle.kts` file

To load list of common plugins, plugin versions, repository list and common configurations we have to create `settings.gradle.kts` file that all projects will copy. Content of this file can be found in: [Maven BOM consuming project example](https://github.com/cubeprogramming/GradleBOM/blob/master/BOMConsumingProject/settings.gradle.kts)

Now implementation of [build.gradle.kts](https://github.com/cubeprogramming/GradleBOM/blob/master/BOMConsumingProject/settings.gradle.kts) in each Maven BOM consuming project can look much simpler
