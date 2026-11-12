# Playwright Test Automation Framework (Java + TestNG + Spring Boot)

An enterprise-grade, parallel-execution-ready UI test automation framework built with **Java**, **Microsoft Playwright**, **TestNG**, and **Spring Boot**.

## 🏗️ Architecture & Key Features

* **Thread-Safe Parallel Execution:** Utilizes `ThreadLocal` via `DriverManager` to isolate Playwright `Page`, `Browser`, and `Playwright` instances per thread, enabling safe concurrent execution without cross-contamination.

* **Stateless Page Object Model (POM):** Page objects (extending `BasePage`) are managed as Spring `@Component` singletons. They dynamically retrieve the active thread's `Page` on demand and centralize locators using lazy-loading helper methods.

* **Centralized Configuration Management:** Leverages Spring Boot profiles (`application.properties`, `application-dev.properties`) and `@Value` injection via `LoginConfigManager` to decouple environment URLs, credentials, and browser settings from test logic.

* **Clean Separation of Concerns:** Infrastructure settings (browser type, headless mode) are managed globally via properties, leaving TestNG `@DataProvider` annotations strictly for business test data.

## 📁 Project Structure

```
PlaywrightAutomationFramework/
├── src/
│   ├── main/
│   │   ├── java/org/gauriKulkarni/
│   │   │   ├── config/
│   │   │   │   ├── DriverManager.java          # ThreadLocal storage for thread-safe Page isolation
│   │   │   │   └── LoginConfigManager.java     # Spring-managed configuration property reader
│   │   │   ├── constants/
│   │   │   │   └── Constants.java              # Application constants and expected titles
│   │   │   └── pages/
│   │   │       ├── BasePage.java               # Abstract base page providing getPage() & common actions
│   │   │       ├── EventHubLoginPage.java      # Page object for EventHub login & event creation
│   │   │       ├── MakeEventBookingPage.java   # Page object for event booking workflows
│   │   │       └── LoginPraticePage.java       # Page object for practice login flows
│   │   └── resources/
│   │       ├── application.properties          # Active Spring profile configuration
│   │       └── application-dev.properties      # Environment URLs, credentials & browser settings
│   └── test/
│       └── java/
│           ├── org/gaurikulkarni/
│           │   └── TestSpringConfiguration.java # Spring Boot test context configuration anchor
│           ├── InitializeContext.java           # Base TestNG runner managing browser lifecycle
│           ├── BasicLoginTest.java              # End-to-end login & event creation tests
│           └── MultipleWindowTest.java          # Multi-tab / window handling tests
└── pom.xml                                      # Maven dependencies and build plugins

```

## 🛠️ Prerequisites

* **Java Development Kit (JDK):** Java 17 or higher

* **Apache Maven:** 3.8+

* **IDE:** IntelliJ IDEA / Eclipse (with TestNG plugin installed)

## ⚙️ Configuration

Environment and browser settings are controlled via `src/main/resources/application-dev.properties` (activated via `spring.profiles.active=dev` in `application.properties`):

```
# Application URLs & Credentials
eventHubLink=https://eventhub.rahulshettyacademy.com/login
eventHubUsername=your-username
eventHubPassword=your-password

# Global Browser Settings
browser.name=chrome       # Supported: chrome, edge, opera, firefox, safari
browser.headless=false    # Set to true for CI/CD headless execution

```

## 🚀 Running the Tests

### 1. Run via Maven CLI

Execute the entire test suite from the terminal:

```
mvn clean test

```

### 2. Run a Specific Test Class

```
mvn test -Dtest=BasicLoginTest

```

### 3. Override Environment or Browser Properties via CLI

You can dynamically override properties at runtime without modifying files:

```
mvn test -Dbrowser.name=firefox -Dbrowser.headless=true

```

### 4. Parallel Execution via TestNG XML

To execute tests concurrently across multiple threads or browsers, configure your `testng.xml` suite with `parallel="methods"` or `parallel="tests"`:

```
<!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd">
<suite name="Playwright Parallel Suite" parallel="methods" thread-count="3">
    <test name="Regression Tests">
        <classes>
            <class name="org.gauriKulkarni.BasicLoginTest"/>
            <class name="org.gauriKulkarni.MultipleWindowTest"/>
        </classes>
    </test>
</suite>

```

## 🧩 Extending the Framework

### Adding a New Page Object

1. Create a new class in `org.gauriKulkarni.pages` annotated with `@Component` and extend `BasePage`.

2. Define locators at the top of the class as private lazy-loading methods (`private Locator myBtn() { return getPage().locator("#id"); }`).

3. Write action methods using those locator helpers—never store `Page` or `Locator` as class-level fields to preserve Spring singleton thread safety.

### Adding a New Test Class

1. Extend `InitializeContext` so your test inherits Spring context loading and automatic browser setup/teardown.

2. Inject your `@Component` page objects using `@Autowired`.

3. Use TestNG `@DataProvider` exclusively for supplying business test data.

## 🗺️ Future Roadmap & Planned Enhancements

The following capabilities are planned in the pipeline to further elevate test observability, coverage, and automation efficiency:

* **CI/CD Pipeline Integration:** Configure automated build and test execution workflows (e.g., GitHub Actions / Jenkins) to trigger headless regression suites on pull requests and scheduled runs.
* **Allure Reporting Integration:** Implement Allure TestNG listeners to generate rich, interactive execution reports featuring step-by-step logs, test categorization, and historical trend analytics.
* **API Testing Capabilities:** Leverage Playwright's native `APIRequestContext` (or Spring `RestTemplate`/`WebClient`) to enable hybrid UI + API test workflows, fast test data seeding, and backend service validation.
* **Execution Tracing & Media Capture:** Enable Playwright Trace Viewer (`tracing.start()`), automated failure screenshots, and video recordings to capture full DOM snapshots, network logs, and visual playback for rapid root-cause debugging.