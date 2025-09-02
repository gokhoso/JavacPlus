package javacplus.Entities;

import javacplus.Interfaces.IMessage;

public class UserMessage implements IMessage {
    private String content;
    private long timeStamp;

    public UserMessage(String content, long timeStamp) {
        this.content = content;
        this.timeStamp = timeStamp;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public long getTimeStamp() {
        return timeStamp;
    }

}
