# DeepLTranslator
> The DeepL Translator is an API written in Java that translates via the DeepL server sentences. This works without having a premium access and can be used free of charge. Requires Java 9.


## Code Example
### Synchronous translating
To translate texts synchronously, please use this method:<br />
`DeepLTranslator#translate(String text, Language sourceLanguage, Language targetLanguage)`

Example:
```java
Translation translation = DeepLTranslator.translate("Hello, my name is Linus. And what's your name?", Language.ENGLISH, Language.GERMAN);

if(translation.printError()) {
    return;
}

translation.getTranslations().forEach(System.out::println);
```

### Asynchronous translating
To translate texts asynchronously, please use this method:<br />
`DeepLTranslator#asyncTranslate(String text, Language sourceLanguage, Language targetLanguage, TranslationConsumer translationConsumer)`

Example:
```java
DeepLTranslator.asyncTranslate("Hello, my name is Linus. And what's your name?", Language.ENGLISH, Language.GERMAN, (translation, exception) -> {
    if(translation != null) {
        if(translation.printError()) {
            return;
        }

        translation.getTranslations().forEach(System.out::println);
    } else if(exception != null) {
        exception.printStackTrace();
    }
});
```

### Changing Timeout Duration
Default timeout duration is 10 seconds.<br /><br />
To change the timeout duration, please use this method:<br />
`DeepLTranslator#setTimeoutDuration(Duration timeoutDuration)`

Example:
```java
DeepLTranslator.setTimeoutDuration(Duration.ofSeconds(5));
```

### Change Waiting Duration For Repeating Request At Error 1042901/"Too many requests."
Default waiting duration is 3 seconds.<br /><br />
To change the waiting duration for repeating a request, please use this method:<br />
`DeepLTranslator#setRepeatRequestTime(Duration repeatRequestTime)`

Example:
```java
DeepLTranslator.setRepeatRequestTime(Duration.ofSeconds(5));
```

### Limit The Number Of Maximum Threads
Default number of maximum threads is Integer.MAX_VALUE.<br /><br />
To limit the number of maximum threads that handle asynchronous translation, use the following method:<br />
`DeepLTranslator#setExecutor(ExecutorService executor)`

Example:
```java
DeepLTranslator.setExecutor(Executors.newFixedThreadPool(5));
```

### Stopping program
If you want to close your program, you should call this method so that the ExecutorService can close all threads:<br />
`DeepLTranslator#shutdown()`

## License
This project is licensed under the Apache License 2.0 - see the [LICENSE.md](LICENSE.md) file for details
