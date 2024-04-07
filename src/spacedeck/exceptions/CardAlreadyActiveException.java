package spacedeck.exceptions;

public class CardAlreadyActiveException extends Exception {
    /**
     * Creates a new instance of <code>CardAlreadyActiveException</code> without
     * detail message.
     */
    public CardAlreadyActiveException() {
    }

    /**
     * Constructs an instance of <code>CardAlreadyActiveException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CardAlreadyActiveException(String msg) {
        super(msg);
    }
}
