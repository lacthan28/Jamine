package spl;

/**
 * Created by lacthan28 on 6/6/2017 - 11:14 PM.
 */
public interface Logger {
    /**
     * System is unusable
     *
     * @param message string
     */
    public void emergency(String message);

    /**
     * Action must me taken immediately
     *
     * @param message string
     */
    public void alert(String message);

    /**
     * Critical conditions
     *
     * @param message string
     */
    public void critical(String message);

    /**
     * Runtime errors that do not require immediate action but should typically
     * be logged and monitored.
     *
     * @param message string
     */
    public void error(String message);

    /**
     * Exceptional occurrences that are not errors.
     * <p>
     * Example: Use of deprecated APIs, poor use of an API, undesirable things
     * that are not necessarily wrong.
     *
     * @param message string
     */
    public void warning(String message);

    /**
     * Normal but significant events.
     *
     * @param message string
     */
    public void notice(String message);

    /**
     * Inersting events.
     *
     * @param message string
     */
    public void info(String message);

    /**
     * Detailed debug information.
     *
     * @param message string
     */
    public void debug(String message);

    /**
     * Logs with an arbitrary level.
     *
     * @param level   mixed
     * @param message string
     */
    public void log(String level, String message);

    /**
     * Logs a Throwable object
     *
     * @param e     Throwable
     * @param trace
     */
    public static void logException(Throwable e, StackTraceElement[] trace) {
        logException(e, null);
    }
}
