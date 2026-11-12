package org.gauriKulkarni.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import lombok.extern.slf4j.Slf4j;
import org.gauriKulkarni.config.LoginConfigManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@Slf4j
@Component
public class LoginPraticePage extends BasePage{
    @Autowired
    private LoginConfigManager configManager;
    //Locators
    private Locator getResumeReviewLink(){return getPage().getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Free Access to InterviewQues/ResumeAssistance/Material"));}
    public void navigateToLoginPracticePage(){
        navigateTo(configManager.getSessionHandleLink());
    }

    public void openTheResumeReviewLink(){
        Page newPage = getBrowserContext().waitForPage(() -> getResumeReviewLink().click());
        log.info("The new page title is " + newPage.title());
        assertThat(newPage).hasTitle("RS Academy");
    }

}
