import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import org.gauriKulkarni.config.DriverManager;
import org.gauriKulkarni.config.LoginConfigManager;
import org.gauriKulkarni.pages.EventHubLoginPage;
import org.gaurikulkarni.TestSpringConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

import org.testng.annotations.BeforeMethod;

@SpringBootTest(classes = TestSpringConfiguration.class)
public class InitializeContext extends AbstractTestNGSpringContextTests {
    // Read browser configurations directly from application-dev.properties
    @Value("${browser.name:chrome}")
    private String browserName;

    @Value("${browser.headless:false}")
    private boolean isBrowserHeadless;
    @Autowired
    LoginConfigManager loginConfigManager;

    @Autowired
    EventHubLoginPage eventHubLoginPage;
    // Use ThreadLocal to ensure thread-safety during parallel test execution
    private static final ThreadLocal<Playwright> playwrightThread = new ThreadLocal<>();
    private static final ThreadLocal<Browser> browserThread = new ThreadLocal<>();
    @BeforeMethod
    public void createBrowserInstance(){
        //Initiate Playwright instance
        Playwright playwright = Playwright.create();
        playwrightThread.set(playwright);
        //Select the browser type
        BrowserType browserType = setTheBrowser(playwright,browserName);
        //Initiate the browser in headless or non headless mode
        Browser browser =  isBrowserHeadless?
                browserType.launch():browserType.launch(new BrowserType.LaunchOptions().setHeadless(false));
        browserThread.set(browser);
        Page page = browser.newPage();
        DriverManager.setPage(page);
    }
    public BrowserType setTheBrowser(Playwright playwright, String browserType){
        return switch (browserType.toLowerCase()) {
            case "chrome", "edge", "opera" -> playwright.chromium();
            case "safari" -> playwright.webkit();
            case "firefox" ->  playwright.firefox();
            default -> throw new IllegalArgumentException("Invalid browserType: " + browserType);
        };
    }

}
