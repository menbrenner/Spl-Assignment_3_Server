package bgu.spl.net.api.message;

import java.io.UnsupportedEncodingException;

public class LogoutMessage extends MessageObject {
    public LogoutMessage(){
        opCode = LogoutOp;
    }

    @Override
    public boolean decode(byte t) {
        return true;
    }

    @Override
    public byte[] encode() throws UnsupportedEncodingException {
        return null;
    }
}
