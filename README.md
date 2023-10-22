![logo](https://raw.githubusercontent.com/cyklon73/SpigotUtils/master/logo.png)
# SpigotUtils
A library that provides various utilities for the development of Spigot plugins. Among them a UI package

**IMPORTANT**
You don't need to add the jar to your plugin folder, it's enough if you add the library to your project via gradle or maven.

## Add it to your Project
you can add it to your project with maven or gradle.
Replace _**{VERSION}**_ with the most recent version of SpigotUtils
You find the Version on the [Github repository](https://github.com/cyklon73/SpigotUtils)

![find-version](https://raw.githubusercontent.com/cyklon73/SpigotUtils/master/find-version.jpg)

**Maven**

```html
<repositories>  
	<repository>  
		<id>jitpack.io</id>  
		<url>https://jitpack.io/</url>  
	</repository>  
</repositories>  
  
<dependencies>  
	<dependency>  
		<groupId>com.github.cyklon73</groupId>  
		<artifactId>SpigotUtils</artifactId>  
		<version>{VERSION}</version>  
	</dependency>  
</dependencies>
```

**Gradle**
```java
repositories {  
	mavenCentral()  
	maven { url "https://jitpack.io"  }  
}  
  
  
dependencies {  
	implementation 'com.github.cyklon73:SpigotUtils:{VERSION}'  
}
```
