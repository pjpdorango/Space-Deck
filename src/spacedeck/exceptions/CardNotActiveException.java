package spacedeck.exceptions;

public class CardNotActiveException extends Exception {

    /**
     * Creates a new instance of <code>CardNotActiveException</code> without
     * detail message.
     */
    public CardNotActiveException() {
    }

    /**
     * Constructs an instance of <code>CardNotActiveException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CardNotActiveException(String msg) {
        super(msg);
    }
}
