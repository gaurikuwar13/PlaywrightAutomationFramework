package org.gauriKulkarni.pages;

import com.microsoft.playwright.Page;
import org.gauriKulkarni.config.DriverManager;

public abstract class BasePage {

    // Automatically provides the thread-isolated page to any child page class
    protected Page getPage() {
        return DriverManager.getPage();
    }
    protected void navigateTo(String url) {
        getPage().navigate(url);
    }

    protected void click(String selector) {
        getPage().click(selector);
    }

    protected void type(String selector, String text) {
        getPage().fill(selector, text);
    }
}