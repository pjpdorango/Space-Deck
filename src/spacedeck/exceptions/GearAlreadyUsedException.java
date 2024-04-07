package spacedeck.exceptions;

public class GearAlreadyUsedException extends Exception {

    /**
     * Creates a new instance of <code>GearAlreadyUsedException</code> without
     * detail message.
     */
    public GearAlreadyUsedException() {
    }

    /**
     * Constructs an instance of <code>GearAlreadyUsedException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public GearAlreadyUsedException(String msg) {
        super(msg);
    }
}
