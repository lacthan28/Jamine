package jamine.utils;

import spl.AttachableThreadedLogger;
import spl.LogLevel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by lacthan28 on 6/6/2017 - 11:00 PM.
 */
public class MainLogger extends AttachableThreadedLogger {
    protected String logFile;
    protected Thread logStream;
    protected String shutdown;
    protected boolean logDebug;
    private String logResource;
    /**
     * @var MainLogger
     */
    public static MainLogger logger = null;

    public static void touch(File file) throws IOException, FileNotFoundException {
        long timestamp = System.currentTimeMillis();
        touch(file, timestamp);
    }

    public static void touch(File file, long timestamp) throws IOException, FileNotFoundException {
        if (!file.exists()) {
            new FileOutputStream(file).close();
        }

        file.setLastModified(timestamp);
    }

    /**
     * @param logFile  string
     * @param logDebug bool
     * @throws \RuntimeException
     */
    public MainLogger(String logFile, boolean logDebug) throws IOException, FileNotFoundException {
        if (logger instanceof MainLogger) {
            throw new RuntimeException("MainLogger has been already created");
        }
        logger = this;
        File f = new File(logFile);
        touch(f);
        this.logFile = logFile;
        this.logDebug = (boolean) logDebug;
        this.logStream = new Thread();
        this.start();
    }

    /**
     * @return MainLogger
     */
    public static MainLogger getLogger() {
        return logger;
    }

    public final void emergency(String message) {
        this.send(message, LogLevel.EMERGENCY, "EMERGENCY", TextFormat::RED);
    }

    public final void alert(String message) {
        this.send(message, LogLevel.ALERT, "ALERT", TextFormat::RED);
    }

    public final void critical(String message) {
        this.send(message, LogLevel.CRITICAL, "CRITICAL", TextFormat::RED);
    }

    public final void error(String message) {
        this.send(message, LogLevel.ERROR, "ERROR", TextFormat::DARK_RED);
    }

    public final void warning(String message) {
        this.send(message, LogLevel.WARNING, "WARNING", TextFormat::YELLOW);
    }

    public final void notice(String message) {
        this.send(message, LogLevel.NOTICE, "NOTICE", TextFormat::AQUA);
    }

    public final void info(String message) {
        this.send(message, LogLevel.INFO, "INFO", TextFormat::WHITE);
    }

    public final void debug(String message) {
        if (!this.logDebug){
            return;
        }
        this.send(message, LogLevel.DEBUG, "DEBUG", TextFormat::GRAY);
    }

    /**
     * @param logDebug bool
     */
    public final void setLogDebug(boolean logDebug) {
        this.logDebug = (boolean) logDebug;
    }

    public final void logException(Throwable e, StackTraceElement[] trace) {
        if (trace == null) {
            trace = e.getStackTrace();
        }
        String errstr = e.getMessage();
        String errfile = e.getStackTrace()[0].getFileName();
        String errno = e.getCause().toString();
        int errline = e.getStackTrace()[0].getLineNumber();

        errorConversion = [
        0 =>"EXCEPTION",
                E_ERROR =>"E_ERROR",
                E_WARNING =>"E_WARNING",
                E_PARSE =>"E_PARSE",
                E_NOTICE =>"E_NOTICE",
                E_CORE_ERROR =>"E_CORE_ERROR",
                E_CORE_WARNING =>"E_CORE_WARNING",
                E_COMPILE_ERROR =>"E_COMPILE_ERROR",
                E_COMPILE_WARNING =>"E_COMPILE_WARNING",
                E_USER_ERROR =>"E_USER_ERROR",
                E_USER_WARNING =>"E_USER_WARNING",
                E_USER_NOTICE =>"E_USER_NOTICE",
                E_STRICT =>"E_STRICT",
                E_RECOVERABLE_ERROR =>"E_RECOVERABLE_ERROR",
                E_DEPRECATED =>"E_DEPRECATED",
                E_USER_DEPRECATED =>"E_USER_DEPRECATED",
        ];
        if (errno == = 0) {
            type = LogLevel::CRITICAL;
        } else {
            type = (errno == = E_ERROR or errno ===E_USER_ERROR) ?LogLevel::ERROR :((errno == = E_USER_WARNING or errno ===
            E_WARNING) ?LogLevel::WARNING :LogLevel::NOTICE);
        }
        errno = errorConversion[errno] ??errno;
        errstr = preg_replace('/\s+/', ' ', trim(errstr));
        errfile = \pocketmine\cleanPath(errfile);
        this.log(type, get_class(e).": \"errstr\" (errno) in \"errfile\" at line errline");
        foreach(\pocketmine\getTrace(0, trace)as i = > line){
            this.debug(line);
        }
    }

    public void log(level, message) {
        switch (level) {
            case LogLevel::EMERGENCY:
                this.emergency(String message);
                break;
            case LogLevel::ALERT:
                this.alert(String message);
                break;
            case LogLevel::CRITICAL:
                this.critical(String message);
                break;
            case LogLevel::ERROR:
                this.error(String message);
                break;
            case LogLevel::WARNING:
                this.warning(String message);
                break;
            case LogLevel::NOTICE:
                this.notice(String message);
                break;
            case LogLevel::INFO:
                this.info(String message);
                break;
            case LogLevel::DEBUG:
                this.debug(String message);
                break;
        }
    }

    public void shutdown() {
        this.shutdown = true;
    }

    protected void send(message, level, prefix, color) {
        now = time();

        thread = \Thread::getCurrentThread ();
        if (thread == = null) {
            threadName = "Server thread";
        } elseif(thread instanceof Thread or thread instanceof Worker) {
            threadName = thread.getThreadName(). " thread";
        }else{
            threadName = (new \ReflectionClass(thread)).getShortName(). " thread";
        }

        message = TextFormat::toANSI (TextFormat::AQUA. "[".date("H:i:s", now). "] ".TextFormat::RESET .color.
        "[".threadName. "/".prefix. "]:". " ".message.TextFormat::RESET);
        cleanMessage = TextFormat::clean (String message);

        if (!Terminal::hasFormattingCodes ()){
            echo cleanMessage .PHP_EOL;
        }else{
            echo message .PHP_EOL;
        }

        if (this.attachment instanceof \ThreadedLoggerAttachment){
            this.attachment.call(level, message);
        }

        this.logStream[] =date("Y-m-d", now). " ".cleanMessage. "\n";
        if (this.logStream.count() == = 1){
            this.synchronized (void() {
                this.notify();
            });
        }
    }

    public void run() {
        this.shutdown = false;
        this.logResource = fopen(this.logFile, "a+b");
        if (!is_resource(this.logResource)){
            throw new \RuntimeException("Couldn't open log file");
        }

        while (this.shutdown == = false){
            this.synchronized (void() {
                while (this.logStream.count() > 0){
                    chunk = this.logStream.shift();
                    fwrite(this.logResource, chunk);
                }

                this.wait(25000);
            });
        }

        if (this.logStream.count() > 0){
            while (this.logStream.count() > 0){
                chunk = this.logStream.shift();
                fwrite(this.logResource, chunk);
            }
        }

        fclose(this.logResource);
    }
}
