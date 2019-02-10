package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.api.bidi.BidiMessagingEncDec;
import bgu.spl.net.api.bidi.BidiMessagingProtocolImplementation;
import bgu.spl.net.api.bidi.UserDataBase;
import bgu.spl.net.srv.Server;

public class ReactorMain {
    public static void main (String [] args) {
    	UserDataBase userdata1=new UserDataBase();
        Server.reactor(
                Integer.parseInt(args[1]),
                Integer.parseInt(args[0]), //port
                () -> new BidiMessagingProtocolImplementation(userdata1), //protocol factory
                BidiMessagingEncDec::new //message encoder decoder factory
        ).serve();

        System.out.println("run successfully");
    }
}
