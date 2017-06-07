package spl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by lacthan28 on 6/6/2017 - 11:19 PM.
 */
public class ThreadedLoggerAttachment extends Thread implements LoggerAttachment {
    /**
     * @var ThreadedLoggerAttachment
     */
    protected ThreadedLoggerAttachment attachment;

    /**
     * @param level   mixed
     * @param message string
     */
    public final void call(String level, String message) {
        this.log(level, message);
        if (this.attachment instanceof ThreadedLoggerAttachment) {
            this.attachment.call(level, message);
        }
    }

    /**
     * @param attachment ThreadedLoggerAttachment
     */
    public final void addAttachment(ThreadedLoggerAttachment attachment) {
        if (this.attachment instanceof ThreadedLoggerAttachment) {
            this.attachment.addAttachment(attachment);
        } else {
            this.attachment = attachment;
        }
    }

    /**
     * @param attachment ThreadedLoggerAttachment
     */
    public final void removeAttachment(ThreadedLoggerAttachment attachment) {
        if (this.attachment instanceof ThreadedLoggerAttachment) {
            if (Objects.equals(this.attachment, attachment)) {
                this.attachment = null;
                for (ThreadedLoggerAttachment attach : attachment.getAttachments()) {
                    this.addAttachment(attachment);
                }
            }
        }
    }

    public final void removeAttachments() {
        if (this.attachment instanceof ThreadedLoggerAttachment) {
            this.attachment.removeAttachments();
            this.attachment = null;
        }
    }

    private static List<ThreadedLoggerAttachment> concatenate(List<ThreadedLoggerAttachment> a, List<ThreadedLoggerAttachment> b) {
        List<ThreadedLoggerAttachment> result = new ArrayList<>(a.size() + b.size());
        result.addAll(a);
        for (ThreadedLoggerAttachment val : b) {
            if (!result.contains(val))
                result.add(val);
        }
        return result;
    }

    /**
     * @return ThreadedLoggerAttachment[]
     */
    public final List<ThreadedLoggerAttachment> getAttachments() {
        List<ThreadedLoggerAttachment> attachments = new ArrayList<>();
        if (this.attachment instanceof ThreadedLoggerAttachment) {
            attachments.add(this.attachment);
            attachments = concatenate(attachments, this.attachment.getAttachments());
        }

        return attachments;
    }

    @Override
    public void log(String level, String message) {

    }
}
