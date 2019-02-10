package bgu.spl.net.api.message;


import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public abstract class MessageObject {
    protected short opCode;
    protected byte[] message = new byte[1 << 10];
    protected int len = 0;
    final public static short RegisterOp = 1;
    final public static short LoginOp = 2;
    final public static short LogoutOp = 3;
    final public static short FollowOp = 4;
    final public static short PostOp = 5;
    final public static short PmOp = 6;
    final public static short UserlistOp = 7;
    final public static short StatOp = 8;
    final public static short NotificationOp = 9;
    final public static short AckOp = 10;
    final public static short ErrorOp = 11;


    public static MessageObject GenerateMessage(short opCode) {
        switch (opCode) {
            case 1: return new RegisterMessage();
            case 2: return new LoginMessage();
            case 3: return new LogoutMessage();
            case 4: return new FollowMessage();
            case 5: return new PostMessage();
            case 6: return new PmMessages();
            case 7: return new UserListMessage();
            default: return new StatMessage();
        }
    }

    protected void checkAndCopy(){
        if (len >= message.length) {
            message = Arrays.copyOf(message, len * 2);
        }

    }
    public short getOpCode() {
        return opCode;
    }

    public byte[] getMessage() {
        return message;
    }

    public int getLen() {
        return len;
    }

    public abstract boolean decode (byte t);
    public abstract byte[] encode () throws UnsupportedEncodingException;
 }
