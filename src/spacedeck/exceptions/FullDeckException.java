package spacedeck.exceptions;

public class FullDeckException extends Exception {

    /**
     * Creates a new instance of <code>FullDeckException</code> without detail
     * message.
     */
    public FullDeckException() {
    }

    /**
     * Constructs an instance of <code>FullDeckException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public FullDeckException(String msg) {
        super(msg);
    }
}
