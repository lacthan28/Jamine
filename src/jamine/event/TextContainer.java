package jamine.event;

/**
 * Created by lacthan28 on 6/6/2017 - 9:50 PM.
 */
public class TextContainer {
    /**
     * @var string text
     */
    protected String text;

    public TextContainer(String text) {
        this.text = text;
    }

    public final void setText(String text) {
        this.text = text;
    }

    /**
     * @return string
     */
    public final String getText() {
        return this.text;
    }

    /**
     * @return string
     */
    public final String toString() {
        return this.getText();
    }
}
