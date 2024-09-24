package stu.cn.ua;

public class TestException extends RuntimeException {
    public TestException(String message) {
        super("Test failed due to: " + message);
    }
}
