import com.microsoft.playwright.impl.junit.PlaywrightExtension;
import org.gauriKulkarni.pages.EventHubLoginPage;
import org.testng.annotations.Test;

import java.util.HashMap;

public class BasicLoginTest extends InitializeContext {


    @Test
    public void createEventBookEvent() throws InterruptedException {
        //Login to event hub
        eventHubLoginPage.navigateToEventHub();
        eventHubLoginPage.loginToEventHub();
        //create a new event and verify that it is created
        HashMap<String, String> finalValues = eventHubLoginPage.createNewEvent(new HashMap<>());
        eventHubLoginPage.verifyEventIsCreated(finalValues.get("title"));
        //Book a ticket to the event created .
        eventHubLoginPage.bookAnEvent(finalValues.get("title"));
        makeEventBookingPage.bookAticket(new HashMap<>());
    }
}
