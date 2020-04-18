# DeepLTranslator
> This project is a simple <a href="https://www.deepl.com/translator">DeepL Translator</a> API written in Java. It translates via the DeepL website sentences. This works without having a premium access and can be used free of charge.

## Maven
```
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```
```
<dependency>
    <groupId>com.github.Linus789</groupId>
    <artifactId>DeepLTranslator</artifactId>
    <version>v1.0.0</version>
</dependency>
```

## Examples
### Using a configuration
```java
DeepLConfiguration deepLConfiguration = new DeepLConfiguration.Builder()
        .setTimeout(Duration.ofSeconds(10))
        .setRepetitions(3)
        .setRepetitionsDelay(retryNumber -> Duration.ofMillis(3000 + 5000 * retryNumber))
        .build();

DeepLTranslator deepLTranslator = new DeepLTranslator(deepLConfiguration);
```

### Synchronous translating
```java
try {
    String translation = deepLTranslator.translate("I ran into a similar problem yesterday.", Language.ENGLISH, Language.GERMAN);
    System.out.println(translation);
} catch (Exception e) {
    e.printStackTrace();
}
```

### Asynchronous translating
```java
deepLTranslator.translateAsync("Hello, guys. My name is Linus.", Language.ENGLISH, Language.GERMAN, new TranslationConsumer() {
    @Override
    public void handleTranslation(String translation) {
        System.out.println(translation);
    }

    @Override
    public void handleException(Exception e) {
        e.printStackTrace();
    }

    @Override
    public void handleFinally() {
        System.out.println("Translation/Exception handled.");
    }
});
```

### Shutdown
Stops all running threads.
```java
DeepLTranslator.shutdown();
```

### Example
* [DeepLTranslatorTest](src/test/java/DeepLTranslatorTest.java)

## License
This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details