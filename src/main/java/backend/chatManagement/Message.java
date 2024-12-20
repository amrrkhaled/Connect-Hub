package backend.chatManagement;

import java.util.Objects;

public class Message {
    private final String sender;
    private final String receiver;
    private String content;

    private static final String DEFAULT_SENDER = "Unknown Sender";
    private static final String DEFAULT_RECEIVER = "Unknown Receiver";
    private static final String DEFAULT_CONTENT = "No Content";

    public Message(String sender, String receiver, String content) {
        this.sender = validateAndSet(sender, DEFAULT_SENDER, "Sender");
        this.receiver = validateAndSet(receiver, DEFAULT_RECEIVER, "Receiver");
        this.content = validateAndSet(content, DEFAULT_CONTENT, "Content");
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = validateAndSet(content, DEFAULT_CONTENT, "Content");
    }

    private String validateAndSet(String value, String defaultValue, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            System.err.println("Warning: " + fieldName + " is null or empty. Defaulting to: " + defaultValue);
            return defaultValue;
        }
        return value.trim();
    }

    @Override
    public String toString() {
        return sender + ": " + content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return sender.equals(message.sender) &&
                receiver.equals(message.receiver) &&
                content.equals(message.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sender, receiver, content);
    }
}
