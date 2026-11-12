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
import java.util.HashMap;
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
    private Locator allEventCards(){return getPage().getByTestId("event-card");}
    private Locator eventCardBasedOnName(String eventName){return allEventCards().filter(new Locator.FilterOptions().setHasText(eventName));}
    private Locator bookNowButton(String eventName){ return eventCardBasedOnName(eventName).getByTestId("book-now-btn");}

    //Book events

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
    public HashMap<String,String> createNewEvent(HashMap<String, String>eventDetails) {
        if (eventDetails == null) {
            eventDetails = new HashMap<>();
        }
        String eventTitle = eventDetails.getOrDefault("title", "New EventTitle" + RandomStringUtils.randomAlphabetic(8));
        log.info("The event title is :"+ eventTitle);
        String description = eventDetails.getOrDefault("description", "New Event Description" + RandomStringUtils.randomAlphabetic(8));
        String category = eventDetails.getOrDefault("category", "Concert");
        String city = eventDetails.getOrDefault("city", "Nagpur" + RandomStringUtils.randomAlphabetic(8));
        String venue = eventDetails.getOrDefault("venue", "Nagpur Venue" + RandomStringUtils.randomAlphabetic(8));
        String price = eventDetails.getOrDefault("price", String.valueOf(Math.random()));
        String seats = eventDetails.getOrDefault("seats", String.valueOf(new Random().nextInt(100)));
        String formattedString;
        if (eventDetails.containsKey("dateTime") && eventDetails.get("dateTime") != null) {
            formattedString = eventDetails.get("dateTime");
        } else {
            LocalDateTime futureDateTime = LocalDateTime.now()
                    .plusDays(5)
                    .plusHours(3);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            formattedString = futureDateTime.format(formatter);
        }
        HashMap<String, String> finalValues = new HashMap<>();
        finalValues.put("title",eventTitle);
        finalValues.put("description",description);
        finalValues.put("category",category);
        finalValues.put("city",city);
        finalValues.put("venue",venue);
        finalValues.put("price",price);
        finalValues.put("seats",seats);
        finalValues.put("dateTime",formattedString);
        adminTab().click();
        manageEvents().click();
        titleInput().isVisible();
        titleInput().fill(eventTitle);
        //parent to child locator
        descriptionInput().fill(description);
        categoryDropDown().selectOption(category);
        eventDateTime().fill(formattedString);
        cityInput().fill(city);
        venueInput().fill(venue);
        priceInput().fill(price);
        seatsInput().fill(String.valueOf(seats));
        addEventButton().click();
        assertThat(eventCreatedToolTip()).isVisible();
        return finalValues;
    }

    public void verifyEventIsCreated(String eventName){
        eventTab().click();
        assertThat(eventCardBasedOnName(eventName)).isVisible();
    }

    public void bookAnEvent(String eventName){
        eventTab().click();
        Locator getTheTargetEvent = eventCardBasedOnName(eventName);
        //Get the seat count before booking the ticket
        String seatText = getTheTargetEvent.getByText("seats").innerText();
        String initialSeatCount = seatText.split("seats")[0];
        //Book the ticket by clicking in book button
        bookNowButton(eventName).click();

    }



}
