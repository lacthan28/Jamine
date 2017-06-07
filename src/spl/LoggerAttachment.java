package spl;

/**
 * Created by lacthan28 on 6/6/2017 - 11:20 PM.
 */
@FunctionalInterface
public interface LoggerAttachment {
    /**
     * @param level mixed
     * @param message string
     */
    public void log(String level, String message);
}
