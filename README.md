# DeepLTranslator
> This project is a simple <a href="https://www.deepl.com/translator">DeepL Translator</a> API written in Java. It translates via the DeepL website sentences. This works without having a premium access and can be used free of charge.

## Prerequisites
* ChromeDriver installed and located in [PATH](https://en.wikipedia.org/wiki/PATH_(variable))

## Install
### Maven
Add the JitPack repository in your pom.xml
```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```
Add the dependency
```xml
<dependency>
    <groupId>com.github.Linus789</groupId>
    <artifactId>DeepLTranslator</artifactId>
    <version>2.1.0</version>
</dependency>
```
### Gradle
Add the JitPack repository in your root build.gradle at the end of repositories
```kotlin
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
Add the dependency
```kotlin
dependencies {
    implementation 'com.github.Linus789:DeepLTranslator:2.1.0'
}
```

## Examples
### Using a configuration
```java
DeepLConfiguration deepLConfiguration = new DeepLConfiguration.Builder()
        .setTimeout(Duration.ofSeconds(10))
        .setRepetitions(3)
        .setRepetitionsDelay(retryNumber -> Duration.ofMillis(3000 + 5000 * retryNumber))
        .setPostProcessing(false)
        .build();

DeepLTranslator deepLTranslator = new DeepLTranslator(deepLConfiguration);
```

### Synchronous translating
```java
try {
    String translation = deepLTranslator.translate("I ran into a similar problem yesterday.", SourceLanguage.ENGLISH, TargetLanguage.GERMAN);
    System.out.println(translation);
} catch (Exception e) {
    e.printStackTrace();
}
```

### Asynchronous translating

```java
deepLTranslator.translateAsync("Detected cow running backwards.", SourceLanguage.ENGLISH, TargetLanguage.GERMAN)
        .whenComplete((res, ex) -> {
            if (ex != null) {
                ex.printStackTrace();
            } else {
                System.out.println(res);
            }
        });
```

### Await termination
Blocks until all async translations from one `DeepLTranslator` instance have completed execution, or the timeout occurs,
or the current thread is interrupted, whichever happens first.
```java
try {
    deepLTranslator.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
} catch (InterruptedException e) {
    e.printStackTrace();
}
```

### Shutdown
Stops all running threads
```java
DeepLTranslator.shutdown();
```

### Example
* [DeepLTranslatorTest](src/test/java/DeepLTranslatorTest.java)

## License
This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details