package spacedeck.exceptions;

public class CardNotInDeckException extends Exception {

    /**
     * Creates a new instance of <code>CardNotInDeckException</code> without
     * detail message.
     */
    public CardNotInDeckException() {
    }

    /**
     * Constructs an instance of <code>CardNotInDeckException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CardNotInDeckException(String msg) {
        super(msg);
    }
}
