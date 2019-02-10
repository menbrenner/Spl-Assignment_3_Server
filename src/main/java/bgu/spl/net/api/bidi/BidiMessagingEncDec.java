package bgu.spl.net.api.bidi;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.message.MessageObject;

import java.io.UnsupportedEncodingException;
public class BidiMessagingEncDec implements MessageEncoderDecoder<MessageObject> {
    final int logoutCode = 3;
    final int userlistCode = 7;
    private byte[] opCodeBytes = new byte[2];
    private short lenOpCode = 0;
    private short opCode = 0;
    private boolean finish = false;
    private MessageObject currentMessage=null;

    @Override
    public MessageObject decodeNextByte(byte nextByte) {
        if (lenOpCode < 2) { //type of message isn't yet known
            opCodeBytes[lenOpCode] = nextByte;
            lenOpCode++;
            if (lenOpCode == 2) {
                opCode = Utils.bytesToShort(opCodeBytes);
                this.currentMessage =  MessageObject.GenerateMessage(opCode);// generate message according to  opcode
                if (opCode == logoutCode || opCode == userlistCode) { //these cases require no further info.
                    finish = true;
                }
            }
        }else
            finish=currentMessage.decode(nextByte);//messages decode according to type
        if (finish) {// block if info is finished, return message for processing
            lenOpCode=0;
            finish =false;
            return currentMessage;
        }//while not a complete message
        return null;
    }


    @Override
    public byte[] encode(MessageObject message) {
        try {
            return message.encode();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
