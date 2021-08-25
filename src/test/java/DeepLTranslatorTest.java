import de.linus.deepltranslator.*;

import java.util.concurrent.TimeUnit;

public class DeepLTranslatorTest {

    public static void main(String[] args) {
        DeepLConfiguration deepLConfiguration = new DeepLConfiguration.Builder()
                .setRepetitions(0)
                .build();

        DeepLTranslator deepLTranslator = new DeepLTranslator(deepLConfiguration);
        async(deepLTranslator);

        for (String sentence : sentences) {
            sync(deepLTranslator, sentence);
        }

        try {
            deepLTranslator.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        DeepLTranslator.shutdown();
    }

    private static void sync(DeepLTranslator deepLTranslator, String text) {
        try {
            String translation = deepLTranslator.translate(text, SourceLanguage.ENGLISH, TargetLanguage.GERMAN);
            System.out.println(translation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void async(DeepLTranslator deepLTranslator) {
        deepLTranslator.translateAsync("Hello, guys. My name is Linus.", SourceLanguage.ENGLISH, TargetLanguage.GERMAN)
                .whenComplete((res, ex) -> {
                    if (ex != null) {
                        ex.printStackTrace();
                    } else {
                        System.out.println(res);
                    }
                });
    }

    private static final String[] sentences = {
            "What?\n\nElephants can fly?\n\nI didn't know that, to be honest.",
            "We speak English here.",
            "There were thousands of English soldiers in Boston.",
            "More people are learning English in China than there are people who speak it in the United States.",
            "Everyone in the future will learn English because it will be the language of the Internet and thus the language of the world and commerce.",
            "You are studying English history, aren't you.",
            "This student selected the novel The Maze Runner for Book Club and then wrote a radio talk reviewing the novel.",
            "She was silent for a few moments in anger.",
            "She became more angry.",
            "He looked at her again, with his wicked blue eyes.",
            "There's no better place to learn how to program! Thank you.",
            "A simple rule definitely could solve the first problem. I'm not sure that's a very compelling argument to use a neural network in that case.",
            "I ran into a similar problem yesterday."
    };

}
