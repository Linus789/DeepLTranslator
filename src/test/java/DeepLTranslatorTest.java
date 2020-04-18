import com.machinepublishers.jbrowserdriver.Timezone;
import de.linus.deepltranslator.DeepLConfiguration;
import de.linus.deepltranslator.DeepLTranslator;
import de.linus.deepltranslator.Language;
import de.linus.deepltranslator.TranslationConsumer;

public class DeepLTranslatorTest {

    public static void main(String[] args) throws InterruptedException {
        DeepLConfiguration deepLConfiguration = new DeepLConfiguration.Builder()
                .setRepetitions(0)
                .setTimezone(Timezone.EUROPE_BERLIN)
                .build();

        DeepLTranslator deepLTranslator = new DeepLTranslator(deepLConfiguration);

        for (String sentence : sentences) {
            sync(deepLTranslator, sentence);
            Thread.sleep(3000);
        }

        async(deepLTranslator);
    }

    private static void sync(DeepLTranslator deepLTranslator, String text) {
        try {
            String translation = deepLTranslator.translate(text, Language.ENGLISH, Language.GERMAN);
            System.out.println(translation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void async(DeepLTranslator deepLTranslator) {
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
                DeepLTranslator.shutdown();
            }
        });
    }

    private static final String[] sentences = {
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
