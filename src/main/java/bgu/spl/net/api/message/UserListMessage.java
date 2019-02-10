package bgu.spl.net.api.message;

import java.io.UnsupportedEncodingException;

public class UserListMessage extends MessageObject{
    public UserListMessage(){
        opCode = UserlistOp;
    }

    @Override
    public boolean decode(byte toHandle) {
        return true;
    }

    @Override
    public byte[] encode() throws UnsupportedEncodingException {
        return null;
    }

}
