package spacedeck.exceptions;

public class InsufficientFuelException extends Exception {

    /**
     * Creates a new instance of <code>InsufficientFuelException</code> without
     * detail message.
     */
    public InsufficientFuelException() {
    }

    /**
     * Constructs an instance of <code>InsufficientFuelException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public InsufficientFuelException(String msg) {
        super(msg);
    }
}
