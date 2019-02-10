package bgu.spl.net.api.message;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class LoginMessage extends MessageObject{
    private String username;
    private String password;
    private int NumOfzerosLeft = 2;

    public LoginMessage() {
        opCode = LoginOp;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
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

    private void processBytes(byte[] message, int len){
        String result = new String(message, 0, len, StandardCharsets.UTF_8);
        String[] parsedData = result.split("\0");
        this.username = parsedData[0];
        this.password = parsedData[1];
    }
}
