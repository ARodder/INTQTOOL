package dev.roder.intqtoolbackend.MessageWrapper;

/**
 * Simple class used to wrap messages sent over websockets
 * in subscribeMapping methods.
 */
public class MessageContent {
    private String content;

    /**
     * Empty constructor to allow creation of an object instance
     */
    public MessageContent(){

    }

    /**
     * Retrieves the content in the message
     *
     * @return Returns the content in the message
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the content in the message to a new value
     *
     * @param content new value for the content of the message.
     */
    public void setContent(String content) {
        this.content = content;
    }
}
