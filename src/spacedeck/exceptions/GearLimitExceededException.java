package spacedeck.exceptions;

public class GearLimitExceededException extends Exception {

    /**
     * Creates a new instance of <code>GearLimitExceededException</code> without
     * detail message.
     */
    public GearLimitExceededException() {
    }

    /**
     * Constructs an instance of <code>GearLimitExceededException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public GearLimitExceededException(String msg) {
        super(msg);
    }
}
