package bgu.spl.net.api.message;

import bgu.spl.net.api.bidi.Utils;

import java.io.UnsupportedEncodingException;

public class NotificationMessage extends MessageObject {
    private boolean notificationType;
    private String postingUser;
    private String content;

    public NotificationMessage(boolean notificationType, String postingUser, String content) {
        opCode = NotificationOp;
        this.notificationType = notificationType;
        this.postingUser = postingUser;
        this.content = content;
    }

    public boolean isNotificationType() {
        return notificationType;
    }

    public String getPostingUser() {
        return postingUser;
    }

    public String getContent() {
        return content;
    }

    public void setNotificationType(boolean notificationType) {
        this.notificationType = notificationType;
    }

    public void setPostingUser(String postingUser) {
        this.postingUser = postingUser;
    }

    public void setContent(String content) {
        this.content = content;
    }


    @Override
    public boolean decode(byte t) {
        return false;
    }

    @Override
    public byte[] encode() throws UnsupportedEncodingException {
        byte[] opCodeBytes = Utils.shortToBytes(opCode);
        String toEncode = new String(opCodeBytes) + (notificationType ? "\1":"\0") + postingUser
                + "\0" + content + "\0";
        return toEncode.getBytes("UTF-8");
    }
}
