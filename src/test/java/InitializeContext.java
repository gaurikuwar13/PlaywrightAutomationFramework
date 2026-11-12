import com.microsoft.playwright.*;

import org.gauriKulkarni.config.DriverManager;
import org.gauriKulkarni.config.LoginConfigManager;
import org.gauriKulkarni.pages.EventHubLoginPage;
import org.gauriKulkarni.pages.LoginPraticePage;
import org.gauriKulkarni.pages.MakeEventBookingPage;
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
    @Autowired
    MakeEventBookingPage makeEventBookingPage;
    @Autowired
    LoginPraticePage loginPraticePage;
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
        BrowserContext browserContext = browser.newContext();
        Page page = browserContext.newPage();
        DriverManager.setBrowserContext(browserContext);
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
