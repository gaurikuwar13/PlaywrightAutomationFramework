import com.microsoft.playwright.impl.junit.PlaywrightExtension;
import org.gauriKulkarni.pages.EventHubLoginPage;
import org.testng.annotations.Test;

public class BasicLoginTest extends InitializeContext {


    @Test
    public void loginEventHub() throws InterruptedException {

        eventHubLoginPage.navigateToEventHub();
        eventHubLoginPage.loginToEventHub();
        eventHubLoginPage.createNewEvent();

    }
}
