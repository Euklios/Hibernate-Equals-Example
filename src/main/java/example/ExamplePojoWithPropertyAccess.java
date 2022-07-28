package example;

import java.util.Objects;

public class ExamplePojoWithPropertyAccess {
    private String testString;

    public String getTestString() {
        return testString;
    }

    public void setTestString(String testString) {
        this.testString = testString;
    }

    @Override
    public boolean equals(Object obj) {
        return Objects.equals(this.testString, ((ExamplePojoWithPropertyAccess) obj).testString);
    }
}
