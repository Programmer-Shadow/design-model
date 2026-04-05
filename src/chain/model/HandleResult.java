package chain.model;

public class HandleResult {
    private final boolean passed;
    private final String handler;
    private final String message;

    private HandleResult(boolean passed, String handler, String message) {
        this.passed = passed;
        this.handler = handler;
        this.message = message;
    }

    public static HandleResult pass(String handler, String message) {
        return new HandleResult(true, handler, message);
    }

    public static HandleResult reject(String handler, String message) {
        return new HandleResult(false, handler, message);
    }

    public boolean isPassed()  { return passed; }
    public String getHandler() { return handler; }
    public String getMessage() { return message; }
}
