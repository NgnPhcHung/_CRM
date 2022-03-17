package CRM_APP.Model;

public class Customer {
    private String testName;
    private String  testEmail;

    public Customer(){}

    public Customer(String testName, String testEmail) {
        this.testName = testName;
        this.testEmail = testEmail;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getTestEmail() {
        return testEmail;
    }

    public void setTestEmail(String testEmail) {
        this.testEmail = testEmail;
    }
}
