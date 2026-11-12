package org.gauriKulkarni.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@Slf4j
@Component
public class MakeEventBookingPage extends BasePage{
    //Get the increase ticketcount locator
    private Locator increaseTicketCount(){return getPage().getByRole(AriaRole.BUTTON,new Page.GetByRoleOptions().setName("+"));}
    private Locator getTicketCount(){return getPage().locator("#ticket-count");}
    private Locator fullname(){ return getPage().locator("#customerName");}
    private Locator customerEmail(){return getPage().getByTestId("customer-email");}
    private Locator customerPhone(){return getPage().locator("#phone");}
    private Locator confirmBookingButton(){return getPage().getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Confirm Booking"));}
    private Locator bookingConfirmed(){return getPage().getByText("Booking Confirmed! \uD83C\uDF89");}
    public void increaseTheTicketCount(int ticketCount){
        //Handle the number of tickets
        for ( int i = 0; i==ticketCount; i++) {
            increaseTicketCount().click();
            String ticketCounter = getTicketCount().innerText();
            if(ticketCounter.equals(String.valueOf(ticketCount))) break;
        }
    }

    public HashMap<String, Object> bookAticket(HashMap<String, Object> ticketDetails){
        if (ticketDetails == null)  ticketDetails = new HashMap<>();

        String customerName = (String) ticketDetails.getOrDefault("customerName", "New Customer" + RandomStringUtils.randomAlphabetic(8));
        String customerEmail = (String) ticketDetails.getOrDefault("customerEmail", "Email" + RandomStringUtils.randomAlphabetic(8) + "@gmail.com");
        String customerPhone = (String) ticketDetails.getOrDefault("customerPhone",  RandomStringUtils.randomNumeric(10));
        int ticketCount = (int) ticketDetails.getOrDefault("ticketCount", 4);
        ticketDetails.put("customerName",customerName);
        ticketDetails.put("customerEmail",customerEmail);
        ticketDetails.put("customerPhone",customerPhone);
        increaseTheTicketCount(ticketCount);
        fullname().fill(customerName);
        customerEmail().fill(customerEmail);
        customerPhone().fill(customerPhone);
        confirmBookingButton().click();
        assertThat(bookingConfirmed()).isVisible();
        return ticketDetails;
    }
}
