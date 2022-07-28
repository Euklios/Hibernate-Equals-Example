package example;

import java.util.Objects;

public class ExamplePojoWithGetterAccess {
    private String testString;

    public String getTestString() {
        return testString;
    }

    public void setTestString(String testString) {
        this.testString = testString;
    }

    @Override
    public boolean equals(Object obj) {
        return Objects.equals(this.getTestString(), ((ExamplePojoWithGetterAccess) obj).getTestString());
    }
}
