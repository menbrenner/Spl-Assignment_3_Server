package bgu.spl.net.api.message;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class StatMessage extends MessageObject {
    private int NumOfzerosLeft=1;
    private String username;
    public StatMessage(){
        opCode = StatOp;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean decode(byte toHandle) {
        checkAndCopy();
        message[len] = toHandle;
        if ((char) toHandle == '\0') {
            NumOfzerosLeft--;
            if (NumOfzerosLeft == 0) {
                processBytes(message,len);
                return true;
            }
        }
        len++;
        return false;
    }

    @Override
    public byte[] encode() throws UnsupportedEncodingException {
        return null;
    }

    private void processBytes(byte[] message , int len){
        String result = new String(message, 0, len, StandardCharsets.UTF_8);
        String[] parsedData = result.split("\0");
        this.username = parsedData[0];
    }


}
