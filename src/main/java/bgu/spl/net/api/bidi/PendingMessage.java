package bgu.spl.net.api.bidi;

public class PendingMessage {
    private boolean notificationType; //true if post, false if pm.
    private String postingUser;
    private String Content;

    public PendingMessage(boolean notificationType, String postingUser, String content) {
        this.notificationType = notificationType;
        this.postingUser = postingUser;
        Content = content;
    }

    public boolean isNotificationType() {
        return notificationType;
    }

    public String getPostingUser() {
        return postingUser;
    }

    public String getContent() {
        return Content;
    }
}
