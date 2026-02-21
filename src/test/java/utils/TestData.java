package utils;

public class TestData {
    public final String testId;
    public final String testName;
    public final String flow;
    public final String emailOrPhone;
    public final String password;
    public final String expectedOutcome;
    public final String expectedMessageMode;
    public final String expectedMessage;

    public TestData(String testId, String testName, String flow, String emailOrPhone, String password,
                    String expectedOutcome, String expectedMessageMode, String expectedMessage) {
        this.testId = testId;
        this.testName = testName;
        this.flow = flow;
        this.emailOrPhone = emailOrPhone;
        this.password = password;
        this.expectedOutcome = expectedOutcome;
        this.expectedMessageMode = expectedMessageMode;
        this.expectedMessage = expectedMessage;
    }
}
