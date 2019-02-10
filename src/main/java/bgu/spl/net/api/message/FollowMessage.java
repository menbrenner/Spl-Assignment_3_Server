package bgu.spl.net.api.message;

import bgu.spl.net.api.bidi.Utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentLinkedQueue;

public class FollowMessage extends MessageObject {
    private ConcurrentLinkedQueue<String> userNameList;
    private int numOfZerosLeft;
    private boolean unfollow;//True if unfollow, false if follow.

    public FollowMessage() {
        opCode = FollowOp;
        numOfZerosLeft = -1;
        len = -1;
        userNameList = new ConcurrentLinkedQueue<>();
    }

    public ConcurrentLinkedQueue<String> getUserNameList() {
        return userNameList;
    }

    public boolean isUnfollow() {
        return unfollow;
    }

    public void setUnfollow(boolean unfollow) {
        this.unfollow = unfollow;
    }

    @Override
    public boolean decode(byte toHandle) {
        checkAndCopy();
        if (len == -1) {
            switch (toHandle) {
                case 0:
                    unfollow = false;
                    break;
                case 1:
                    unfollow = true;
                    break;
            }
            len++;
        } else {
            message[len++] = toHandle;
            if (numOfZerosLeft == -1) {
                if(len == 2) {
                    this.numOfZerosLeft = Utils.bytesToShort(message);
                    len = 0;
                }
            } else {
                if ((char) toHandle == '\0') {
                    numOfZerosLeft--;
                    processBytes(message, len-1);
                    len = 0;
                    if (numOfZerosLeft == 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public byte[] encode() throws UnsupportedEncodingException {
        return message;
    }

    private void processBytes(byte[] message, int len) {
        String s = new String(message, 0, len, StandardCharsets.UTF_8);
        this.userNameList.add(s);
    }
}