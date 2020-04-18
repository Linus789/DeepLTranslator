import com.machinepublishers.jbrowserdriver.JBrowserDriver;
import com.machinepublishers.jbrowserdriver.Settings;
import com.machinepublishers.jbrowserdriver.Timezone;
import com.machinepublishers.jbrowserdriver.UserAgent;
import org.openqa.selenium.Dimension;

public class JBrowserDriverTest {

    public static void main(String[] args) {
        // You can optionally pass a Settings object here,
        // constructed using Settings.Builder
        JBrowserDriver driver = new JBrowserDriver(
                Settings.builder()
                        .userAgent(UserAgent.CHROME)
                        .timezone(Timezone.EUROPE_BERLIN)
                        .screen(new Dimension(1920, 1080))
                        .ajaxWait(10000)
                        .ajaxResourceTimeout(10000)
                        .cache(true)
                        .build()
        );

        // This will block for the page load and any
        // associated AJAX requests
        driver.get("https://www.deepl.com/translator#en/de/Hello%2C%20my%20name%20is%20Linus.%20And%20what's%20your%20name%3F");

        // You can get status code unlike other Selenium drivers.
        // It blocks for AJAX requests and page loads after clicks
        // and keyboard events.
        System.out.println(driver.getStatusCode());

        // Getting translation result
        String result = driver
                .findElementByClassName("lmt__translations_as_text__text_btn")
                .getAttribute("textContent")
                .replaceAll("(?:\r?\n)+", " ");

        System.out.println(result);

        // Close the browser. Allows this thread to terminate.
        driver.quit();
    }

}