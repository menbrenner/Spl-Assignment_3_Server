package bgu.spl.net.api.message;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PostMessage extends MessageObject {
    private String content;
    private ConcurrentLinkedQueue<String> tagList;
    public PostMessage(){
        opCode = PostOp;
    }

    public String getContent() {
        return content;
    }

    public ConcurrentLinkedQueue<String> getTagList() {
        return tagList;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTagList(ConcurrentLinkedQueue<String> tagList) {
        this.tagList = tagList;
    }

    @Override
    public boolean decode(byte toHandle) {
        checkAndCopy();
        message[len] = toHandle;
        if((char) toHandle == '\0'){
            processBytes(message , len);
            return true;
        }
        len++;
        return false;
    }

    @Override
    public byte[] encode() throws UnsupportedEncodingException {
        return null;
    }

    private void processBytes(byte[] message , int len){
        this.content = new String(message, 0, len, StandardCharsets.UTF_8);
        String [] tagTempList = content.split(" ");
        tagList = new ConcurrentLinkedQueue<String>();
        for(int i = 0 ; i < tagTempList.length ; i++){
            if(tagTempList[i].charAt(0) == '@')
                this.tagList.add(tagTempList[i].substring(1,tagTempList[i].length()));
        }
    }
}
