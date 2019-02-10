package bgu.spl.net.api.bidi;

import java.util.concurrent.ConcurrentLinkedQueue;

public class User {
    private String username;
    private String password;
    private boolean login;
    private int connectionId;
    private ConcurrentLinkedQueue <User> Followers;
    private ConcurrentLinkedQueue <User> Following;
    private ConcurrentLinkedQueue <String> PMMessages;
    private ConcurrentLinkedQueue <String> PostMessages;


    private ConcurrentLinkedQueue <PendingMessage> pendingMessages;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.connectionId = -1;//safety initialization.
        Followers = new ConcurrentLinkedQueue<>();
        Following =new ConcurrentLinkedQueue<>();
        this.PMMessages = new ConcurrentLinkedQueue<>();
        pendingMessages = new ConcurrentLinkedQueue<>();
        PostMessages = new ConcurrentLinkedQueue<>();
        this.login = false;
    }

    public String getUsername() {
        return username;
    }

    public int getConnectionId() {
        return connectionId;
    }

    public String getPassword() {
        return password;
    }

    public ConcurrentLinkedQueue<User> getFollowers() {
        return Followers;
    }
    public void addFollower(User newFollower){//input validity is checked in protocol.
        Followers.add(newFollower);
    }

    public ConcurrentLinkedQueue<User> getFollowing() {
        return Following;
    }
    public void addFollowing(User newFollowing){//input validity is checked in protocol.
        Following.add(newFollowing);
    }

    public boolean removeFollower(User FollowerToRemove){
        return Followers.remove(FollowerToRemove);
    }

    public boolean removeFollowing(User FollowingToRemove){
        return Following.remove(FollowingToRemove);
    }
    public ConcurrentLinkedQueue<String> getPMMessages() {
        return PMMessages;
    }
    public void addPM(String PM){//input validity is checked in protocol.
        PMMessages.add(PM);
    }

    public ConcurrentLinkedQueue<String> getPostMessages() {
        return PostMessages;
    }
    public void addPostMessage(String postMessages){//input validity is checked in protocol.
        PMMessages.add(postMessages);
    }
    public boolean isLogin() {
        return login;
    }

    public void Login(int connectionId) {
        this.login = true;
        this.connectionId = connectionId;

    }

    public void logout(){
        this.login = false;
        this.connectionId = -1;
    }

    public short numOfPosts(){
        return (short) this.PostMessages.size();
    }
    public short numOfFollowers(){
        return (short)this.Followers.size();
    }
    public short numOfFollowing(){
        return (short)this.Following.size();
    }

    public ConcurrentLinkedQueue<PendingMessage> getPendingMessages() {
        return pendingMessages;
    }

    public void addPendingMessages(PendingMessage waitingMessages) {
        this.pendingMessages.add(waitingMessages);
    }


}
