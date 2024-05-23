package exception;

public class ExceptionResult extends Exception {
    final private int statusCode;

    public ExceptionResult(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public int statusCode() {
        return statusCode;
    }
}
