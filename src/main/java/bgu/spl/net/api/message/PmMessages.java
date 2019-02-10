package bgu.spl.net.api.message;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class PmMessages extends MessageObject {
    private String username;
    private String content;
    private int NumOfzerosLeft=2;
    public PmMessages() {
        opCode = PmOp;
    }

    public String getUsername() {
        return username;
    }

    public String getContent() {
        return content;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setContent(String content) {
        this.content = content;
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
        this.content = parsedData[1];
    }

}
