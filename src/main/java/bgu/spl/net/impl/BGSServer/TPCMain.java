package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.api.bidi.BidiMessagingEncDec;
import bgu.spl.net.api.bidi.BidiMessagingProtocolImplementation;
import bgu.spl.net.api.bidi.UserDataBase;
import bgu.spl.net.srv.Server;

public class TPCMain {
    public static void main (String [] args) {
    	UserDataBase userdata1=new UserDataBase();
        Server.threadPerClient(
                Integer.parseInt(args[0]), //port
                () -> new BidiMessagingProtocolImplementation(userdata1), //protocol factory
                BidiMessagingEncDec::new //message encoder decoder factory
        ).serve();
    }
}
