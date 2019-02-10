package bgu.spl.net.api.bidi;

import bgu.spl.net.api.message.*;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BidiMessagingProtocolImplementation implements BidiMessagingProtocol<MessageObject> {
    private int connectionId;
    private Connections<MessageObject> connections;
    private UserDataBase userDataBase;
    private boolean ShouldTerminate;
    private User currentUser;
    public BidiMessagingProtocolImplementation(UserDataBase userDataBase) {
        this.userDataBase = userDataBase;
    }

    @Override
    public void start(int connectionId, Connections<MessageObject> connections) {
        this.connectionId = connectionId;
        this.connections = connections;
        this.ShouldTerminate = false;
    }

    @Override
    public void process(MessageObject message) {
        switch (message.getOpCode()) {
            case 1:
                processRegister((RegisterMessage) message);
                break;
            case 2:
                processLogin((LoginMessage) message);
                break;
            case 3:
                processLogout((LogoutMessage) message);
                break;
            case 4:
                processFollow((FollowMessage) message);
                break;
            case 5:
                processPost((PostMessage) message);
                break;
            case 6:
                processPM((PmMessages) message);
                break;
            case 7:
                processUserlist((UserListMessage) message);
                break;
            case 8:
                processStat((StatMessage) message);
                break;

        }
    }




    private void processRegister(RegisterMessage message) {
        MessageObject msg;
        User toAdd = new User(message.getUsername() , message.getPassword());
        if(this.userDataBase.addUser(toAdd)){
             msg = new AckMessages(message.getOpCode());//send ack
        }
        else
            msg=new ErrorMessages(message.getOpCode());//send error if user is already registered
        connections.send(connectionId, msg);
    }



    private void processLogin(LoginMessage message) {
        MessageObject msg = new ErrorMessages(message.getOpCode());
        if(currentUser==null) {
            User loginUser = userDataBase.getUser(message.getUsername());
            if (loginUser != null) {
                synchronized (loginUser) {//race condition: two users try to login at the same time, both call login
                    if (loginUser.getPassword().equals(message.getPassword()) && !loginUser.isLogin()) {//check validity
                        loginUser.Login(connectionId);
                        this.currentUser = loginUser;
                        msg = new AckMessages(message.getOpCode());
                        connections.send(connectionId, msg);
                        for (PendingMessage eachMessage : loginUser.getPendingMessages()) {//pending messages are sent to the login user
                            msg = new NotificationMessage(eachMessage.isNotificationType(), eachMessage.getPostingUser(), eachMessage.getContent());
                            connections.send(connectionId, msg);
                        }
                        return;
                    }
                }
            }
        }
        connections.send(connectionId,msg);
    }

    private void processLogout(LogoutMessage message) {
        MessageObject msg;
        if(currentUser == null){
            msg = new ErrorMessages(message.getOpCode());
        }else{
        	synchronized(currentUser)//for notifications
        	{
	            this.ShouldTerminate = true;
	            currentUser.logout();
	            msg = new AckMessages(message.getOpCode());
        	}
        }
        connections.send(connectionId , msg);
        if(msg instanceof AckMessages) {//if logout successfully, disconnect from client
            connections.disconnect(connectionId);
        }
    }

    private void processFollow(FollowMessage message) {
        MessageObject msg = new ErrorMessages(message.getOpCode());
        LinkedList<String> handledUsers = new LinkedList<>();//list containing successfully followed/unfollowed users
        if (currentUser != null && currentUser.isLogin()) {//validity check
            for (String username : message.getUserNameList()) {
                User toHandle = userDataBase.getUser(username);
                if (toHandle != null && toHandle!=currentUser) {//checking to handle is registered and not self
                    if (!message.isUnfollow()) {
                        if (!currentUser.getFollowing().contains(toHandle)) {//if already following
                            toHandle.addFollower(currentUser);
                            currentUser.getFollowing().add(toHandle);
                            handledUsers.add(toHandle.getUsername());
                        }
                    } else {
                        if (currentUser.removeFollowing(toHandle)) { //if not following
                            toHandle.removeFollower(currentUser);
                            handledUsers.add(toHandle.getUsername());
                        }
                    }
                }
            }
            if (!handledUsers.isEmpty()) {//notify if some are successful
                msg = new AckMessages(message.getOpCode(), handledUsers);
            }

        }
        connections.send(connectionId,msg);
    }


    private void processPost(PostMessage message) {
        MessageObject msg = new ErrorMessages(message.getOpCode());
        if(currentUser != null){
            String poster = currentUser.getUsername();
            String content = message.getContent();
            currentUser.getPostMessages().add(content);//database maintenance
            ConcurrentLinkedQueue <User> toSend = new ConcurrentLinkedQueue<>();//list of post recipients
            toSend.addAll(currentUser.getFollowers());// initialize followers
            for(String eachUser: message.getTagList()) {//add tag list
                User taggedUser = userDataBase.getUser(eachUser);
                if (taggedUser != null && !toSend.contains(taggedUser) && taggedUser!=currentUser)// prevent double notification
                    toSend.add(taggedUser);
            }
            sendNotification(true, poster, content , toSend);//send notification to all in the list, true for POST
            msg = new AckMessages(message.getOpCode());
        }
        connections.send(connectionId,msg);
    }

    private void processPM(PmMessages message) {
        MessageObject msg = new ErrorMessages(message.getOpCode());
        if(currentUser!=null) {
            String poster = currentUser.getUsername();
            String content = message.getContent();
            currentUser.getPMMessages().add(content);//database maintenance
            ConcurrentLinkedQueue <User> toSend = new ConcurrentLinkedQueue<>();//single element list is sended to sendNotification
            User targetUser = userDataBase.getUser(message.getUsername());
            if(targetUser!=null) {
                toSend.add(targetUser);
                sendNotification(false, poster,content , toSend);
                msg = new AckMessages(message.getOpCode());
            }
        }
        connections.send(connectionId , msg);
    }


    private void processUserlist(UserListMessage message) {
        MessageObject msg = new ErrorMessages(message.getOpCode());
        if(currentUser!=null){
            msg = new AckMessages(message.getOpCode() ,userDataBase.getUSerList());
        }
        connections.send(connectionId , msg);
    }


    private void processStat(StatMessage message) {
        MessageObject msg=new ErrorMessages(message.getOpCode());
        if(currentUser!=null){
            User statUser = userDataBase.getUser(message.getUsername());
            if(statUser!=null){
                msg = new AckMessages(message.getOpCode(), statUser.numOfPosts(),statUser.numOfFollowers() , statUser.numOfFollowing());
            }
        }
        connections.send(connectionId,msg);
    }

    private void sendNotification(boolean notificationType, String poster, String content , ConcurrentLinkedQueue <User> toSend){
        for(User eachUser : toSend)
        	synchronized(eachUser)//race condition -user logs out/in mid notification, but some messages are missed 
        	{
	            if(eachUser.isLogin()){ //send now
	                MessageObject msg = new NotificationMessage(notificationType , poster , content);
	                connections.send(eachUser.getConnectionId() , msg);
	            }
	            else {
	                PendingMessage pendingMessage = new PendingMessage(notificationType , poster,content); //send when login occurs
	                eachUser.addPendingMessages(pendingMessage);
	            }
        	}
    }
    @Override
    public boolean shouldTerminate() {
        return ShouldTerminate;
    }
}
