package bgu.spl.net.api.message;

import bgu.spl.net.api.bidi.Utils;
import java.io.UnsupportedEncodingException;

public class ErrorMessages extends MessageObject {
    private short messageOpCode;

    public ErrorMessages( short messageOpCode) {
        opCode = ErrorOp;
        this.messageOpCode = messageOpCode;
    }

    public int getMessageOpCode() {
        return messageOpCode;
    }

    public void setMessageOpCode(short messageOpCode) {
        this.messageOpCode = messageOpCode;
    }

    @Override
    public boolean decode(byte tohHandle) {
        return false;
    }

    @Override
    public byte[] encode() throws UnsupportedEncodingException {
        byte [] encodedBytes1 = Utils.shortToBytes(opCode);
        byte [] encodedBytes2 = Utils.shortToBytes(messageOpCode);
        return Utils.Concatenate(encodedBytes1,encodedBytes2);
    }
}
