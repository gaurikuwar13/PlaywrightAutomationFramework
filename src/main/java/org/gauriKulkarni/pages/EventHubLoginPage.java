package org.gauriKulkarni.pages;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.gauriKulkarni.config.LoginConfigManager;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.gauriKulkarni.constants.Constants.LOGIN_PAGE_TITLE;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Slf4j
@Component
public class EventHubLoginPage extends BasePage{
    @Autowired
    private LoginConfigManager configManager;

    // Login Locators
    private Locator usernameInput(){return getPage().getByLabel("Email");}
    private Locator passwordInput(){return getPage().getByLabel("Password");}
    private Locator signInButton(){return getPage().getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In"));}
    private Locator signInPageTitle(){return getPage().getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Browse Events →"));}

    //Header locators for Admin tabs
    private Locator adminTab(){return getPage().getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Admin"));}
    private Locator manageEvents(){return getPage().locator(".absolute")
            .getByRole(AriaRole.LINK,new Locator.GetByRoleOptions().setName("Manage Events"));}
    //Create Event Header
    private Locator titleInput(){return getPage().getByLabel("Title");}
    private Locator descriptionInput(){return getPage().locator("#admin-event-form textarea");}
    private Locator categoryDropDown(){ return getPage().getByLabel("Category");}
    private Locator eventDateTime(){ return getPage().getByLabel("Event Date & Time");}
    private Locator cityInput(){return getPage().getByLabel("City");}
    private Locator venueInput(){return getPage().getByLabel("Venue");}
    private Locator priceInput(){return getPage().getByLabel("Price ($)");}
    private Locator seatsInput(){return getPage().getByLabel("Total Seats");}
    private Locator addEventButton(){return getPage().locator("#add-event-btn");}
    private Locator eventCreatedToolTip(){return getPage().getByText("Event created!");}

    //Events tab
    private Locator eventTab(){return getPage().locator("#nav-events");}

    public void navigateToEventHub() throws InterruptedException {
        navigateTo(configManager.getEventHubLoginUrl());
        log.info("The current page title is :" + getPage().title());
        assertThat(getPage()).hasTitle(LOGIN_PAGE_TITLE);
    }

    public void loginToEventHub() {
        Page currentPage = getPage();
        usernameInput().fill(configManager.getEventHubUsername());
        passwordInput().fill(configManager.getEventHubPassword());
        signInButton().click();
        assertThat(signInPageTitle())
                .isVisible();
    }
    public void createNewEvent(){
        String randomEventTitle = "New EventTitle" + RandomStringUtils.randomAlphabetic(8);
        String randomDescription = "New Event Description" + RandomStringUtils.randomAlphabetic(8);
        String city = "Nagpur" + RandomStringUtils.randomAlphabetic(8);
        String venue = "Nagpur Venue" + RandomStringUtils.randomAlphabetic(8);
        String price = String.valueOf(Math.random());
        Random random = new Random();
        int seatCount = random.nextInt(100);
        // Datetime
        LocalDateTime futureDateTime = LocalDateTime.now()
                .plusDays(5)
                .plusHours(3);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        String formattedString = futureDateTime.format(formatter);
        adminTab().click();
        manageEvents().click();
        titleInput().isVisible();
        titleInput().fill(randomEventTitle);
        //parent to child locator
        descriptionInput().fill(randomDescription);
        categoryDropDown().selectOption("Concert");
        eventDateTime().fill(formattedString);
        cityInput().fill(city);
        venueInput().fill(venue);
        priceInput().fill(price);
        seatsInput().fill(String.valueOf(seatCount));
        addEventButton().click();
        assertThat(eventCreatedToolTip()).isVisible();
        eventTab().click();
        Locator allEventCards =getPage().getByTestId("event-card");
        Locator targetCard = allEventCards.filter(new Locator.FilterOptions().setHasText(randomEventTitle));
        String textBeforeBooking = targetCard.getByText("seats").innerText();
    }

}
