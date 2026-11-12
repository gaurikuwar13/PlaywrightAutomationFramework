import org.testng.annotations.Test;

import java.util.HashMap;

public class MultipleWindowTest extends InitializeContext{
    @Test
    public void habdleMutipleWindow() throws InterruptedException {
        //Login to event hub
        loginPraticePage.navigateToLoginPracticePage();
        loginPraticePage.openTheResumeReviewLink();
    }

}
