package bgu.spl.net.api.message;

import bgu.spl.net.api.bidi.Utils;
import java.util.LinkedList;

public class AckMessages extends MessageObject{
    private final short FollowOpCOde = 4;
    private final short UserLiseOpCode = 7;
    private final short StatOpCode = 8;
    private final short ackOpCOde = 10;

    private short opcodeMessage = 10;
    private LinkedList<String> usernameList;
    private short numPosts;
    private short numFollowers;
    private short numFollowings;

    public AckMessages(short opcodeMessage) {
        opCode=ackOpCOde;
        this.opcodeMessage = opcodeMessage;
    }
    public AckMessages (short opcodeMessage , LinkedList<String> usernameList){
        opCode=ackOpCOde;
        if(opcodeMessage != FollowOpCOde && opcodeMessage != UserLiseOpCode){
            throw new IllegalArgumentException("opcode message not matching parameters");
        }
        this.opcodeMessage = opcodeMessage;
        this.usernameList = usernameList;
    }
    public AckMessages (short opcodeMessage , short numPosts , short numFollowers , short numFollowings){
        opCode=ackOpCOde;
        if(opcodeMessage != StatOpCode){
            throw new IllegalArgumentException("opcode message not matching parameters");
        }
        this.opcodeMessage = StatOpCode;
        this.numPosts = numPosts;
        this.numFollowers = numFollowers;
        this.numFollowings = numFollowings;
    }
    public void setOpcodeMessage(short opcodeMessage) {
        this.opcodeMessage = opcodeMessage;
    }

    public void setUsernameList(LinkedList<String> usernameList) {
        this.usernameList = usernameList;
    }

    public void setNumPosts(short numPosts) {
        this.numPosts = numPosts;
    }

    public void setNumFollowers(short numFollowers) {
        this.numFollowers = numFollowers;
    }

    public void setNumFollowings(short numFollowings) {
        this.numFollowings = numFollowings;
    }



    public short getOpcodeMessage() {
        return opcodeMessage;
    }

    public LinkedList<String> getUsernameList() {
        return usernameList;
    }

    public short getNumPosts() {
        return numPosts;
    }

    public short getNumFollowers() {
        return numFollowers;
    }

    public short getNumFollowings() {
        return numFollowings;
    }

    @Override
    public boolean decode(byte toHandle) {
        return false;
    }

    @Override
    public byte[] encode() {
        byte [] opCodeBytes = Utils.shortToBytes(opCode);
        byte [] OpCodeMessageBytes = Utils.shortToBytes(opcodeMessage);
        byte [] result = Utils.Concatenate(opCodeBytes , OpCodeMessageBytes);
        switch (opcodeMessage){
            case 4:
            case 7:
                short numOfUsers = (short) usernameList.size();
                byte [] numOfUsersBytes = Utils.shortToBytes(numOfUsers);
                result = Utils.Concatenate(result, numOfUsersBytes);
                String usernames = "";
                if(usernameList.isEmpty()){
                    usernames += "\0";
                }
                for(String eachUserName :usernameList){
                    usernames += eachUserName +"\0";
                }
                byte [] usernameBytes = usernames.getBytes();
                return Utils.Concatenate(result, usernameBytes);
            case 8:
                byte [] numOfpostsBytes = Utils.shortToBytes(numPosts);
                result = Utils.Concatenate(result, numOfpostsBytes);
                byte [] numOfFollowers = Utils.shortToBytes(numFollowers);
                result = Utils.Concatenate(result, numOfFollowers);
                byte [] numOfFollowing = Utils.shortToBytes(numFollowings);
                return Utils.Concatenate(result, numOfFollowing);
            default: return result;
        }
    }
}
