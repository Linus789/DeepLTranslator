package example;

import de.linus.deepltranslator.DeepLTranslator;
import de.linus.deepltranslator.Language;
import de.linus.deepltranslator.Translation;

import java.time.Duration;
import java.util.concurrent.Executors;

public final class Example {

    public static void main(String[] args) {
        try {
            //Sets the timeout duration for requests (default is 10 seconds)
            DeepLTranslator.setTimeoutDuration(Duration.ofSeconds(5));

            //Sets a new executor: now the threads are limited to 5 (default Integer.MAX_VALUE)
            DeepLTranslator.setExecutor(Executors.newFixedThreadPool(5));

            //Synchronously request
            synchronous();

            //Asynchronously request
            asynchronous();

            //After finishing everything, the executor should shutdown, else the program won't stop
            DeepLTranslator.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void synchronous() throws Exception {
        System.out.println("Synchronously");

        //Translating this text from english to german synchronously
        Translation translation = DeepLTranslator.translate("Hello, my name is Linus. And what's your name?", Language.ENGLISH, Language.GERMAN);

        //Checks if an error occurred on server side, if yes it will be printed
        if(translation.printError()) {
            return;
        }

        //Checks if response has any translations
        if(translation.hasTranslations()) {
            //If yes, they should print them out:
            System.out.println("Translations:");
            translation.getTranslations().forEach(System.out::println);
        }
    }

    private static void asynchronous() {
        System.out.println();
        System.out.println("Asynchronously");

        //Translating this text from english to german asynchronously
        DeepLTranslator.asyncTranslate("Hello, my name is Linus. And what's your name?", Language.ENGLISH, Language.GERMAN, (translation, exception) -> {
            //Checks if there's a translation (either there's a translation or an exception)
            if(translation != null) {
                //Checks if an error occurred on server side, if yes it will be printed
                if(translation.printError()) {
                    return;
                }

                //Checks if response has any translations
                if(translation.hasTranslations()) {
                    //If yes, they should print them out:
                    System.out.println("Translations:");
                    translation.getTranslations().forEach(System.out::println);
                }
            }
            //Checks if there's a client side exception
            else if(exception != null) {
                //If yes, it should print it:
                exception.printStackTrace();
            }
        });
    }
}
