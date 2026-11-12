package org.gauriKulkarni.config;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;

public class DriverManager {
    private static final ThreadLocal<Page> pageThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<BrowserContext> browserContextThreadLocal = new ThreadLocal<>();

    public static void setPage(Page page) {
        pageThreadLocal.set(page);
    }

    public static Page getPage() {
        return pageThreadLocal.get();
    }

    public static void unload() {
        pageThreadLocal.remove();
        browserContextThreadLocal.remove();
    }
    public static void setBrowserContext(BrowserContext context) {
        browserContextThreadLocal.set(context);
    }

    public static BrowserContext getBrowserContext() {
        return browserContextThreadLocal.get();
    }
}