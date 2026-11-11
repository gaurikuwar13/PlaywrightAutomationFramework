package org.gauriKulkarni.config;

import com.microsoft.playwright.Page;

public class DriverManager {
    private static final ThreadLocal<Page> pageThreadLocal = new ThreadLocal<>();

    public static void setPage(Page page) {
        pageThreadLocal.set(page);
    }

    public static Page getPage() {
        return pageThreadLocal.get();
    }

    public static void unload() {
        pageThreadLocal.remove();
    }
}