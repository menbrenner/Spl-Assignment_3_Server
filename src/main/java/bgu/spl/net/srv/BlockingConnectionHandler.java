package bgu.spl.net.srv;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.ConnectionHandler;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.api.bidi.ConnectionsImplementation;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BlockingConnectionHandler<T> implements Runnable, ConnectionHandler<T> {

    private int connectionId;
    private final BidiMessagingProtocol<T> protocol;
    private final MessageEncoderDecoder<T> encdec;
    private ConnectionsImplementation<T> connections;
    private ConcurrentLinkedQueue<byte[]> messages = new ConcurrentLinkedQueue<>();
    private final Socket sock;
    private BufferedInputStream in;
    private BufferedOutputStream out;
    private volatile boolean connected = true;
    public BlockingConnectionHandler(Socket sock, MessageEncoderDecoder<T> reader, BidiMessagingProtocol<T> protocol,ConnectionsImplementation<T> connections,int connectionId ) {
        this.sock = sock;
        this.encdec = reader;
        this.protocol = protocol;
        this.connections =  connections;
        this.connectionId = connectionId;
    }

    @Override
    public void run() {
        protocol.start(connectionId,connections);
        connections.addConnection(this,connectionId);
        try (Socket sock = this.sock) { //just for automatic closing
            int read;
            in = new BufferedInputStream(sock.getInputStream());
            out = new BufferedOutputStream(sock.getOutputStream());

            while (!protocol.shouldTerminate() && connected && (read = in.read()) >= 0) {
                T nextMessage = encdec.decodeNextByte((byte) read);
                if (nextMessage != null) {
                     protocol.process(nextMessage);
                }
             }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void close() throws IOException {
        connected = false;
        sock.close();

    }

	@Override
	public synchronized void send(T msg) {
		byte [] encodedData = encdec.encode(msg);
        try {
            out.write(encodedData);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
